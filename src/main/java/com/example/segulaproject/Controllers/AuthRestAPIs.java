package com.example.segulaproject.Controllers;

import com.example.segulaproject.DTO.SignIn;
import com.example.segulaproject.Entities.Enum.RoleUser;
import com.example.segulaproject.Entities.Role; // Import correct
import com.example.segulaproject.Entities.User;
import com.example.segulaproject.JWT.JwtAuthTokenFilter;
import com.example.segulaproject.JWT.JwtProvider;
import com.example.segulaproject.JWT.JwtResponse;
import com.example.segulaproject.JWT.NewTokensResponses;
import com.example.segulaproject.Repositories.RoleRepository;
import com.example.segulaproject.Repositories.UserRepository;
import com.example.segulaproject.ServiceImpl.MailSenderService;
import com.example.segulaproject.ServiceImpl.UserServiceIMP;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3000)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserServiceIMP userServiceIMP;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    MailSenderService mailSending;

    @Autowired
    JwtAuthTokenFilter jwtAuthTokenFilter;

    @PostMapping("/signIn")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody SignIn login, HttpServletRequest request) {
        Optional<User> userByEmail = userRepository.findByEmail(login.getEmail());
        Optional<User> userByUsername = userRepository.findByUsername(login.getEmail());

        Optional<User> user = userByEmail.isPresent() ? userByEmail : userByUsername;

        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtResponse(null, null, null, null, null));
        }

        User foundUser = user.get();
        if (foundUser.isBlocked() && foundUser.isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<String> jwt = jwtProvider.generateJwtTokens(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt.get(0), jwt.get(1), userDetails.getUsername(), foundUser.getId(), userDetails.getAuthorities(),user.get().getRole()));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtAuthTokenFilter.extractRefreshToken(request);
        if (refreshToken != null && jwtAuthTokenFilter.isValidRefreshToken(refreshToken)) {
            String newAccessToken = jwtAuthTokenFilter.issueNewAccessToken(refreshToken);
            return ResponseEntity.ok(new NewTokensResponses(refreshToken, newAccessToken));
        } else {
            return ResponseEntity.badRequest().body("expired refresh token");
        }
    }

    @RequestMapping(value = "/signup/employee/{roleName}", method = RequestMethod.POST)
    public ResponseEntity<User> registerUser(@Validated @RequestBody User user1, @PathVariable("roleName") String roleName) {
        return userServiceIMP.registerUser(user1, roleName);
    }

    @RequestMapping(value = "/signupPatient", method = RequestMethod.POST)
    public ResponseEntity<?> registerPatient(@Validated @RequestBody User user1) {
        try {
            // Vérifier si l'email est valide
            if (user1.getEmail() == null || !user1.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return ResponseEntity.badRequest().body("Invalid email format");
            }

            // Template d'email avec couleur verte
            String htmlMessage = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset=\"UTF-8\">" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "<title>Bienvenue sur HealthConnect</title>" +
                    "</head>" +
                    "<body style=\"margin: 0; padding: 0; font-family: Arial, Helvetica, sans-serif; background-color: #f4f4f4;\">" +
                    "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);\">" +
                    "<tr>" +
                    "<td style=\"padding: 20px 0; text-align: center; background-color: #28a745; border-top-left-radius: 10px; border-top-right-radius: 10px;\">" +
                    "<img src=\"https://png.pngtree.com/png-vector/20230328/ourmid/pngtree-medical-logo-design-vector-png-image_6672378.png\" alt=\"HealthConnect Logo\" style=\"max-width: 150px; height: auto;\">" +
                    "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td style=\"padding: 30px;\">" +
                    "<h1 style=\"font-size: 24px; color: #333; margin: 0 0 20px;\">Bienvenue sur HealthConnect, " + user1.getName() + " !</h1>" +
                    "<p style=\"font-size: 16px; color: #555; line-height: 1.5; margin: 0 0 20px;\">" +
                    "Merci de vous être inscrit(e) sur <strong>HealthConnect</strong>, votre plateforme dédiée à la gestion de votre santé. Nous sommes ravis de vous accueillir dans notre communauté." +
                    "</p>" +
                    "<p style=\"font-size: 16px; color: #555; line-height: 1.5; margin: 0 0 20px;\">" +
                    "Pour finaliser votre inscription et accéder à votre espace personnel, veuillez cliquer sur le bouton ci-dessous pour vous authentifier :" +
                    "</p>" +
                    "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin: 20px auto;\">" +
                    "<tr>" +
                    "<td style=\"text-align: center;\">" +
                    "<a href=\"http://localhost:3000/login\" style=\"background-color: #28a745; color: #ffffff; padding: 12px 24px; text-decoration: none; font-size: 16px; border-radius: 5px; display: inline-block;\">" +
                    "S'authentifier maintenant" +
                    "</a>" +
                    "</td>" +
                    "</tr>" +
                    "</table>" +
                    "<p style=\"font-size: 14px; color: #777; line-height: 1.5; margin: 20px 0 0;\">" +
                    "Si le bouton ne fonctionne pas, copiez et collez ce lien dans votre navigateur : <br>" +
                    "<a href=\"http://localhost:3000/login\" style=\"color: #28a745; text-decoration: none;\">http://localhost:3000/login</a>" +
                    "</p>" +
                    "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td style=\"padding: 20px; background-color: #f8f9fa; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px; text-align: center;\">" +
                    "<p style=\"font-size: 14px; color: #777; margin: 0;\">" +
                    "© 2025 HealthConnect. Tous droits réservés.<br>" +
                    "Vous avez reçu cet email car vous vous êtes inscrit(e) sur notre plateforme." +
                    "</p>" +
                    "<p style=\"font-size: 14px; color: #777; margin: 10px 0 0;\">" +
                    "<a href=\"mailto:support@healthconnect.com\" style=\"color: #28a745; text-decoration: none;\">Contactez-nous</a> | " +
                    "<a href=\"https://healthconnect.com/privacy\" style=\"color: #28a745; text-decoration: none;\">Politique de confidentialité</a>" +
                    "</p>" +
                    "</td>" +
                    "</tr>" +
                    "</table>" +
                    "</body>" +
                    "</html>";

            mailSending.send(user1.getEmail(), "Bienvenue sur HealthConnect", htmlMessage);
            return userServiceIMP.registerPatient(user1);

        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec de l'envoi de l'email : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec de l'inscription : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/signupadmin", method = RequestMethod.POST)
    public ResponseEntity<User> registerAdmin(@Valid @RequestBody User user) {
        return userServiceIMP.registerAdmin(user);
    }
    @RequestMapping(value = "/createMedecin", method = RequestMethod.POST)
    public ResponseEntity<User> registerMedecin(@Valid @RequestBody User user) {
        return userServiceIMP.registerMedecin(user);
    }


}


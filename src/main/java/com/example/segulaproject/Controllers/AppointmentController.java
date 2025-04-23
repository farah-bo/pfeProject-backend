package com.example.segulaproject.Controllers;

import com.example.segulaproject.Entities.Appointment; 
import com.example.segulaproject.Entities.User; 
import com.example.segulaproject.Repositories.AppointmentRepository; 
import com.example.segulaproject.Repositories.UserRepository; 
import com.example.segulaproject.Services.AppointmentService; 
import com.example.segulaproject.Services.RappelEmailService;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.security.access.prepost.PreAuthorize; 
import org.springframework.web.bind.annotation.*;

import java.util.List; 
import java.util.Map; 
import java.util.Optional; 
import java.util.stream.Collectors;

@RestController @RequestMapping("/api/appointments") @CrossOrigin(origins = "*") public class AppointmentController {

@Autowired
private AppointmentService service;

@Autowired
private UserRepository userRepository;

@Autowired
private AppointmentRepository appointmentRepository;

@Autowired
private RappelEmailService rappelEmailService;

@PostMapping("/add/{patientId}/{medecinId}")
@PreAuthorize("hasAnyRole('PATIENT', 'ADMIN', 'MEDECIN')")
public Appointment createWithUsers(@RequestBody Appointment appointment,
                                   @PathVariable Long patientId,
                                   @PathVariable Long medecinId) {
    User patient = userRepository.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found"));
    User medecin = userRepository.findById(medecinId)
            .orElseThrow(() -> new RuntimeException("Médecin not found"));

    appointment.setPatient(patient);
    appointment.setMedecin(medecin);
    return service.create(appointment);
}

@GetMapping
@PreAuthorize("hasAnyRole('PATIENT', 'ADMIN', 'MEDECIN')")
public List<Appointment> getAppointments(@RequestParam(required = false) Long patientId,
                                         @RequestParam(required = false) Long medecinId) {
    List<Appointment> all = service.getAll();
    return all.stream()
            .filter(a -> a.getPatient() != null && a.getMedecin() != null)
            .filter(a -> patientId == null || a.getPatient().getId().equals(patientId))
            .filter(a -> medecinId == null || a.getMedecin().getId().equals(medecinId))
            .collect(Collectors.toList());
}

@PutMapping("/{id}/confirm")
@PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN')")
public Appointment confirm(@PathVariable Long id) {
    return service.confirm(id);
}

@PutMapping("/{id}/cancel")
@PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN')")
public Appointment cancel(@PathVariable Long id) {
    return service.cancel(id);
}

@PostMapping("/{id}/notes")
@PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN')")
public Appointment addNote(@PathVariable Long id, @RequestBody Map<String, String> body) {
    String text = body.get("text");
    if (text == null || text.trim().isEmpty()) {
        throw new IllegalArgumentException("Le champ 'text' est requis pour ajouter une note.");
    }
    return service.addNote(id, text);
}

@PostMapping("/{id}/reminder")
@PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN')")
public ResponseEntity<?> sendReminder(@PathVariable Long id) {
    Appointment appointment = appointmentRepository.findById(id).orElseThrow();
    if (appointment.getPatient() != null && appointment.getPatient().getEmail() != null) {
        rappelEmailService.sendReminderEmail(
                appointment.getPatient().getEmail(),
                appointment.getPatient().getName(),
                appointment.getMedecin().getName(),
                appointment.getDate().toString(),
                appointment.getTime().toString()
        );
        return ResponseEntity.ok().body("Email envoyé avec succès.");
    }
    return ResponseEntity.badRequest().body("Email patient introuvable.");
}

@DeleteMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
    Optional<Appointment> appt = appointmentRepository.findById(id);
    if (appt.isPresent()) {
        appointmentRepository.deleteById(id);
        return ResponseEntity.ok("Rendez-vous supprimé");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rendez-vous non trouvé");
    }
}
}
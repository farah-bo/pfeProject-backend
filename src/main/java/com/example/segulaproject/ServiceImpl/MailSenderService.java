package com.example.segulaproject.ServiceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Service
public class MailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;
    public void send(String to, String subject, String body) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("farahbouricha98@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            javaMailSender.send(message);
        } catch (MailAuthenticationException e) {
            throw new MessagingException("Authentication failed. Please check email credentials.", e);
        } catch (MailSendException e) {
            throw new MessagingException("Failed to send email. Please check email configuration.", e);
        } catch (Exception e) {
            throw new MessagingException("Unexpected error while sending email", e);
        }
    }
}

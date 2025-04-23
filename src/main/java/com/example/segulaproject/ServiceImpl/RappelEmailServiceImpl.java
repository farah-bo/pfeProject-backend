package com.example.segulaproject.ServiceImpl;

import com.example.segulaproject.Services.RappelEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class RappelEmailServiceImpl implements RappelEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendReminderEmail(String to, String patientName, String doctorName, String date, String time) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Rappel de rendez-vous");
        message.setText("Bonjour " + patientName + ",\n\n"
                + "Ceci est un rappel pour votre rendez-vous avec le Dr. " + doctorName
                + " prévu le " + date + " à " + time + ".\n\n"
                + "Merci de votre confiance.\nClinique Segula");

        mailSender.send(message);
    }
}

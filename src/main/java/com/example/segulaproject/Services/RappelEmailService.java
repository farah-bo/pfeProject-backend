package com.example.segulaproject.Services;

public interface RappelEmailService {
    void sendReminderEmail(String to, String patientName, String doctorName, String date, String time);
}

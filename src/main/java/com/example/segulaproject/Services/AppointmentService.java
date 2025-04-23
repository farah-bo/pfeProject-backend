package com.example.segulaproject.Services;

import com.example.segulaproject.Entities.Appointment; import java.util.List;

public interface AppointmentService { Appointment create(Appointment appointment); List<Appointment> getAll(); Appointment confirm(Long id); Appointment cancel(Long id); Appointment addNote(Long appointmentId, String noteText); }
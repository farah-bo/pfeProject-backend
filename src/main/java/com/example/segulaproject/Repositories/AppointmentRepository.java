package com.example.segulaproject.Repositories;

import com.example.segulaproject.Entities.Appointment;
import com.example.segulaproject.Entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByStatus(Appointment.Status status);
}
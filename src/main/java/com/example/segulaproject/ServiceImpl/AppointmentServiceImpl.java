package com.example.segulaproject.ServiceImpl;

import com.example.segulaproject.Entities.Appointment; import com.example.segulaproject.Entities.Note; import com.example.segulaproject.Repositories.AppointmentRepository; import com.example.segulaproject.Repositories.NoteRepository; import com.example.segulaproject.Services.AppointmentService; import org.springframework.beans.factory.annotation.Autowired; import org.springframework.stereotype.Service;

import java.util.List;

@Service public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NoteRepository noteRepository;

    public Appointment create(Appointment a) {
        return appointmentRepository.save(a);
    }

    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    public Appointment confirm(Long id) {
        Appointment a = appointmentRepository.findById(id).orElseThrow();
        a.setStatus(Appointment.Status.CONFIRMED);
        return appointmentRepository.save(a);
    }

    public Appointment cancel(Long id) {
        Appointment a = appointmentRepository.findById(id).orElseThrow();
        a.setStatus(Appointment.Status.CANCELLED);
        return appointmentRepository.save(a);
    }

    public Appointment addNote(Long appointmentId, String text) {
        Appointment a = appointmentRepository.findById(appointmentId).orElseThrow();
        Note note = new Note();
        note.setText(text);
        note.setCreatedAt(java.time.LocalDateTime.now().toString());
        note.setAppointment(a);
        a.getNotes().add(note);
        noteRepository.save(note);
        return appointmentRepository.save(a);
    }
}
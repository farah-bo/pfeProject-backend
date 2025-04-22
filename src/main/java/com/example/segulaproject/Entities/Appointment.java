package com.example.segulaproject.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference; import jakarta.persistence.*;

import java.time.LocalDate; import java.time.LocalTime; import java.util.ArrayList; import java.util.List;

@Entity public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String doctorName;
    private String specialty;
    private String patientName;
    private String location;

    private LocalDate date;
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Note> notes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "medecin_id")
    private User medecin;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;

    public enum Status {
        PENDING,
        CONFIRMED,
        CANCELLED
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<Note> getNotes() { return notes; }
    public void setNotes(List<Note> notes) { this.notes = notes; }

    public User getMedecin() { return medecin; }
    public void setMedecin(User medecin) { this.medecin = medecin; }

    public User getPatient() { return patient; }
    public void setPatient(User patient) { this.patient = patient; }
}
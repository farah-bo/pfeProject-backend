package com.example.segulaproject.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore; 
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
}
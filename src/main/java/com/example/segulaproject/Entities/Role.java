package com.example.segulaproject.Entities;

import com.example.segulaproject.Entities.Enum.RoleUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RoleUser roleName;

}

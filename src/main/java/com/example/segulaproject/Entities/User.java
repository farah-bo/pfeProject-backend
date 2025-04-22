package com.example.segulaproject.Entities;

import com.example.segulaproject.Entities.Enum.RoleUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String username;
    private String email;
    private String password;
    private String address;

    @Column(name = "number")
    private String number;

    private boolean blocked;
    private boolean valid;
    private String token;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String image;

    @Enumerated(EnumType.STRING)
    private RoleUser role;

    @ManyToOne 
    @JoinColumn(name = "specialty_id") 
    private Specialty specialty;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();

    public User(String name, String username, String email, String password,
                boolean blocked, String address, boolean valid) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.blocked = blocked;
        this.address = address;
        this.valid = valid;
    }
}

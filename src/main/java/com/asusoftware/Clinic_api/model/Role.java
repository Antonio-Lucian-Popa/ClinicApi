package com.asusoftware.Clinic_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;


@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;
}


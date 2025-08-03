package com.asusoftware.Clinic_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "cabinets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cabinet {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String name;
    private String address;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
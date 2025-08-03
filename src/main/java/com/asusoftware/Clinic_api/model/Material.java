package com.asusoftware.Clinic_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "materials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String name;
    private String unit;

    @Column(nullable = false)
    private UUID tenantId;

    private LocalDateTime createdAt = LocalDateTime.now();
}

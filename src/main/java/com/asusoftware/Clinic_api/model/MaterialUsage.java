package com.asusoftware.Clinic_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "material_usages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialUsage {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @ManyToOne
    private Doctor doctor;

    @ManyToOne
    private Assistant assistant;

    @ManyToOne
    private Appointment appointment;

    @ManyToOne
    private Material material;

    private Double quantity;
    private LocalDateTime usageDate = LocalDateTime.now();
    private String notes;
}

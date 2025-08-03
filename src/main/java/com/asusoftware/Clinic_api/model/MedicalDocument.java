package com.asusoftware.Clinic_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "medical_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalDocument {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @ManyToOne
    private Patient patient;

    @Column(nullable = false)
    private UUID uploadedBy;

    private String documentType;
    private String fileUrl;
    private String notes;
    private LocalDateTime uploadedAt = LocalDateTime.now();
}

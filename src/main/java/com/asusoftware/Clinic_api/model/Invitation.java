package com.asusoftware.Clinic_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "invitations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invitation {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String email;
    private String role;

    @ManyToOne
    private Cabinet cabinet;

    @ManyToOne
    private Doctor doctor;

    private UUID invitedBy;
    private String status;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime acceptedAt;
}

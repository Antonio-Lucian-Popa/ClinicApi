package com.asusoftware.Clinic_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(
        name = "doctors",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "cabinet_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "cabinet_id", nullable = false)
    private Cabinet cabinet;

    private String specialization;
    private String roomLabel;
    private boolean active = true;
    private LocalDateTime createdAt;
}
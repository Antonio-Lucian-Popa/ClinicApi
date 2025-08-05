package com.asusoftware.Clinic_api.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentRequest {
    @NotNull
    private UUID doctorId;

    private UUID assistantId;

    @NotNull
    private UUID patientId;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    private String notes;

    private String status; // SCHEDULED, CONFIRMED, COMPLETED, etc.
}
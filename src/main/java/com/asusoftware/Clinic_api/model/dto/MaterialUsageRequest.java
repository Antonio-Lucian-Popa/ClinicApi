package com.asusoftware.Clinic_api.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MaterialUsageRequest {
    @NotNull
    private UUID materialId;

    private UUID doctorId;
    private UUID assistantId;
    private UUID appointmentId;

    @NotNull
    private double quantity;

    private String notes;
}

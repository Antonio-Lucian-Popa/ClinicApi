package com.asusoftware.Clinic_api.model.dto;

import com.asusoftware.Clinic_api.model.MaterialUsage;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class MaterialUsageResponse {
    private UUID id;
    private UUID materialId;
    private String materialName;
    private double quantity;
    private UUID doctorId;
    private UUID assistantId;
    private UUID appointmentId;
    private Instant usageDate;
    private String notes;

    public static MaterialUsageResponse fromEntity(MaterialUsage usage) {
        return MaterialUsageResponse.builder()
                .id(usage.getId())
                .materialId(usage.getMaterial().getId())
                .materialName(usage.getMaterial().getName())
                .quantity(usage.getQuantity())
                .doctorId(usage.getDoctor() != null ? usage.getDoctor().getId() : null)
                .assistantId(usage.getAssistant() != null ? usage.getAssistant().getId() : null)
                .appointmentId(usage.getAppointment().getId())
                .usageDate(usage.getUsageDate() != null ? Instant.from(usage.getUsageDate()) : null)
                .notes(usage.getNotes())
                .build();
    }
}

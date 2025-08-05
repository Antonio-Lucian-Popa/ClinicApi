package com.asusoftware.Clinic_api.model.dto;

import com.asusoftware.Clinic_api.model.Material;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MaterialResponse {
    private UUID id;
    private String name;
    private String unit;
    private UUID cabinetId;
    private String cabinetName;
    private LocalDateTime createdAt;

    public static MaterialResponse fromEntity(Material material) {
        return MaterialResponse.builder()
                .id(material.getId())
                .name(material.getName())
                .unit(material.getUnit())
                .cabinetId(material.getCabinet().getId())
                .cabinetName(material.getCabinet().getName())
                .createdAt(material.getCreatedAt())
                .build();
    }
}

package com.asusoftware.Clinic_api.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CabinetResponse {
    private UUID id;
    private String name;
    private String address;
    private String phone;
    private UUID ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.asusoftware.Clinic_api.model.dto;

import com.asusoftware.Clinic_api.model.Receptionist;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ReceptionistResponse {
    private UUID id;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private UUID cabinetId;
    private String cabinetName;
    private boolean active;
    private LocalDateTime createdAt;

    public static ReceptionistResponse fromEntity(Receptionist receptionist) {
        return ReceptionistResponse.builder()
                .id(receptionist.getId())
                .userId(receptionist.getUser().getId())
                .firstName(receptionist.getUser().getFirstName())
                .lastName(receptionist.getUser().getLastName())
                .email(receptionist.getUser().getEmail())
                .cabinetId(receptionist.getCabinet().getId())
                .cabinetName(receptionist.getCabinet().getName())
                .active(receptionist.isActive())
                .createdAt(receptionist.getCreatedAt())
                .build();
    }
}
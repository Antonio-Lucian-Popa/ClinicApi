package com.asusoftware.Clinic_api.model.dto;

import com.asusoftware.Clinic_api.model.ClinicHistory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ClinicHistoryResponse {
    private UUID id;
    private String action;
    private String details;
    private LocalDateTime createdAt;

    private UUID userId;
    private String userFullName;

    public static ClinicHistoryResponse fromEntity(ClinicHistory history) {
        return ClinicHistoryResponse.builder()
                .id(history.getId())
                .action(history.getAction())
                .details(history.getDetails())
                .createdAt(history.getCreatedAt())
                .userId(history.getUser() != null ? history.getUser().getId() : null)
                .userFullName(history.getUser() != null ? history.getUser().getFirstName()  + " " + history.getUser().getLastName() : null)
                .build();
    }
}

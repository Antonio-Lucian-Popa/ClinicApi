package com.asusoftware.Clinic_api.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import com.asusoftware.Clinic_api.model.TimeOffRequest;

@Data
@Builder
public class TimeOffResponse {

    private UUID id;
    private UUID userId;
    private String userName;
    private String role;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status;
    private UUID approvedById;
    private String approvedByName;
    private Instant approvedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public static TimeOffResponse fromEntity(TimeOffRequest timeOff) {
        return TimeOffResponse.builder()
                .id(timeOff.getId())
                .userId(timeOff.getUser().getId())
                .userName(timeOff.getUser().getUsername())
                .role(timeOff.getRole())
                .startDate(timeOff.getStartDate())
                .endDate(timeOff.getEndDate())
                .reason(timeOff.getReason())
                .status(timeOff.getStatus())
                .approvedById(timeOff.getApprovedBy() != null ? timeOff.getApprovedBy().getId() : null)
                .approvedByName(timeOff.getApprovedBy() != null ? timeOff.getApprovedBy().getUsername() : null)
                .approvedAt(timeOff.getApprovedAt() != null ? Instant.from(timeOff.getApprovedAt()) : null)
                .createdAt(Instant.from(timeOff.getCreatedAt()))
                .updatedAt(Instant.from(timeOff.getUpdatedAt()))
                .build();
    }
}

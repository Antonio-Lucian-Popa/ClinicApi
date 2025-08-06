package com.asusoftware.Clinic_api.model.dto;

import com.asusoftware.Clinic_api.model.Invitation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class InvitationResponse {

    private UUID id;
    private String email;
    private String role;
    private String clinicName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime acceptedAt;

    public static InvitationResponse fromEntity(Invitation invitation) {
        return InvitationResponse.builder()
                .id(invitation.getId())
                .email(invitation.getEmail())
                .role(invitation.getRole())
                .clinicName(invitation.getCabinet().getName())
                .status(invitation.getStatus())
                .createdAt(invitation.getCreatedAt())
                .acceptedAt(invitation.getAcceptedAt())
                .build();
    }
}
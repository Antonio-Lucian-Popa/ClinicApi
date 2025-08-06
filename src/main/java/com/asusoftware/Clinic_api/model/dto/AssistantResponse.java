package com.asusoftware.Clinic_api.model.dto;

import com.asusoftware.Clinic_api.model.Assistant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssistantResponse {

    private UUID id;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private boolean active;

    public static AssistantResponse fromEntity(Assistant assistant) {
        return AssistantResponse.builder()
                .id(assistant.getId())
                .userId(assistant.getUser().getId())
                .firstName(assistant.getUser().getFirstName())
                .lastName(assistant.getUser().getLastName())
                .email(assistant.getUser().getEmail())
                .active(assistant.isActive())
                .build();
    }
}

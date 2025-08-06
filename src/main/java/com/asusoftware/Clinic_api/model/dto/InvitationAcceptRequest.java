package com.asusoftware.Clinic_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InvitationAcceptRequest {

    @NotBlank
    private String token;

    @NotBlank
    private String encodedPassword; // deja hash-uit în frontend

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}

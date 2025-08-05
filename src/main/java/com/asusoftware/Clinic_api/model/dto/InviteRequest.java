package com.asusoftware.Clinic_api.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class InviteRequest {
    private String email;
    private String role; // DOCTOR, ASSISTANT, RECEPTIONIST
    private UUID cabinetId;

    // Opțional - doar dacă e ASSISTANT
    private UUID doctorId;
}
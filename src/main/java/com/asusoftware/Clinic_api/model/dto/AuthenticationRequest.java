package com.asusoftware.Clinic_api.model.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}

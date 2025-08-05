package com.asusoftware.Clinic_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CabinetRequest {
    @NotBlank
    private String name;

    private String address;
    private String phone;
}

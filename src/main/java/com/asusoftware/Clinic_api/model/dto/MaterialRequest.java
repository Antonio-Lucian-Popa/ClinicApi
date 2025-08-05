package com.asusoftware.Clinic_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class MaterialRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String unit;

    @NotNull
    private UUID cabinetId;
}

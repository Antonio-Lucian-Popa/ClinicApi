package com.asusoftware.Clinic_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TimeOffRequestDto {

    @NotBlank(message = "Rolul este obligatoriu")
    private String role;

    @NotNull(message = "Data de început este obligatorie")
    private LocalDate startDate;

    @NotNull(message = "Data de sfârșit este obligatorie")
    private LocalDate endDate;

    private String reason;
}

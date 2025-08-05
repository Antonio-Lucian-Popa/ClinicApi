package com.asusoftware.Clinic_api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class PatientRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

    private String phone;

    private LocalDate dateOfBirth;

    private String gender;

    private String cnp;

    @NotNull
    private UUID cabinetId;

    private String address;

    private String emergencyContact;
}

package com.asusoftware.Clinic_api.model.dto;

import com.asusoftware.Clinic_api.model.Patient;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PatientResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String cnp;
    private UUID cabinetId;
    private String address;
    private String emergencyContact;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;

    private List<String> medicalHistory;
    private List<String> allergies;

    public static PatientResponse fromEntity(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .gender(patient.getGender())
                .cnp(patient.getCnp())
                .dateOfBirth(patient.getDateOfBirth())
                .address(patient.getAddress())
                .emergencyContact(patient.getEmergencyContact())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .cabinetId(patient.getCabinet().getId())
                .createdBy(patient.getCreatedBy().getId()) // sau .getId(), în funcție de preferință
                .build();
    }
}

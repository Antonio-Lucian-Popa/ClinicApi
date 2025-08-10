package com.asusoftware.Clinic_api.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class MedicalRecordResponse {
    private UUID patientId;
    private List<String> medicalHistory; // ex: ["Diabet", "HTA"]
    private List<String> allergies;      // ex: ["Penicilină", "Arahide"]
}

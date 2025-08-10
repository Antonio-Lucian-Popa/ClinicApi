package com.asusoftware.Clinic_api.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class MedicalRecordUpdateRequest {
    private List<String> medicalHistory; // null = nu modifica, [] = golește
    private List<String> allergies;      // null = nu modifica, [] = golește
}

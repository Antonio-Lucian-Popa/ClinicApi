package com.asusoftware.Clinic_api.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class MedicalDocumentRequest {
    private UUID patientId;
    private String documentType;
    private String notes;
}

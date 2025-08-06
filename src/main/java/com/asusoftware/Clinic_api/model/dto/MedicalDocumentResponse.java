package com.asusoftware.Clinic_api.model.dto;

import com.asusoftware.Clinic_api.model.MedicalDocument;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class MedicalDocumentResponse {
    private UUID id;
    private UUID patientId;
    private String uploadedBy;
    private String documentType;
    private String fileUrl;
    private String notes;
    private Instant uploadedAt;

    public static MedicalDocumentResponse fromEntity(MedicalDocument document) {
        return MedicalDocumentResponse.builder()
                .id(document.getId())
                .patientId(document.getPatient().getId())
                .uploadedBy(document.getUploadedBy().getEmail())
                .documentType(document.getDocumentType())
                .fileUrl(document.getFileUrl())
                .notes(document.getNotes())
                .uploadedAt(document.getUploadedAt().toInstant())
                .build();
    }
}

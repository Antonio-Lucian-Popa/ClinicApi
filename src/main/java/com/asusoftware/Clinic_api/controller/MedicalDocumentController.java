package com.asusoftware.Clinic_api.controller;

import com.asusoftware.Clinic_api.model.dto.MedicalDocumentRequest;
import com.asusoftware.Clinic_api.model.dto.MedicalDocumentResponse;
import com.asusoftware.Clinic_api.service.MedicalDocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/medical-documents")
@RequiredArgsConstructor
public class MedicalDocumentController {

    private final MedicalDocumentService medicalDocumentService;

    @PostMapping("/upload")
    public ResponseEntity<MedicalDocumentResponse> uploadDocument(
            @Valid @RequestPart("info") MedicalDocumentRequest request,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(medicalDocumentService.uploadDocument(request, file, userDetails));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalDocumentResponse>> getDocumentsByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(medicalDocumentService.getDocumentsByPatient(patientId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        medicalDocumentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}

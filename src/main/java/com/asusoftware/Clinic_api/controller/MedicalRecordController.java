package com.asusoftware.Clinic_api.controller;

import com.asusoftware.Clinic_api.model.dto.MedicalRecordResponse;
import com.asusoftware.Clinic_api.model.dto.MedicalRecordUpdateRequest;
import com.asusoftware.Clinic_api.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PreAuthorize("hasAnyRole('OWNER','DOCTOR','ASSISTANT')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<MedicalRecordResponse> getByPatient(
            @PathVariable UUID patientId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(medicalRecordService.getByPatient(patientId, userDetails));
    }

    // opțional: update parțial (doar dacă vrei să editezi separat de /patients/{id})
    @PreAuthorize("hasAnyRole('OWNER','DOCTOR','ASSISTANT')")
    @PutMapping("/patient/{patientId}")
    public ResponseEntity<MedicalRecordResponse> updateForPatient(
            @PathVariable UUID patientId,
            @RequestBody MedicalRecordUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(medicalRecordService.updateForPatient(patientId, request, userDetails));
    }
}

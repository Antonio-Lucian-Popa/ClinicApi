package com.asusoftware.Clinic_api.controller;

import com.asusoftware.Clinic_api.model.dto.PatientRequest;
import com.asusoftware.Clinic_api.model.dto.PatientResponse;
import com.asusoftware.Clinic_api.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(
            @Valid @RequestBody PatientRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        PatientResponse response = patientService.createPatient(request, userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PatientResponse>> getAllPatients(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(patientService.getAllPatients(pageable));
    }

    @GetMapping("/by-cabinet/{cabinetId}")
    public ResponseEntity<Page<PatientResponse>> getByCabinet(
            @PathVariable UUID cabinetId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(patientService.getPatientsByCabinet(cabinetId, pageable));
    }


    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable UUID id,
            @Valid @RequestBody PatientRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(patientService.updatePatient(id, request, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok("Pacientul a fost șters cu succes.");
    }

}

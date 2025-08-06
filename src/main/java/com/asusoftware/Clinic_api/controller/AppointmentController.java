package com.asusoftware.Clinic_api.controller;

import com.asusoftware.Clinic_api.model.dto.AppointmentRequest;
import com.asusoftware.Clinic_api.model.dto.AppointmentResponse;
import com.asusoftware.Clinic_api.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.createAppointment(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody AppointmentRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, request, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(appointmentService.getAppointment(id));
    }

    @GetMapping
    public ResponseEntity<Page<AppointmentResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getAllAppointments(pageable));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Page<AppointmentResponse>> getByDoctor(
            @PathVariable UUID doctorId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId, pageable));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<AppointmentResponse>> getByPatient(
            @PathVariable UUID patientId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId, pageable));
    }

}

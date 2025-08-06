package com.asusoftware.Clinic_api.controller;

import com.asusoftware.Clinic_api.model.dto.AppointmentResponse;
import com.asusoftware.Clinic_api.model.dto.DashboardResponse;
import com.asusoftware.Clinic_api.repository.UserRepository;
import com.asusoftware.Clinic_api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService service;

    @PreAuthorize("hasAnyRole('OWNER', 'DOCTOR')")
    @GetMapping
    public DashboardResponse getDashboard(@AuthenticationPrincipal Jwt jwt) {
        return service.getDashboard(jwt);
    }

    @PreAuthorize("hasAnyRole('OWNER', 'DOCTOR', 'ASSISTANT', 'RECEPTIONIST')")
    @GetMapping("/recent-appointments")
    public List<AppointmentResponse> getRecentAppointments(@AuthenticationPrincipal Jwt jwt) {
        return service.getRecentAppointments(jwt);
    }
}


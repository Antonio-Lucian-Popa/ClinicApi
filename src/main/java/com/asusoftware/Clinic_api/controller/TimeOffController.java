package com.asusoftware.Clinic_api.controller;

import com.asusoftware.Clinic_api.model.dto.TimeOffRequestDto;
import com.asusoftware.Clinic_api.model.dto.TimeOffResponse;
import com.asusoftware.Clinic_api.service.TimeOffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/time-off")
@RequiredArgsConstructor
public class TimeOffController {

    private final TimeOffService timeOffService;

    @PostMapping
    public ResponseEntity<TimeOffResponse> requestTimeOff(
            @RequestBody @Valid TimeOffRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(timeOffService.createTimeOff(request, userDetails));
    }

    @GetMapping
    public ResponseEntity<Page<TimeOffResponse>> getAllRequests(Pageable pageable) {
        return ResponseEntity.ok(timeOffService.getAllTimeOffs(pageable));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<TimeOffResponse> approveRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails approver
    ) {
        return ResponseEntity.ok(timeOffService.approveTimeOff(id, approver));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<TimeOffResponse> rejectRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails approver
    ) {
        return ResponseEntity.ok(timeOffService.rejectTimeOff(id, approver));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable UUID id) {
        timeOffService.deleteTimeOff(id);
        return ResponseEntity.noContent().build();
    }
}

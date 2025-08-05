package com.asusoftware.Clinic_api.controller;

import com.asusoftware.Clinic_api.model.dto.CabinetRequest;
import com.asusoftware.Clinic_api.model.dto.CabinetResponse;
import com.asusoftware.Clinic_api.service.CabinetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cabinets")
@RequiredArgsConstructor
public class CabinetController {

    private final CabinetService cabinetService;

    // === Creează un cabinet nou
    @PostMapping
    public ResponseEntity<CabinetResponse> createCabinet(
            @RequestBody @Valid CabinetRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(cabinetService.createCabinet(request, userDetails));
    }

    // === Returnează lista cabinetelor OWNER-ului curent
    @GetMapping
    public ResponseEntity<List<CabinetResponse>> getMyCabinets(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(cabinetService.getMyCabinets(userDetails));
    }

    // === Returnează un cabinet după ID (doar dacă aparține OWNER-ului curent)
    @GetMapping("/{id}")
    public ResponseEntity<CabinetResponse> getCabinetById(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(cabinetService.getCabinetById(id, userDetails));
    }

    // === Editează un cabinet (doar dacă aparține OWNER-ului curent)
    @PutMapping("/{id}")
    public ResponseEntity<CabinetResponse> updateCabinet(
            @PathVariable UUID id,
            @RequestBody @Valid CabinetRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(cabinetService.updateCabinet(id, request, userDetails));
    }
}

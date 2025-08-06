package com.asusoftware.Clinic_api.controller;

import com.asusoftware.Clinic_api.model.dto.MaterialRequest;
import com.asusoftware.Clinic_api.model.dto.MaterialResponse;
import com.asusoftware.Clinic_api.service.MaterialService;
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
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    public ResponseEntity<MaterialResponse> createMaterial(@Valid @RequestBody MaterialRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(materialService.createMaterial(request, userDetails));
    }

    @GetMapping
    public ResponseEntity<Page<MaterialResponse>> getAllMaterials(Pageable pageable) {
        return ResponseEntity.ok(materialService.getAllMaterials(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponse> getMaterialById(@PathVariable UUID id) {
        return ResponseEntity.ok(materialService.getMaterialById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialResponse> updateMaterial(
            @PathVariable UUID id,
            @Valid @RequestBody MaterialRequest request
    ) {
        return ResponseEntity.ok(materialService.updateMaterial(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable UUID id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}

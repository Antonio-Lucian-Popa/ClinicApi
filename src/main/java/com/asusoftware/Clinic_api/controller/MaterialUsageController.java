package com.asusoftware.Clinic_api.controller;

import com.asusoftware.Clinic_api.model.dto.MaterialUsageRequest;
import com.asusoftware.Clinic_api.model.dto.MaterialUsageResponse;
import com.asusoftware.Clinic_api.service.MaterialUsageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/material-usages")
@RequiredArgsConstructor
public class MaterialUsageController {

    private final MaterialUsageService materialUsageService;

    @PostMapping
    public ResponseEntity<MaterialUsageResponse> create(@Valid @RequestBody MaterialUsageRequest request) {
        return ResponseEntity.ok(materialUsageService.createUsage(request));
    }

    @GetMapping
    public ResponseEntity<Page<MaterialUsageResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(materialUsageService.getAllUsages(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialUsageResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(materialUsageService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        materialUsageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
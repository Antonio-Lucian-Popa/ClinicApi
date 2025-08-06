package com.asusoftware.Clinic_api.controller;

import com.asusoftware.Clinic_api.model.dto.ClinicHistoryResponse;
import com.asusoftware.Clinic_api.service.ClinicHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class ClinicHistoryController {

    private final ClinicHistoryService clinicHistoryService;

    @GetMapping("/cabinet/{cabinetId}")
    public Page<ClinicHistoryResponse> getHistoryByCabinet(
            @PathVariable UUID cabinetId,
            Pageable pageable
    ) {
        return clinicHistoryService.getHistoryByCabinet(cabinetId, pageable);
    }
}
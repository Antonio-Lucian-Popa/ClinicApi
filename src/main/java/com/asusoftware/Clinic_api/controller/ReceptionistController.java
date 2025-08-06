package com.asusoftware.Clinic_api.controller;

import com.asusoftware.Clinic_api.model.dto.ReceptionistResponse;
import com.asusoftware.Clinic_api.service.ReceptionistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/receptionists")
@RequiredArgsConstructor
public class ReceptionistController {

    private final ReceptionistService receptionistService;

    @GetMapping
    public ResponseEntity<List<ReceptionistResponse>> getAllByCabinet(@RequestParam UUID cabinetId) {
        return ResponseEntity.ok(receptionistService.getAllByCabinet(cabinetId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceptionistResponse> getReceptionistById(@PathVariable UUID id) {
        return ResponseEntity.ok(receptionistService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceptionist(@PathVariable UUID id) {
        receptionistService.deleteReceptionist(id);
        return ResponseEntity.noContent().build();
    }
}

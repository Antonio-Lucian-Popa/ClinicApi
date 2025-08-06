package com.asusoftware.Clinic_api.controller;

import com.asusoftware.Clinic_api.model.dto.AssistantResponse;
import com.asusoftware.Clinic_api.service.DoctorAssistantService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctor-assistants")
@RequiredArgsConstructor
public class DoctorAssistantController {

    private final DoctorAssistantService doctorAssistantService;

    // ✅ Asociază un asistent cu un doctor
    @PostMapping
    public ResponseEntity<String> linkAssistantToDoctor(
            @RequestParam @NotNull UUID doctorId,
            @RequestParam @NotNull UUID assistantId
    ) {
        doctorAssistantService.assignAssistantToDoctor(doctorId, assistantId);
        return ResponseEntity.ok("Asistent asociat cu succes doctorului.");
    }

    // ✅ Obține toți asistenții unui doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AssistantResponse>> getAssistantsByDoctor(
            @PathVariable UUID doctorId
    ) {
        List<AssistantResponse> assistants = doctorAssistantService.getAssistantsForDoctor(doctorId);
        return ResponseEntity.ok(assistants);
    }

    // ✅ Elimină un asistent din lista unui doctor
    @DeleteMapping("/{doctorId}/{assistantId}")
    public ResponseEntity<String> unlinkAssistantFromDoctor(
            @PathVariable UUID doctorId,
            @PathVariable UUID assistantId
    ) {
        doctorAssistantService.removeAssistantFromDoctor(doctorId, assistantId);
        return ResponseEntity.ok("Asistent eliminat din lista doctorului.");
    }
}

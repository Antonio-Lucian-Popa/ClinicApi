package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.*;
import com.asusoftware.Clinic_api.model.dto.MaterialUsageRequest;
import com.asusoftware.Clinic_api.model.dto.MaterialUsageResponse;
import com.asusoftware.Clinic_api.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaterialUsageService {

    private final MaterialUsageRepository materialUsageRepository;
    private final DoctorRepository doctorRepository;
    private final AssistantRepository assistantRepository;
    private final AppointmentRepository appointmentRepository;
    private final MaterialRepository materialRepository;

    public MaterialUsageResponse createUsage(MaterialUsageRequest request) {
        Material material = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new EntityNotFoundException("Materialul nu a fost găsit"));

        Doctor doctor = null;
        if (request.getDoctorId() != null) {
            doctor = doctorRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new EntityNotFoundException("Doctorul nu a fost găsit"));
        }

        Assistant assistant = null;
        if (request.getAssistantId() != null) {
            assistant = assistantRepository.findById(request.getAssistantId())
                    .orElseThrow(() -> new EntityNotFoundException("Asistentul nu a fost găsit"));
        }

        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            appointment = appointmentRepository.findById(request.getAppointmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Programarea nu a fost găsită"));
        }

        MaterialUsage usage = MaterialUsage.builder()
                .material(material)
                .doctor(doctor)
                .assistant(assistant)
                .appointment(appointment)
                .quantity(request.getQuantity())
                .usageDate(LocalDateTime.now())
                .notes(request.getNotes())
                .build();

        materialUsageRepository.save(usage);
        return MaterialUsageResponse.fromEntity(usage);
    }

    public Page<MaterialUsageResponse> getAllUsages(Pageable pageable) {
        return materialUsageRepository.findAll(pageable)
                .map(MaterialUsageResponse::fromEntity);
    }

    public MaterialUsageResponse getById(UUID id) {
        MaterialUsage usage = materialUsageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilizarea nu a fost găsită"));
        return MaterialUsageResponse.fromEntity(usage);
    }

    public void delete(UUID id) {
        if (!materialUsageRepository.existsById(id)) {
            throw new EntityNotFoundException("Utilizarea nu a fost găsită");
        }
        materialUsageRepository.deleteById(id);
    }
}


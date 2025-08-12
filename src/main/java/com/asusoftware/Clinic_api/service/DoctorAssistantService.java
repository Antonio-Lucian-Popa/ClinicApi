package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.Assistant;
import com.asusoftware.Clinic_api.model.Doctor;
import com.asusoftware.Clinic_api.model.DoctorAssistant;
import com.asusoftware.Clinic_api.model.dto.AssistantResponse;
import com.asusoftware.Clinic_api.model.dto.DoctorDto;
import com.asusoftware.Clinic_api.repository.AssistantRepository;
import com.asusoftware.Clinic_api.repository.DoctorAssistantRepository;
import com.asusoftware.Clinic_api.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorAssistantService {

    private final DoctorAssistantRepository doctorAssistantRepository;
    private final DoctorRepository doctorRepository;
    private final AssistantRepository assistantRepository;

    public void assignAssistantToDoctor(UUID doctorId, UUID assistantId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctorul nu a fost găsit"));

        Assistant assistant = assistantRepository.findById(assistantId)
                .orElseThrow(() -> new EntityNotFoundException("Asistentul nu a fost găsit"));

        boolean exists = doctorAssistantRepository.existsByDoctorIdAndAssistantId(doctorId, assistantId);
        if (exists) {
            throw new IllegalStateException("Asistentul este deja asociat cu acest doctor");
        }

        DoctorAssistant relation = DoctorAssistant.builder()
                .doctor(doctor)
                .assistant(assistant)
                .build();

        doctorAssistantRepository.save(relation);
    }

    public void removeAssistantFromDoctor(UUID doctorId, UUID assistantId) {
        DoctorAssistant relation = doctorAssistantRepository.findByDoctorIdAndAssistantId(doctorId, assistantId)
                .orElseThrow(() -> new EntityNotFoundException("Relația doctor-asistent nu a fost găsită"));

        doctorAssistantRepository.delete(relation);
    }

    public List<AssistantResponse> getAssistantsForDoctor(UUID doctorId) {
        return doctorAssistantRepository.findByDoctorId(doctorId).stream()
                .map(DoctorAssistant::getAssistant)
                .map(AssistantResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<DoctorDto> getDoctorsByClinic(UUID clinicId) {
        return doctorRepository.findByCabinetId(clinicId).stream()
                .map(DoctorDto::fromEntity)
                .collect(Collectors.toList());
    }
}

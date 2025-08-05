package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.Cabinet;
import com.asusoftware.Clinic_api.model.Patient;
import com.asusoftware.Clinic_api.model.User;
import com.asusoftware.Clinic_api.model.dto.PatientRequest;
import com.asusoftware.Clinic_api.model.dto.PatientResponse;
import com.asusoftware.Clinic_api.repository.CabinetRepository;
import com.asusoftware.Clinic_api.repository.PatientRepository;
import com.asusoftware.Clinic_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final CabinetRepository cabinetRepository;

    public PatientResponse createPatient(PatientRequest request, UserDetails userDetails) {
        User creator = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Cabinet cabinet = cabinetRepository.findById(request.getCabinetId())
                .orElseThrow(() -> new EntityNotFoundException("Cabinet not found"));

        Patient patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .cnp(request.getCnp())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .emergencyContact(request.getEmergencyContact())
                .cabinet(cabinet)
                .createdBy(creator)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        patientRepository.save(patient);
        return PatientResponse.fromEntity(patient);
    }

    public List<PatientResponse> getPatientsForCabinet(UUID cabinetId) {
        List<Patient> patients = patientRepository.findByCabinetId(cabinetId);
        return patients.stream().map(PatientResponse::fromEntity).collect(Collectors.toList());
    }

    public Page<PatientResponse> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable)
                .map(PatientResponse::fromEntity);
    }

    public Page<PatientResponse> getPatientsByCabinet(UUID cabinetId, Pageable pageable) {
        return patientRepository.findByCabinetId(cabinetId, pageable)
                .map(PatientResponse::fromEntity);
    }

    public PatientResponse getPatientById(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pacientul nu a fost găsit"));
        return PatientResponse.fromEntity(patient);
    }

    public PatientResponse updatePatient(UUID id, PatientRequest request, UserDetails userDetails) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pacientul nu a fost găsit"));

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setGender(request.getGender());
        patient.setCnp(request.getCnp());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setAddress(request.getAddress());
        patient.setEmergencyContact(request.getEmergencyContact());
        patient.setUpdatedAt(LocalDateTime.now());
        User updater = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        patient.setCreatedBy(updater);

        patientRepository.save(patient);
        return PatientResponse.fromEntity(patient);
    }

    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new EntityNotFoundException("Pacientul nu a fost găsit pentru ștergere");
        }
        patientRepository.deleteById(id);
    }
}


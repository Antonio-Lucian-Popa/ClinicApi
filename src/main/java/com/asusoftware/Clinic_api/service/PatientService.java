package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.*;
import com.asusoftware.Clinic_api.model.dto.PatientRequest;
import com.asusoftware.Clinic_api.model.dto.PatientResponse;
import com.asusoftware.Clinic_api.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

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
    private final ClinicHistoryService clinicHistoryService;
    private final OwnerRepository ownerRepository;
    private final DoctorRepository doctorRepository;
    private final AssistantRepository assistantRepository;
    private final PatientMedicalHistoryRepository patientMedicalHistoryRepository;
    private final PatientAllergyRepository patientAllergyRepository;

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

        // === Salvare istoricul medical ===
        for (String condition : request.getMedicalHistory()) {
            patientMedicalHistoryRepository.save(
                    PatientMedicalHistory.builder()
                            .patient(patient)
                            .medicalHistory(condition)
                            .build()
            );
        }

        // === Salvare alergii ===
        for (String allergy : request.getAllergies()) {
            patientAllergyRepository.save(
                    PatientAllergy.builder()
                            .patient(patient)
                            .allergies(allergy)
                            .build()
            );
        }

        clinicHistoryService.recordAction(
                patient.getCabinet().getId(),
                creator.getId(),
                "CREARE PACIENT",
                "A fost creat pacientul " + patient.getFirstName() + " " + patient.getLastName()
        );

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

        /*if (!patient.getCabinet().getOwner().getUser().getId().equals(updater.getId())) {
            throw new AccessDeniedException("Nu ai dreptul să modifici acest pacient.");
        }*/
        patient.setCreatedBy(updater);

        patientRepository.save(patient);
        return PatientResponse.fromEntity(patient);
    }

    public int getNewPatientsThisMonth(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();

        if (hasRole(user, "OWNER")) {
            Owner owner = ownerRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            List<UUID> cabinetIds = cabinetRepository.findByOwnerId(owner.getId())
                    .stream().map(Cabinet::getId).toList();
            return patientRepository.countByCabinetIdsAndCreatedAtAfter(cabinetIds, startOfMonth);
        }

        if (hasRole(user, "DOCTOR")) {
            Doctor doctor = doctorRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            return patientRepository.countNewPatientsThisMonthForDoctor(doctor.getId(), startOfMonth);
        }

        if (hasRole(user, "ASSISTANT")) {
            Assistant assistant = assistantRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Assistant not found"));
            return patientRepository.countNewPatientsThisMonthForAssistant(assistant.getId(), startOfMonth);
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
    }

    private boolean hasRole(User user, String role) {
        return user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(r -> r.equalsIgnoreCase(role));
    }


    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new EntityNotFoundException("Pacientul nu a fost găsit pentru ștergere");
        }
        patientRepository.deleteById(id);
    }

    public PatientResponse mapToResponse(Patient patient) {
        List<String> medicalHistory = patientMedicalHistoryRepository
                .findByPatientId(patient.getId())
                .stream()
                .map(PatientMedicalHistory::getMedicalHistory) // sau .getName(), cum ai definit
                .collect(Collectors.toList());

        List<String> allergies = patientAllergyRepository
                .findByPatientId(patient.getId())
                .stream()
                .map(PatientAllergy::getAllergies)
                .collect(Collectors.toList());

        return PatientResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .cnp(patient.getCnp())
                .address(patient.getAddress())
                .emergencyContact(patient.getEmergencyContact())
                .cabinetId(patient.getCabinet().getId())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .createdBy(patient.getCreatedBy().getId())
                .medicalHistory(medicalHistory)
                .allergies(allergies)
                .build();
    }

}


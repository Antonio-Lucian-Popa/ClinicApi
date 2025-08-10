package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.Patient;
import com.asusoftware.Clinic_api.model.PatientAllergy;
import com.asusoftware.Clinic_api.model.PatientMedicalHistory;
import com.asusoftware.Clinic_api.model.dto.MedicalRecordResponse;
import com.asusoftware.Clinic_api.model.dto.MedicalRecordUpdateRequest;
import com.asusoftware.Clinic_api.repository.PatientAllergyRepository;
import com.asusoftware.Clinic_api.repository.PatientMedicalHistoryRepository;
import com.asusoftware.Clinic_api.repository.PatientRepository;
import com.asusoftware.Clinic_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {
    private final PatientRepository patientRepository;
    private final PatientMedicalHistoryRepository patientMedicalHistoryRepository;
    private final PatientAllergyRepository patientAllergyRepository;
    private final UserRepository userRepository; // dacă vrei verificări de acces

    @Transactional(readOnly = true)
    public MedicalRecordResponse getByPatient(UUID patientId, UserDetails userDetails) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Pacientul nu a fost găsit"));

        // opțional: verifică drepturi (același cabinet, sau roluri etc.)

        List<String> history = patientMedicalHistoryRepository.findByPatientId(patientId)
                .stream().map(PatientMedicalHistory::getMedicalHistory).toList();

        List<String> allergies = patientAllergyRepository.findByPatientId(patientId)
                .stream().map(PatientAllergy::getAllergies).toList();

        return MedicalRecordResponse.builder()
                .patientId(patientId)
                .medicalHistory(history)
                .allergies(allergies)
                .build();
    }

    @Transactional
    public MedicalRecordResponse updateForPatient(UUID patientId, MedicalRecordUpdateRequest req, UserDetails userDetails) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Pacientul nu a fost găsit"));

        // opțional: verifică drepturi

        // — sync istoricul medical, doar dacă a fost trimis —
        if (req.getMedicalHistory() != null) {
            List<String> target = normalize(req.getMedicalHistory());
            var existing = patientMedicalHistoryRepository.findByPatientId(patientId);

            var existingSet = existing.stream().map(PatientMedicalHistory::getMedicalHistory).collect(Collectors.toSet());
            var targetSet   = new HashSet<>(target);

            var toAdd = targetSet.stream()
                    .filter(v -> !existingSet.contains(v))
                    .map(v -> PatientMedicalHistory.builder().patient(patient).medicalHistory(v).build())
                    .toList();

            var toDelete = existing.stream()
                    .filter(e -> !targetSet.contains(e.getMedicalHistory()))
                    .toList();

            if (!toDelete.isEmpty()) patientMedicalHistoryRepository.deleteAllInBatch(toDelete);
            if (!toAdd.isEmpty())    patientMedicalHistoryRepository.saveAll(toAdd);
        }

        // — sync alergii, doar dacă au fost trimise —
        if (req.getAllergies() != null) {
            List<String> target = normalize(req.getAllergies());
            var existing = patientAllergyRepository.findByPatientId(patientId);

            var existingSet = existing.stream().map(PatientAllergy::getAllergies).collect(Collectors.toSet());
            var targetSet   = new HashSet<>(target);

            var toAdd = targetSet.stream()
                    .filter(v -> !existingSet.contains(v))
                    .map(v -> PatientAllergy.builder().patient(patient).allergies(v).build())
                    .toList();

            var toDelete = existing.stream()
                    .filter(e -> !targetSet.contains(e.getAllergies()))
                    .toList();

            if (!toDelete.isEmpty()) patientAllergyRepository.deleteAllInBatch(toDelete);
            if (!toAdd.isEmpty())    patientAllergyRepository.saveAll(toAdd);
        }

        return getByPatient(patientId, userDetails);
    }

    private static List<String> normalize(List<String> input) {
        return input.stream()
                .map(s -> s == null ? "" : s.trim())
                .filter(s -> !s.isEmpty())
                .distinct()
                .toList();
    }
}

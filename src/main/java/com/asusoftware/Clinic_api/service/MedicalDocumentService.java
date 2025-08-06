package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.MedicalDocument;
import com.asusoftware.Clinic_api.model.Patient;
import com.asusoftware.Clinic_api.model.User;
import com.asusoftware.Clinic_api.model.dto.MedicalDocumentRequest;
import com.asusoftware.Clinic_api.model.dto.MedicalDocumentResponse;
import com.asusoftware.Clinic_api.repository.MedicalDocumentRepository;
import com.asusoftware.Clinic_api.repository.PatientRepository;
import com.asusoftware.Clinic_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalDocumentService {

    private final MedicalDocumentRepository medicalDocumentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public MedicalDocumentResponse uploadDocument(MedicalDocumentRequest request, MultipartFile file, UserDetails userDetails) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Pacientul nu a fost găsit"));

        User uploadedBy = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Utilizatorul nu a fost găsit"));

        String fileUrl = fileStorageService.storeFile(file);

        MedicalDocument document = MedicalDocument.builder()
                .patient(patient)
                .uploadedBy(uploadedBy)
                .documentType(request.getDocumentType())
                .fileUrl(fileUrl)
                .notes(request.getNotes())
                .uploadedAt(LocalDateTime.now())
                .build();

        medicalDocumentRepository.save(document);

        return MedicalDocumentResponse.fromEntity(document);
    }

    public List<MedicalDocumentResponse> getDocumentsByPatient(UUID patientId) {
        List<MedicalDocument> documents = medicalDocumentRepository.findByPatientId(patientId);
        return documents.stream()
                .map(MedicalDocumentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteDocument(UUID id) {
        if (!medicalDocumentRepository.existsById(id)) {
            throw new EntityNotFoundException("Documentul nu a fost găsit");
        }
        medicalDocumentRepository.deleteById(id);
    }
}
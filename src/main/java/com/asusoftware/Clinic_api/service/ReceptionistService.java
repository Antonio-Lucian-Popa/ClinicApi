package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.Cabinet;
import com.asusoftware.Clinic_api.model.Receptionist;
import com.asusoftware.Clinic_api.model.User;
import com.asusoftware.Clinic_api.model.dto.ReceptionistResponse;
import com.asusoftware.Clinic_api.repository.CabinetRepository;
import com.asusoftware.Clinic_api.repository.ReceptionistRepository;
import com.asusoftware.Clinic_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceptionistService {

    private final ReceptionistRepository receptionistRepository;
    private final UserRepository userRepository;
    private final CabinetRepository cabinetRepository;

    public ReceptionistResponse createReceptionist(UUID userId, UUID cabinetId) {
        if (receptionistRepository.existsByUserId(userId)) {
            throw new IllegalStateException("Utilizatorul este deja înregistrat ca recepționist.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilizatorul nu a fost găsit"));

        Cabinet cabinet = cabinetRepository.findById(cabinetId)
                .orElseThrow(() -> new EntityNotFoundException("Cabinetul nu a fost găsit"));

        Receptionist receptionist = Receptionist.builder()
                .user(user)
                .cabinet(cabinet)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        receptionistRepository.save(receptionist);
        return ReceptionistResponse.fromEntity(receptionist);
    }

    public List<ReceptionistResponse> getAllByCabinet(UUID cabinetId) {
        return receptionistRepository.findByCabinetId(cabinetId).stream()
                .map(ReceptionistResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteReceptionist(UUID id) {
        if (!receptionistRepository.existsById(id)) {
            throw new EntityNotFoundException("Recepționistul nu a fost găsit.");
        }
        receptionistRepository.deleteById(id);
    }

    public ReceptionistResponse getById(UUID id) {
        Receptionist receptionist = receptionistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recepționistul nu a fost găsit."));
        return ReceptionistResponse.fromEntity(receptionist);
    }
}
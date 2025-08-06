package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.Cabinet;
import com.asusoftware.Clinic_api.model.ClinicHistory;
import com.asusoftware.Clinic_api.model.User;
import com.asusoftware.Clinic_api.model.dto.ClinicHistoryResponse;
import com.asusoftware.Clinic_api.repository.CabinetRepository;
import com.asusoftware.Clinic_api.repository.ClinicHistoryRepository;
import com.asusoftware.Clinic_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClinicHistoryService {

    private final ClinicHistoryRepository historyRepository;
    private final CabinetRepository cabinetRepository;
    private final UserRepository userRepository;

    public void recordAction(UUID cabinetId, UUID userId, String action, String details) {
        Cabinet cabinet = cabinetRepository.findById(cabinetId)
                .orElseThrow(() -> new EntityNotFoundException("Cabinet inexistent"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilizator inexistent"));

        ClinicHistory history = ClinicHistory.builder()
                .cabinet(cabinet)
                .user(user)
                .action(action)
                .details(details)
                .createdAt(LocalDateTime.now())
                .build();

        historyRepository.save(history);
    }

    public Page<ClinicHistoryResponse> getHistoryByCabinet(UUID cabinetId, Pageable pageable) {
        return historyRepository.findByCabinetId(cabinetId, pageable)
                .map(ClinicHistoryResponse::fromEntity);
    }
}

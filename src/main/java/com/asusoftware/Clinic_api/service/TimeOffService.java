package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.TimeOffRequest;
import com.asusoftware.Clinic_api.model.User;
import com.asusoftware.Clinic_api.model.dto.TimeOffResponse;
import com.asusoftware.Clinic_api.repository.TimeOffRequestRepository;
import com.asusoftware.Clinic_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeOffService {

    private final TimeOffRequestRepository timeOffRepository;
    private final UserRepository userRepository;

    public TimeOffResponse requestTimeOff(TimeOffRequest request, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        TimeOffRequest timeOff = TimeOffRequest.builder()
                .user(user)
                .role(request.getRole())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        timeOffRepository.save(timeOff);
        return TimeOffResponse.fromEntity(timeOff);
    }

    public List<TimeOffResponse> getTimeOffsForUser(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return timeOffRepository.findByUserId(user.getId()).stream()
                .map(TimeOffResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<TimeOffResponse> getAllTimeOffs() {
        return timeOffRepository.findAll().stream()
                .map(TimeOffResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public TimeOffResponse approve(UUID id, UserDetails approverDetails) {
        TimeOffRequest timeOff = timeOffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cererea nu a fost găsită"));

        User approver = userRepository.findByEmail(approverDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        timeOff.setStatus("APPROVED");
        timeOff.setApprovedBy(approver);
        timeOff.setApprovedAt(LocalDateTime.now());
        timeOff.setUpdatedAt(LocalDateTime.now());

        timeOffRepository.save(timeOff);
        return TimeOffResponse.fromEntity(timeOff);
    }

    public void deleteRequest(UUID id) {
        if (!timeOffRepository.existsById(id)) {
            throw new EntityNotFoundException("Cererea nu există");
        }
        timeOffRepository.deleteById(id);
    }
}
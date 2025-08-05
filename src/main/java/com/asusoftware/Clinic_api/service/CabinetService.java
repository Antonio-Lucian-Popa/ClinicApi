package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.Cabinet;
import com.asusoftware.Clinic_api.model.Owner;
import com.asusoftware.Clinic_api.model.User;
import com.asusoftware.Clinic_api.model.dto.CabinetRequest;
import com.asusoftware.Clinic_api.model.dto.CabinetResponse;
import com.asusoftware.Clinic_api.repository.CabinetRepository;
import com.asusoftware.Clinic_api.repository.OwnerRepository;
import com.asusoftware.Clinic_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CabinetService {

    private final CabinetRepository cabinetRepository;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;

    public CabinetResponse createCabinet(CabinetRequest request, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Owner owner = ownerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Owner profile not found"));

        if (cabinetRepository.existsByOwnerAndName(owner, request.getName())) {
            throw new IllegalArgumentException("Ai deja un cabinet cu acest nume.");
        }

        Cabinet cabinet = Cabinet.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        cabinet = cabinetRepository.save(cabinet);
        return mapToResponse(cabinet);
    }

    public List<CabinetResponse> getMyCabinets(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Owner owner = ownerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Owner profile not found"));

        return cabinetRepository.findByOwnerId(owner.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CabinetResponse getCabinetById(UUID id, UserDetails userDetails) {
        Cabinet cabinet = cabinetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cabinetul nu a fost găsit."));

        verifyOwnership(userDetails, cabinet);

        return mapToResponse(cabinet);
    }

    public CabinetResponse updateCabinet(UUID id, CabinetRequest request, UserDetails userDetails) {
        Cabinet cabinet = cabinetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cabinetul nu a fost găsit."));

        verifyOwnership(userDetails, cabinet);

        cabinet.setName(request.getName());
        cabinet.setAddress(request.getAddress());
        cabinet.setPhone(request.getPhone());
        cabinet.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(cabinetRepository.save(cabinet));
    }

    private void verifyOwnership(UserDetails userDetails, Cabinet cabinet) {
        String email = userDetails.getUsername();
        if (!cabinet.getOwner().getUser().getEmail().equals(email)) {
            throw new SecurityException("Nu ai permisiunea să accesezi acest cabinet.");
        }
    }

    private CabinetResponse mapToResponse(Cabinet cabinet) {
        return CabinetResponse.builder()
                .id(cabinet.getId())
                .name(cabinet.getName())
                .address(cabinet.getAddress())
                .phone(cabinet.getPhone())
                .ownerId(cabinet.getOwner().getId())
                .createdAt(cabinet.getCreatedAt())
                .updatedAt(cabinet.getUpdatedAt())
                .build();
    }
}
package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.Cabinet;
import com.asusoftware.Clinic_api.model.Material;
import com.asusoftware.Clinic_api.model.User;
import com.asusoftware.Clinic_api.model.dto.MaterialRequest;
import com.asusoftware.Clinic_api.model.dto.MaterialResponse;
import com.asusoftware.Clinic_api.repository.CabinetRepository;
import com.asusoftware.Clinic_api.repository.MaterialRepository;
import com.asusoftware.Clinic_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final CabinetRepository cabinetRepository;
    private final UserRepository userRepository;
    private ClinicHistoryService clinicHistoryService;

    public MaterialResponse createMaterial(MaterialRequest request, UserDetails userDetails) {
        Cabinet cabinet = cabinetRepository.findById(request.getCabinetId())
                .orElseThrow(() -> new EntityNotFoundException("Cabinetul nu a fost găsit"));

        Material material = Material.builder()
                .name(request.getName())
                .unit(request.getUnit())
                .cabinet(cabinet)
                .createdAt(LocalDateTime.now())
                .build();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Utilizatorul nu a fost găsit"));

        // Record the creation action in the clinic history
        clinicHistoryService.recordAction(
                cabinet.getId(),
                user.getId(), // Assuming no user is associated with material creation
                "CREATE_MATERIAL",
                "Material created: " + material.getName()
        );

        materialRepository.save(material);

        return MaterialResponse.fromEntity(material);
    }

    public Page<MaterialResponse> getAllMaterials(Pageable pageable) {
        return materialRepository.findAll(pageable)
                .map(MaterialResponse::fromEntity);
    }

    public MaterialResponse getMaterialById(UUID id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Materialul nu a fost găsit"));
        return MaterialResponse.fromEntity(material);
    }

    public MaterialResponse updateMaterial(UUID id, MaterialRequest request, UserDetails userDetails) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Materialul nu a fost găsit"));

        Cabinet cabinet = cabinetRepository.findById(request.getCabinetId())
                .orElseThrow(() -> new EntityNotFoundException("Cabinetul nu a fost găsit"));

        material.setName(request.getName());
        material.setUnit(request.getUnit());
        material.setCabinet(cabinet);

        materialRepository.save(material);

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Utilizatorul nu a fost găsit"));

        // Record the update action in the clinic history
        clinicHistoryService.recordAction(
                cabinet.getId(),
                user.getId(), // Assuming no user is associated with material updates
                "UPDATE_MATERIAL",
                "Material updated: " + material.getName()
        );

        return MaterialResponse.fromEntity(material);
    }

    public void deleteMaterial(UUID id) {
        if (!materialRepository.existsById(id)) {
            throw new EntityNotFoundException("Materialul nu a fost găsit");
        }
        materialRepository.deleteById(id);
    }
}

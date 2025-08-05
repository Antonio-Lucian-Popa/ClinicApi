package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.Cabinet;
import com.asusoftware.Clinic_api.model.Material;
import com.asusoftware.Clinic_api.model.dto.MaterialRequest;
import com.asusoftware.Clinic_api.model.dto.MaterialResponse;
import com.asusoftware.Clinic_api.repository.CabinetRepository;
import com.asusoftware.Clinic_api.repository.MaterialRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final CabinetRepository cabinetRepository;

    public MaterialResponse createMaterial(MaterialRequest request) {
        Cabinet cabinet = cabinetRepository.findById(request.getCabinetId())
                .orElseThrow(() -> new EntityNotFoundException("Cabinetul nu a fost găsit"));

        Material material = Material.builder()
                .name(request.getName())
                .unit(request.getUnit())
                .cabinet(cabinet)
                .createdAt(LocalDateTime.now())
                .build();

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

    public MaterialResponse updateMaterial(UUID id, MaterialRequest request) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Materialul nu a fost găsit"));

        Cabinet cabinet = cabinetRepository.findById(request.getCabinetId())
                .orElseThrow(() -> new EntityNotFoundException("Cabinetul nu a fost găsit"));

        material.setName(request.getName());
        material.setUnit(request.getUnit());
        material.setCabinet(cabinet);

        materialRepository.save(material);
        return MaterialResponse.fromEntity(material);
    }

    public void deleteMaterial(UUID id) {
        if (!materialRepository.existsById(id)) {
            throw new EntityNotFoundException("Materialul nu a fost găsit");
        }
        materialRepository.deleteById(id);
    }
}

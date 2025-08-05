package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MaterialRepository extends JpaRepository<Material, UUID> {
    List<Material> findByCabinetId(UUID cabinetId);
    boolean existsByCabinetIdAndName(UUID cabinetId, String name);
}
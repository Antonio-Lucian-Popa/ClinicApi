package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.Cabinet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CabinetRepository extends JpaRepository<Cabinet, UUID> {
    List<Cabinet> findByOwnerId(UUID ownerId);
    boolean existsByOwnerIdAndName(UUID ownerId, String name);
}

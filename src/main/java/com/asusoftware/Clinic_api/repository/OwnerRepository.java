package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, UUID> {
    Optional<Owner> findByUserId(UUID userId);
}

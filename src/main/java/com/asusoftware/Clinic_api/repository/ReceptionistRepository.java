package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.Receptionist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReceptionistRepository extends JpaRepository<Receptionist, UUID> {
    Optional<Receptionist> findByUserId(UUID userId);
    List<Receptionist> findByCabinetId(UUID cabinetId);

    boolean existsByUserId(UUID userId);
    boolean existsByUserIdAndCabinetId(UUID userId, UUID cabinetId); // în ReceptionistRepository & DoctorRepository

}

package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    Optional<Doctor> findByUserId(UUID userId);
    List<Doctor> findByCabinetId(UUID cabinetId);
}

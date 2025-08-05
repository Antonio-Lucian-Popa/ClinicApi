package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    List<Patient> findByCabinetId(UUID cabinetId);
}

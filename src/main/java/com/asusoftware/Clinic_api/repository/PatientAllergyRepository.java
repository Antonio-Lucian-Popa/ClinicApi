package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.PatientAllergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientAllergyRepository extends JpaRepository<PatientAllergy, UUID> {
    List<PatientAllergy> findByPatientId(UUID patientId);
}

package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.PatientMedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientMedicalHistoryRepository extends JpaRepository<PatientMedicalHistory, UUID> {
    List<PatientMedicalHistory> findByPatientId(UUID patientId);
}
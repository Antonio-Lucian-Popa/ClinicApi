package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.PatientMedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientMedicalHistoryRepository extends JpaRepository<PatientMedicalHistory, UUID> {
    List<PatientMedicalHistory> findByPatientId(UUID patientId);

    @Modifying
    @Query("DELETE FROM PatientMedicalHistory h WHERE h.patient.id = :patientId AND h.medicalHistory IN :values")
    void deleteByPatientIdAndMedicalHistoryIn(@Param("patientId") UUID patientId,
                                              @Param("values") List<String> values);
}
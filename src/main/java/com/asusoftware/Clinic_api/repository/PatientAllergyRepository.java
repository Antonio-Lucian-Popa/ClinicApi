package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.PatientAllergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientAllergyRepository extends JpaRepository<PatientAllergy, UUID> {
    List<PatientAllergy> findByPatientId(UUID patientId);

    @Modifying
    @Query("DELETE FROM PatientAllergy a WHERE a.patient.id = :patientId AND a.allergies IN :values")
    void deleteByPatientIdAndAllergiesIn(@Param("patientId") UUID patientId,
                                         @Param("values") List<String> values);
}

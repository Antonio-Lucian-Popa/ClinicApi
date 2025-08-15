package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    List<Patient> findByCabinetId(UUID cabinetId);
    Page<Patient> findByCabinetId(UUID cabinetId, Pageable pageable);

    @Query("""
        SELECT p FROM Patient p
        WHERE p.cabinet.id = :cabinetId
          AND (
               :q = ''
            OR LOWER(p.firstName) LIKE CONCAT('%', LOWER(:q), '%')
            OR LOWER(p.lastName)  LIKE CONCAT('%', LOWER(:q), '%')
            OR LOWER(p.email)     LIKE CONCAT('%', LOWER(:q), '%')
            OR LOWER(p.phone)     LIKE CONCAT('%', LOWER(:q), '%')
            OR LOWER(p.cnp)       LIKE CONCAT('%', LOWER(:q), '%')
          )
        """)
    Page<Patient> searchByCabinetAndQuery(@Param("cabinetId") UUID cabinetId,
                                          @Param("q") String q,
                                          Pageable pageable);



    long countByCabinetIdIn(List<UUID> cabinetIds);

    // pentru OWNER
    @Query("SELECT COUNT(p) FROM Patient p WHERE p.cabinet.id IN :cabinetIds AND p.createdAt >= :startOfMonth")
    int countByCabinetIdsAndCreatedAtAfter(@Param("cabinetIds") List<UUID> cabinetIds, @Param("startOfMonth") LocalDateTime startOfMonth);

    // pentru DOCTOR
    @Query("SELECT COUNT(DISTINCT a.patient.id) FROM Appointment a WHERE a.doctor.id = :doctorId AND a.createdAt >= :startOfMonth")
    int countNewPatientsThisMonthForDoctor(@Param("doctorId") UUID doctorId, @Param("startOfMonth") LocalDateTime startOfMonth);

    // pentru ASSISTANT
    @Query("""
    SELECT COUNT(DISTINCT a.patient.id)
    FROM Appointment a
    WHERE a.assistant.id = :assistantId AND a.createdAt >= :startOfMonth
""")
    int countNewPatientsThisMonthForAssistant(@Param("assistantId") UUID assistantId, @Param("startOfMonth") LocalDateTime startOfMonth);

}

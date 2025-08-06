package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.MaterialUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MaterialUsageRepository extends JpaRepository<MaterialUsage, UUID> {
    List<MaterialUsage> findByDoctorId(UUID doctorId);
    List<MaterialUsage> findByAppointmentId(UUID appointmentId);

    @Query("""
    SELECT COUNT(mu)
    FROM MaterialUsage mu
    WHERE (
        (mu.doctor IS NOT NULL AND mu.doctor.cabinet.id IN :cabinetIds)
        OR
        (mu.assistant IS NOT NULL AND mu.assistant IN (
            SELECT da.assistant FROM DoctorAssistant da WHERE da.doctor.cabinet.id IN :cabinetIds
        ))
    )
    AND mu.usageDate BETWEEN :start AND :end
""")
    int countByCabinetIdsConsideringDoctorAndAssistant(
            @Param("cabinetIds") List<UUID> cabinetIds,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    int countByDoctorIdAndUsageDateBetween(UUID doctorId, LocalDateTime start, LocalDateTime end);
    int countByAssistantIdAndUsageDateBetween(UUID assistantId, LocalDateTime start, LocalDateTime end);

}

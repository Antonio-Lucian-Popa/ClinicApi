package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.MaterialUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MaterialUsageRepository extends JpaRepository<MaterialUsage, UUID> {
    List<MaterialUsage> findByDoctorId(UUID doctorId);
    List<MaterialUsage> findByAppointmentId(UUID appointmentId);

    int countByCabinetIdInAndUsedAtBetween(List<UUID> cabinetIds, LocalDateTime start, LocalDateTime end);

    int countByDoctorIdAndUsedAtBetween(UUID doctorId, LocalDateTime start, LocalDateTime end);
    int countByAssistantIdAndUsedAtBetween(UUID assistantId, LocalDateTime start, LocalDateTime end);

}

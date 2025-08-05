package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.MaterialUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MaterialUsageRepository extends JpaRepository<MaterialUsage, UUID> {
    List<MaterialUsage> findByDoctorId(UUID doctorId);
    List<MaterialUsage> findByAppointmentId(UUID appointmentId);
}

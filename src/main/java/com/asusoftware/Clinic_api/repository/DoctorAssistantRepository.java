package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.DoctorAssistant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorAssistantRepository extends JpaRepository<DoctorAssistant, UUID> {
    List<DoctorAssistant> findByDoctorId(UUID doctorId);
    List<DoctorAssistant> findByAssistantId(UUID assistantId);
    boolean existsByDoctorIdAndAssistantId(UUID doctorId, UUID assistantId);
}

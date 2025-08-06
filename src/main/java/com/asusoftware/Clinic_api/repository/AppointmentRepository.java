package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Page<Appointment> findByDoctorId(UUID doctorId, Pageable pageable);
    Page<Appointment> findByPatientId(UUID patientId, Pageable pageable);

    long countByDoctorCabinetIdIn(List<UUID> cabinetIds);

    long countByDoctorCabinetIdInAndStartTimeAfter(List<UUID> cabinetIds, LocalDateTime time);

    long countByDoctorId(UUID doctorId);

    long countByDoctorIdAndStartTimeAfter(UUID doctorId, LocalDateTime time);

    long countDistinctPatientsByDoctorId(UUID doctorId);

    List<Appointment> findTop10ByDoctorCabinetIdInOrderByStartTimeDesc(List<UUID> cabinetIds);

    List<Appointment> findTop10ByDoctorIdOrderByStartTimeDesc(UUID doctorId);

    List<Appointment> findTop10ByAssistantIdOrderByStartTimeDesc(UUID assistantId);

    // AppointmentRepository
    int countByDoctor_Cabinet_IdInAndStartTimeBetween(List<UUID> cabinetIds, LocalDateTime start, LocalDateTime end);
    int countByDoctor_IdAndStartTimeBetween(UUID doctorId, LocalDateTime start, LocalDateTime end);
    int countByAssistant_IdAndStartTimeBetween(UUID assistantId, LocalDateTime start, LocalDateTime end);
    int countByDoctorCabinetIdInAndStatus(List<UUID> cabinetIds, String status);

    int countByDoctorIdAndStatus(UUID doctorId, String status);
    int countByAssistantIdAndStatus(UUID assistantId, String status);


}
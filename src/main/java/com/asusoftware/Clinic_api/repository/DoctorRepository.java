package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    Optional<Doctor> findByUserId(UUID userId);
    List<Doctor> findByCabinetId(UUID cabinetId);
    boolean existsByUserIdAndCabinetId(UUID userId, UUID cabinetId); // în ReceptionistRepository & DoctorRepository

    List<Doctor> findByCabinet_Owner_Id(UUID ownerId);

}

package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.ClinicHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClinicHistoryRepository extends JpaRepository<ClinicHistory, UUID> {
    Page<ClinicHistory> findByCabinetId(UUID cabinetId, Pageable pageable);
}
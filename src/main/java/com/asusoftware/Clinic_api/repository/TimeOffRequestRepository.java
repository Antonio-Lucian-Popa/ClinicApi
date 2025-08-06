package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.TimeOffRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TimeOffRequestRepository extends JpaRepository<TimeOffRequest, UUID> {
    Page<TimeOffRequest> findByUserId(UUID userId, Pageable pageable);

    List<TimeOffRequest> findByUserId(UUID userId);

    int countByStatus(String status);
}

package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.Cabinet;
import com.asusoftware.Clinic_api.model.Owner;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CabinetRepository extends JpaRepository<Cabinet, UUID> {
    List<Cabinet> findByOwnerId(UUID ownerId);
    boolean existsByOwnerIdAndName(UUID ownerId, String name);

    boolean existsByOwnerAndName(Owner owner, @NotBlank String name);

    @Query("""
        SELECT c FROM Cabinet c
        JOIN Receptionist r ON r.cabinet.id = c.id
        WHERE r.user.id = :userId
    """)
    List<Cabinet> findAllByReceptionistUserId(@Param("userId") UUID userId);
}

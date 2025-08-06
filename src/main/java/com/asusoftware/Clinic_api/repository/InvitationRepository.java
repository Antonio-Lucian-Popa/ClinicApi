package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.Invitation;
import com.asusoftware.Clinic_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    Optional<Invitation> findByEmailAndRole(String email, String role);
    List<Invitation> findByCabinetId(UUID cabinetId);
    Optional<Invitation> findByEmail(String email);

    boolean existsByEmailAndStatus(String email, String status);
    List<Invitation> findByInvitedBy(User user);

}

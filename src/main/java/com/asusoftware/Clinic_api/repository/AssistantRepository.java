package com.asusoftware.Clinic_api.repository;

import com.asusoftware.Clinic_api.model.Assistant;
import com.asusoftware.Clinic_api.model.Cabinet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssistantRepository extends JpaRepository<Assistant, UUID> {
    Optional<Assistant> findByUserId(UUID userId);

    @Query("""
    SELECT a FROM Assistant a
    JOIN DoctorAssistant da ON da.assistant = a
    JOIN Doctor d ON da.doctor = d
    JOIN Cabinet c ON d.cabinet = c
    WHERE c.owner.id = :ownerId
""")
    List<Assistant> findAllByOwnerId(@Param("ownerId") UUID ownerId);


    @Query("""
    select distinct d.cabinet
    from DoctorAssistant da
      join da.doctor d
      join da.assistant a
    where a.user.id = :userId
  """)
    List<Cabinet> findCabinetsForAssistantUser(@Param("userId") UUID userId);

}

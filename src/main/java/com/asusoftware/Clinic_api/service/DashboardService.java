package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.*;
import com.asusoftware.Clinic_api.model.dto.AppointmentResponse;
import com.asusoftware.Clinic_api.model.dto.DashboardResponse;
import com.asusoftware.Clinic_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final DoctorRepository doctorRepository;
    private final AssistantRepository assistantRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final MaterialRepository materialRepository;
    private final CabinetRepository cabinetRepository;

    public DashboardResponse getDashboard(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit."));

        boolean isOwner = user.getRoles().stream()
                .map(Role::getName)
                .anyMatch("OWNER"::equals);

        boolean isDoctor = user.getRoles().stream()
                .map(Role::getName)
                .anyMatch("DOCTOR"::equals);

        if (isOwner) {
            Owner owner = ownerRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Owner-ul nu a fost găsit."));

            List<Cabinet> cabinets = cabinetRepository.findByOwnerId(owner.getId());

            List<UUID> cabinetIds = cabinets.stream()
                    .map(Cabinet::getId)
                    .toList();

            long patients = patientRepository.countByCabinetIdIn(cabinetIds);
            long appointments = appointmentRepository.countByDoctorCabinetIdIn(cabinetIds);
            long upcoming = appointmentRepository.countByDoctorCabinetIdInAndStartTimeAfter(cabinetIds, LocalDateTime.now());
            long materials = materialRepository.countByCabinetIdIn(cabinetIds);

            return DashboardResponse.builder()
                    .totalPatients(patients)
                    .totalAppointments(appointments)
                    .upcomingAppointments(upcoming)
                    .totalMaterials(materials)
                    .build();
        }

        if (isDoctor) {
            Doctor doctor = doctorRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Doctorul nu a fost găsit."));

            long patients = appointmentRepository.countDistinctPatientsByDoctorId(doctor.getId());
            long appointments = appointmentRepository.countByDoctorId(doctor.getId());
            long upcoming = appointmentRepository.countByDoctorIdAndStartTimeAfter(doctor.getId(), LocalDateTime.now());
            long materials = materialRepository.countByCabinetId(doctor.getCabinet().getId());

            return DashboardResponse.builder()
                    .totalPatients(patients)
                    .totalAppointments(appointments)
                    .upcomingAppointments(upcoming)
                    .totalMaterials(materials)
                    .build();
        }

        throw new RuntimeException("Rolul nu este acceptat pentru dashboard.");
    }

    public List<AppointmentResponse> getRecentAppointments(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit."));

        UUID userId = user.getId();
        List<Appointment> appointments;

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        if (roleNames.contains("OWNER")) {
            Owner owner = ownerRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Owner-ul nu a fost găsit."));
            List<Cabinet> cabinets = cabinetRepository.findByOwnerId(owner.getId());
            List<UUID> cabinetIds = cabinets.stream()
                    .map(Cabinet::getId)
                    .toList();

            appointments = appointmentRepository.findTop10ByDoctorCabinetIdInOrderByStartTimeDesc(cabinetIds);

        } else if (roleNames.contains("DOCTOR")) {
            Doctor doctor = doctorRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Doctorul nu a fost găsit."));
            appointments = appointmentRepository.findTop10ByDoctorIdOrderByStartTimeDesc(doctor.getId());

        } else if (roleNames.contains("ASSISTANT")) {
            Assistant assistant = assistantRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Asistentul nu a fost găsit."));
            appointments = appointmentRepository.findTop10ByAssistantIdOrderByStartTimeDesc(assistant.getId());

        } else {
            throw new RuntimeException("Rolul nu este acceptat pentru programări recente.");
        }

        return appointments.stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }


    private String extractRole(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null || roles.isEmpty()) throw new RuntimeException("Rolul lipsește din token.");
        return roles.get(0); // Presupunem un singur rol principal
    }
}

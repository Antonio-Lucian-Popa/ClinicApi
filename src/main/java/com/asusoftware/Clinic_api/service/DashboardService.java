package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.*;
import com.asusoftware.Clinic_api.model.dto.AppointmentResponse;
import com.asusoftware.Clinic_api.model.dto.DashboardResponse;
import com.asusoftware.Clinic_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    public DashboardResponse getDashboard(Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        String role = extractRole(jwt);

        if ("OWNER".equals(role)) {
            Owner owner = ownerRepository.findByUserId(userId)
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

        if ("DOCTOR".equals(role)) {
            Doctor doctor = doctorRepository.findByUserId(userId)
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

    public List<AppointmentResponse> getRecentAppointments(Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        String role = extractRole(jwt);

        List<Appointment> appointments;

        switch (role) {
            case "OWNER" -> {
                Owner owner = ownerRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Owner-ul nu a fost găsit."));
                List<Cabinet> cabinets = cabinetRepository.findByOwnerId(owner.getId());

                List<UUID> cabinetIds = cabinets.stream()
                        .map(Cabinet::getId)
                        .toList();
                appointments = appointmentRepository.findTop10ByDoctorCabinetIdInOrderByStartTimeDesc(cabinetIds);
            }
            case "DOCTOR" -> {
                Doctor doctor = doctorRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Doctorul nu a fost găsit."));
                appointments = appointmentRepository.findTop10ByDoctorIdOrderByStartTimeDesc(doctor.getId());
            }
            case "ASSISTANT" -> {
                Assistant assistant = assistantRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Asistentul nu a fost găsit."));
                appointments = appointmentRepository.findTop10ByAssistantIdOrderByStartTimeDesc(assistant.getId());
            }
            default -> throw new RuntimeException("Rolul nu este acceptat pentru programări recente.");
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

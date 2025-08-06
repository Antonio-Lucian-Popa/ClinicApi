package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.*;
import com.asusoftware.Clinic_api.model.dto.AppointmentResponse;
import com.asusoftware.Clinic_api.model.dto.DashboardResponse;
import com.asusoftware.Clinic_api.model.dto.TimeOffRequestDto;
import com.asusoftware.Clinic_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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
    private final MaterialUsageRepository materialUsageRepository;
    private final CabinetRepository cabinetRepository;
    private final TimeOffRequestRepository timeOffRepository;

    public DashboardResponse getDashboard(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit."));

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        if (roles.contains("OWNER")) {
            Owner owner = ownerRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Owner-ul nu a fost găsit."));

            List<Cabinet> cabinets = cabinetRepository.findByOwnerId(owner.getId());
            List<UUID> cabinetIds = cabinets.stream().map(Cabinet::getId).toList();

            int totalCabinets = cabinets.size();
            int totalDoctors = doctorRepository.findByCabinet_Owner_Id(owner.getId()).size();
            int totalAssistants = assistantRepository.findAllByOwnerId(owner.getId()).size();
            int totalPatients = (int) patientRepository.countByCabinetIdIn(cabinetIds);

            int todayAppointments = appointmentRepository.countByDoctor_Cabinet_IdInAndStartTimeBetween(cabinetIds, startOfDay, endOfDay);
            int pendingAppointments = appointmentRepository.countByDoctorCabinetIdInAndStatus(cabinetIds, "PENDING");
            int todayMaterialUsages = countTodayByCabinetIds(cabinetIds);
            int timeOffRequests = timeOffRepository.countByStatus("PENDING");

            return DashboardResponse.builder()
                    .totalCabinets(totalCabinets)
                    .totalDoctors(totalDoctors)
                    .totalAssistants(totalAssistants)
                    .totalPatients(totalPatients)
                    .todayAppointments(todayAppointments)
                    .pendingAppointments(pendingAppointments)
                    .todayMaterialUsages(todayMaterialUsages)
                    .timeOffRequests(timeOffRequests)
                    .revenueThisMonth(0) // încă neimplementat
                    .build();
        }

        if (roles.contains("DOCTOR")) {
            Doctor doctor = doctorRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Doctorul nu a fost găsit."));

            int todayAppointments = appointmentRepository.countByDoctor_IdAndStartTimeBetween(doctor.getId(), startOfDay, endOfDay);
            int pendingAppointments = appointmentRepository.countByDoctorIdAndStatus(doctor.getId(), "PENDING");
            LocalDateTime start = LocalDate.now().atStartOfDay();
            LocalDateTime end = start.plusDays(1);

            int usagesToday = materialUsageRepository.countByDoctorIdAndUsageDateBetween(doctor.getId(), start, end);

            int timeOffRequests = timeOffRepository.findByUserId(user.getId()).size();

            return DashboardResponse.builder()
                    .todayAppointments(todayAppointments)
                    .pendingAppointments(pendingAppointments)
                    .todayMaterialUsages(usagesToday)
                    .timeOffRequests(timeOffRequests)
                    .revenueThisMonth(0)
                    .build();
        }

        if (roles.contains("ASSISTANT")) {
            Assistant assistant = assistantRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Asistentul nu a fost găsit."));

            int todayAppointments = appointmentRepository.countByAssistant_IdAndStartTimeBetween(assistant.getId(), startOfDay, endOfDay);
            int pendingAppointments = appointmentRepository.countByAssistantIdAndStatus(assistant.getId(), "PENDING");
            LocalDateTime start = LocalDate.now().atStartOfDay();
            LocalDateTime end = start.plusDays(1);

            int usagesToday = materialUsageRepository.countByAssistantIdAndUsageDateBetween(assistant.getId(), start, end);

            int timeOffRequests = timeOffRepository.findByUserId(user.getId()).size();

            return DashboardResponse.builder()
                    .todayAppointments(todayAppointments)
                    .pendingAppointments(pendingAppointments)
                    .todayMaterialUsages(usagesToday)
                    .timeOffRequests(timeOffRequests)
                    .revenueThisMonth(0)
                    .build();
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
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


    public int countTodayByCabinetIds(List<UUID> cabinetIds) {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return materialUsageRepository.countByCabinetIdsConsideringDoctorAndAssistant(cabinetIds, start, end);
    }


    private String extractRole(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null || roles.isEmpty()) throw new RuntimeException("Rolul lipsește din token.");
        return roles.get(0); // Presupunem un singur rol principal
    }
}

package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.*;
import com.asusoftware.Clinic_api.model.dto.InviteRequest;
import com.asusoftware.Clinic_api.repository.*;
import com.asusoftware.Clinic_api.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CabinetRepository cabinetRepository;
    private final DoctorRepository doctorRepository;
    private final AssistantRepository assistantRepository;
    private final DoctorAssistantRepository doctorAssistantRepository;
    private final ReceptionistRepository receptionistRepository;
    private final EmailService emailService;
    private final JwtService jwtService;

    @Value("${frontend.url}")
    private String frontendUrl;

    /**
     * 1. Creează invitația
     */
    public void sendInvitation(InviteRequest request, UserDetails userDetails) {
        User invitedBy = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Utilizator deja înregistrat cu acest email.");
        }

        if (invitationRepository.existsByEmailAndStatus(request.getEmail(), "PENDING")) {
            throw new RuntimeException("Există deja o invitație activă pentru acest email.");
        }

        Cabinet cabinet = cabinetRepository.findById(request.getCabinetId())
                .orElseThrow(() -> new RuntimeException("Cabinet inexistent."));

        Doctor doctor = null;
        if ("ASSISTANT".equals(request.getRole()) && request.getDoctorId() != null) {
            doctor = doctorRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctorul nu există."));
        }

        Invitation invitation = Invitation.builder()
                .email(request.getEmail())
                .role(request.getRole())
                .cabinet(cabinet)
                .doctor(doctor)
                .invitedBy(invitedBy)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        invitationRepository.save(invitation);

        // Generăm token JWT pentru acceptare
        String token = jwtService.generateInvitationToken(invitation);
        String acceptLink = frontendUrl + "/accept-invite?token=" + token;

        Map<String, Object> model = new HashMap<>();
        model.put("role", request.getRole());
        model.put("clinic", cabinet.getName());
        model.put("acceptLink", acceptLink);

        emailService.sendHtmlEmail(
                request.getEmail(),
                "Invitație în platforma clinică",
                "invitation-email",  // fișierul HTML
                model
        );
    }

    /**
     * 2. Acceptă invitația și creează user + entitate
     */
    @Transactional
    public void acceptInvitation(String token, String password, String firstName, String lastName) {
        if (!jwtService.isValidToken(token)) {
            throw new RuntimeException("Token invalid sau expirat.");
        }

        var claims = jwtService.extractAllClaims(token);
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);
        UUID cabinetId = UUID.fromString(claims.get("cabinet_id", String.class));
        UUID doctorId = claims.get("doctor_id") != null
                ? UUID.fromString(claims.get("doctor_id", String.class))
                : null;

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Contul a fost deja creat.");
        }

        // 1. Creează user
        Role roleEntity = roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Rolul nu există în DB"));

        User user = User.builder()
                .email(email)
                .username(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(password) // deja codificat în controller!
                .enabled(true)
                .roles(Set.of(roleEntity))
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        // 2. Creează entitatea asociată rolului
        Cabinet cabinet = cabinetRepository.findById(cabinetId)
                .orElseThrow(() -> new RuntimeException("Cabinetul nu există."));

        switch (role) {
            case "DOCTOR" -> {
                doctorRepository.save(Doctor.builder()
                        .user(user)
                        .cabinet(cabinet)
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .build());
            }

            case "ASSISTANT" -> {
                Assistant assistant = Assistant.builder()
                        .user(user)
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .build();
                assistantRepository.save(assistant);

                if (doctorId != null) {
                    Doctor doctor = doctorRepository.findById(doctorId)
                            .orElseThrow(() -> new RuntimeException("Doctorul nu există."));

                    doctorAssistantRepository.save(DoctorAssistant.builder()
                            .doctor(doctor)
                            .assistant(assistant)
                            .build());
                }
            }

            case "RECEPTIONIST" -> {
                receptionistRepository.save(Receptionist.builder()
                        .user(user)
                        .cabinet(cabinet)
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .build());
            }

            default -> throw new RuntimeException("Rol invalid: " + role);
        }

        // 3. Marchează invitația ca ACCEPTED
        Invitation invitation = invitationRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invitație inexistentă"));
        invitation.setStatus("ACCEPTED");
        invitation.setAcceptedAt(LocalDateTime.now());
        invitationRepository.save(invitation);
    }
}

package com.asusoftware.Clinic_api.controller;

import com.asusoftware.Clinic_api.model.dto.InvitationAcceptRequest;
import com.asusoftware.Clinic_api.model.dto.InvitationResponse;
import com.asusoftware.Clinic_api.model.dto.InviteRequest;
import com.asusoftware.Clinic_api.service.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    /**
     * Trimite o invitație către un utilizator (DOCTOR, ASSISTANT, RECEPTIONIST)
     */
    @PostMapping
    public ResponseEntity<?> sendInvitation(
            @RequestBody @Valid InviteRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        invitationService.sendInvitation(request, userDetails);
        return ResponseEntity.ok("Invitație trimisă cu succes.");
    }

    /**
     * Returnează toate invitațiile trimise de utilizatorul curent (OWNER)
     */
    @GetMapping("/my")
    public ResponseEntity<List<InvitationResponse>> getMyInvitations(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(invitationService.getInvitationsByInviter(userDetails));
    }

    /**
     * Acceptarea unei invitații (de către invitat) - POST cu token + datele userului
     */
    @PostMapping("/accept")
    public ResponseEntity<?> acceptInvitation(@RequestBody @Valid InvitationAcceptRequest request) {
        invitationService.acceptInvitation(
                request.getToken(),
                request.getEncodedPassword(),
                request.getFirstName(),
                request.getLastName()
        );
        return ResponseEntity.ok("Invitația a fost acceptată cu succes.");
    }

    /**
     * Anulează o invitație trimisă (doar dacă e încă PENDING)
     */
    @DeleteMapping("/{invitationId}")
    public ResponseEntity<?> cancelInvitation(@PathVariable UUID invitationId, @AuthenticationPrincipal UserDetails userDetails) {
        invitationService.cancelInvitation(invitationId, userDetails);
        return ResponseEntity.ok("Invitația a fost anulată.");
    }

    /**
     * Verifică dacă un token de invitație este valid (folosit de frontend pentru afișare formular accept)
     */
    @GetMapping("/verify")
    public ResponseEntity<?> verifyInvitation(@RequestParam("token") String token) {
        return ResponseEntity.ok(invitationService.verifyTokenDetails(token));
    }
}

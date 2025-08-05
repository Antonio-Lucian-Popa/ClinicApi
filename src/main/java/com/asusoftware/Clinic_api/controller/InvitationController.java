package com.asusoftware.Clinic_api.controller;

import com.asusoftware.Clinic_api.model.dto.InviteRequest;
import com.asusoftware.Clinic_api.service.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    // POST: Trimite invitație (doar OWNER)
    @PostMapping
    public ResponseEntity<?> sendInvitation(
            @RequestBody @Valid InviteRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        invitationService.sendInvitation(request, userDetails);
        return ResponseEntity.ok("Invitație trimisă cu succes.");
    }

    // GET: Acceptă invitația pe baza tokenului din email
    @GetMapping("/accept")
    public ResponseEntity<?> acceptInvitation(@RequestParam("token") String token) {
        invitationService.acceptInvitation(token);
        return ResponseEntity.ok("Invitație acceptată cu succes.");
    }

    // GET: Opțional - listare invitații trimise de userul curent
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingInvitations(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(invitationService.getPendingInvitations(userDetails));
    }
}

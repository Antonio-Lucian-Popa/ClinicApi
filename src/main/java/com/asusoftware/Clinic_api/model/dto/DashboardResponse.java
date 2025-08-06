package com.asusoftware.Clinic_api.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {
    private Integer totalCabinets;
    private Integer totalDoctors;
    private Integer totalAssistants;
    private Integer totalPatients; // 👈 adăugat

    // OWNER + DOCTOR + ASSISTANT
    private Integer todayAppointments;
    private Integer pendingAppointments;
    private Integer todayMaterialUsages;

    // OWNER => cereri pending, alții => cereri proprii
    private Integer timeOffRequests;

    private int revenueThisMonth = 0;
}

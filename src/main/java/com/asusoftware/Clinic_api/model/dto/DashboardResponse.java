package com.asusoftware.Clinic_api.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {
    private long totalPatients;
    private long totalAppointments;
    private long upcomingAppointments;
    private long totalMaterials;
}

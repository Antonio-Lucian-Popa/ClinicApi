package com.asusoftware.Clinic_api.model.dto;

import com.asusoftware.Clinic_api.model.Doctor;
import lombok.Data;

import java.util.UUID;

@Data
public class DoctorDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String specialization;

    public static DoctorDto fromEntity(Doctor doctor) {
        DoctorDto dto = new DoctorDto();
        dto.setId(doctor.getId());
        dto.setFirstName(doctor.getUser().getFirstName());
        dto.setLastName(doctor.getUser().getLastName());
        dto.setEmail(doctor.getUser().getEmail());

        dto.setSpecialization(doctor.getSpecialization());
        return dto;
    }
}

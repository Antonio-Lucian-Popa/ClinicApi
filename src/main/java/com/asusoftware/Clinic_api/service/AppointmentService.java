package com.asusoftware.Clinic_api.service;

import com.asusoftware.Clinic_api.model.Appointment;
import com.asusoftware.Clinic_api.model.Assistant;
import com.asusoftware.Clinic_api.model.Doctor;
import com.asusoftware.Clinic_api.model.Patient;
import com.asusoftware.Clinic_api.model.dto.AppointmentRequest;
import com.asusoftware.Clinic_api.model.dto.AppointmentResponse;
import com.asusoftware.Clinic_api.repository.AppointmentRepository;
import com.asusoftware.Clinic_api.repository.AssistantRepository;
import com.asusoftware.Clinic_api.repository.DoctorRepository;
import com.asusoftware.Clinic_api.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final AssistantRepository assistantRepository;
    private final PatientRepository patientRepository;

    public AppointmentResponse createAppointment(AppointmentRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctorul nu a fost găsit"));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Pacientul nu a fost găsit"));

        Assistant assistant = null;
        if (request.getAssistantId() != null) {
            assistant = assistantRepository.findById(request.getAssistantId())
                    .orElseThrow(() -> new EntityNotFoundException("Asistentul nu a fost găsit"));
        }

        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .assistant(assistant)
                .patient(patient)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status("SCHEDULED")
                .notes(request.getNotes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        appointmentRepository.save(appointment);
        return AppointmentResponse.fromEntity(appointment);
    }

    public AppointmentResponse updateAppointment(UUID id, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Programarea nu a fost găsită"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctorul nu a fost găsit"));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Pacientul nu a fost găsit"));

        Assistant assistant = null;
        if (request.getAssistantId() != null) {
            assistant = assistantRepository.findById(request.getAssistantId())
                    .orElseThrow(() -> new EntityNotFoundException("Asistentul nu a fost găsit"));
        }

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAssistant(assistant);
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(request.getEndTime());
        appointment.setStatus(request.getStatus());
        appointment.setNotes(request.getNotes());
        appointment.setUpdatedAt(LocalDateTime.now());

        appointmentRepository.save(appointment);
        return AppointmentResponse.fromEntity(appointment);
    }

    public void deleteAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Programarea nu a fost găsită"));

        appointmentRepository.delete(appointment);
    }

    public AppointmentResponse getAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Programarea nu a fost găsită"));

        return AppointmentResponse.fromEntity(appointment);
    }

    public Page<AppointmentResponse> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAll(pageable)
                .map(AppointmentResponse::fromEntity);
    }

    public Page<AppointmentResponse> getAppointmentsByDoctor(UUID doctorId, Pageable pageable) {
        return appointmentRepository.findByDoctorId(doctorId, pageable)
                .map(AppointmentResponse::fromEntity);
    }

    public Page<AppointmentResponse> getAppointmentsByPatient(UUID patientId, Pageable pageable) {
        return appointmentRepository.findByPatientId(patientId, pageable)
                .map(AppointmentResponse::fromEntity);
    }

}
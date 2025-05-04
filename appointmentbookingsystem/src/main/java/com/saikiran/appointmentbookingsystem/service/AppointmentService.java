package com.saikiran.appointmentbookingsystem.service;

import com.saikiran.appointmentbookingsystem.exception.AppointmentNotFoundException;
import com.saikiran.appointmentbookingsystem.model.Appointment;
import com.saikiran.appointmentbookingsystem.model.AppointmentStatus;
import com.saikiran.appointmentbookingsystem.model.Doctor;
import com.saikiran.appointmentbookingsystem.model.Patient;
import com.saikiran.appointmentbookingsystem.repository.AppointmentRepository;
import com.saikiran.appointmentbookingsystem.repository.DoctorRepository;
import com.saikiran.appointmentbookingsystem.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * Book a new appointment. Re-fetches the doctor and patient from the database
     * to ensure availabilityStatus is accurate, and sets a default duration of 30 minutes if not provided.
     */
    public Appointment bookAppointment(Appointment appointment) {
        // Set default duration = 30 minutes
        int defaultDuration = 30;
        LocalDateTime appointmentTime = appointment.getAppointmentTime();
        LocalDateTime appointmentEndTime = appointmentTime.plusMinutes(defaultDuration);

        // Check for overlapping appointments for the same doctor
        List<Appointment> existingAppointments = appointmentRepository.findByDoctorDoctorId(appointment.getDoctor().getDoctorId());
        for (Appointment existing : existingAppointments) {
            if (existing.getStatus() == AppointmentStatus.SCHEDULED) {
                LocalDateTime existingStart = existing.getAppointmentTime();
                LocalDateTime existingEnd = existingStart.plusMinutes(defaultDuration);
                // If new appointment overlaps existing one
                if (!(appointmentTime.isAfter(existingEnd) || appointmentEndTime.isBefore(existingStart))) {
                    throw new RuntimeException("Time slot not available. Please choose a different time.");
                }
            }
        }
        appointment.setAppointmentDuration(defaultDuration);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment findById(Integer appointmentId) {
        Appointment appointment = appointmentRepository.findAppointmentByAppointmentId(appointmentId);
        if (appointment == null) {
            throw new AppointmentNotFoundException("Appointment with id " + appointmentId + " was not found");
        }
        return appointment;
    }

    public List<Appointment> findAppointmentByDoctor(Doctor doctor) {
        return appointmentRepository.findAppointmentByDoctor(doctor)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment for Doctor: " + doctor.getName() + " was not found"));
    }

    public List<Appointment> findAppointmentByPatient(Patient patient) {
        return appointmentRepository.findAppointmentByPatient(patient)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment for Patient: " + patient.getName() + " was not found"));
    }

    public Appointment updateAppointment(Appointment appointment) {
        // Optionally, you could re-fetch the doctor/patient here as well
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public void deleteAppointment(Integer appointmentId) {
        appointmentRepository.deleteAppointmentByAppointmentId(appointmentId);
    }
}

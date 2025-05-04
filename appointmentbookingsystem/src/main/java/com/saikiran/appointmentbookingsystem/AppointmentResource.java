package com.saikiran.appointmentbookingsystem;

import com.saikiran.appointmentbookingsystem.model.*;
import com.saikiran.appointmentbookingsystem.repository.AppointmentRepository;
import com.saikiran.appointmentbookingsystem.repository.DoctorAvailabilityRepository;
import com.saikiran.appointmentbookingsystem.repository.DoctorRepository;
import com.saikiran.appointmentbookingsystem.repository.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "https://appointment-booking-system-frontend.vercel.app/")
@RestController
@RequestMapping("/appointments")
public class AppointmentResource {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;

    public AppointmentResource(AppointmentRepository appointmentRepository,
                                 DoctorRepository doctorRepository,
                                 PatientRepository patientRepository,
                                 DoctorAvailabilityRepository doctorAvailabilityRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getDoctorAppointments(@PathVariable Integer doctorId) {
        return appointmentRepository.findByDoctorDoctorId(doctorId);
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getPatientAppointments(@PathVariable Integer patientId) {
        return appointmentRepository.findByPatientPatientId(patientId);
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment) {
        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getDoctorId()).orElse(null);
        Patient patient = patientRepository.findById(appointment.getPatient().getPatientId()).orElse(null);

        if (doctor == null || patient == null) {
            return ResponseEntity.badRequest().body("Doctor or Patient not found");
        }

        LocalDateTime appointmentStart = appointment.getAppointmentTime();
        LocalDateTime appointmentEnd = appointmentStart.plusMinutes(30);
        System.out.println(appointmentStart);
        System.out.println(appointmentEnd);
        // Save appointment
        appointment.setAppointmentDuration(30);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Update DoctorAvailability slot to mark as booked
        Optional<DoctorAvailability> slotOpt = doctorAvailabilityRepository
                .findByDoctorDoctorId(doctor.getDoctorId())
                .stream()
                .filter(slot ->
                        slot.getStartTime().isEqual(appointmentStart) &&
                                slot.getEndTime().isEqual(appointmentEnd)
                )
                .findFirst();

        slotOpt.ifPresent(slot -> {
            slot.setBooked(true);
            doctorAvailabilityRepository.save(slot);
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(savedAppointment);
    }

    @PutMapping("/cancel/{appointmentId}")
    public String cancelAppointment(@PathVariable Integer appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) return "Appointment not found";

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        // Step 2: Find and update the associated availability slot
        Doctor doctor = appointment.getDoctor();
        LocalDateTime startTime = appointment.getAppointmentTime();
        LocalDateTime endTime = startTime.plusMinutes(appointment.getAppointmentDuration());

        List<DoctorAvailability> availabilities = doctorAvailabilityRepository.findByDoctorDoctorId(doctor.getDoctorId());

        for (DoctorAvailability slot : availabilities) {
            if (slot.getStartTime().isEqual(startTime) && slot.getEndTime().isEqual(endTime)) {
                slot.setBooked(false);
                doctorAvailabilityRepository.save(slot);
                break;
            }
        }

        return "Appointment cancelled";
    }

    @PutMapping("/complete/{appointmentId}")
    public String markAppointmentAsCompleted(@PathVariable Integer appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) return "Appointment not found";

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
        return "Appointment marked as completed";
    }
}

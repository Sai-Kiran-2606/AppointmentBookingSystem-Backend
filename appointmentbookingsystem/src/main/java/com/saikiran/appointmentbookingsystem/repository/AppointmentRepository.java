package com.saikiran.appointmentbookingsystem.repository;

import com.saikiran.appointmentbookingsystem.model.Appointment;
import com.saikiran.appointmentbookingsystem.model.Doctor;
import com.saikiran.appointmentbookingsystem.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Appointment findAppointmentByAppointmentId(Integer appointmentId);

    Optional<List<Appointment>> findAppointmentByDoctor(Doctor doctor);

    Optional<List<Appointment>> findAppointmentByPatient(Patient patient);

    void deleteAppointmentByAppointmentId(Integer appointmentId);

    List<Appointment> findByDoctorDoctorId(Integer doctorId);

    List<Appointment> findByPatientPatientId(Integer patientId);
}

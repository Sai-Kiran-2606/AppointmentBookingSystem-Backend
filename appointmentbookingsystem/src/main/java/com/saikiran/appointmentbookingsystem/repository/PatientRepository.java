package com.saikiran.appointmentbookingsystem.repository;

import com.saikiran.appointmentbookingsystem.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    Optional<Patient> findPatientByName(String name);

    Optional<Patient> findPatientByPatientId(Integer patientId);

    void deletePatientByPatientId(Integer patientId);

    Patient findByEmail(String email);
}

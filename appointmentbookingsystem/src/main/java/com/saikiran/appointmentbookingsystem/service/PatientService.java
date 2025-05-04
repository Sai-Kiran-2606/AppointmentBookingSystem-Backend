package com.saikiran.appointmentbookingsystem.service;

import com.saikiran.appointmentbookingsystem.exception.PatientNotFoundException;
import com.saikiran.appointmentbookingsystem.model.Patient;
import com.saikiran.appointmentbookingsystem.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    @Autowired
    private final PatientRepository patientRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Patient registerPatient(Patient patient) {
        patient.setPassword(passwordEncoder.encode(patient.getPassword())); // Hash password
        return patientRepository.save(patient);
    }

    public PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    public List<Patient> retrieveAllPatients(){
        return patientRepository.findAll();
    }

    public Patient findPatientByID(Integer patientId){
        return patientRepository.findPatientByPatientId(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient with id " + patientId + " was not found"));
    }

    public Patient findPatientByName(String name){
        return patientRepository.findPatientByName(name)
                .orElseThrow(() -> new PatientNotFoundException("Patient with name " + name + " was not found"));
    }

    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Patient patient){
        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(Integer patientId){
        patientRepository.deletePatientByPatientId(patientId);
    }
}

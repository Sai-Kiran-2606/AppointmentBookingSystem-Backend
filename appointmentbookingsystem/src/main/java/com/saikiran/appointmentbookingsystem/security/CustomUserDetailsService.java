package com.saikiran.appointmentbookingsystem.security;

import com.saikiran.appointmentbookingsystem.model.Doctor;
import com.saikiran.appointmentbookingsystem.model.Patient;
import com.saikiran.appointmentbookingsystem.repository.DoctorRepository;
import com.saikiran.appointmentbookingsystem.repository.PatientRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public CustomUserDetailsService(PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Patient patient = patientRepository.findByEmail(email);
        if (patient != null) {
            return new User(patient.getEmail(), patient.getPassword(), Collections.emptyList());
        }

        Doctor doctor = doctorRepository.findByEmail(email);
        if (doctor != null) {
            return new User(doctor.getEmail(), doctor.getPassword(), Collections.emptyList());
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}

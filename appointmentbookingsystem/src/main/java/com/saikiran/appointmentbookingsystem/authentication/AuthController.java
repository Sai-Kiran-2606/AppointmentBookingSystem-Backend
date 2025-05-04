package com.saikiran.appointmentbookingsystem.authentication;

import com.saikiran.appointmentbookingsystem.model.Doctor;
import com.saikiran.appointmentbookingsystem.model.Patient;
import com.saikiran.appointmentbookingsystem.repository.DoctorRepository;
import com.saikiran.appointmentbookingsystem.repository.PatientRepository;
import com.saikiran.appointmentbookingsystem.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://appointment-booking-system-frontend.vercel.app/") // Allow frontend calls
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(PatientRepository patientRepository, DoctorRepository doctorRepository, JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register/patient")
    public String registerPatient(@RequestBody Patient patient) {
        System.out.println("Received Password: " + patient.getPassword()); // Debugging log

        if (patient.getPassword() == null || patient.getPassword().trim().isEmpty()) {
            return "Password cannot be null or empty";
        }

        patient.setPassword(passwordEncoder.encode(patient.getPassword())); // Hash password
        patientRepository.save(patient);
        return "Patient registered successfully!";
    }

    @PostMapping("/register/doctor")
    public String registerDoctor(@RequestBody Doctor doctor) {
        if (doctor.getPassword() == null || doctor.getPassword().isEmpty()) {
            return "Password cannot be null or empty";
        }
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword())); // Hash password
        doctorRepository.save(doctor);
        return "Doctor registered successfully!";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        Patient patient = patientRepository.findByEmail(loginRequest.getEmail());
        if (patient != null && passwordEncoder.matches(loginRequest.getPassword(), patient.getPassword())) {
            String token = jwtUtil.generateToken(patient.getEmail());
            return new LoginResponse(token, "PATIENT", patient.getPatientId());
        }

        Doctor doctor = doctorRepository.findByEmail(loginRequest.getEmail());
        if (doctor != null && passwordEncoder.matches(loginRequest.getPassword(), doctor.getPassword())) {
            String token = jwtUtil.generateToken(doctor.getEmail());
            return new LoginResponse(token, "DOCTOR", doctor.getDoctorId());
        }

        return new LoginResponse(null, "INVALID_CREDENTIALS", null);
    }
}

// DTO for login request
class LoginRequest {
    private String email;
    private String password;

    public String getEmail() { return email; }
    public String getPassword() { return password; }
}

// DTO for login response
class LoginResponse {
    private String token;
    private String role;
    private Integer userId; // new field to store patientId or doctorId

    public LoginResponse(String token, String role, Integer userId) {
        this.token = token;
        this.role = role;
        this.userId = userId;
    }

    public String getToken() { return token; }
    public String getRole() { return role; }
    public Integer getUserId() { return userId; }
}


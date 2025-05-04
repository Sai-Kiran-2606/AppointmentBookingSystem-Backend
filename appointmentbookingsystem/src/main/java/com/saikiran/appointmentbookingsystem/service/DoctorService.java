package com.saikiran.appointmentbookingsystem.service;

import com.saikiran.appointmentbookingsystem.exception.DoctorNotFoundException;
import com.saikiran.appointmentbookingsystem.model.Doctor;
import com.saikiran.appointmentbookingsystem.model.Specialization;
import com.saikiran.appointmentbookingsystem.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor addDoctor(Doctor doctor){
        return doctorRepository.save(doctor);
    }

    public List<Doctor> retrieveAllDoctors(){
        return doctorRepository.findAll();
    }

    public Doctor updateDoctor(Doctor doctor){
        return doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(Integer doctorId){
        doctorRepository.deleteDoctorByDoctorId(doctorId);
    }

    public Doctor findDoctorById(Integer doctorId){
        return doctorRepository.findDoctorByDoctorId(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor by id " + doctorId + " was not found"));
    }

    public List<Doctor> findDoctorsBySpecialization(Specialization specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

}

package com.saikiran.appointmentbookingsystem.repository;

import com.saikiran.appointmentbookingsystem.model.Doctor;
import com.saikiran.appointmentbookingsystem.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    void deleteDoctorByDoctorId(Integer doctorId);

    Optional<Doctor> findDoctorByDoctorId(Integer doctorId);

    Doctor findByEmail(String email);

    List<Doctor> findBySpecialization(Specialization specialization);

//    Optional<Doctor> findDoctorBySpecialisation(Specialization specialisation);
}

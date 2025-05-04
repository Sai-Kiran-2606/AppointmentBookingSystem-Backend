package com.saikiran.appointmentbookingsystem.repository;

import com.saikiran.appointmentbookingsystem.model.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Integer> {
    List<DoctorAvailability> findByDoctorDoctorId(Integer doctorId);
}

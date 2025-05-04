package com.saikiran.appointmentbookingsystem;

import com.saikiran.appointmentbookingsystem.model.Doctor;
import com.saikiran.appointmentbookingsystem.model.DoctorAvailability;
import com.saikiran.appointmentbookingsystem.model.Specialization;
import com.saikiran.appointmentbookingsystem.repository.DoctorAvailabilityRepository;
import com.saikiran.appointmentbookingsystem.repository.DoctorRepository;
import com.saikiran.appointmentbookingsystem.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "https://appointment-booking-system-frontend.vercel.app/")
public class DoctorResource {
    @Autowired
    private DoctorService doctorService;

    private final DoctorRepository doctorRepository;

    @Autowired
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    public DoctorResource(DoctorService doctorService, DoctorRepository doctorRepository) {
        this.doctorService = doctorService;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors(){
        List<Doctor> doctors = doctorService.retrieveAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Integer doctorId){
        Doctor doctor = doctorService.findDoctorById(doctorId);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @PutMapping("/doctors/update")
    public ResponseEntity<Doctor> updateDoctor(@RequestBody Doctor doctor){
        Doctor updatedDoctor = doctorService.updateDoctor(doctor);
        return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
    }

    @DeleteMapping("/doctors/delete/{doctorId}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Integer doctorId){
        doctorService.deleteDoctor(doctorId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/doctors/find/{specialization}")
    public ResponseEntity<List<Doctor>> getDoctorBySpecialization(@PathVariable Specialization specialization){
        List<Doctor> doctors = doctorService.findDoctorsBySpecialization(specialization);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @PostMapping("/doctors/{doctorId}/availability")
    public ResponseEntity<?> addAvailability(@PathVariable Integer doctorId, @RequestBody DoctorAvailability availability) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (!doctorOpt.isPresent()) {
            return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
        }
        Doctor doctor = doctorOpt.get();

        LocalDateTime start = availability.getStartTime();
        LocalDateTime end = availability.getEndTime();

        if (start == null || end == null || !start.isBefore(end)) {
            return new ResponseEntity<>("Invalid start or end time", HttpStatus.BAD_REQUEST);
        }

        long duration = Duration.between(start, end).toMinutes();
        if (duration % 30 != 0) {
            return new ResponseEntity<>("Duration must be a multiple of 30 minutes", HttpStatus.BAD_REQUEST);
        }

        List<DoctorAvailability> slots = new ArrayList<>();
        while (start.plusMinutes(30).compareTo(end) <= 0) {
            DoctorAvailability slot = new DoctorAvailability();
            slot.setDoctor(doctor);
            slot.setStartTime(start);
            slot.setEndTime(start.plusMinutes(30));
            slot.setBooked(false);
            slots.add(slot);
            start = start.plusMinutes(30);
        }

        List<DoctorAvailability> savedSlots = doctorAvailabilityRepository.saveAll(slots);
        return new ResponseEntity<>(savedSlots, HttpStatus.CREATED);
    }


    // Endpoint to update an existing availability slot for a doctor
    @PutMapping("/doctors/{doctorId}/availability/{availabilityId}")
    public ResponseEntity<?> updateAvailability(@PathVariable Integer doctorId, @PathVariable Integer availabilityId, @RequestBody DoctorAvailability availability) {
        Optional<DoctorAvailability> existingOpt = doctorAvailabilityRepository.findById(availabilityId);
        if (!existingOpt.isPresent()) {
            return new ResponseEntity<>("Availability slot not found", HttpStatus.NOT_FOUND);
        }
        DoctorAvailability existing = existingOpt.get();
        if (!existing.getDoctor().getDoctorId().equals(doctorId)) {
            return new ResponseEntity<>("Doctor mismatch", HttpStatus.BAD_REQUEST);
        }
        long duration = Duration.between(availability.getStartTime(), availability.getEndTime()).toMinutes();
        if (duration % 30 != 0) {
            return new ResponseEntity<>("Duration must be a multiple of 30 minutes", HttpStatus.BAD_REQUEST);
        }
        existing.setStartTime(availability.getStartTime());
        existing.setEndTime(availability.getEndTime());
        DoctorAvailability updated = doctorAvailabilityRepository.save(existing);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

@GetMapping("/availability")
public ResponseEntity<List<String>> getAvailableTimeSlots(
        @RequestParam Integer doctorId,
        @RequestParam String date
) {
    List<DoctorAvailability> availabilities = doctorAvailabilityRepository.findByDoctorDoctorId(doctorId);
    List<String> availableSlots = new ArrayList<>();

    for (DoctorAvailability slot : availabilities) {
        if (slot.isBooked()) continue;
        if (!slot.getStartTime().toLocalDate().toString().equals(date)) continue;

        String formatted = slot.getStartTime().toLocalTime().toString().substring(0, 5)
                + "-" +
                slot.getEndTime().toLocalTime().toString().substring(0, 5);
        availableSlots.add(formatted);
    }

    return new ResponseEntity<>(availableSlots, HttpStatus.OK);
}

    @GetMapping("/doctors/{doctorId}/availability-by-date")
    public ResponseEntity<List<DoctorAvailability>> getAvailabilityByDate(
            @PathVariable Integer doctorId,
            @RequestParam String date
    ) {
        List<DoctorAvailability> allSlots = doctorAvailabilityRepository.findByDoctorDoctorId(doctorId);

        List<DoctorAvailability> filteredSlots = allSlots.stream()
                .filter(slot -> slot.getStartTime().toLocalDate().toString().equals(date))
                .collect(Collectors.toList());

        return new ResponseEntity<>(filteredSlots, HttpStatus.OK);
    }


}


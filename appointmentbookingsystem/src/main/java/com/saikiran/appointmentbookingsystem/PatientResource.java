package com.saikiran.appointmentbookingsystem;

import com.saikiran.appointmentbookingsystem.model.Patient;
import com.saikiran.appointmentbookingsystem.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://appointment-booking-system-frontend.vercel.app/")
public class PatientResource {
    @Autowired
    private final PatientService patientService;

    public PatientResource(PatientService patientService){
        this.patientService = patientService;
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients(){
        List<Patient> patients = patientService.retrieveAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity<Patient> getPatientByID(@PathVariable Integer patientId){
        Patient patient = patientService.findPatientByID(patientId);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    @GetMapping("/patients/find/{name}")
    public ResponseEntity<Patient> getPatientByName(@PathVariable String name){
        Patient patient = patientService.findPatientByName(name);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    @PutMapping("/patients/update")
    public ResponseEntity<Patient> updatePatient(@RequestBody Patient patient){
        Patient updatedPatient = patientService.updatePatient(patient);
        return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
    }

    @DeleteMapping("/patients/delete/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer patientId){
        patientService.deletePatient(patientId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

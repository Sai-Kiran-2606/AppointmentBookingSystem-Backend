package com.saikiran.appointmentbookingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Doctor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer doctorId;

    private String name;

    @Column(unique = true, nullable = false)
    private String email; // Add this field to store doctor's email

//    @JsonIgnore
    @JsonProperty("password")
    private String password;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    private String location;
    private Double rating;
    private Integer experienceYears;
    private Boolean availabilityStatus;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    public Doctor() {
    }

    public Doctor(Integer doctorId, String name, String email, String password, Specialization specialization, String location, Double rating, Integer experienceYears, Boolean availabilityStatus) {
        super();
        this.doctorId = doctorId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.specialization = specialization;
        this.location = location;
        this.rating = rating;
        this.experienceYears = experienceYears;
        this.availabilityStatus = availabilityStatus;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specializationStr) {
        this.specialization = Specialization.valueOf(specializationStr);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Boolean getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(Boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", name='" + name + '\'' +
                ", specialisation='" + specialization + '\'' +
                ", location='" + location + '\'' +
                ", rating=" + rating +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

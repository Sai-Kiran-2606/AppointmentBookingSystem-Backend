package com.saikiran.appointmentbookingsystem.exception;

public class AppointmentNotFoundException extends RuntimeException{
    public AppointmentNotFoundException(String message){
        super(message);
    }
}

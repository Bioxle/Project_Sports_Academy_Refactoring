package com.example.SprotsAcademy.model;

import java.time.LocalDate;

public class Coach extends User {
    private Sport sport;
    private String degrees;

    // Default constructor
    public Coach() {
        super(); // Call the default constructor of User
    }

    // Parameterized constructor
    public Coach(String name, String surname, String sheet, LocalDate dateOfBirth, String contact, Sport sport, String degrees) {
        super(name, surname, sheet, dateOfBirth, contact); // Call the parameterized constructor of User
        this.sport = sport;
        this.degrees = degrees;
    }

    // Getter and Setter for sport
    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    // Getter and Setter for degrees
    public String getDegrees() {
        return degrees;
    }

    public void setDegrees(String degrees) {
        this.degrees = degrees;
    }
}

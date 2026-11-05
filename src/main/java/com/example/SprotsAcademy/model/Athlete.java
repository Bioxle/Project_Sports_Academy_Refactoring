package com.example.SprotsAcademy.model;

import java.time.LocalDate;

public class Athlete extends User {
    private Boolean professional;
    private int experienceLevel;

    // Default constructor
    public Athlete() {
        super(); // Call the default constructor of User
    }

    // Parameterized constructor
    public Athlete(String name, String surname, String sheet, LocalDate dateOfBirth, String contact, Boolean professional, int experienceLevel) {
        super(name, surname, sheet, dateOfBirth, contact); // Call the parameterized constructor of User
        this.professional = professional;
        this.experienceLevel = experienceLevel;
    }

    public Athlete(int serialNumber,String name, String surname, String sheet, LocalDate dateOfBirth, String contact, Boolean professional, int experienceLevel) {
        super(serialNumber, name, surname, sheet, dateOfBirth, contact); // Call the parameterized constructor of User
        this.professional = professional;
        this.experienceLevel = experienceLevel;
    }

    // Getter and Setter for professional
    public Boolean getProfessional() {
        return professional;
    }

    public void setProfessional(Boolean professional) {
        this.professional = professional;
    }

    // Getter and Setter for experienceLevel
    public int getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(int experienceLevel) {
        this.experienceLevel = experienceLevel;
    }
}

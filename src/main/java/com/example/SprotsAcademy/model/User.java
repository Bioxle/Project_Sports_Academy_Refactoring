package com.example.SprotsAcademy.model;

import java.time.LocalDate;

public abstract class User {
    private int serialNumber;
    private String name;
    private String surname;
    private String sheet;
    private LocalDate dateOfBirth;
    private String contact;
    private LocalDate dateOfRegistration;

    // Default constructor
    protected User() {
    }

    protected User(int serialNumber, String name, String surname, String sheet,
                   LocalDate dateOfBirth, String contact) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.surname = surname;
        this.sheet = sheet;
        this.dateOfBirth = dateOfBirth;
        this.contact = contact;
        this.dateOfRegistration = LocalDate.now();
    }

    // Constructor without serial number (ID assigned later by the service layer)
    protected User(String name, String surname, String sheet,
                   LocalDate dateOfBirth, String contact) {
        this.name = name;
        this.surname = surname;
        this.sheet = sheet;
        this.dateOfBirth = dateOfBirth;
        this.contact = contact;
        this.dateOfRegistration = LocalDate.now();
    }

    // Getter and Setter for serialNumber
    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDate dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for surname
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    // Getter and Setter for sheet
    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    // Getter and Setter for dateOfBirth
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Getter and Setter for contact
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}

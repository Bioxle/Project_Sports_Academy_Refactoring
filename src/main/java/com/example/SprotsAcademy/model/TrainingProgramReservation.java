package com.example.SprotsAcademy.model;

import java.time.LocalDate;

public class TrainingProgramReservation {
    private String uniqueCode;
    private Athlete athlete;
    private TrainingProgram trainingProgram;
    private LocalDate trainingProgramDate;

    public TrainingProgramReservation(String uniqueCode, Athlete athlete, TrainingProgram trainingProgram, LocalDate trainingProgramDate) {
        this.uniqueCode = uniqueCode;
        this.athlete = athlete;
        this.trainingProgram = trainingProgram;
        this.trainingProgramDate = trainingProgramDate;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public TrainingProgram getTrainingProgram() {
        return trainingProgram;
    }

    public void setTrainingProgram(TrainingProgram trainingProgram) {
        this.trainingProgram = trainingProgram;
    }

    public LocalDate getTrainingProgramDate() {
        return trainingProgramDate;
    }

    public void setTrainingProgramDate(LocalDate trainingProgramDate) {
        this.trainingProgramDate = trainingProgramDate;
    }
}

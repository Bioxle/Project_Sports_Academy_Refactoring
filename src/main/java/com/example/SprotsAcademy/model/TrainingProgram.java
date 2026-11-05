package com.example.SprotsAcademy.model;
public class TrainingProgram {
    private int serialNumber;
    private String sport;
    private int facility;
    private String coach;
    private int minimumExp;
    private boolean weekOfTheAthlete;
    private String sheet;
    private int duration;
    private String dayOfTheWeek;

    // Constructor
    public TrainingProgram(int serialNumber, String sport, int facility, String coach, int minimumExp, boolean weekOfTheAthlete, String sheet, int duration, String dayOfTheWeek) {
        this.serialNumber = serialNumber;
        this.sport = sport;
        this.facility = facility;
        this.coach = coach;
        this.minimumExp = minimumExp;
        this.weekOfTheAthlete = weekOfTheAthlete;
        this.sheet = sheet;
        this.duration = duration;
        this.dayOfTheWeek = dayOfTheWeek;
    }

    // Getters and Setters
    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public int getFacility() {
        return facility;
    }

    public void setFacility(int facility) {
        this.facility = facility;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public int getMinimumExp() {
        return minimumExp;
    }

    public void setMinimumExp(int minimumExp) {
        this.minimumExp = minimumExp;
    }

    public boolean isWeekOfTheAthlete() {
        return weekOfTheAthlete;
    }

    public void setWeekOfTheAthlete(boolean weekOfTheAthlete) {
        this.weekOfTheAthlete = weekOfTheAthlete;
    }

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }
}
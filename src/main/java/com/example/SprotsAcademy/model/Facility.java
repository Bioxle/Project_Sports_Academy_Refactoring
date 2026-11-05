package com.example.SprotsAcademy.model;

public class Facility {
    private String nameInstallation;
    private int maxCapacity;

    public Facility(String nameInstallation, int maxCapacity) {
        this.nameInstallation = nameInstallation;
        this.maxCapacity = maxCapacity;
    }

    public String getNameInstallation() {
        return nameInstallation;
    }

    public void setNameInstallation(String nameInstallation) {
        this.nameInstallation = nameInstallation;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public String toString() {
        return nameInstallation; // Return the facility name for ComboBox display
    }
}

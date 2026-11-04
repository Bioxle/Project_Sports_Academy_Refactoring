package com.example.SprotsAcademy.model;

import java.time.LocalDate;

import com.example.SprotsAcademy.util.PriceList;

public class Subscription implements PriceList {

    private int serialNumber;
    private Athlete athlete;
    private TrainingProgram trainingProgram;
    private LocalDate startDate;
    private LocalDate endDate;
    private double monthlyCost;
    private double discountPercentage;

    public Subscription(int serialNumber, Athlete athlete, TrainingProgram trainingProgram, LocalDate startDate,
            LocalDate endDate, double monthlyCost, double discountPercentage) {
        this.serialNumber = serialNumber;
        this.athlete = athlete;
        this.trainingProgram = trainingProgram;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyCost = monthlyCost;
        this.discountPercentage = discountPercentage;
    }

    public double calculateTotalPrice() {
        int totalMonths = getMonthsBetweenDates(startDate, endDate);
        double totalCost = totalMonths * monthlyCost;
        double discountAmount = (totalCost * discountPercentage) / 100;
        return totalCost - discountAmount;
    }

    private int getMonthsBetweenDates(LocalDate start, LocalDate end) {
        return (end.getYear() - start.getYear()) * 12 + end.getMonthValue() - start.getMonthValue();
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getMonthlyCost() {
        return monthlyCost;
    }

    public void setMonthlyCost(double monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}

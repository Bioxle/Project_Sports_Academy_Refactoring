package com.example.SprotsAcademy.model;

import java.time.LocalDate;

import com.example.SprotsAcademy.util.PriceList;

public class Enrollment implements PriceList {
    private int serialNumber;
    private String athleteName;
    private LocalDate date;
    private double cost;
    private double discountPercentage;

    public Enrollment(int serialNumber, String athleteName, LocalDate date, double cost, double discountPercentage) {
        this.serialNumber = serialNumber;
        this.athleteName = athleteName;
        this.date = date;
        this.cost = cost;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double calculateTotalPrice() {
        return cost - (cost * discountPercentage / 100);
    }

    public int getUniqueCode() {
        return serialNumber;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getCost() {
        return cost;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }
}

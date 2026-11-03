package com.example.SprotsAcademy;

import java.time.LocalDate;

import com.example.SprotsAcademy.util.PriceList;

public class Payment implements PriceList {
    private int serialNumber;
    private LocalDate date;
    private String paymentMethod;
    private Enrollment enrollment;
    private double totalCost;
    private String subscriptionOrRegistration;

    public Payment(int serialNumber, LocalDate date, String paymentMethod, Enrollment enrollment,
            String subscriptionOrRegistration) {
        this.serialNumber = serialNumber;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.enrollment = enrollment;
        this.totalCost = enrollment.calculateTotalPrice();
        this.subscriptionOrRegistration = subscriptionOrRegistration;
    }

    public String getSubscriptionOrRegistration() {
        return subscriptionOrRegistration;
    }

    @Override
    public double calculateTotalPrice() {
        return totalCost;
    }

    public int getUniqueCode() {
        return serialNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public double getTotalCost() {
        return totalCost;
    }
}

package com.example.SprotsAcademy.service;

import com.example.SprotsAcademy.model.Athlete;
import com.example.SprotsAcademy.model.Subscription;
import com.example.SprotsAcademy.model.TrainingProgram;
import com.example.SprotsAcademy.repositories.SubscriptionRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Handles all business logic related to subscriptions:
 * creation, cancellation, serial-number generation, and lookups.
 */
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepo;

    public SubscriptionService(SubscriptionRepository subscriptionRepo) {
        this.subscriptionRepo = subscriptionRepo;
    }

    // ── Queries ──────────────────────────────────────────────

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepo.findAll();
    }

    // ── Commands ─────────────────────────────────────────────


    public Subscription createSubscription(Athlete athlete,
                                           TrainingProgram program,
                                           LocalDate startDate,
                                           LocalDate endDate,
                                           double monthlyCost,
                                           double discountPercentage) {

        validateSubscriptionFields(athlete, program, startDate, endDate,
                monthlyCost, discountPercentage);

        int serialNumber = generateNextSerialNumber();

        Subscription subscription = new Subscription(
                serialNumber, athlete, program,
                startDate, endDate, monthlyCost, discountPercentage);

        boolean saved = subscriptionRepo.save(subscription);
        if (!saved) {
            throw new RuntimeException("Failed to persist subscription.");
        }

        return subscription;
    }

    /**
     * Removes the given subscription from the repository.
     *
     * @throws IllegalArgumentException if the subscription is null.
     */
    public void cancelSubscription(Subscription subscription) {
        if (subscription == null) {
            throw new IllegalArgumentException("No subscription selected for cancellation.");
        }

        boolean deleted = subscriptionRepo.delete(subscription);
        if (!deleted) {
            throw new RuntimeException("Failed to cancel subscription.");
        }
    }

    // ── Helpers ──────────────────────────────────────────────

    /**
     * Scans existing subscriptions and returns max(serialNumber) + 1.
     * Returns 1 when there are no subscriptions yet.
     */
    private int generateNextSerialNumber() {
        int maxSerial = 0;
        for (Subscription s : subscriptionRepo.findAll()) {
            if (s.getSerialNumber() > maxSerial) {
                maxSerial = s.getSerialNumber();
            }
        }
        return maxSerial + 1;
    }

    // ── Validation ───────────────────────────────────────────

    private void validateSubscriptionFields(Athlete athlete,
                                            TrainingProgram program,
                                            LocalDate startDate,
                                            LocalDate endDate,
                                            double monthlyCost,
                                            double discountPercentage) {

        if (athlete == null) {
            throw new IllegalArgumentException("Athlete is required.");
        }
        if (program == null) {
            throw new IllegalArgumentException("Training program is required.");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date is required.");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date is required.");
        }
        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException(
                    "End date must be after start date.");
        }
        if (monthlyCost < 0) {
            throw new IllegalArgumentException(
                    "Monthly cost cannot be negative.");
        }
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException(
                    "Discount percentage must be between 0 and 100.");
        }
    }
}

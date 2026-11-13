package com.example.SprotsAcademy.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionTest {

    private Subscription subscription;

    @BeforeEach
    void setUp() {
        subscription = new Subscription(
                1,
                null,
                null,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 4, 1),
                100,
                10
        );
    }

    @Test
    void testTotalPriceWithoutDiscount() {
        subscription.setDiscountPercentage(0);

        // 3 months * 100
        assertEquals(300, subscription.calculateTotalPrice(), 0.001);
    }

    @Test
    void testTotalPriceWithDiscount() {
        // 3 months * 100 = 300
        // 10% discount = 30
        assertEquals(270, subscription.calculateTotalPrice(), 0.001);
    }

    @Test
    void testTotalPriceWithFullDiscount() {
        subscription.setDiscountPercentage(100);

        assertEquals(0, subscription.calculateTotalPrice(), 0.001);
    }

    @Test
    void testTotalPriceAcrossYears() {
        subscription.setStartDate(LocalDate.of(2025, 11, 1));
        subscription.setEndDate(LocalDate.of(2026, 2, 1));
        subscription.setDiscountPercentage(0);

        assertEquals(300, subscription.calculateTotalPrice(), 0.001);
    }

    @Test
    void testSameStartAndEndDate() {
        subscription.setEndDate(subscription.getStartDate());

        assertEquals(0, subscription.calculateTotalPrice(), 0.001);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 100, 0, 100",
            "3, 100, 0, 300",
            "3, 100, 10, 270",
            "6, 200, 25, 900",
            "12, 100, 50, 600",
            "12, 100, 100, 0"
    })
    void testTotalPriceWithDifferentInputs(
            int months,
            double monthlyCost,
            double discount,
            double expected) {

        subscription.setStartDate(LocalDate.of(2026, 1, 1));
        subscription.setEndDate(
                LocalDate.of(2026, 1, 1).plusMonths(months)
        );
        subscription.setMonthlyCost(monthlyCost);
        subscription.setDiscountPercentage(discount);

        assertEquals(
                expected,
                subscription.calculateTotalPrice(),
                0.001
        );
    }
}

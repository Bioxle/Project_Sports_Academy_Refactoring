package com.example.SprotsAcademy.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentTest {

    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        enrollment = new Enrollment(
                1,
                "Adam",
                LocalDate.of(2026, 1, 1),
                1000,
                10
        );
    }

    @Test
    void testTotalPriceWithoutDiscount() {
        Enrollment e = new Enrollment(
                2, "Adam", LocalDate.now(), 1000, 0
        );

        assertEquals(1000, e.calculateTotalPrice(), 0.001);
    }

    @Test
    void testTotalPriceWithDiscount() {
        // 1000 - 10% = 900
        assertEquals(900, enrollment.calculateTotalPrice(), 0.001);
    }

    @Test
    void testTotalPriceWithFullDiscount() {
        Enrollment e = new Enrollment(
                2, "Adam", LocalDate.now(), 1000, 100
        );

        assertEquals(0, e.calculateTotalPrice(), 0.001);
    }

    @Test
    void testZeroCost() {
        Enrollment e = new Enrollment(
                2, "Adam", LocalDate.now(), 0, 10
        );

        assertEquals(0, e.calculateTotalPrice(), 0.001);
    }

    @ParameterizedTest
    @CsvSource({
            "1000, 0, 1000",
            "1000, 10, 900",
            "1000, 25, 750",
            "1000, 50, 500",
            "1000, 100, 0",
            "200, 15, 170",
            "500, 20, 400"
    })
    void testTotalPriceWithDifferentDiscounts(
            double cost,
            double discount,
            double expected) {

        Enrollment e = new Enrollment(
                1,
                "Adam",
                LocalDate.of(2026, 1, 1),
                cost,
                discount
        );

        assertEquals(
                expected,
                e.calculateTotalPrice(),
                0.001
        );
    }
}


package com.example.SprotsAcademy.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Enrollment enrollment;
    private Payment payment;

    @BeforeEach
    void setUp() {
        enrollment = new Enrollment(
                1,
                "Adam",
                LocalDate.of(2026, 1, 1),
                1000,
                10
        );

        payment = new Payment(
                100,
                LocalDate.of(2026, 1, 2),
                "Cash",
                enrollment,
                "Registration"
        );
    }

    @Test
    void testPaymentTotalCost() {
        // Enrollment: 1000 - 10% = 900

        assertEquals(
                900,
                payment.getTotalCost(),
                0.001
        );
    }

    @Test
    void testCalculateTotalPrice() {
        assertEquals(
                900,
                payment.calculateTotalPrice(),
                0.001
        );
    }

    @Test
    void testPaymentTotalMatchesEnrollment() {
        assertEquals(
                enrollment.calculateTotalPrice(),
                payment.calculateTotalPrice(),
                0.001
        );
    }

    @Test
    void testPaymentWithNoDiscount() {
        Enrollment e = new Enrollment(
                2, "Youssef", LocalDate.now(), 500, 0
        );

        Payment p = new Payment(
                101, LocalDate.now(), "Card", e, "Registration"
        );

        assertEquals(500, p.getTotalCost(), 0.001);
    }

    @Test
    void testPaymentWithFullDiscount() {
        Enrollment e = new Enrollment(
                2, "Youssef", LocalDate.now(), 500, 100
        );

        Payment p = new Payment(
                101, LocalDate.now(), "Cash", e, "Registration"
        );

        assertEquals(0, p.calculateTotalPrice(), 0.001);
    }

    @ParameterizedTest
    @CsvSource({
            "1000, 0, 1000",
            "1000, 10, 900",
            "1000, 25, 750",
            "1000, 50, 500",
            "1000, 100, 0",
            "200, 15, 170"
    })
    void testPaymentWithDifferentEnrollmentPrices(
            double cost,
            double discount,
            double expected) {

        Enrollment e = new Enrollment(
                1, "Adam", LocalDate.now(), cost, discount
        );

        Payment p = new Payment(
                100, LocalDate.now(), "Cash", e, "Registration"
        );

        assertEquals(
                expected,
                p.calculateTotalPrice(),
                0.001
        );
    }

    @Test
    void testNullEnrollmentThrowsException() {
        assertThrows(
                NullPointerException.class,
                () -> new Payment(
                        100,
                        LocalDate.now(),
                        "Cash",
                        null,
                        "Registration"
                )
        );
    }
}

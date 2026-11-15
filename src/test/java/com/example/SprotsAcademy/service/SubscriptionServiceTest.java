package com.example.SprotsAcademy.service;

import com.example.SprotsAcademy.model.Athlete;
import com.example.SprotsAcademy.model.Subscription;
import com.example.SprotsAcademy.model.TrainingProgram;
import com.example.SprotsAcademy.repositories.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepo;

    private SubscriptionService service;

    private static final LocalDate DOB = LocalDate.of(1995, 5, 15);
    private static final LocalDate START = LocalDate.of(2026, 1, 1);
    private static final LocalDate END = LocalDate.of(2026, 6, 1);

    private Athlete athlete;
    private TrainingProgram program;

    @BeforeEach
    void setUp() {
        service = new SubscriptionService(subscriptionRepo);
        athlete = new Athlete(1, "Alice", "Brown", "Female", DOB, "123", true, 3);
        program = new TrainingProgram(1, "Soccer", 1, "Coach A", 2, false, "Mixed", 90, "Monday");
    }

    // ── getAllSubscriptions ───────────────────────────────────

    @Test
    void getAllSubscriptions_returnsEmptyList() {
        when(subscriptionRepo.findAll()).thenReturn(new ArrayList<>());

        assertTrue(service.getAllSubscriptions().isEmpty());
    }

    @Test
    void getAllSubscriptions_returnsAll() {
        Subscription s = new Subscription(1, athlete, program, START, END, 50, 10);
        when(subscriptionRepo.findAll()).thenReturn(List.of(s));

        assertEquals(1, service.getAllSubscriptions().size());
    }

    // ── createSubscription – happy path ──────────────────────

    @Test
    void createSubscription_createsWithSerialNumber1WhenEmpty() {
        when(subscriptionRepo.findAll()).thenReturn(new ArrayList<>());
        when(subscriptionRepo.save(any(Subscription.class))).thenReturn(true);

        Subscription result = service.createSubscription(athlete, program, START, END, 50, 10);

        assertEquals(1, result.getSerialNumber());
        assertEquals(50, result.getMonthlyCost());
        assertEquals(10, result.getDiscountPercentage());
    }

    @Test
    void createSubscription_generatesNextSerialNumber() {
        Subscription existing = new Subscription(5, athlete, program, START, END, 30, 0);
        when(subscriptionRepo.findAll()).thenReturn(List.of(existing));
        when(subscriptionRepo.save(any(Subscription.class))).thenReturn(true);

        Subscription result = service.createSubscription(athlete, program, START, END, 50, 10);

        assertEquals(6, result.getSerialNumber());
    }

    @Test
    void createSubscription_callsSaveOnRepository() {
        when(subscriptionRepo.findAll()).thenReturn(new ArrayList<>());
        when(subscriptionRepo.save(any(Subscription.class))).thenReturn(true);

        service.createSubscription(athlete, program, START, END, 50, 10);

        verify(subscriptionRepo).save(any(Subscription.class));
    }

    // ── createSubscription – validation ──────────────────────

    @Test
    void createSubscription_throwsWhenAthleteIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createSubscription(null, program, START, END, 50, 10));
    }

    @Test
    void createSubscription_throwsWhenProgramIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createSubscription(athlete, null, START, END, 50, 10));
    }

    @Test
    void createSubscription_throwsWhenStartDateIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createSubscription(athlete, program, null, END, 50, 10));
    }

    @Test
    void createSubscription_throwsWhenEndDateIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createSubscription(athlete, program, START, null, 50, 10));
    }

    @Test
    void createSubscription_throwsWhenEndDateIsBeforeStartDate() {
        LocalDate before = START.minusDays(1);

        assertThrows(IllegalArgumentException.class,
                () -> service.createSubscription(athlete, program, START, before, 50, 10));
    }

    @Test
    void createSubscription_throwsWhenEndDateEqualsStartDate() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createSubscription(athlete, program, START, START, 50, 10));
    }

    @Test
    void createSubscription_throwsWhenCostIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createSubscription(athlete, program, START, END, -1, 10));
    }

    @Test
    void createSubscription_throwsWhenDiscountIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createSubscription(athlete, program, START, END, 50, -5));
    }

    @Test
    void createSubscription_throwsWhenDiscountIsOver100() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createSubscription(athlete, program, START, END, 50, 101));
    }

    @Test
    void createSubscription_doesNotSaveWhenValidationFails() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createSubscription(null, program, START, END, 50, 10));

        verify(subscriptionRepo, never()).save(any());
    }

    // ── createSubscription – save failure ────────────────────

    @Test
    void createSubscription_throwsRuntimeExceptionWhenSaveFails() {
        when(subscriptionRepo.findAll()).thenReturn(new ArrayList<>());
        when(subscriptionRepo.save(any(Subscription.class))).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> service.createSubscription(athlete, program, START, END, 50, 10));
    }

    // ── cancelSubscription ───────────────────────────────────

    @Test
    void cancelSubscription_deletesSuccessfully() {
        Subscription sub = new Subscription(1, athlete, program, START, END, 50, 10);
        when(subscriptionRepo.delete(sub)).thenReturn(true);

        assertDoesNotThrow(() -> service.cancelSubscription(sub));
        verify(subscriptionRepo).delete(sub);
    }

    @Test
    void cancelSubscription_throwsWhenNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.cancelSubscription(null));
    }

    @Test
    void cancelSubscription_throwsWhenDeleteFails() {
        Subscription sub = new Subscription(1, athlete, program, START, END, 50, 10);
        when(subscriptionRepo.delete(sub)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> service.cancelSubscription(sub));
    }
}

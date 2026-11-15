package com.example.SprotsAcademy.service;

import com.example.SprotsAcademy.model.Athlete;
import com.example.SprotsAcademy.model.TrainingProgram;
import com.example.SprotsAcademy.model.TrainingProgramReservation;
import com.example.SprotsAcademy.repositories.ReservationRepository;
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
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepo;

    private ReservationService service;

    private static final LocalDate DOB = LocalDate.of(1995, 5, 15);
    private static final LocalDate RESERVATION_DATE = LocalDate.of(2026, 3, 15);

    private Athlete athlete;
    private TrainingProgram program;

    @BeforeEach
    void setUp() {
        service = new ReservationService(reservationRepo);
        // Athlete with experience level 3
        athlete = new Athlete(10, "Alice", "Brown", "Female", DOB, "123", true, 3);
        // Program with minimum experience level 2
        program = new TrainingProgram(5, "Soccer", 1, "Coach A", 2, false, "Mixed", 90, "Monday");
    }

    // ── getAllReservations ────────────────────────────────────

    @Test
    void getAllReservations_returnsEmptyList() {
        when(reservationRepo.findAll()).thenReturn(new ArrayList<>());

        assertTrue(service.getAllReservations().isEmpty());
    }

    @Test
    void getAllReservations_returnsAll() {
        TrainingProgramReservation r = new TrainingProgramReservation(
                "5_10_20260315", athlete, program, RESERVATION_DATE);
        when(reservationRepo.findAll()).thenReturn(List.of(r));

        assertEquals(1, service.getAllReservations().size());
    }

    // ── createReservation – happy path ────────────────────────

    @Test
    void createReservation_createsWithCorrectUniqueCodeAndSaves() {
        when(reservationRepo.save(any(TrainingProgramReservation.class))).thenReturn(true);

        TrainingProgramReservation result = service.createReservation(athlete, program, RESERVATION_DATE);

        assertNotNull(result);
        assertEquals("5_10_20260315", result.getUniqueCode());
        assertSame(athlete, result.getAthlete());
        assertSame(program, result.getTrainingProgram());
        assertEquals(RESERVATION_DATE, result.getTrainingProgramDate());
        verify(reservationRepo).save(any(TrainingProgramReservation.class));
    }

    @Test
    void createReservation_allowsWhenAthleteExpEqualsProgramMinimum() {
        // Athlete exp = 2, Program minExp = 2 -> should pass
        Athlete exactMatch = new Athlete(20, "Bob", "Smith", "Male", DOB, "555", false, 2);
        when(reservationRepo.save(any(TrainingProgramReservation.class))).thenReturn(true);

        assertDoesNotThrow(() -> service.createReservation(exactMatch, program, RESERVATION_DATE));
    }

    @Test
    void createReservation_formatsUniqueCodeCorrectly() {
        when(reservationRepo.save(any(TrainingProgramReservation.class))).thenReturn(true);

        LocalDate date = LocalDate.of(2026, 12, 5);
        TrainingProgramReservation result = service.createReservation(athlete, program, date);

        // programId_athleteId_yyyyMMdd -> 5_10_20261205
        assertEquals("5_10_20261205", result.getUniqueCode());
    }

    // ── createReservation – validation ────────────────────────

    @Test
    void createReservation_throwsWhenAthleteIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createReservation(null, program, RESERVATION_DATE));
    }

    @Test
    void createReservation_throwsWhenProgramIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createReservation(athlete, null, RESERVATION_DATE));
    }

    @Test
    void createReservation_throwsWhenDateIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createReservation(athlete, program, null));
    }

    @Test
    void createReservation_throwsWhenAthleteExpIsBelowMinimum() {
        // Athlete exp = 1, Program minExp = 2 -> should fail
        Athlete lowExp = new Athlete(30, "Charlie", "Z", "Male", DOB, "777", false, 1);

        assertThrows(IllegalArgumentException.class,
                () -> service.createReservation(lowExp, program, RESERVATION_DATE));
    }

    @Test
    void createReservation_doesNotSaveWhenValidationFails() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createReservation(null, program, RESERVATION_DATE));

        verify(reservationRepo, never()).save(any());
    }

    // ── createReservation – save failure ──────────────────────

    @Test
    void createReservation_throwsRuntimeExceptionWhenSaveFails() {
        when(reservationRepo.save(any(TrainingProgramReservation.class))).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> service.createReservation(athlete, program, RESERVATION_DATE));
    }

    // ── cancelReservation ─────────────────────────────────────

    @Test
    void cancelReservation_deletesSuccessfully() {
        TrainingProgramReservation reservation = new TrainingProgramReservation(
                "5_10_20260315", athlete, program, RESERVATION_DATE);
        when(reservationRepo.delete(reservation)).thenReturn(true);

        assertDoesNotThrow(() -> service.cancelReservation(reservation));
        verify(reservationRepo).delete(reservation);
    }

    @Test
    void cancelReservation_throwsWhenNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.cancelReservation(null));
    }

    @Test
    void cancelReservation_throwsWhenDeleteFails() {
        TrainingProgramReservation reservation = new TrainingProgramReservation(
                "5_10_20260315", athlete, program, RESERVATION_DATE);
        when(reservationRepo.delete(reservation)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> service.cancelReservation(reservation));
    }
}

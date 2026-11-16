
package com.example.SprotsAcademy.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TrainingProgramReservationTest {

    private TrainingProgramReservation reservation;
    private Athlete athlete;
    private TrainingProgram trainingProgram;
    private LocalDate reservationDate;

    @BeforeEach
    void setUp() {
        athlete = new Athlete();

        trainingProgram = new TrainingProgram(
                1,
                "Football",
                2,
                "Adam",
                3,
                false,
                "Sheet1",
                60,
                "Monday"
        );

        reservationDate = LocalDate.of(2026, 12, 7);

        reservation = new TrainingProgramReservation(
                "RES001",
                athlete,
                trainingProgram,
                reservationDate
        );
    }

    @Test
    void testReservationCreation() {
        assertNotNull(reservation);

        assertAll(
                () -> assertEquals(
                        "RES001",
                        reservation.getUniqueCode()
                ),
                () -> assertSame(
                        athlete,
                        reservation.getAthlete()
                ),
                () -> assertSame(
                        trainingProgram,
                        reservation.getTrainingProgram()
                ),
                () -> assertEquals(
                        reservationDate,
                        reservation.getTrainingProgramDate()
                )
        );
    }

    @Test
    void testSetUniqueCode() {
        reservation.setUniqueCode("RES002");

        assertEquals(
                "RES002",
                reservation.getUniqueCode()
        );
    }

    @Test
    void testSetAthlete() {
        Athlete newAthlete = new Athlete();

        reservation.setAthlete(newAthlete);

        assertSame(
                newAthlete,
                reservation.getAthlete()
        );
    }

    @Test
    void testSetTrainingProgram() {
        TrainingProgram newProgram = new TrainingProgram(
                2,
                "Basketball",
                3,
                "Youssef",
                2,
                true,
                "Sheet2",
                90,
                "Friday"
        );

        reservation.setTrainingProgram(newProgram);

        assertSame(
                newProgram,
                reservation.getTrainingProgram()
        );
    }

    @Test
    void testSetTrainingProgramDate() {
        LocalDate newDate = LocalDate.of(2026, 12, 14);

        reservation.setTrainingProgramDate(newDate);

        assertEquals(
                newDate,
                reservation.getTrainingProgramDate()
        );
    }

    @Test
    void testNullAthlete() {
        reservation.setAthlete(null);

        assertNull(reservation.getAthlete());
    }

    @Test
    void testNullTrainingProgram() {
        reservation.setTrainingProgram(null);

        assertNull(reservation.getTrainingProgram());
    }

    @Test
    void testNullTrainingProgramDate() {
        reservation.setTrainingProgramDate(null);

        assertNull(reservation.getTrainingProgramDate());
    }

    @Test
    void testEmptyUniqueCode() {
        reservation.setUniqueCode("");

        assertEquals("", reservation.getUniqueCode());
    }

    @Test
    void testAthleteReferenceRemainsSame() {
        assertSame(
                athlete,
                reservation.getAthlete()
        );
    }

    @Test
    void testTrainingProgramReferenceRemainsSame() {
        assertSame(
                trainingProgram,
                reservation.getTrainingProgram()
        );
    }
}

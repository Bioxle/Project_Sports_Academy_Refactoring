
package com.example.SprotsAcademy.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TrainingProgramTest {

    private TrainingProgram program;

    @BeforeEach
    void setUp() {
        program = new TrainingProgram(
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
    }

    @Test
    void testTrainingProgramCreation() {
        assertNotNull(program);

        assertAll(
                () -> assertEquals(1, program.getSerialNumber()),
                () -> assertEquals("Football", program.getSport()),
                () -> assertEquals(2, program.getFacility()),
                () -> assertEquals("Adam", program.getCoach()),
                () -> assertEquals(3, program.getMinimumExp()),
                () -> assertFalse(program.isWeekOfTheAthlete()),
                () -> assertEquals("Sheet1", program.getSheet()),
                () -> assertEquals(60, program.getDuration()),
                () -> assertEquals("Monday", program.getDayOfTheWeek())
        );
    }

    @Test
    void testSetSerialNumber() {
        program.setSerialNumber(100);

        assertEquals(100, program.getSerialNumber());
    }

    @Test
    void testSetSport() {
        program.setSport("Basketball");

        assertEquals("Basketball", program.getSport());
    }

    @Test
    void testSetFacility() {
        program.setFacility(5);

        assertEquals(5, program.getFacility());
    }

    @Test
    void testSetCoach() {
        program.setCoach("Youssef");

        assertEquals("Youssef", program.getCoach());
    }

    @Test
    void testSetMinimumExperience() {
        program.setMinimumExp(5);

        assertEquals(5, program.getMinimumExp());
    }

    @Test
    void testSetWeekOfTheAthlete() {
        program.setWeekOfTheAthlete(true);

        assertTrue(program.isWeekOfTheAthlete());
    }

    @Test
    void testSetSheet() {
        program.setSheet("Sheet2");

        assertEquals("Sheet2", program.getSheet());
    }

    @Test
    void testSetDuration() {
        program.setDuration(90);

        assertEquals(90, program.getDuration());
    }

    @Test
    void testSetDayOfTheWeek() {
        program.setDayOfTheWeek("Friday");

        assertEquals("Friday", program.getDayOfTheWeek());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 30, 60, 90, 120})
    void testDifferentDurations(int duration) {
        program.setDuration(duration);

        assertEquals(duration, program.getDuration());
    }

    @Test
    void testZeroMinimumExperience() {
        program.setMinimumExp(0);

        assertEquals(0, program.getMinimumExp());
    }

    @Test
    void testWeekOfTheAthleteFalse() {
        program.setWeekOfTheAthlete(true);
        program.setWeekOfTheAthlete(false);

        assertFalse(program.isWeekOfTheAthlete());
    }

    @Test
    void testNullCoach() {
        program.setCoach(null);

        assertNull(program.getCoach());
    }

}

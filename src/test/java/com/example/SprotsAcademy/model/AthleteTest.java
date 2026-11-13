package com.example.SprotsAcademy.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AthleteTest {

    private Athlete athlete;

    @BeforeEach
    void setUp() {
        athlete = new Athlete(
                "Adam",
                "Smith",
                "Sheet1",
                LocalDate.of(2005, 5, 15),
                "0612345678",
                false,
                3
        );
    }

    @Test
    void testDefaultConstructor() {
        Athlete emptyAthlete = new Athlete();

        assertNotNull(emptyAthlete);
        assertNull(emptyAthlete.getProfessional());
        assertEquals(0, emptyAthlete.getExperienceLevel());
    }

    @Test
    void testConstructorWithSerialNumber() {
        Athlete athleteWithId = new Athlete(
                100,
                "Adam",
                "Smith",
                "Sheet1",
                LocalDate.of(2005, 5, 15),
                "0612345678",
                true,
                5
        );

        assertEquals(100, athleteWithId.getSerialNumber());
        assertTrue(athleteWithId.getProfessional());
        assertEquals(5, athleteWithId.getExperienceLevel());
    }

    @Test
    void testSetProfessional() {
        athlete.setProfessional(true);

        assertTrue(athlete.getProfessional());
    }

    @Test
    void testSetExperienceLevel() {
        athlete.setExperienceLevel(5);

        assertEquals(5, athlete.getExperienceLevel());
    }

    @Test
    void testSetName() {
        athlete.setName("Youssef");

        assertEquals("Youssef", athlete.getName());
    }

    @Test
    void testRegistrationDate() {
        assertEquals(
                LocalDate.now(),
                athlete.getDateOfRegistration()
        );
    }
}


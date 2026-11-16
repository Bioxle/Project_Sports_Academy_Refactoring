
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
    void testAthleteCreation() {
        assertNotNull(athlete);

        assertAll(
                () -> assertEquals("Adam", athlete.getName()),
                () -> assertEquals("Smith", athlete.getSurname()),
                () -> assertEquals("Sheet1", athlete.getSheet()),
                () -> assertEquals(
                        LocalDate.of(2005, 5, 15),
                        athlete.getDateOfBirth()
                ),
                () -> assertEquals(
                        "0612345678",
                        athlete.getContact()
                ),
                () -> assertEquals(
                        Boolean.FALSE,
                        athlete.getProfessional()
                ),
                () -> assertEquals(
                        3,
                        athlete.getExperienceLevel()
                )
        );
    }

    @Test
    void testDefaultConstructor() {
        Athlete emptyAthlete = new Athlete();

        assertNotNull(emptyAthlete);
        assertNull(emptyAthlete.getName());
        assertNull(emptyAthlete.getProfessional());
        assertEquals(0, emptyAthlete.getExperienceLevel());
        assertEquals(0, emptyAthlete.getSerialNumber());
    }

    @Test
    void testConstructorWithoutSerialNumber() {
        assertEquals(0, athlete.getSerialNumber());
        assertEquals(
                LocalDate.now(),
                athlete.getDateOfRegistration()
        );
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

        assertAll(
                () -> assertEquals(
                        100,
                        athleteWithId.getSerialNumber()
                ),
                () -> assertTrue(
                        athleteWithId.getProfessional()
                ),
                () -> assertEquals(
                        5,
                        athleteWithId.getExperienceLevel()
                )
        );
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
    void testSetSurname() {
        athlete.setSurname("Bennani");

        assertEquals("Bennani", athlete.getSurname());
    }

    @Test
    void testSetContact() {
        athlete.setContact("0698765432");

        assertEquals("0698765432", athlete.getContact());
    }

    @Test
    void testSetSerialNumber() {
        athlete.setSerialNumber(50);

        assertEquals(50, athlete.getSerialNumber());
    }

    @Test
    void testSetDateOfBirth() {
        LocalDate newDate = LocalDate.of(2004, 1, 10);

        athlete.setDateOfBirth(newDate);

        assertEquals(newDate, athlete.getDateOfBirth());
    }

    @Test
    void testSetSheet() {
        athlete.setSheet("Sheet2");

        assertEquals("Sheet2", athlete.getSheet());
    }

    @Test
    void testSetDateOfRegistration() {
        LocalDate newDate = LocalDate.of(2025, 1, 1);

        athlete.setDateOfRegistration(newDate);

        assertEquals(
                newDate,
                athlete.getDateOfRegistration()
        );
    }

    @Test
    void testAthleteIsUser() {
        assertInstanceOf(User.class, athlete);
    }
}

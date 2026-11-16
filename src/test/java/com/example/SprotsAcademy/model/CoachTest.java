package com.example.SprotsAcademy.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CoachTest {

    private Coach coach;
    private Sport sport;

    @BeforeEach
    void setUp() {
        sport = new Sport("Football");

        coach = new Coach(
                "Adam",
                "Smith",
                "Sheet1",
                LocalDate.of(1990, 5, 15),
                "0612345678",
                sport,
                "UEFA Coaching License"
        );
    }

    @Test
    void testCoachCreation() {
        assertNotNull(coach);

        assertAll(
                () -> assertEquals("Adam", coach.getName()),
                () -> assertEquals("Smith", coach.getSurname()),
                () -> assertEquals("Sheet1", coach.getSheet()),
                () -> assertEquals(
                        LocalDate.of(1990, 5, 15),
                        coach.getDateOfBirth()
                ),
                () -> assertEquals("0612345678", coach.getContact()),
                () -> assertSame(sport, coach.getSport()),
                () -> assertEquals(
                        "UEFA Coaching License",
                        coach.getDegrees()
                )
        );
    }

    @Test
    void testDefaultConstructor() {
        Coach emptyCoach = new Coach();

        assertNotNull(emptyCoach);

        assertAll(
                () -> assertNull(emptyCoach.getName()),
                () -> assertNull(emptyCoach.getSport()),
                () -> assertNull(emptyCoach.getDegrees()),
                () -> assertEquals(0, emptyCoach.getSerialNumber())
        );
    }

    @Test
    void testSetSport() {
        Sport newSport = new Sport("Basketball");

        coach.setSport(newSport);

        assertSame(newSport, coach.getSport());
        assertEquals(
                "Basketball",
                coach.getSport().getSportName()
        );
    }

    @Test
    void testSetDegrees() {
        coach.setDegrees("Master in Sports Science");

        assertEquals(
                "Master in Sports Science",
                coach.getDegrees()
        );
    }

    @Test
    void testSetName() {
        coach.setName("Youssef");

        assertEquals("Youssef", coach.getName());
    }

    @Test
    void testSetContact() {
        coach.setContact("0698765432");

        assertEquals("0698765432", coach.getContact());
    }

    @Test
    void testSetSerialNumber() {
        coach.setSerialNumber(100);

        assertEquals(100, coach.getSerialNumber());
    }

    @Test
    void testRegistrationDate() {
        assertEquals(
                LocalDate.now(),
                coach.getDateOfRegistration()
        );
    }

    @Test
    void testCoachIsUser() {
        assertInstanceOf(User.class, coach);
    }

    @Test
    void testChangingSportName() {
        sport.setSportName("Tennis");

        assertEquals(
                "Tennis",
                coach.getSport().getSportName()
        );
    }
}

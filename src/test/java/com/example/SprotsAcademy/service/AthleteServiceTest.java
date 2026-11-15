package com.example.SprotsAcademy.service;

import com.example.SprotsAcademy.model.Athlete;
import com.example.SprotsAcademy.repositories.AthleteRepository;
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
class AthleteServiceTest {

    @Mock
    private AthleteRepository athleteRepo;

    private AthleteService service;

    private static final LocalDate DOB = LocalDate.of(1995, 5, 15);

    @BeforeEach
    void setUp() {
        service = new AthleteService(athleteRepo);
    }

    // ── getAllAthletes ────────────────────────────────────────

    @Test
    void getAllAthletes_returnsEmptyListWhenNoAthletes() {
        when(athleteRepo.findAll()).thenReturn(new ArrayList<>());

        List<Athlete> result = service.getAllAthletes();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllAthletes_returnsAllAthletes() {
        Athlete a1 = new Athlete(1, "Alice", "A", "F", DOB, "111", true, 3);
        Athlete a2 = new Athlete(2, "Bob", "B", "M", DOB, "222", false, 1);
        when(athleteRepo.findAll()).thenReturn(List.of(a1, a2));

        List<Athlete> result = service.getAllAthletes();

        assertEquals(2, result.size());
    }

    // ── findBySerialNumber ───────────────────────────────────

    @Test
    void findBySerialNumber_returnsAthleteWhenFound() {
        Athlete expected = new Athlete(5, "Alice", "Brown", "F", DOB, "123", true, 3);
        when(athleteRepo.findAthleteBySerialNumber(5)).thenReturn(expected);

        Athlete result = service.findBySerialNumber(5);

        assertEquals("Alice", result.getName());
    }

    @Test
    void findBySerialNumber_returnsNullWhenNotFound() {
        when(athleteRepo.findAthleteBySerialNumber(99)).thenReturn(null);

        assertNull(service.findBySerialNumber(99));
    }

    // ── registerAthlete – happy path ─────────────────────────

    @Test
    void registerAthlete_createsAthleteWithSerialNumber1WhenEmpty() {
        when(athleteRepo.findAll()).thenReturn(new ArrayList<>());
        when(athleteRepo.save(any(Athlete.class))).thenReturn(true);

        Athlete result = service.registerAthlete("Alice", "Brown", "Female",
                DOB, "123", true, 3);

        assertEquals(1, result.getSerialNumber());
        assertEquals("Alice", result.getName());
        assertEquals("Brown", result.getSurname());
    }

    @Test
    void registerAthlete_generatesNextSerialNumber() {
        Athlete existing = new Athlete(7, "Bob", "Smith", "M", DOB, "999", false, 2);
        when(athleteRepo.findAll()).thenReturn(List.of(existing));
        when(athleteRepo.save(any(Athlete.class))).thenReturn(true);

        Athlete result = service.registerAthlete("Alice", "Brown", "Female",
                DOB, "123", true, 3);

        assertEquals(8, result.getSerialNumber());
    }

    @Test
    void registerAthlete_callsSaveOnRepository() {
        when(athleteRepo.findAll()).thenReturn(new ArrayList<>());
        when(athleteRepo.save(any(Athlete.class))).thenReturn(true);

        service.registerAthlete("Alice", "Brown", "Female", DOB, "123", true, 3);

        verify(athleteRepo).save(any(Athlete.class));
    }

    // ── registerAthlete – validation ─────────────────────────

    @Test
    void registerAthlete_throwsWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registerAthlete(null, "Brown", "F", DOB, "123", true, 3));
    }

    @Test
    void registerAthlete_throwsWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registerAthlete("  ", "Brown", "F", DOB, "123", true, 3));
    }

    @Test
    void registerAthlete_throwsWhenSurnameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registerAthlete("Alice", null, "F", DOB, "123", true, 3));
    }

    @Test
    void registerAthlete_throwsWhenDobIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registerAthlete("Alice", "Brown", "F", null, "123", true, 3));
    }

    @Test
    void registerAthlete_throwsWhenContactIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registerAthlete("Alice", "Brown", "F", DOB, null, true, 3));
    }

    @Test
    void registerAthlete_throwsWhenExpLevelIsZero() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registerAthlete("Alice", "Brown", "F", DOB, "123", true, 0));
    }

    @Test
    void registerAthlete_throwsWhenExpLevelIsSix() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registerAthlete("Alice", "Brown", "F", DOB, "123", true, 6));
    }

    @Test
    void registerAthlete_doesNotCallSaveWhenValidationFails() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registerAthlete(null, "Brown", "F", DOB, "123", true, 3));

        verify(athleteRepo, never()).save(any());
    }

    // ── registerAthlete – save failure ───────────────────────

    @Test
    void registerAthlete_throwsRuntimeExceptionWhenSaveFails() {
        when(athleteRepo.findAll()).thenReturn(new ArrayList<>());
        when(athleteRepo.save(any(Athlete.class))).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> service.registerAthlete("Alice", "Brown", "F", DOB, "123", true, 3));
    }

    // ── updateAthlete ────────────────────────────────────────

    @Test
    void updateAthlete_updatesFieldsAndSaves() {
        Athlete existing = new Athlete(1, "Old", "Name", "M", DOB, "000", false, 1);
        when(athleteRepo.save(any(Athlete.class))).thenReturn(true);

        Athlete result = service.updateAthlete(existing, "New", "Surname", "Female",
                DOB, "999", true, 4);

        assertEquals("New", result.getName());
        assertEquals("Surname", result.getSurname());
        assertEquals("Female", result.getSheet());
        assertEquals(true, result.getProfessional());
        assertEquals(4, result.getExperienceLevel());
        verify(athleteRepo).save(existing);
    }

    @Test
    void updateAthlete_throwsWhenAthleteIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.updateAthlete(null, "A", "B", "F", DOB, "123", true, 3));
    }

    @Test
    void updateAthlete_throwsWhenNameIsBlank() {
        Athlete existing = new Athlete(1, "Old", "Name", "M", DOB, "000", false, 1);

        assertThrows(IllegalArgumentException.class,
                () -> service.updateAthlete(existing, "", "B", "F", DOB, "123", true, 3));
    }

    // ── deleteAthlete ────────────────────────────────────────

    @Test
    void deleteAthlete_returnsTrueOnSuccess() {
        Athlete athlete = new Athlete(1, "Alice", "Brown", "F", DOB, "123", true, 3);
        when(athleteRepo.delete(athlete)).thenReturn(true);

        assertTrue(service.deleteAthlete(athlete));
    }

    @Test
    void deleteAthlete_returnsFalseOnFailure() {
        Athlete athlete = new Athlete(1, "Alice", "Brown", "F", DOB, "123", true, 3);
        when(athleteRepo.delete(athlete)).thenReturn(false);

        assertFalse(service.deleteAthlete(athlete));
    }

    @Test
    void deleteAthlete_throwsWhenAthleteIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.deleteAthlete(null));
    }
}

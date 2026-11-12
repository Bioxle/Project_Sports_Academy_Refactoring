package com.example.SprotsAcademy.service;

import com.example.SprotsAcademy.model.Athlete;
import com.example.SprotsAcademy.repositories.AthleteRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Handles all business logic related to athletes:
 * registration, lookup, and profile updates.
 */
public class AthleteService {

    private final AthleteRepository athleteRepo;

    public AthleteService(AthleteRepository athleteRepo) {
        this.athleteRepo = athleteRepo;
    }

    // ── Queries ──────────────────────────────────────────────

    public List<Athlete> getAllAthletes() {
        return athleteRepo.findAll();
    }

    public Athlete findBySerialNumber(int serialNumber) {
        return athleteRepo.findAthleteBySerialNumber(serialNumber);
    }

    // ── Commands ─────────────────────────────────────────────


    public Athlete registerAthlete(String name, String surname, String sheet,
                                   LocalDate dateOfBirth, String contact,
                                   boolean professional, int experienceLevel) {

        validateAthleteFields(name, surname, sheet, dateOfBirth, contact, experienceLevel);

        Athlete athlete = new Athlete(name, surname, sheet, dateOfBirth,
                contact, professional, experienceLevel);

        boolean saved = athleteRepo.save(athlete);
        if (!saved) {
            throw new RuntimeException("Failed to persist athlete.");
        }

        return athlete;
    }


    public Athlete updateAthlete(Athlete athlete, String name, String surname,
                                 String sheet, LocalDate dateOfBirth,
                                 String contact, boolean professional,
                                 int experienceLevel) {

        if (athlete == null) {
            throw new IllegalArgumentException("No athlete selected for update.");
        }

        validateAthleteFields(name, surname, sheet, dateOfBirth, contact, experienceLevel);

        athlete.setName(name);
        athlete.setSurname(surname);
        athlete.setSheet(sheet);
        athlete.setDateOfBirth(dateOfBirth);
        athlete.setContact(contact);
        athlete.setProfessional(professional);
        athlete.setExperienceLevel(experienceLevel);

        boolean saved = athleteRepo.save(athlete);
        if (!saved) {
            throw new RuntimeException("Failed to update athlete.");
        }

        return athlete;
    }

    /**
     * Removes an athlete from the repository.
     *
     * @return true if the athlete was deleted, false otherwise.
     */
    public boolean deleteAthlete(Athlete athlete) {
        if (athlete == null) {
            throw new IllegalArgumentException("Athlete cannot be null.");
        }
        return athleteRepo.delete(athlete);
    }

    // ── Validation ───────────────────────────────────────────

    private void validateAthleteFields(String name, String surname, String sheet,
                                       LocalDate dateOfBirth, String contact,
                                       int experienceLevel) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (surname == null || surname.trim().isEmpty()) {
            throw new IllegalArgumentException("Surname is required.");
        }
        if (sheet == null || sheet.trim().isEmpty()) {
            throw new IllegalArgumentException("Sheet is required.");
        }
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth is required.");
        }
        if (contact == null || contact.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact is required.");
        }
        if (experienceLevel < 1 || experienceLevel > 5) {
            throw new IllegalArgumentException(
                    "Experience level must be between 1 and 5.");
        }
    }
}

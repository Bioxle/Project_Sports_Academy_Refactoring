package com.example.SprotsAcademy.service;

import com.example.SprotsAcademy.model.Athlete;
import com.example.SprotsAcademy.model.TrainingProgram;
import com.example.SprotsAcademy.model.TrainingProgramReservation;
import com.example.SprotsAcademy.repositories.ReservationRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Handles all business logic related to training-program reservations:
 * creation (with unique-code generation), cancellation, and lookups.
 */
public class ReservationService {

    private final ReservationRepository reservationRepo;

    public ReservationService(ReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    // ── Queries ──────────────────────────────────────────────

    public List<TrainingProgramReservation> getAllReservations() {
        return reservationRepo.findAll();
    }

    // ── Commands ─────────────────────────────────────────────


    public TrainingProgramReservation createReservation(Athlete athlete,
                                                       TrainingProgram program,
                                                       LocalDate date) {

        validateReservationFields(athlete, program, date);

        String uniqueCode = generateUniqueCode(athlete, program, date);

        TrainingProgramReservation reservation =
                new TrainingProgramReservation(uniqueCode, athlete, program, date);

        boolean saved = reservationRepo.save(reservation);
        if (!saved) {
            throw new RuntimeException("Failed to persist reservation.");
        }

        return reservation;
    }

    /**
     * Removes the given reservation from the repository.
     *
     * @throws IllegalArgumentException if the reservation is null.
     */
    public void cancelReservation(TrainingProgramReservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("No reservation selected for cancellation.");
        }

        boolean deleted = reservationRepo.delete(reservation);
        if (!deleted) {
            throw new RuntimeException("Failed to cancel reservation.");
        }
    }

    // ── Helpers ──────────────────────────────────────────────

    /**
     * Builds a unique code from programId, athleteId, and the date
     * in yyyyMMdd format (matches the original logic in AddReservation).
     */
    private String generateUniqueCode(Athlete athlete,
                                      TrainingProgram program,
                                      LocalDate date) {
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return program.getSerialNumber() + "_"
                + athlete.getSerialNumber() + "_"
                + formattedDate;
    }

    // ── Validation ───────────────────────────────────────────

    private void validateReservationFields(Athlete athlete,
                                           TrainingProgram program,
                                           LocalDate date) {

        if (athlete == null) {
            throw new IllegalArgumentException("Athlete is required.");
        }
        if (program == null) {
            throw new IllegalArgumentException("Training program is required.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Reservation date is required.");
        }
        if (athlete.getExperienceLevel() < program.getMinimumExp()) {
            throw new IllegalArgumentException(
                    "Athlete does not meet the minimum experience level ("
                            + program.getMinimumExp()
                            + ") required for this program.");
        }
    }
}

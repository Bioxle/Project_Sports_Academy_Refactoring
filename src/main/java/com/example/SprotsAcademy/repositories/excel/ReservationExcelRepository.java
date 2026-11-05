package com.example.SprotsAcademy.repositories.excel;

import com.example.SprotsAcademy.Athlete;
import com.example.SprotsAcademy.Subscription;
import com.example.SprotsAcademy.TrainingProgram;
import com.example.SprotsAcademy.TrainingProgramReservation;
import com.example.SprotsAcademy.repositories.AthleteRepository;
import com.example.SprotsAcademy.repositories.ReservationRepository;
import com.example.SprotsAcademy.repositories.TrainingProgramRepository;
import com.example.SprotsAcademy.repositories.Util;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationExcelRepository implements ReservationRepository {

    private Path filePath;
    private AthleteRepository athleteRepository;
    private TrainingProgramRepository trainingProgramRepository;

    public ReservationExcelRepository(AthleteRepository athleteRepository,
                                       TrainingProgramRepository trainingProgramRepository) {
        this(Path.of("data", "subscriptions.xlsx"), athleteRepository, trainingProgramRepository);
    }

    public ReservationExcelRepository(Path filePath, AthleteRepository athleteRepository, TrainingProgramRepository trainingProgramRepository) {
        this.filePath = filePath;
        this.athleteRepository = athleteRepository;
        this.trainingProgramRepository = trainingProgramRepository;
    }

    @Override
    public List<TrainingProgramReservation> findAll() {
        List<TrainingProgramReservation> reservations = new ArrayList<>();

        if (Files.notExists(filePath)) {
            return reservations;
        }

        try (InputStream input = Files.newInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(input)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                String uniqueNumber =  row.getCell(0).getStringCellValue();
                int athleteSerialNumber = (int) row.getCell(1).getNumericCellValue();
                int trainingProgramSerialNumber = (int) row.getCell(2).getNumericCellValue();
                LocalDate trainingProgramDate = LocalDate.parse(row.getCell(3).getStringCellValue());


                Athlete athlete = athleteRepository.findAthleteBySerialNumber(athleteSerialNumber);
                TrainingProgram trainingProgram = trainingProgramRepository.findTrainingProgramBySerialNumber(trainingProgramSerialNumber);

                if (athlete != null && trainingProgram != null) {
                    TrainingProgramReservation reservation = new TrainingProgramReservation(uniqueNumber, athlete, trainingProgram, trainingProgramDate);
                    reservations.add(reservation);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    @Override
    public boolean save(TrainingProgramReservation reservation) {
        try {
            Files.createDirectories(filePath.toAbsolutePath().getParent());

            Workbook loadedWorkbook;

            if (Files.exists(filePath)) {
                try (InputStream input = Files.newInputStream(filePath)) {
                    loadedWorkbook = new XSSFWorkbook(input);
                }
            } else {
                loadedWorkbook = new XSSFWorkbook();
                Sheet sheet = loadedWorkbook.createSheet("Reservations");

                String[] headers = {
                        "Unique Code",
                        "Athlete Serial Number",
                        "Training Program Serial Number",
                        "Training Program Date"
                };

                Row header = sheet.createRow(0);

                for (int i = 0; i < headers.length; i++) {
                    Util.createHeaderCell(header, i, headers[i]);
                }
            }

            try (Workbook workbook = loadedWorkbook) {
                Sheet sheet = workbook.getSheetAt(0);
                Row targetRow = null;

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        continue;
                    }

                    Cell codeCell = row.getCell(0);

                    if (codeCell != null
                            && codeCell.getCellType() == CellType.STRING
                            && codeCell.getStringCellValue()
                            .equals(reservation.getUniqueCode())) {

                        targetRow = row;
                        break;
                    }
                }

                if (targetRow == null) {
                    targetRow = sheet.createRow(sheet.getLastRowNum() + 1);
                }

                targetRow.createCell(0).setCellValue(
                        reservation.getUniqueCode());

                targetRow.createCell(1).setCellValue(
                        reservation.getAthlete().getSerialNumber());

                targetRow.createCell(2).setCellValue(
                        reservation.getTrainingProgram().getSerialNumber());

                targetRow.createCell(3).setCellValue(
                        reservation.getTrainingProgramDate().toString());

                try (OutputStream output = Files.newOutputStream(filePath)) {
                    workbook.write(output);
                }

                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(TrainingProgramReservation reservation) {
        if (Files.notExists(filePath)) {
            return false;
        }

        try {
            Workbook loadedWorkbook;

            try (InputStream input = Files.newInputStream(filePath)) {
                loadedWorkbook = new XSSFWorkbook(input);
            }

            try (Workbook workbook = loadedWorkbook) {
                Sheet sheet = workbook.getSheetAt(0);
                Row targetRow = null;

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        continue;
                    }

                    Cell codeCell = row.getCell(0);

                    if (codeCell != null
                            && codeCell.getCellType() == CellType.STRING
                            && codeCell.getStringCellValue()
                            .equals(reservation.getUniqueCode())) {

                        targetRow = row;
                        break;
                    }
                }

                if (targetRow == null) {
                    return false;
                }

                int rowIndex = targetRow.getRowNum();
                int lastRowIndex = sheet.getLastRowNum();

                sheet.removeRow(targetRow);

                if (rowIndex < lastRowIndex) {
                    sheet.shiftRows(rowIndex + 1, lastRowIndex, -1);
                }

                try (OutputStream output = Files.newOutputStream(filePath)) {
                    workbook.write(output);
                }

                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

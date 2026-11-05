package com.example.SprotsAcademy.repositories.excel;

import com.example.SprotsAcademy.model.Athlete;
import com.example.SprotsAcademy.model.Subscription;
import com.example.SprotsAcademy.model.TrainingProgram;
import com.example.SprotsAcademy.repositories.AthleteRepository;
import com.example.SprotsAcademy.repositories.SubscriptionRepository;
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

public class SubscriptionExcelRepository implements SubscriptionRepository {

    private final Path filePath;
    private AthleteRepository athleteRepository;
    private TrainingProgramRepository trainingProgramRepository;

    public SubscriptionExcelRepository(AthleteRepository athleteRepository,
                                       TrainingProgramRepository trainingProgramRepository) {
        this(Path.of("data", "subscriptions.xlsx"), athleteRepository, trainingProgramRepository);
    }

    public SubscriptionExcelRepository(Path filePath, AthleteRepository athleteRepository, TrainingProgramRepository trainingProgramRepository) {
        this.filePath = filePath;
        this.athleteRepository = athleteRepository;
        this.trainingProgramRepository = trainingProgramRepository;
    }

    @Override
    public List<Subscription> findAll() {
        List<Subscription> subscriptions = new ArrayList<>();

        if (Files.notExists(filePath)) {
            return subscriptions;
        }

        try (InputStream input = Files.newInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(input)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                Cell idCell = row.getCell(0);

                if (idCell == null
                        || idCell.getCellType() == CellType.BLANK) {
                    continue;
                }

                int serialNumber =
                        (int) idCell.getNumericCellValue();

                int athleteSerialNumber =
                        (int) row.getCell(1).getNumericCellValue();

                int trainingProgramSerialNumber =
                        (int) row.getCell(2).getNumericCellValue();

                LocalDate startDate = LocalDate.parse(
                        row.getCell(3).getStringCellValue());

                LocalDate endDate = LocalDate.parse(
                        row.getCell(4).getStringCellValue());

                double monthlyCost =
                        row.getCell(5).getNumericCellValue();

                double discountPercentage =
                        row.getCell(6).getNumericCellValue();

                Athlete athlete = athleteRepository
                        .findAthleteBySerialNumber(athleteSerialNumber);

                TrainingProgram program = trainingProgramRepository
                        .findTrainingProgramBySerialNumber(
                                trainingProgramSerialNumber);

                if (athlete == null || program == null) {
                    throw new IllegalStateException(
                            "Missing athlete or training program for subscription "
                                    + serialNumber
                    );
                }

                subscriptions.add(new Subscription(
                        serialNumber,
                        athlete,
                        program,
                        startDate,
                        endDate,
                        monthlyCost,
                        discountPercentage
                ));
            }

            return subscriptions;

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Could not read subscriptions from " + filePath, e
            );
        }
    }

    public boolean saveAll(List<Subscription> subscriptions) {
        for (Subscription subscription : subscriptions) {
            if (!save(subscription)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean save(Subscription subscription) {
        try {
            Files.createDirectories(filePath.getParent());

            Workbook loadedWorkbook;

            if (Files.exists(filePath)) {
                try (FileInputStream input =
                             new FileInputStream(filePath.toFile())) {

                    loadedWorkbook = new XSSFWorkbook(input);
                }
            } else {
                loadedWorkbook = new XSSFWorkbook();
                Sheet sheet = loadedWorkbook.createSheet("Subscriptions");

                Row header = sheet.createRow(0);
                String[] headers = {
                        "Serial Number",
                        "Athlete Serial Number",
                        "Training Program Serial Number",
                        "Start Date",
                        "End Date",
                        "Monthly Cost",
                        "Discount Percentage"
                };

                for (int i = 0; i < headers.length; i++) {
                    Util.createHeaderCell(header, i, headers[i]);
                }
            }

            try (Workbook workbook = loadedWorkbook) {
                Sheet sheet = workbook.getSheetAt(0);
                Row targetRow = null;

                // Find an existing subscription with the same ID.
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        continue;
                    }

                    Cell idCell = row.getCell(0);

                    if (idCell != null
                            && idCell.getCellType() == CellType.NUMERIC
                            && idCell.getNumericCellValue()
                            == subscription.getSerialNumber()) {

                        targetRow = row;
                        break;
                    }
                }

                if (targetRow == null) {
                    targetRow = sheet.createRow(sheet.getLastRowNum() + 1);
                }

                targetRow.createCell(0).setCellValue(
                        subscription.getSerialNumber());
                targetRow.createCell(1).setCellValue(
                        subscription.getAthlete().getSerialNumber());
                targetRow.createCell(2).setCellValue(
                        subscription.getTrainingProgram().getSerialNumber());
                targetRow.createCell(3).setCellValue(
                        subscription.getStartDate().toString());
                targetRow.createCell(4).setCellValue(
                        subscription.getEndDate().toString());
                targetRow.createCell(5).setCellValue(
                        subscription.getMonthlyCost());
                targetRow.createCell(6).setCellValue(
                        subscription.getDiscountPercentage());

                try (FileOutputStream output =
                             new FileOutputStream(filePath.toFile())) {

                    workbook.write(output);
                }
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Subscription subscription) {
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

                    Cell idCell = row.getCell(0);

                    if (idCell != null
                            && idCell.getCellType() == CellType.NUMERIC
                            && idCell.getNumericCellValue()
                            == subscription.getSerialNumber()) {

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

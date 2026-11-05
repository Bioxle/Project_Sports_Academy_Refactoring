package com.example.SprotsAcademy.repositories.excel;

import com.example.SprotsAcademy.Athlete;
import com.example.SprotsAcademy.repositories.AthleteRepository;
import com.example.SprotsAcademy.repositories.Util;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AthleteExcelRepository implements AthleteRepository {

    private final Path filePath;

    public AthleteExcelRepository() {
        this(Path.of("data", "athletesData.xlsx"));
    }

    public AthleteExcelRepository(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Athlete> findAll() {
        List<Athlete> athletes = new ArrayList<>();

        if (Files.notExists(filePath)) {
            return athletes;
        }

        try (Workbook workbook = openWorkbook()) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0 || isBlank(row)) {
                    continue;
                }

                athletes.add(readAthlete(row));
            }

            return athletes;

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Could not read athletes from " + filePath, e
            );
        }
    }

    @Override
    public Athlete findAthleteBySerialNumber(int athleteSerialNumber) {
        for (Athlete athlete : findAll()) {
            if (athlete.getSerialNumber() == athleteSerialNumber) {
                return athlete;
            }
        }

        return null;
    }

    @Override
    public boolean save(Athlete athlete) {
        try (Workbook workbook = openWorkbook()) {
            Sheet sheet = workbook.getSheetAt(0);

            Row row = findRow(sheet, athlete.getSerialNumber());

            if (row == null) {
                row = sheet.createRow(sheet.getLastRowNum() + 1);
            }

            writeAthlete(row, athlete);
            writeWorkbook(workbook);

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Athlete athlete) {
        if (Files.notExists(filePath)) {
            return false;
        }

        try (Workbook workbook = openWorkbook()) {
            Sheet sheet = workbook.getSheetAt(0);
            Row row = findRow(sheet, athlete.getSerialNumber());

            if (row == null) {
                return false;
            }

            int rowIndex = row.getRowNum();
            int lastRowIndex = sheet.getLastRowNum();

            sheet.removeRow(row);

            if (rowIndex < lastRowIndex) {
                sheet.shiftRows(rowIndex + 1, lastRowIndex, -1);
            }

            writeWorkbook(workbook);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Workbook openWorkbook() throws IOException {
        if (Files.exists(filePath)) {
            // Close the input before later writing to the same file.
            try (InputStream input = Files.newInputStream(filePath)) {
                return new XSSFWorkbook(input);
            }
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Athlete Data");

        String[] headers = {
                "Serial Number",
                "Name",
                "Surname",
                "DOB",
                "Contact",
                "Experience Level",
                "Professional",
                "Sheet"
        };

        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < headers.length; i++) {
            Util.createHeaderCell(headerRow, i, headers[i]);
        }

        return workbook;
    }

    private Row findRow(Sheet sheet, int serialNumber) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            Cell cell = row.getCell(0);

            if (cell != null
                    && cell.getCellType() == CellType.NUMERIC
                    && cell.getNumericCellValue() == serialNumber) {
                return row;
            }
        }

        return null;
    }

    private Athlete readAthlete(Row row) {
        LocalDate dateOfBirth = row.getCell(3)
                .getLocalDateTimeCellValue()
                .toLocalDate();

        return new Athlete(
                (int) row.getCell(0).getNumericCellValue(),
                row.getCell(1).getStringCellValue(),
                row.getCell(2).getStringCellValue(),
                row.getCell(7).getStringCellValue(),
                dateOfBirth,
                row.getCell(4).getStringCellValue(),
                row.getCell(6).getBooleanCellValue(),
                (int) row.getCell(5).getNumericCellValue()
        );
    }

    private void writeAthlete(Row row, Athlete athlete) {
        row.createCell(0).setCellValue(athlete.getSerialNumber());
        row.createCell(1).setCellValue(athlete.getName());
        row.createCell(2).setCellValue(athlete.getSurname());
        row.createCell(3).setCellValue(athlete.getDateOfBirth());
        row.createCell(4).setCellValue(athlete.getContact());
        row.createCell(5).setCellValue(athlete.getExperienceLevel());
        row.createCell(6).setCellValue(athlete.getProfessional());
        row.createCell(7).setCellValue(athlete.getSheet());
    }

    private void writeWorkbook(Workbook workbook) throws IOException {
        Path parent = filePath.toAbsolutePath().getParent();
        Files.createDirectories(parent);

        try (OutputStream output = Files.newOutputStream(filePath)) {
            workbook.write(output);
        }
    }

    private boolean isBlank(Row row) {
        DataFormatter formatter = new DataFormatter();

        for (Cell cell : row) {
            if (!formatter.formatCellValue(cell).isBlank()) {
                return false;
            }
        }

        return true;
    }
}
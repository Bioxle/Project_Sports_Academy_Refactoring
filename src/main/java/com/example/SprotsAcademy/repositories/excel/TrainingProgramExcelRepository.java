package com.example.SprotsAcademy.repositories.excel;

import com.example.SprotsAcademy.TrainingProgram;
import com.example.SprotsAcademy.repositories.TrainingProgramRepository;
import com.example.SprotsAcademy.repositories.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TrainingProgramExcelRepository implements TrainingProgramRepository {

    private Path filePath;


    public TrainingProgramExcelRepository() {
        Path filePath = Paths.get("data", "trainingProgram.xlsx");
    }

    public TrainingProgramExcelRepository(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<TrainingProgram> findAll() {
        List<TrainingProgram> programs = new ArrayList<>();

        if (Files.notExists(filePath)) {
            return programs;
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

                programs.add(new TrainingProgram(
                        (int) idCell.getNumericCellValue(),
                        row.getCell(1).getStringCellValue(),
                        (int) row.getCell(2).getNumericCellValue(),
                        row.getCell(3).getStringCellValue(),
                        (int) row.getCell(4).getNumericCellValue(),
                        row.getCell(5).getBooleanCellValue(),
                        row.getCell(6).getStringCellValue(),
                        (int) row.getCell(7).getNumericCellValue(),
                        row.getCell(8).getStringCellValue()
                ));
            }

            return programs;

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Could not read training programs from " + filePath, e
            );
        }
    }

    @Override
    public ObservableList<TrainingProgram> findAllObservable() {return FXCollections.observableArrayList(findAll());}

    @Override
    public boolean save(TrainingProgram program) {
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
                Sheet sheet = loadedWorkbook.createSheet("Training Programs");

                String[] headers = {
                        "Serial Number",
                        "Sport",
                        "Facility",
                        "Coach",
                        "Minimum Experience",
                        "Week Of The Athlete",
                        "Sheet",
                        "Duration",
                        "Day Of The Week"
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

                    Cell idCell = row.getCell(0);

                    if (idCell != null
                            && idCell.getCellType() == CellType.NUMERIC
                            && idCell.getNumericCellValue()
                            == program.getSerialNumber()) {

                        targetRow = row;
                        break;
                    }
                }

                if (targetRow == null) {
                    targetRow = sheet.createRow(sheet.getLastRowNum() + 1);
                }

                targetRow.createCell(0).setCellValue(
                        program.getSerialNumber());
                targetRow.createCell(1).setCellValue(
                        program.getSport());
                targetRow.createCell(2).setCellValue(
                        program.getFacility());
                targetRow.createCell(3).setCellValue(
                        program.getCoach());
                targetRow.createCell(4).setCellValue(
                        program.getMinimumExp());
                targetRow.createCell(5).setCellValue(
                        program.isWeekOfTheAthlete());
                targetRow.createCell(6).setCellValue(
                        program.getSheet());
                targetRow.createCell(7).setCellValue(
                        program.getDuration());
                targetRow.createCell(8).setCellValue(
                        program.getDayOfTheWeek());

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
    public boolean delete(TrainingProgram program) {
        if (Files.notExists(filePath)) {
            return false;
        }

        try {
            Workbook loadedWorkbook;

            try (FileInputStream input =
                         new FileInputStream(filePath.toFile())) {

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
                            == program.getSerialNumber()) {

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

                // Move subsequent rows up to fill the gap.
                if (rowIndex < lastRowIndex) {
                    sheet.shiftRows(rowIndex + 1, lastRowIndex, -1);
                }

                try (FileOutputStream output =
                             new FileOutputStream(filePath.toFile())) {

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
    public TrainingProgram findTrainingProgramBySerialNumber(int trainingProgramSerialNumber){
        List<TrainingProgram> programs = findAll();
        for (TrainingProgram program : programs) {
            if (program.getSerialNumber() == trainingProgramSerialNumber) {
                return program;
            }
        }
        return null;
    }

}

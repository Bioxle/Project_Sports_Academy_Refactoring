package com.example.SprotsAcademy;

import com.example.SprotsAcademy.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {


    public static void savePaymentToExcel(Payment payment) {


        Path dataDir = Paths.get("data");
        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File file = new File(dataDir.toFile(), "payments.xlsx");
        Workbook workbook;
        Sheet sheet;

        try {
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Payments");

                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Athlete Name");
                headerRow.createCell(1).setCellValue("Payment Date");
                headerRow.createCell(2).setCellValue("Payment Method");
                headerRow.createCell(3).setCellValue("Enrollment Serial Number");
                headerRow.createCell(4).setCellValue("Total Cost");
                headerRow.createCell(4).setCellValue("Payment Type");

            }

            int lastRowNum = sheet.getLastRowNum();
            Row dataRow = sheet.createRow(lastRowNum + 1);
            dataRow.createCell(0).setCellValue(payment.getEnrollment().getAthleteName());
            dataRow.createCell(1).setCellValue(payment.getDate().toString());
            dataRow.createCell(2).setCellValue(payment.getPaymentMethod());
            dataRow.createCell(3).setCellValue(payment.getEnrollment().getUniqueCode());
            dataRow.createCell(4).setCellValue(payment.getTotalCost());
            dataRow.createCell(5).setCellValue(payment.getSubscriptionOrRegistration());

            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static List<TrainingProgramReservation>  getReservations(){
        List<TrainingProgramReservation> reservations = new ArrayList<>();

        Path dataDir = Paths.get("data");
        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File file = new File(dataDir.toFile(), "reservations.xlsx");

        if (!file.exists()) {
            return reservations; // No subscriptions file found
        }

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                String uniqueNumber =  row.getCell(0).getStringCellValue();
                int athleteSerialNumber = (int) row.getCell(1).getNumericCellValue();
                int trainingProgramSerialNumber = (int) row.getCell(2).getNumericCellValue();
                LocalDate trainingProgramDate = LocalDate.parse(row.getCell(3).getStringCellValue());


                Athlete athlete = findAthleteBySerialNumber(athleteSerialNumber);
                TrainingProgram trainingProgram = findTrainingProgramBySerialNumber(trainingProgramSerialNumber);

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

    public static List<Subscription>  getSubscriptions(){
        List<Subscription> subscriptions = new ArrayList<>();

        Path dataDir = Paths.get("data");
        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File file = new File(dataDir.toFile(), "subscriptions.xlsx");

        if (!file.exists()) {
            System.out.println("returned null");
            return null; // No subscriptions file found
        }

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                int serialNumber = (int) row.getCell(0).getNumericCellValue();
                int athleteSerialNumber = (int) row.getCell(1).getNumericCellValue();
                int trainingProgramSerialNumber = (int) row.getCell(2).getNumericCellValue();
                LocalDate startDate = LocalDate.parse(row.getCell(3).getStringCellValue());
                LocalDate endDate = LocalDate.parse(row.getCell(4).getStringCellValue());
                double monthlyCost = row.getCell(5).getNumericCellValue();
                double discountPercentage = row.getCell(6).getNumericCellValue();

                Athlete athlete = findAthleteBySerialNumber(athleteSerialNumber);
                TrainingProgram trainingProgram = findTrainingProgramBySerialNumber(trainingProgramSerialNumber);

                if (athlete != null && trainingProgram != null) {
                    Subscription subscription = new Subscription(serialNumber, athlete, trainingProgram, startDate, endDate, monthlyCost, discountPercentage);
                    subscriptions.add(subscription);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subscriptions;
    }
    static Athlete findAthleteBySerialNumber(int athleteSerialNumber){
        List<Athlete> athletes = getAthleteList();
        for (Athlete athlete : athletes) {
            if (athlete.getSerialNumber() == athleteSerialNumber) {
                return athlete;
            }
        }
        return null;
    }

    static TrainingProgram findTrainingProgramBySerialNumber(int trainingProgramSerialNumber){
        ObservableList<TrainingProgram> programs = getTrainingPrograms();
        for (TrainingProgram program : programs) {
            if (program.getSerialNumber() == trainingProgramSerialNumber) {
                return program;
            }
        }
        return null;
    }


    static private ObservableList<TrainingProgram> getTrainingPrograms() {
        ObservableList<TrainingProgram> programs = FXCollections.observableArrayList();

        Path dataDir = Paths.get("data");
        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        File file = new File(dataDir.toFile(), "trainingProgram.xlsx");

        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                int serialNumber = (int) row.getCell(0).getNumericCellValue();
                String sport = row.getCell(1).getStringCellValue();
                int facility = (int) row.getCell(2).getNumericCellValue();
                String coach = row.getCell(3).getStringCellValue();
                int minimumExp = (int) row.getCell(4).getNumericCellValue();
                boolean weekOfTheAthlete = row.getCell(5).getBooleanCellValue();
                String sheetValue = row.getCell(6).getStringCellValue();
                int duration = (int) row.getCell(7).getNumericCellValue();
                String dayOfTheWeek = row.getCell(8).getStringCellValue();

                programs.add(new TrainingProgram(serialNumber, sport, facility, coach, minimumExp, weekOfTheAthlete, sheetValue, duration, dayOfTheWeek));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return programs;
    }









    public static void saveReservationsToExcel(List<TrainingProgramReservation> reservations) {
        Path dataDir = Paths.get("data");
        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File filePath = new File(dataDir.toFile(), "reservations.xlsx");



        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reservations");

        // Create header row
        Row headerRow = sheet.createRow(0);
        createHeaderCell(headerRow, 0, "Unique Code");
        createHeaderCell(headerRow, 1, "Athlete Serial Number");
        createHeaderCell(headerRow, 2, "Training Program Serial Number");
        createHeaderCell(headerRow, 3, "Training Program Date");


        // Populate data rows
        int rowNum = 1;
        for (TrainingProgramReservation reservation : reservations) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(reservation.getUniqueCode());
            row.createCell(1).setCellValue(reservation.getAthlete().getSerialNumber());
            row.createCell(2).setCellValue(reservation.getTrainingProgram().getSerialNumber());
            row.createCell(3).setCellValue(reservation.getTrainingProgramDate().toString());
        }

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void saveSubscriptionsToExcel(List<Subscription> subscriptions) {
        Path dataDir = Paths.get("data");
        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File filePath = new File(dataDir.toFile(), "subscriptions.xlsx");



        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Subscriptions");

        // Create header row
        Row headerRow = sheet.createRow(0);
        createHeaderCell(headerRow, 0, "Serial Number");
        createHeaderCell(headerRow, 1, "Athlete Serial Number");
        createHeaderCell(headerRow, 2, "Training Program Serial Number");
        createHeaderCell(headerRow, 3, "Start Date");
        createHeaderCell(headerRow, 4, "End Date");
        createHeaderCell(headerRow, 5, "Monthly Cost");
        createHeaderCell(headerRow, 6, "Discount Percentage");

        // Populate data rows
        int rowNum = 1;
        for (Subscription subscription : subscriptions) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(subscription.getSerialNumber());
            row.createCell(1).setCellValue(subscription.getAthlete().getSerialNumber());
            row.createCell(2).setCellValue(subscription.getTrainingProgram().getSerialNumber());
            row.createCell(3).setCellValue(subscription.getStartDate().toString());
            row.createCell(4).setCellValue(subscription.getEndDate().toString());
            row.createCell(5).setCellValue(subscription.getMonthlyCost());
            row.createCell(6).setCellValue(subscription.getDiscountPercentage());
        }

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    private static void createHeaderCell(Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        CellStyle style = cell.getSheet().getWorkbook().createCellStyle();
        Font font = cell.getSheet().getWorkbook().createFont();
        font.setBold(true);
        style.setFont(font);
        cell.setCellStyle(style);
    }

    public static boolean updateAthlete(Athlete athlete) {
        try {
            Path dataDir = Paths.get("data");
            File file = new File(dataDir.toFile(), "athletesData.xlsx");

            if (!file.exists()) {
                // If the file doesn't exist, you can't update, return false or handle accordingly
                return false;
            }

            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            boolean athleteFound = false;

            for (Row row : sheet) {
                Cell serialNumberCell = row.getCell(0);
                if (serialNumberCell != null && serialNumberCell.getCellType() == CellType.NUMERIC) {
                    int serialNumber = (int) serialNumberCell.getNumericCellValue();
                    if (serialNumber == athlete.getSerialNumber()) {
                        // Update the athlete's information in this row
                        row.createCell(1).setCellValue(athlete.getName());
                        row.createCell(2).setCellValue(athlete.getSurname());
                        row.createCell(3).setCellValue(athlete.getDateOfBirth());
                        row.createCell(4).setCellValue(athlete.getContact());
                        row.createCell(5).setCellValue(athlete.getExperienceLevel());
                        row.createCell(6).setCellValue(athlete.getProfessional());
                        row.createCell(7).setCellValue(athlete.getSheet());

                        athleteFound = true;
                        break;
                    }
                }
            }

            if (!athleteFound) {
                // If athlete with the specified serial number was not found, return false or handle accordingly
                return false;
            }

            // Write the updated workbook back to the file
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                workbook.write(outputStream);
            }

            workbook.close();
            inputStream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public static List<Athlete> getAthleteList() {
        List<Athlete> athleteList = new ArrayList<>();
        try {
            Path dataDir = Paths.get("data");
            File file = new File(dataDir.toFile(), "athletesData.xlsx");

            if (!file.exists()) {
                return athleteList; // Return an empty list if the file doesn't exist
            }

            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                int serialNumber = (int) row.getCell(0).getNumericCellValue();
                String name = row.getCell(1).getStringCellValue();
                String surname = row.getCell(2).getStringCellValue();
                LocalDate dob = row.getCell(3).getLocalDateTimeCellValue().toLocalDate();
                String contact = row.getCell(4).getStringCellValue();
                int experienceLevel = (int) row.getCell(5).getNumericCellValue();
                boolean professional = row.getCell(6).getBooleanCellValue();
                String sheetName = row.getCell(7).getStringCellValue();

                Athlete athlete = new Athlete(serialNumber, name, surname, sheetName, dob, contact, professional, experienceLevel);
                athleteList.add(athlete);
            }

            workbook.close();
            inputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return athleteList;
    }

    static boolean registerAthlete(Athlete athlete) {
        try {
            Path dataDir = Paths.get("data");
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }

            File file = new File(dataDir.toFile(), "athletesData.xlsx");

            if (!file.exists()) {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Athlete Data");

                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Serial Number");
                headerRow.createCell(1).setCellValue("Name");
                headerRow.createCell(2).setCellValue("Surname");
                headerRow.createCell(3).setCellValue("DOB");
                headerRow.createCell(4).setCellValue("Contact");
                headerRow.createCell(5).setCellValue("Experience Level");
                headerRow.createCell(6).setCellValue("Professional");
                headerRow.createCell(7).setCellValue("Sheet");

                Row row = sheet.createRow(1);
                row.createCell(0).setCellValue(athlete.getSerialNumber());
                row.createCell(1).setCellValue(athlete.getName());
                row.createCell(2).setCellValue(athlete.getSurname());
                row.createCell(3).setCellValue(athlete.getDateOfBirth());
                row.createCell(4).setCellValue(athlete.getContact());
                row.createCell(5).setCellValue(athlete.getExperienceLevel());
                row.createCell(6).setCellValue(athlete.getProfessional());
                row.createCell(7).setCellValue(athlete.getSheet());

                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    workbook.write(outputStream);
                }

                workbook.close();
            } else {
                FileInputStream inputStream = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0);

                boolean alreadyExists = false;
                for (Row row : sheet) {
                    Cell cell = row.getCell(1);
                    if (cell != null && cell.getCellType() == CellType.STRING && cell.getStringCellValue().equalsIgnoreCase(athlete.getName())) {
                        alreadyExists = true;
                        break;
                    }
                }

                if (alreadyExists) {
                    return false;
                } else {
                    int rowCount = sheet.getPhysicalNumberOfRows();
                    Row row = sheet.createRow(rowCount);

                    row.createCell(0).setCellValue(athlete.getSerialNumber());
                    row.createCell(1).setCellValue(athlete.getName());
                    row.createCell(2).setCellValue(athlete.getSurname());
                    row.createCell(3).setCellValue(athlete.getDateOfBirth());
                    row.createCell(4).setCellValue(athlete.getContact());
                    row.createCell(5).setCellValue(athlete.getExperienceLevel());
                    row.createCell(6).setCellValue(athlete.getProfessional());
                    row.createCell(7).setCellValue(athlete.getSheet());

                    try (FileOutputStream outputStream = new FileOutputStream(file)) {
                        workbook.write(outputStream);
                    }

                    inputStream.close();
                    workbook.close();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}

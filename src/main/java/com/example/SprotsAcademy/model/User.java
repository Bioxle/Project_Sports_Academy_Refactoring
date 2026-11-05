package com.example.SprotsAcademy.model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public abstract class User {
    private int serialNumber;
    private String name;
    private String surname;
    private String sheet;
    private LocalDate dateOfBirth;
    private String contact;
    private LocalDate dateOfRegistration;

    int currentSerial=-1;

    int getCurrentSerial() throws FileNotFoundException {

        System.out.println("here");
        if(currentSerial!=-1){
            System.out.println("here2");
            return currentSerial++;
        }
        else{
            System.out.println("here3");

            Path dataDir = Paths.get("data");
            File file = new File(dataDir.toFile(), "athletesData.xlsx");

            if (!file.exists()) {
                // If the file doesn't exist, you can't update, return false or handle accordingly
                currentSerial =1;
                System.out.println("here4");
                return currentSerial++;
            }


            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                System.out.println("here5");
                currentSerial =1;
                return currentSerial++;
            }
            Workbook workbook = null;
            try {
                workbook = new XSSFWorkbook(inputStream);
            } catch (IOException e) {
                System.out.println("here6");
                throw new RuntimeException(e);
            }
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell serialNumberCell = row.getCell(0);
                if (serialNumberCell != null && serialNumberCell.getCellType() == CellType.NUMERIC) {
                    int serialNumber = (int) serialNumberCell.getNumericCellValue();
                    if(currentSerial <=serialNumber){
                        currentSerial=serialNumber+1;
                    }
                }
            }
            System.out.println("here7");
            if(currentSerial==-1){
                currentSerial=1;
                return currentSerial++;
            }
            else{
                return currentSerial++;
            }
        }
    }






    // Default constructor
    public User() {
        // to implement id logic
    }

    public User(int serialNumber,String name, String surname, String sheet, LocalDate dateOfBirth, String contact) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.surname = surname;
        this.sheet = sheet;
        this.dateOfBirth = dateOfBirth;
        this.contact = contact;
        this.dateOfRegistration = LocalDate.now();
    }

    // Parameterized constructor
    public User(String name, String surname, String sheet, LocalDate dateOfBirth, String contact) {
        // to implement id logic
        this.name = name;
        this.surname = surname;
        this.sheet = sheet;
        this.dateOfBirth = dateOfBirth;
        this.contact = contact;
        this.dateOfRegistration = LocalDate.now();



    }

    public LocalDate getDateOfRegistration(){
        return dateOfRegistration;
    }
    public void setDateOfRegistration(LocalDate dateOfRegistration){
        this.dateOfRegistration = dateOfRegistration;
    }

    // Getter for serialNumber
    public int getSerialNumber() {
        return serialNumber;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for surname
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    // Getter and Setter for sheet
    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    // Getter and Setter for dateOfBirth
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Getter and Setter for contact
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}

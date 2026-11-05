package com.example.SprotsAcademy.repositories.excel;

import com.example.SprotsAcademy.model.Enrollment;
import com.example.SprotsAcademy.model.Payment;
import com.example.SprotsAcademy.repositories.PaymentRepository;
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

public class PaymentExcelRepository implements PaymentRepository {

    private final Path filePath =
            Path.of("data", "payments.xlsx");

    private static final int PAYMENT_ID_COLUMN = 6;

    private static final String[] HEADERS = {
            "Athlete Name",
            "Payment Date",
            "Payment Method",
            "Enrollment Serial Number",
            "Total Cost",
            "Payment Type",
            "Payment Serial Number",
            "Enrollment Date",
            "Enrollment Cost",
            "Enrollment Discount Percentage"
    };

    @Override
    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();

        if (Files.notExists(filePath)) {
            return payments;
        }

        try (Workbook workbook = openWorkbook()) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0 || isBlank(row)) {
                    continue;
                }

                requirePaymentId(row);

                Enrollment enrollment = new Enrollment(
                        (int) row.getCell(3).getNumericCellValue(),
                        row.getCell(0).getStringCellValue(),
                        LocalDate.parse(
                                row.getCell(7).getStringCellValue()
                        ),
                        row.getCell(8).getNumericCellValue(),
                        row.getCell(9).getNumericCellValue()
                );

                payments.add(new Payment(
                        (int) row.getCell(PAYMENT_ID_COLUMN)
                                .getNumericCellValue(),
                        LocalDate.parse(
                                row.getCell(1).getStringCellValue()
                        ),
                        row.getCell(2).getStringCellValue(),
                        enrollment,
                        row.getCell(5).getStringCellValue()
                ));
            }

            return payments;

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Could not read payments from " + filePath, e
            );
        }
    }

    @Override
    public boolean save(Payment payment) {
        try (Workbook workbook = openWorkbook()) {
            Sheet sheet = workbook.getSheetAt(0);

            Row targetRow = findRow(sheet, payment.getUniqueCode());

            if (targetRow == null) {
                targetRow = sheet.createRow(sheet.getLastRowNum() + 1);
            }

            Enrollment enrollment = payment.getEnrollment();

            targetRow.createCell(0).setCellValue(
                    enrollment.getAthleteName());

            targetRow.createCell(1).setCellValue(
                    payment.getDate().toString());

            targetRow.createCell(2).setCellValue(
                    payment.getPaymentMethod());

            targetRow.createCell(3).setCellValue(
                    enrollment.getUniqueCode());

            targetRow.createCell(4).setCellValue(
                    payment.getTotalCost());

            targetRow.createCell(5).setCellValue(
                    payment.getSubscriptionOrRegistration());

            targetRow.createCell(6).setCellValue(
                    payment.getUniqueCode());

            targetRow.createCell(7).setCellValue(
                    enrollment.getDate().toString());

            targetRow.createCell(8).setCellValue(
                    enrollment.getCost());

            targetRow.createCell(9).setCellValue(
                    enrollment.getDiscountPercentage());

            writeWorkbook(workbook);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Payment payment) {
        if (Files.notExists(filePath)) {
            return false;
        }

        try (Workbook workbook = openWorkbook()) {
            Sheet sheet = workbook.getSheetAt(0);
            Row targetRow = findRow(sheet, payment.getUniqueCode());

            if (targetRow == null) {
                return false;
            }

            int rowIndex = targetRow.getRowNum();
            int lastRowIndex = sheet.getLastRowNum();

            sheet.removeRow(targetRow);

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
            try (InputStream input = Files.newInputStream(filePath)) {
                return new XSSFWorkbook(input);
            }
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Payments");
        Row header = sheet.createRow(0);

        for (int i = 0; i < HEADERS.length; i++) {
            header.createCell(i).setCellValue(HEADERS[i]);
        }

        return workbook;
    }

    private Row findRow(Sheet sheet, int paymentId) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0 || isBlank(row)) {
                continue;
            }

            requirePaymentId(row);

            if (row.getCell(PAYMENT_ID_COLUMN).getNumericCellValue()
                    == paymentId) {
                return row;
            }
        }

        return null;
    }

    private void requirePaymentId(Row row) {
        Cell idCell = row.getCell(PAYMENT_ID_COLUMN);

        if (idCell == null
                || idCell.getCellType() != CellType.NUMERIC) {
            throw new IllegalStateException(
                    "Payment row " + (row.getRowNum() + 1)
                            + " is missing a numeric payment ID in column 6."
            );
        }
    }

    private void writeWorkbook(Workbook workbook) throws IOException {
        Files.createDirectories(
                filePath.toAbsolutePath().getParent()
        );

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
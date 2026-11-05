package com.example.SprotsAcademy.repositories;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

public class Util {

    public static void createHeaderCell(Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        CellStyle style = cell.getSheet().getWorkbook().createCellStyle();
        Font font = cell.getSheet().getWorkbook().createFont();
        font.setBold(true);
        style.setFont(font);
        cell.setCellStyle(style);
    }

}

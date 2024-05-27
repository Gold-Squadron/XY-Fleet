package de.cae.XYFleet.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import static org.jooq.codegen.XYFleet.Tables.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

public class ExcelConverter {
    private static DSLContext dslContext;
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3308", "root", "u2SG7FdmzNE4vZU3kVALCQhPYywfBH5X9raxWJ6T");
            dslContext =  DSL.using(conn, SQLDialect.MARIADB);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Vehicles");
        Result<org.jooq.codegen.XYFleet.tables.records.CompletevehicleRecord> completeVehicleRecords = dslContext.fetch(COMPLETEVEHICLE);

        //set create Style for first Columns with qualifier
        XSSFCellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        Font font = workbook.getFontAt(0);
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);

        int rowCount = 0;
        Field<?>[] fields = COMPLETEVEHICLE.fields();
        Row row = sheet.createRow(++rowCount);
        //set first row with names of the table column
        int columnCount = 0;
        for (Field<?> field : fields) {
            Cell cell = row.createCell(++columnCount);
            cell.setCellValue(nameConverter(field.getUnqualifiedName().first()));
            cell.setCellStyle(style);
        }
        //fill the table in excel with the values found in the database vehicles table
        for (org.jooq.codegen.XYFleet.tables.records.CompletevehicleRecord completevehicleRecord: completeVehicleRecords) {
            row = sheet.createRow(++rowCount);

            columnCount = 0;

            for (Field<?> field : fields) {
                //checking whether amendments have been requested or not. Setting all types to String type safty in tests
                String name = field.getUnqualifiedName().first();
                Object value = completevehicleRecord.get(field);
                Cell cell = row.createCell(++columnCount);
                if (value instanceof String) {
                    cell.setCellValue((String) value);
                } else if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                } else if (value instanceof LocalDate) {
                    cell.setCellValue((LocalDate) value);
                }else if (value instanceof Byte) {
                    cell.setCellValue((Byte) value);
                } else if (value instanceof Long) {
                    cell.setCellValue((Long) value);
                } else if (value instanceof BigInteger) {
                    cell.setCellValue(((BigInteger) value).doubleValue());
                }
            }
            for(columnCount = 1;columnCount<= fields.length;columnCount++){
                sheet.autoSizeColumn(columnCount);
            }
        }
        //beautify first row
        autoFitRowHeight(sheet);
        try (FileOutputStream outputStream = new FileOutputStream("Test.xlsx")) {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void autoFitRowHeight(XSSFSheet sheet) {
        DataFormatter dataFormatter = new DataFormatter();
        for (Row row : sheet) {
            float maxHeight = sheet.getDefaultRowHeightInPoints();
            for (Cell cell : row) {
                CellStyle cellStyle = cell.getCellStyle();
                Font font = sheet.getWorkbook().getFontAt(cellStyle.getFontIndexAsInt());
                String cellValue = dataFormatter.formatCellValue(cell);
                float cellHeight = getCellHeightInPoints(cellValue, font);
                if (cellHeight > maxHeight) {
                    maxHeight = cellHeight;
                }
            }
            row.setHeightInPoints(maxHeight);
        }
    }
    private static float getCellHeightInPoints(String cellValue, Font font) {
        // A rough estimate of row height based on the length of the text and font size
        int lineCount = (cellValue.length() / 30) + 1;
        float fontHeight = font.getFontHeightInPoints();
        return lineCount * fontHeight;
    }
    //translate the column names into a better human readable name
    private static String nameConverter(String columnName){
        switch(columnName){
            case "id":
                return "Identifikationsnummer";
            case "license_plate":
                return "Kennzeichen";
            case "brand":
                return "Marke";
            case "model":
                return "Model";
            case "chassis_number":
                return "Fahrgestellnummer";
            case "mileage":
                return "Kilometerstand";
            case "expected_mileage":
                return "erwarteter \nKilometerstand";
            case "annual_performance":
                return "KM jährlich";
            case "seats":
                return "Sitzanzahl";
            case "purchase_date":
                return "Kauf-datum";
            case "list_price_gross":
                return "Listenpreis \n(Brutto)";
            case "leasing_installment_net":
                return "Leasingrate \n(netto)";
            case "leasing_start":
                return "Leasing \nbeginn";
            case "leasing_end":
                return "Leasing \nende";
            case "group":
                return "Gruppe\\Fahrzeugführer";
            case "is_bookable":
                return "buchbar";
            case "insurance_number":
                return "Versicherungsscheinnummer";
            case "registration_date":
                return "Registrierungsdatum";
            case "insurance_number_expiration_date":
                return "Versicherungsnummer Verfallsdatum";
            case "aral":
                return "Tankkarte Aral";
            case "shell":
                return "Tankkarte Shell";
            case "pin":
                return "PIN Tankkarte";
            default:
                return columnName;
        }

    }
}

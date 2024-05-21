package de.cae.XYFleet.excel;

import org.apache.poi.xssf.usermodel.*;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.codegen.XYFleet.tables.records.VehiclesRecord;
import org.jooq.codegen.XYFleet.tables.records.FuelCardRecord;
import org.jooq.codegen.XYFleet.tables.records.AccessGroupsRecord;
import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.jooq.codegen.XYFleet.tables.records.PricingRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import static org.jooq.codegen.XYFleet.Tables.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ExcelReader{
    private static DSLContext dslContext;
    public static void main(String[] args) {
        try {
            try {
                Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3308", "root", "u2SG7FdmzNE4vZU3kVALCQhPYywfBH5X9raxWJ6T");
                dslContext =  DSL.using(conn, SQLDialect.MARIADB);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            InputStream inp = new FileInputStream("C:\\Users\\Lennard Helbig\\JavaVorkurs\\XY-Fleet\\backend\\Test.xlsx");
            XSSFWorkbook wb = (XSSFWorkbook) XSSFWorkbookFactory.create(inp);;
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell = null;

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();

            int cols = 0; // No of columns
            int tmp = 0;
            for(int i=0;i<10;i++){
                row = sheet.getRow(i);
                if(row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if(tmp > cols) cols = tmp;
                    break;
                }
            }
            for(int r = 2; r <= rows; r++) {
                row = sheet.getRow(r);
                if(row==null){
                    continue;
                }
                FuelCardRecord fuelCardRecord = dslContext.newRecord(FUEL_CARD);
                AccessGroupsRecord accessGroupsRecord = dslContext.newRecord(ACCESS_GROUPS);
                InsurancesRecord insurancesRecord = dslContext.newRecord(INSURANCES);
                PricingRecord pricingRecord = dslContext.newRecord(PRICING);
                VehiclesRecord vehiclesRecord = dslContext.newRecord(VEHICLES);
                for (int c = 0; c <= cols; c++) {
                    cell = row.getCell((short) c);
                    if(cell == null){
                        continue;
                    }
                    String columnName = sheet.getRow(1).getCell((short) c).toString();

                    StringBuilder value = new StringBuilder(cell.toString());
                    BigInteger temp;
                    LocalDate date;
                    String[] split;
                    switch (columnName) {
                        case "Kennzeichen":
                            vehiclesRecord.setLicensePlate(value.toString());
                            break;
                        case "Fahrzeugführer aktuell":
                                accessGroupsRecord = dslContext.fetchOne(ACCESS_GROUPS, ACCESS_GROUPS.GROUP.eq(cell.getRawValue()));
                                if(accessGroupsRecord == null){
                                    accessGroupsRecord = dslContext.newRecord(ACCESS_GROUPS);
                                    accessGroupsRecord.setGroup(value.toString());
                                    accessGroupsRecord.setIsBookable((byte) 0);
                                }
                            break;
                        case "Typ":
                            if(value.isEmpty()) break;
                            split = value.toString().split(" ");
                            if(split.length>0){
                            vehiclesRecord.setBrand(split[0]);
                            value = new StringBuilder();
                            for(int i=1; i<split.length; i++){
                                value.append(split[i]);
                            }
                            vehiclesRecord.setModel(value.toString());
                            }
                            break;
                        case "Tankkarte Aral":

                            try {
                                temp = BigInteger.valueOf((long) cell.getNumericCellValue());
                            } catch(NumberFormatException e) {
                                temp = new BigInteger("0");
                            }
                            fuelCardRecord.setAral(temp);
                            break;
                        case "Tankkarte Shell":
                            try {
                                temp = BigInteger.valueOf((long) cell.getNumericCellValue());
                            } catch(NumberFormatException e) {
                                temp = new BigInteger("0");
                            }
                            fuelCardRecord.setShell(temp);
                            break;
                        case "PIN Tankkarte":
                            fuelCardRecord.setPin((value.length() == 0) ? null : (int) cell.getNumericCellValue());
                            break;
                        case "Kennzeichen Tankkarte":
                            //nothing to do actually. very unnecessary column
                            break;
                        case "Halter Tankkarte":
                            //this column is not directly implemented. We assumed that holder of the vehicle and tankcard is identical
                            break;
                        case "Fahrgestellnummer":
                            vehiclesRecord.setChassisNumber(value.toString());
                            break;
                        case "Service":
                            //no idea
                            break;
                        case "Zulassung":
                            //pricingRecord.setPurchaseDate(value.isEmpty()? null : cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            date = cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            insurancesRecord.setRegistrationDate((value.length() == 0) ? null : date);
                            //maybe not implemented
                            break;
                        case "Leasing \nende":
                            date = cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            pricingRecord.setLeasingEnd((value.length() == 0) ? null : date);
                            break;
                        case "Leasing \nbeginn":
                            date = cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            pricingRecord.setLeasingStart((value.length() == 0) ? null : date);
                            break;
                        case "Versicherungsschein-Nr.":
                            split = value.toString().split("\n");
                            if(split.length == 2){
                                split[0] = split[0].replaceAll("\\.", "");
                                split[1] = split[1].replace("gültig bis ", "").replaceAll("/", "-");
                                try{
                                    LocalDate localDate = LocalDate.parse(split[1], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                                    insurancesRecord.setInsuranceNumberExpirationDate(localDate);
                                    insurancesRecord.setInsuranceNumber(Long.parseLong(split[0]));
                                }catch(DateTimeParseException | NumberFormatException e ){
                                    System.out.println(e);
                                    break;
                                }
                            }
                            break;
                        case "Kauf-datum":
                            pricingRecord.setPurchaseDate((value.length() == 0) ? null: cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        break;
                        case "KM jährlich", "jährliche Leistung":
                            vehiclesRecord.setAnnualPerformance((int) cell.getNumericCellValue());
                            break;
                        case "Leasingrate (netto)", "Leasingrate \n(netto)":
                            pricingRecord.setLeasingInstallmentNet((int) cell.getNumericCellValue());
                            break;
                        case "Listenpreis (Brutto)", "Listenpreis \n(Brutto)":
                            pricingRecord.setListPriceGross((int) cell.getNumericCellValue());
                            break;
                        case "Marke":
                            vehiclesRecord.setBrand(cell.getStringCellValue());
                            break;
                        case "Model":
                            vehiclesRecord.setModel(cell.getStringCellValue());
                            break;
                        case "Kilometerstand":
                            vehiclesRecord.setMileage((int) cell.getNumericCellValue());
                            break;
                        case "erwarteter \nKilometerstand":
                            vehiclesRecord.setExpectedMileage((int) cell.getNumericCellValue());
                            break;
                        case "Sitzanzahl":
                            vehiclesRecord.setSeats((int) cell.getNumericCellValue());
                            break;
                        case "Gruppe\\Fahrzeugführer":
                            accessGroupsRecord.setGroup(cell.getStringCellValue());
                            break;
                        case "buchbar":
                            accessGroupsRecord.setIsBookable((byte) cell.getNumericCellValue());
                            break;
                        case "Registrierungsdatum":
                            insurancesRecord.setRegistrationDate(cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            break;
                        case "Versicherungsnummer Verfallsdatum":
                            insurancesRecord.setInsuranceNumberExpirationDate(cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            break;
                        case "Versicherungsscheinnummer":
                            insurancesRecord.setInsuranceNumber((long) cell.getNumericCellValue());
                            default:
                            break;
                    }
                }
                try{
                    insurancesRecord.merge();
                    pricingRecord.merge();
                    accessGroupsRecord.merge();
                    fuelCardRecord.merge();
                    //set foreign key IDs according to the generated Records
                    vehiclesRecord.setInsuranceId(insurancesRecord.getId());
                    vehiclesRecord.setPricingId(pricingRecord.getId());
                    vehiclesRecord.setAccessGroupId(accessGroupsRecord.getId());
                    vehiclesRecord.setFuelCardId(fuelCardRecord.getId());
                    //set all default values, that are not mentioned in the old excel format
                    //these values have no real meaning and must be changed by the user
                    vehiclesRecord.setMileage(0);
                    vehiclesRecord.setExpectedMileage(0);
                    vehiclesRecord.setAnnualPerformance(0);
                    vehiclesRecord.setType("car");
                    vehiclesRecord.merge();
                } catch(DataAccessException e){
                    //System.out.println(e);
                    System.out.println("This column has false or missing Data, vehicle cannot be converted.");
                    vehiclesRecord.reset();
                    insurancesRecord.reset();
                    pricingRecord.reset();
                    accessGroupsRecord.reset();
                    fuelCardRecord.reset();
                }


            }
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }
}

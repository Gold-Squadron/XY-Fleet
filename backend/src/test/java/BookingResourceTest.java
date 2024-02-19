import org.jooq.UpdatableRecord;
import org.jooq.codegen.XYFleet.tables.AccessGroups;
import org.jooq.codegen.XYFleet.tables.records.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Date;
import java.time.LocalDate;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.*;

public class BookingResourceTest extends EntryResourceTest {
    @BeforeAll
    public static void initAll(){
            //Arrange
            PricingRecord pricing = new PricingRecord(0, LocalDate.parse("2023-01-01"),3000, 5000, LocalDate.parse("2023-01-01"), LocalDate.parse("2023-01-01"));
            pricing.setId(scenario.add(PRICING, pricing));
            //dslContext.insertInto(PRICING).values(pricing).onDuplicateKeyIgnore().execute();

            InsurancesRecord insurances = new InsurancesRecord(0, 10, 10, LocalDate.parse("2024-01-01"));
            insurances.setId(scenario.add(INSURANCES, insurances));
            //dslContext.insertInto(INSURANCES).values(insurances).onDuplicateKeyIgnore().execute();
            FuelCardRecord fuelCard = new FuelCardRecord(0, 10000000000000000L, 10000000000000001L,null);

            VehiclesRecord vehicle = new VehiclesRecord(0, "STO-XY-123", "VW", "Käfer", "123", 2000, 2000,4000, insurances.getId(),"car" ,pricing.getId(), fuelCard.getId(), ACCESS_GROUP_ID);
            vehicle.setId(scenario.add(VEHICLES, vehicle));
            //dslContext.insertInto(VEHICLES).values(vehicle).onDuplicateKeyIgnore().execute();

            BookingsRecord booking = new BookingsRecord(0, ADMIN_ID, vehicle.getId(),  LocalDate.parse("2023-01-01"),  LocalDate.parse("2023-01-01"), "none", 200, null);
            booking.setId(scenario.add(BOOKINGS, booking));
            uri = "/booking/" + booking.getId();
            testRecord = booking;
            testTable = testRecord;
            table = BOOKINGS;
    }
    @BeforeEach
    public void initSingle(){
        scenario.merge(BOOKINGS, testRecord);
    }
    @Override
    @Test
    public void delete_validCall_shouldReturnEntryInDatabase() {
       super.delete_validCall_shouldReturnEntryInDatabase();
    }
    @Override
    @ParameterizedTest
    @CsvSource(value = {
            FORBIDDEN+":"+ROLE_SECURITY,
            UNAUTHORIZED+": "}, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }
    @ParameterizedTest
    @CsvSource(value = {UNAUTHORIZED+": abc"}, delimiter = ':')
    public void get_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.get_invalidCall_shouldThrowResourceException(responseMessage, role);
    }
    @Override
    @Test
    public void get_validCall_shouldReturnEntryInDatabase() {
        super.get_validCall_shouldReturnEntryInDatabase();
    }
}
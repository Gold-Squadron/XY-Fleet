import org.jooq.UpdatableRecord;
import org.jooq.codegen.XYFleet.tables.records.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Date;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.*;

public class BookingResourceTest extends EntryResourceTest {
    @BeforeAll
    public static void initAll(){
            //Arrange
            PricingRecord pricing = new PricingRecord(0, new Date(1, 1, 1).toLocalDate(), 3000, 5000);
            pricing.setId(scenario.add(PRICING, pricing));
            //dslContext.insertInto(PRICING).values(pricing).onDuplicateKeyIgnore().execute();

            InsurancesRecord insurances = new InsurancesRecord(0, 10, 10, 10);
            insurances.setId(scenario.add(INSURANCES, insurances));
            //dslContext.insertInto(INSURANCES).values(insurances).onDuplicateKeyIgnore().execute();

            VehiclesRecord vehicle = new VehiclesRecord(0, "STO-XY-123", "VW", "Käfer", "123", 2000, 4000, insurances.getId(), pricing.getId());
            vehicle.setId(scenario.add(VEHICLES, vehicle));
            //dslContext.insertInto(VEHICLES).values(vehicle).onDuplicateKeyIgnore().execute();

            BookingsRecord booking = new BookingsRecord(0, ADMIN_ID, vehicle.getId(), new Date(1, 1, 1).toLocalDate(), new Date(1, 1, 2).toLocalDate(), "none");
            booking.setId(scenario.add(BOOKINGS, booking));
            uri = "/booking/" + booking.getId();
            testRecord = booking;
    }
    @BeforeEach
    public void initSingle(){
        scenario.merge(BOOKINGS, testRecord);
    }
    @Test
    public void delete_validCall_shouldReturnEntryInDatabase() {
       super.delete_validCall_shouldReturnEntryInDatabase();
    }
    @ParameterizedTest
    @CsvSource(value = {
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:"+ROLE_USER,
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:"+ROLE_SECURITY,
            "Unauthorized (401) - The request requires user authentication: "}, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }
    @ParameterizedTest
    @CsvSource(value = {"Unauthorized (401) - The request requires user authentication: abc"}, delimiter = ':')
    public void get_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.get_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Test
    public void get_validCall_shouldReturnEntryInDatabase() {
        super.get_validCall_shouldReturnEntryInDatabase();
    }
}
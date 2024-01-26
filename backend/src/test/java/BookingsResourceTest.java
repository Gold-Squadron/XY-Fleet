import de.cae.XYFleet.Database;
import org.jooq.codegen.XYFleet.tables.Users;
import org.jooq.codegen.XYFleet.tables.records.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Date;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static org.jooq.codegen.XYFleet.Tables.*;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import static org.jooq.codegen.XYFleet.tables.Users.USERS;

public class BookingsResourceTest extends ResourceTest{

    @BeforeAll
    public static void initAll() {
        //Arrange
        PricingRecord pricing = new PricingRecord(0, new Date(1, 1, 1).toLocalDate(), 3000, 5000);
        pricing.setId(scenario.add(PRICING, pricing));

        InsurancesRecord insurances = new InsurancesRecord(0, 10, 10, 10);
        insurances.setId(scenario.add(INSURANCES, insurances));

        VehiclesRecord vehicle = new VehiclesRecord(0, "STO-XY-123", "VW", "Käfer", "123", 2000, 4000, insurances.getId(), pricing.getId());
        vehicle.setId(scenario.add(VEHICLES, vehicle));

        BookingsRecord booking = new BookingsRecord(0, ADMIN_ID, vehicle.getId(), new Date(1, 1, 1).toLocalDate(), new Date(1, 1, 2).toLocalDate(), "none");
        booking.setId(scenario.add(BOOKINGS, booking));
        uri = "/booking";
        testTable = Database.getDSLContext().select().from(BOOKINGS).fetch();
        table=BOOKINGS;
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {
            "Unauthorized (401) - The request requires user authentication:abc"
    }, delimiter = ':')
    public void get_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.get_invalidCall_shouldThrowResourceException(responseMessage, role);
    }
    @Override
    @Test
    public void get_validCall_shouldReturnEntryInDatabase() {
        super.get_validCall_shouldReturnEntryInDatabase();
    }
    @Override
    public void put_validCall_shouldReturnEntryInDatabase(String params) {
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }
    @Override
    @ParameterizedTest
    @CsvSource(value = {
            "Method Not Allowed (405) - The method specified in the request is not allowed for the resource identified by the request URI:" + ROLE_USER,
            "Unauthorized (401) - The request requires user authentication:abc",
    }, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }
}

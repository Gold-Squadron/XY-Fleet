import de.cae.XYFleet.Database;
import org.jooq.codegen.XYFleet.tables.Users;
import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.jooq.codegen.XYFleet.tables.records.PricingRecord;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.jooq.codegen.XYFleet.tables.records.VehiclesRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Date;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.*;
import static org.jooq.codegen.XYFleet.Tables.VEHICLES;

public class VehiclesResourceTest extends ResourceTest {
    public static int INSURANCE_ID;
    public static int PRICING_ID;
    @BeforeAll
    public static void initAll() {
        //Arrange
        PricingRecord pricing = new PricingRecord(0, new Date(2000, 12, 4).toLocalDate(), 3000, 5000);
        pricing.setId(scenario.add(PRICING, pricing));
        //dslContext.insertInto(PRICING).values(pricing).onDuplicateKeyIgnore().execute();
        PRICING_ID = pricing.getId();
        InsurancesRecord insurances = new InsurancesRecord(0, 123, 456, 2020);
        insurances.setId(scenario.add(INSURANCES, insurances));
        INSURANCE_ID = insurances.getId();
        //dslContext.insertInto(INSURANCES).values(insurances).onDuplicateKeyIgnore().execute();

        VehiclesRecord vehicle = new VehiclesRecord(0, "STO-XY-666", "MERZEDES", "C2", "5", 100, 20000, insurances.getId(), pricing.getId());
        vehicle.setId(scenario.add(VEHICLES, vehicle));

        uri = "/xywing";
        testTable = Database.getDSLContext().select().from(VEHICLES).fetch();
        table = VEHICLES;
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
    @ParameterizedTest
    @CsvSource(value = {
            "Method Not Allowed (405) - The method specified in the request is not allowed for the resource identified by the request URI:" + ROLE_USER,
            "Unauthorized (401) - The request requires user authentication:abc",
    }, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Test
    public void put_validCall_shouldReturnEntryInDatabase() {
        String params = "license_plate=STO-XY-1&brand=Tesla&model=C2&chassis_number=100&mileage=400&annual_performance=2000&pricing_id=" + PRICING_ID + "&insurance_id=" + INSURANCE_ID;
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {"Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_USER + ":license_plate=STO-XY-1&brand=Tesla&model=C2&chassis_number=1000&mileage=100&annual_performance=2000&",
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_SECURITY + ":license_plate=STO-XY-1&brand=Tesla&model=C2&chassis_number=1000&mileage=100&annual_performance=2000&",
            "Unauthorized (401) - The request requires user authentication:abc:license_plate=STO-XY-1&brand=Tesla&model=C2&chassis_number=1000&mileage=100&annual_performance=2000&"}, delimiter= ':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        params = params + "pricing_id=" + PRICING_ID + "&insurance_id=" + INSURANCE_ID;
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }
}

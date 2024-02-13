import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.jooq.codegen.XYFleet.tables.records.PricingRecord;
import org.jooq.codegen.XYFleet.tables.records.VehiclesRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Date;
import java.time.LocalDate;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.*;

public class VehicleResourceTest extends EntryResourceTest {
    public static int INSURANCE_ID;
    public static int PRICING_ID;

    @BeforeAll
    public static void initAll() {
        //Arrange
        PricingRecord pricing = new PricingRecord(0,  LocalDate.parse("2020-04-03"), 3000, 5000);
        pricing.setId(scenario.add(PRICING, pricing));
        //dslContext.insertInto(PRICING).values(pricing).onDuplicateKeyIgnore().execute();
        PRICING_ID = pricing.getId();
        InsurancesRecord insurances = new InsurancesRecord(0, 123, 456, 2020);
        insurances.setId(scenario.add(INSURANCES, insurances));
        INSURANCE_ID = insurances.getId();
        //dslContext.insertInto(INSURANCES).values(insurances).onDuplicateKeyIgnore().execute();

        VehiclesRecord vehicle = new VehiclesRecord(0, "STO-XY-666", "MERZEDES", "C2", "5", 100,1000, 20000, insurances.getId(), "car",pricing.getId());
        vehicle.setId(scenario.add(VEHICLES, vehicle));

        uri = "/xywing/" + vehicle.getId();
        testRecord = vehicle;
        testTable = testRecord;
        table = VEHICLES;
    }

    @BeforeEach
    public void setupSingle() {
        scenario.merge(VEHICLES, testRecord);
    }


    @Override
    @ParameterizedTest
    @CsvSource(value = {"brand=Citroen&chassis_number=1000"}, delimiter = ':')
    public void post_validCall_shouldReturnEntryInDatabase(String params) {
        super.post_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {UNAUTHORIZED+":abc::brand=Citroen&chassis_number=1000",
            FORBIDDEN+":" + ROLE_USER + ":brand=Citroen&chassis_number=1000",
            FORBIDDEN+":" + ROLE_SECURITY + ":brand=Citroen&chassis_number=1000",
            BAD_REQUEST+":" + ROLE_ADMIN+ ":brand=Citroen&insurance_id=0",
            BAD_REQUEST+":" + ROLE_ADMIN + ":brand=Citroen&pricing_id=0",
            BAD_REQUEST+":" + ROLE_ADMIN + ": "}, delimiter = ':')
    public void post_invalidCall_shouldReturnEntryInDatabase(String responseMessage, String role, String params) {
        super.post_invalidCall_shouldReturnEntryInDatabase(responseMessage, role, params);
    }

    @Override
    @Test
    public void delete_validCall_shouldReturnEntryInDatabase() {
        super.delete_validCall_shouldReturnEntryInDatabase();
    }

    @Override
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

    @Override
    @ParameterizedTest
    @CsvSource(value = {UNAUTHORIZED+": abc",
            FORBIDDEN+":" + ROLE_USER,
            FORBIDDEN+":" + ROLE_SECURITY}, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Test
    public void put_validCall_shouldReturnEntryInDatabase() {
        String params = "license_plate=STO-XY-1&brand=Tesla&model=C2&chassis_number=100&mileage=400&annual_performance=2000&expected_mileage=3000&type=car&pricing_id=" + PRICING_ID + "&insurance_id=" + INSURANCE_ID;
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {FORBIDDEN+":" + ROLE_USER + ":license_plate=STO-XY-1&brand=Tesla&model=C2&chassis_number=1000&mileage=100&annual_performance=2000&",
            FORBIDDEN+":" + ROLE_SECURITY + ":license_plate=STO-XY-1&brand=Tesla&model=C2&chassis_number=1000&mileage=100&annual_performance=2000&",
            UNAUTHORIZED+":abc:license_plate=STO-XY-1&brand=Tesla&model=C2&chassis_number=1000&mileage=100&annual_performance=2000&"}, delimiter= ':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        params = params + "pricing_id=" + PRICING_ID + "&insurance_id=" + INSURANCE_ID;
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }

    @Test
    public void put_invalidCallWithEmptyQuery_shouldThrowResourceException400() {
        super.put_invalidCall_shouldThrowResourceException(BAD_REQUEST, ROLE_ADMIN, "");
    }

    @Test
    public void put_invalidCalWithInvalidPricingId_shouldThrowResourceException400() {
        super.put_invalidCall_shouldThrowResourceException(BAD_REQUEST, ROLE_ADMIN, "brand=Tesla&model=C2&chassis_number=1000&mileage=100&annual_performance=2000&pricing_id=" + (-1) + "&insurance_id=" + INSURANCE_ID);
    }

    @Test
    public void put_invalidCalWithInvalidInsuranceId_shouldThrowResourceException400() {
        super.put_invalidCall_shouldThrowResourceException(BAD_REQUEST, ROLE_ADMIN, "brand=Tesla&model=C2&chassis_number=1000&mileage=100&annual_performance=2000&pricing_id=" + INSURANCE_ID + "&insurance_id=" + (-1));
    }
}

import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.jooq.codegen.XYFleet.tables.records.PricingRecord;
import org.jooq.codegen.XYFleet.tables.records.VehiclesRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Date;

import static org.jooq.codegen.XYFleet.Tables.*;

public class VehicleResourceTest extends EntryResourceTest{
    public static String INSURANCEID;
    @BeforeAll
    public static void initAll(){
        //Arrange
        PricingRecord pricing = new PricingRecord(0, new Date(2000, 12, 4).toLocalDate(), 3000, 5000);
        pricing.setId(scenario.add(PRICING, pricing));
        //dslContext.insertInto(PRICING).values(pricing).onDuplicateKeyIgnore().execute();

        InsurancesRecord insurances = new InsurancesRecord(0, 123, 456, 2020);
        insurances.setId(scenario.add(INSURANCES, insurances));
        INSURANCEID = insurances.getId().toString();
        //dslContext.insertInto(INSURANCES).values(insurances).onDuplicateKeyIgnore().execute();

        VehiclesRecord vehicle = new VehiclesRecord(0, "STO-XY-666", "MERZEDES", "C2", "5", 100, 20000, insurances.getId(), pricing.getId());
        vehicle.setId(scenario.add(VEHICLES, vehicle));

        uri = "/xywing/" + vehicle.getId();
        testRecord = vehicle;
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
    @CsvSource(value = {}, delimiter = ':')
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
    @CsvSource(value = {}, delimiter = ':')
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
    @CsvSource(value = {}, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {"license_plate=STO-XY-1&brand=Tesla&model=C2&chassis_number=1000&mileage=100&annual_performance=2000&pricing_id&insurance_id="}, delimiter = ':')
    public void put_validCall_shouldReturnEntryInDatabase(String params) {
        params = params+ INSURANCEID;
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {}, delimiter = ':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }
}

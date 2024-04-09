import de.cae.XYFleet.Database;
import org.jooq.codegen.XYFleet.tables.Users;
import org.jooq.codegen.XYFleet.tables.records.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Date;
import java.time.LocalDate;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.*;
import static org.jooq.codegen.XYFleet.Tables.VEHICLES;

public class VehiclesResourceTest extends ResourceTest {
    public static int INSURANCE_ID;
    public static int PRICING_ID;
    public static int FUEL_CARD_ID;
    @BeforeAll
    public static void initAll() {
        //Arrange
        PricingRecord pricing = new PricingRecord(0, LocalDate.parse("2023-01-01"),3000, 5000, LocalDate.parse("2023-01-01"), LocalDate.parse("2023-01-01"));
        pricing.setId(scenario.add(PRICING, pricing));
        //dslContext.insertInto(PRICING).values(pricing).onDuplicateKeyIgnore().execute();

        InsurancesRecord insurances = new InsurancesRecord(0, 10, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-01"));
        insurances.setId(scenario.add(INSURANCES, insurances));
        //dslContext.insertInto(INSURANCES).values(insurances).onDuplicateKeyIgnore().execute();

        FuelCardRecord fuelCard = new FuelCardRecord(0, 10000000000000000L, 10000000000000001L,null);
        fuelCard.setId(scenario.add(FUEL_CARD, fuelCard));
        //dslContext.insertInto(FUEL_CARD).values(fuelCard).onDuplicateKeyIgnore().execute();

        VehiclesRecord vehicle = new VehiclesRecord(0, "STO-XY-123", "VW", "Käfer", "123", 2000, 2000,4000, insurances.getId(),"car" ,pricing.getId(), fuelCard.getId(), ACCESS_GROUP_ID);
        vehicle.setId(scenario.add(VEHICLES, vehicle));
        //dslContext.insertInto(VEHICLES).values(vehicle).onDuplicateKeyIgnore().execute();

        uri = "/xywing";
        testTable = Database.getDSLContext().select().from(VEHICLES).fetch();
        table = VEHICLES;
        FUEL_CARD_ID=fuelCard.getId();
        PRICING_ID= pricing.getId();
        INSURANCE_ID= insurances.getId();
    }
    @Override
    @ParameterizedTest
    @CsvSource(value = {
            UNAUTHORIZED+":abc"
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
            METHOD_NOT_ALLOWED+":" + ROLE_USER,
            UNAUTHORIZED+":abc",
    }, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Test
    public void put_validCall_shouldReturnEntryInDatabase() {
        String params = "license_plate=STO-XY-1&brand=Tesla&model=C2&chassis_number=100&mileage=400&annual_performance=2000&expected_mileage=3000&type=car&pricing_id=" + PRICING_ID + "&insurance_id=" + INSURANCE_ID+ "&fuel_card_id=" + FUEL_CARD_ID + "&access_group_id="+ACCESS_GROUP_ID;
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
}

import org.jooq.codegen.XYFleet.tables.records.FuelCardRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.FUEL_CARD;
import static org.jooq.codegen.XYFleet.Tables.INSURANCES;

public class FuelCardResourceTest extends EntryResourceTest{
    @BeforeAll
    public static void initAll() {
        FuelCardRecord fuelCard = new FuelCardRecord(0, 10000000000000000L, 10000000000000001L,1000);
        fuelCard.setId(scenario.add(FUEL_CARD, fuelCard));
        FuelCardRecord fuelCardsRecord = new FuelCardRecord(0,10000000000000001L, 10000000000000002L,0000);
        scenario.add(FUEL_CARD, fuelCard);

        fuelCardsRecord.setId(scenario.add(FUEL_CARD, fuelCardsRecord));
        uri = "/fuelCard/" + fuelCardsRecord.getId();
        testRecord = fuelCardsRecord;
        testTable = testRecord;
        table = FUEL_CARD;
    }
    @BeforeEach
    public void setupSingle() {
        scenario.merge(FUEL_CARD, testRecord);
    }
    @Override
    @ParameterizedTest
    @CsvSource(value = {"aral=100000001&shell=123456789&pin=9999"}, delimiter = ':')
    public void post_validCall_shouldReturnEntryInDatabase(String params) {
        super.post_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {FORBIDDEN+":" + ROLE_USER + ":aral=10000000000000000&shell=10000000000000000&pin=9999",
            FORBIDDEN+":" + ROLE_SECURITY + ":aral=10000000000000000&shell=10000000000000000&pin=9999",
            UNAUTHORIZED+":abc:aral=10000000000000000&shell=10000000000000000&pin=9999",
            BAD_REQUEST+":" + ROLE_ADMIN + ":aral=10000000000000000&shell=10000000000000000&pin=9999",
            BAD_REQUEST+":"+ ROLE_ADMIN +": "}, delimiter = ':')
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
    @CsvSource(value = {
            UNAUTHORIZED+": abc"
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
            UNAUTHORIZED+": abc",
            FORBIDDEN+":" + ROLE_USER,
            FORBIDDEN+":" + ROLE_SECURITY
    }, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {"aral=100000001&shell=123456789&pin=9999"}, delimiter = ':')
    public void put_validCall_shouldReturnEntryInDatabase(String params) {
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {FORBIDDEN+":" + ROLE_USER + ":aral=10000000000000000&shell=123456789&pin=9999",
            FORBIDDEN+":" + ROLE_SECURITY + ":aral=10000000000000000&shell=123456789&pin=9999",
            UNAUTHORIZED+":abc:aral=10000000000000000&shell=123456789&pin=9999",
            BAD_REQUEST+":" + ROLE_ADMIN + ":aral=10000000000000000&shell=123456789&pin=9999",
            BAD_REQUEST+":"+ ROLE_ADMIN +":aral=10000000000&pin=9999"}, delimiter = ':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }
}

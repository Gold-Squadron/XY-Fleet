import de.cae.XYFleet.authentication.XYAuthorizer;
import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.INSURANCES;
import static org.jooq.codegen.XYFleet.Tables.VEHICLES;

public class InsuranceResourceTest extends EntryResourceTest {
    @BeforeAll
    public static void initAll() {
        InsurancesRecord insurances = new InsurancesRecord(0, 10, 10, 10);
        InsurancesRecord insurances2 = new InsurancesRecord(0, 5, 10020202, 1028362);
        scenario.add(INSURANCES, insurances2);

        insurances.setId(scenario.add(INSURANCES, insurances));
        uri = "/insurance/" + insurances.getId();
        testRecord = insurances;
        testTable = testRecord;
        table = INSURANCES;
    }
    @BeforeEach
    public void setupSingle() {
        scenario.merge(INSURANCES, testRecord);
    }
    @Override
    @ParameterizedTest
    @CsvSource(value = {"insurance_number=1234&registration_date=01012024"}, delimiter = ':')
    public void post_validCall_shouldReturnEntryInDatabase(String params) {
        super.post_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {"Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_USER + ":insurance_number=123&registration_date=01012024&insurance_number_expiration=5",
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_SECURITY + ":insurance_number=123&registration_date=01012024&insurance_number_expiration=5",
            "Unauthorized (401) - The request requires user authentication:abc:insurance_number=123&registration_date=01012024",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:" + ROLE_ADMIN + ":insurance_number=5&registration_date=01012024&insurance_number_expiration=5",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:"+ ROLE_ADMIN +":"}, delimiter = ':')
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
            "Unauthorized (401) - The request requires user authentication: abc"
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
            "Unauthorized (401) - The request requires user authentication: abc",
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_USER,
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_SECURITY
    }, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {"insurance_number=123&registration_date=01012024&insurance_number_expiration=24122025"}, delimiter = ':')
    public void put_validCall_shouldReturnEntryInDatabase(String params) {
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {"Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_USER + ":insurance_number=123&registration_date=01012024&insurance_number_expiration=5",
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_SECURITY + ":insurance_number=123&registration_date=01012024&insurance_number_expiration=5",
            "Unauthorized (401) - The request requires user authentication:abc:insurance_number=123&registration_date=01012024&insurance_number_expiration=24122025",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:" + ROLE_ADMIN + ":insurance_number=5&registration_date=01012024&insurance_number_expiration=5",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:"+ ROLE_ADMIN +":registration_date=01012024&insurance_number_expiration=5"}, delimiter = ':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }
}
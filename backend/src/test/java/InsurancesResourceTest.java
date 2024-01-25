import de.cae.XYFleet.Database;
import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import static org.jooq.codegen.XYFleet.Tables.INSURANCES;

public class InsurancesResourceTest extends ResourceTest {
    @BeforeAll
    public static void initAll() {
        InsurancesRecord insurances = new InsurancesRecord(0, 10, 10, 10);
        InsurancesRecord insurances2 = new InsurancesRecord(0, 5, 10020202, 1028362);
        scenario.add(INSURANCES, insurances2);

        insurances.setId(scenario.add(INSURANCES, insurances));
        uri = "/insurance";
        testTable = Database.getDSLContext().select().from(INSURANCES).fetch();
        table = INSURANCES;
    }
    @Override
    @ParameterizedTest
    @CsvSource(value = {"Unauthorized (401) - The request requires user authentication: abc"}, delimiter = ':')
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
            "Unauthorized (401) - The request requires user authentication:abc"}, delimiter = ':')
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

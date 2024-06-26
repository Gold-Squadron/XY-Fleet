import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.INSURANCES;
public class InsurancesResourceTest extends ResourceTest {
    @BeforeAll
    public static void initAll() {
        InsurancesRecord insurances = new InsurancesRecord(0, 10, LocalDate.parse("2024-01-01"), LocalDate.parse("1001-12-12"));
        InsurancesRecord insurances2 = new InsurancesRecord(0, 5, LocalDate.parse("2024-01-01"), LocalDate.parse("1002-12-12"));
        scenario.add(INSURANCES, insurances2);

        insurances.setId(scenario.add(INSURANCES, insurances));
        uri = "/insurance";
        testTable = Database.getDSLContext().select().from(INSURANCES).fetch();
        table = INSURANCES;
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
    @CsvSource(value = {
            METHOD_NOT_ALLOWED+":" + ROLE_USER,
            UNAUTHORIZED+":abc"}, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {"insurance_number=123&registration_date=2024-01-01&insurance_number_expiration_date=2412-12-01"}, delimiter = ':')
    public void put_validCall_shouldReturnEntryInDatabase(String params) {
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {FORBIDDEN+":" + ROLE_USER + ":insurance_number=123&registration_date=01012024&insurance_number_expiration_date=5",
            FORBIDDEN+":" + ROLE_SECURITY + ":insurance_number=123&registration_date=01012024&insurance_number_expiration_date=5",
            UNAUTHORIZED+":abc:insurance_number=123&registration_date=01012024&insurance_number_expiration_date=24122025",
            BAD_REQUEST+":" + ROLE_ADMIN + ":insurance_number=5&registration_date=01012024&insurance_number_expiration_date=5",
            BAD_REQUEST+":"+ ROLE_ADMIN +":registration_date=01012024&insurance_number_expiration_date=5"}, delimiter = ':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }
}

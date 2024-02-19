import de.cae.XYFleet.authentication.XYAuthorizer;
import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.INSURANCES;
import static org.jooq.codegen.XYFleet.Tables.VEHICLES;

public class InsuranceResourceTest extends EntryResourceTest {
    @BeforeAll
    public static void initAll() {
        InsurancesRecord insurances = new InsurancesRecord(0, 10, 10, LocalDate.parse("1001-12-12"));
        InsurancesRecord insurances2 = new InsurancesRecord(0, 5, 10020202, LocalDate.parse("1002-12-12"));
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
    @CsvSource(value = {FORBIDDEN+":" + ROLE_USER + ":insurance_number=123&registration_date=01012024&insurance_number_expiration=5",
            FORBIDDEN+":" + ROLE_SECURITY + ":insurance_number=123&registration_date=01012024&insurance_number_expiration=5",
            UNAUTHORIZED+":abc:insurance_number=123&registration_date=01012024",
            BAD_REQUEST+":" + ROLE_ADMIN + ":insurance_number=5&registration_date=01012024&insurance_number_expiration=5",
            BAD_REQUEST+":"+ ROLE_ADMIN +":"}, delimiter = ':')
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
    @CsvSource(value = {"insurance_number=123&registration_date=01012024&insurance_number_expiration=24122025"}, delimiter = ':')
    public void put_validCall_shouldReturnEntryInDatabase(String params) {
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {FORBIDDEN+":" + ROLE_USER + ":insurance_number=123&registration_date=01012024&insurance_number_expiration=5",
            FORBIDDEN+":" + ROLE_SECURITY + ":insurance_number=123&registration_date=01012024&insurance_number_expiration=5",
            UNAUTHORIZED+":abc:insurance_number=123&registration_date=01012024&insurance_number_expiration=24122025",
            BAD_REQUEST+":" + ROLE_ADMIN + ":insurance_number=5&registration_date=01012024&insurance_number_expiration=5",
            BAD_REQUEST+":"+ ROLE_ADMIN +":registration_date=01012024&insurance_number_expiration=5"}, delimiter = ':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }
}

import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.jooq.codegen.XYFleet.tables.records.PricingRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Date;
import java.time.LocalDate;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.INSURANCES;
import static org.jooq.codegen.XYFleet.Tables.PRICING;

public class PricingResourceTest extends EntryResourceTest{
    @BeforeAll
    public static void initAll() {
        PricingRecord pricing = new PricingRecord(0, LocalDate.parse("2023-01-01"), 3000, 5000, LocalDate.parse("2022-01-01"), LocalDate.parse("2025-01-01"));
        PricingRecord pricing2 = new PricingRecord(0, LocalDate.parse("2025-11-05"), 3040, 1999,LocalDate.parse("2022-01-01"), LocalDate.parse("2025-01-01"));
        pricing.setId(scenario.add(PRICING, pricing));

        scenario.add(PRICING, pricing2);

        pricing.setId(scenario.add(PRICING, pricing));
        uri = "/pricing/" + pricing.getId();
        testRecord = pricing;
        testTable = testRecord;
        table = PRICING;
    }
    @BeforeEach
    public void setupSingle() {
        scenario.merge(PRICING, testRecord);
    }
    @Override
    @ParameterizedTest
    @CsvSource(value = {"purchase_date=2013-08-29"}, delimiter = ':')
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
    @CsvSource(value = { UNAUTHORIZED+": abc",
            FORBIDDEN+":" + ROLE_USER,
            FORBIDDEN+":" + ROLE_SECURITY}, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {"purchase_date=2013-08-29&list_price_gross=10000&leasing_installment_net=2345&leasing_start=2024-01-01&leasing_end=2025-01-01"}, delimiter = ':')
    public void put_validCall_shouldReturnEntryInDatabase(String params) {
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {FORBIDDEN+":" + ROLE_USER + ":purchase_date=2013-08-29&list_price_gross=10000&leasing_installment_net=2345&leasing_start=2024-01-01&leasing_end=2025-01-01",
            FORBIDDEN+":" + ROLE_SECURITY + ":purchase_date=2013-08-29&list_price_gross=10000&leasing_installment_net=2345&leasing_start=2024-01-01&leasing_end=2025-01-01",
            UNAUTHORIZED+":abc:purchase_date=2013-08-29&list_price_gross=10000&leasing_installment_net=2345&leasing_start=2024-01-01&leasing_end=2025-01-01",
            BAD_REQUEST+":" + ROLE_ADMIN + ":list_price_gross=10000&leasing_installment_net=2345&leasing_start=2024-01-01&leasing_end=2025-01-01",
            BAD_REQUEST+":"+ ROLE_ADMIN +":"}, delimiter = ':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }
}

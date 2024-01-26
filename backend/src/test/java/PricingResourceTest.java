import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.jooq.codegen.XYFleet.tables.records.PricingRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Date;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.INSURANCES;
import static org.jooq.codegen.XYFleet.Tables.PRICING;

public class PricingResourceTest extends EntryResourceTest{
    @BeforeAll
    public static void initAll() {
        PricingRecord pricing = new PricingRecord(0, new Date(1, 1, 1).toLocalDate(), 3000, 5000);
        PricingRecord pricing2 = new PricingRecord(0, new Date(2025, 3, 18).toLocalDate(), 3040, 1999);

        pricing.setId(scenario.add(PRICING, pricing));

        scenario.add(PRICING, pricing2);

        pricing.setId(scenario.add(PRICING, pricing));
        uri = "/pricing/" + pricing.getId();
        testRecord = pricing;
        testTable = testRecord;
        table = PRICING;
    }
    @Override
    @ParameterizedTest
    @CsvSource(value = {"purchase_date=2013-08-29"}, delimiter = ':')
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
    @CsvSource(value = { "Unauthorized (401) - The request requires user authentication: abc",
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_USER,
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_SECURITY}, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {"purchase_date=2013-08-29&list_price_gross=10000&leasing_installment_net=2345"}, delimiter = ':')
    public void put_validCall_shouldReturnEntryInDatabase(String params) {
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {"Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_USER + ":purchase_date=2013-08-29&list_price_gross=10000&leasing_installment_net=2345",
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_SECURITY + ":purchase_date=2013-08-29&list_price_gross=10000&leasing_installment_net=2345",
            "Unauthorized (401) - The request requires user authentication:abc:purchase_date=2013-08-29&list_price_gross=10000&leasing_installment_net=2345",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:" + ROLE_ADMIN + ":list_price_gross=10000&leasing_installment_net=2345",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:"+ ROLE_ADMIN +":"}, delimiter = ':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }
}

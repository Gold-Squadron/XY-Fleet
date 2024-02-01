import de.cae.XYFleet.Database;
import org.jooq.codegen.XYFleet.tables.records.PricingRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Date;
import java.time.LocalDate;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.INSURANCES;
import static org.jooq.codegen.XYFleet.Tables.PRICING;

public class PricingsResourceTest extends ResourceTest{
    @BeforeAll
    public static void initAll() {
        PricingRecord pricing = new PricingRecord(0,  LocalDate.parse("2023-01-01"), 3000, 5000);
        PricingRecord pricing2 = new PricingRecord(0,  LocalDate.parse("2020-06-05"), 3040, 1999);

        pricing.setId(scenario.add(PRICING, pricing));

        scenario.add(PRICING, pricing2);

        pricing.setId(scenario.add(PRICING, pricing));
        uri = "/pricing";
        testTable = Database.getDSLContext().select().from(PRICING).fetch();;
        table = PRICING;
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
    @CsvSource(value={ METHOD_NOT_ALLOWED+":" + ROLE_USER,
            UNAUTHORIZED+":abc"}, delimiter = ':')
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
    @CsvSource(value = {FORBIDDEN+":" + ROLE_USER + ":purchase_date=2013-08-29&list_price_gross=10000&leasing_installment_net=2345",
            FORBIDDEN+":" + ROLE_SECURITY + ":purchase_date=2013-08-29&list_price_gross=10000&leasing_installment_net=2345",
            UNAUTHORIZED+":abc:purchase_date=2013-08-29&list_price_gross=10000&leasing_installment_net=2345",
            BAD_REQUEST+":" + ROLE_ADMIN + ":list_price_gross=10000&leasing_installment_net=2345",
            BAD_REQUEST+":"+ ROLE_ADMIN +":"}, delimiter = ':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }
}

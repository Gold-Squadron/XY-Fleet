import de.cae.XYFleet.Database;
import org.jooq.codegen.XYFleet.tables.records.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_USER;
import static org.jooq.codegen.XYFleet.Tables.*;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;

public class AccessGroupsResourceTest extends ResourceTest{

    @BeforeAll
    public static void initAll() {
        AccessGroupsRecord accessGroup = new AccessGroupsRecord(0, ADMIN_ID, "other", Byte.parseByte("1"));
        AccessGroupsRecord accessGroup2 = new AccessGroupsRecord(0, ADMIN_ID, "something", Byte.parseByte("1"));

        scenario.add(ACCESS_GROUPS, accessGroup2);

        accessGroup.setId(scenario.add(ACCESS_GROUPS, accessGroup));
        uri = "/accessGroup";
        testTable = Database.getDSLContext().select().from(ACCESS_GROUPS).fetch();
        table = ACCESS_GROUPS;
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
    public void put_validCall_shouldReturnEntryInDatabase(String params) {
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
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
}
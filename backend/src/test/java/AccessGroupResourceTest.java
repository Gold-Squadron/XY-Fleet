import org.jooq.codegen.XYFleet.tables.records.AccessGroupsRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.ACCESS_GROUPS;

public class AccessGroupResourceTest extends EntryResourceTest{
    @BeforeAll
    public static void initAll() {
        AccessGroupsRecord accessGroup = new AccessGroupsRecord(0, ADMIN_ID, "other", Byte.parseByte("1"));
        AccessGroupsRecord accessGroup2 = new AccessGroupsRecord(0, ADMIN_ID, "something", Byte.parseByte("1"));

        scenario.add(ACCESS_GROUPS, accessGroup2);

        accessGroup.setId(scenario.add(ACCESS_GROUPS, accessGroup));
        uri = "/accessGroup/" + accessGroup.getId();
        testRecord = accessGroup;
        testTable = testRecord;
        table = ACCESS_GROUPS;
    }
    @BeforeEach
    public void setupSingle() {
        scenario.merge(ACCESS_GROUPS, testRecord);
    }
    @Override
    @ParameterizedTest
    @CsvSource(value = {"user_id=&group=abc&is_bookable=0"}, delimiter = ':')
    public void post_validCall_shouldReturnEntryInDatabase(String params) {
        super.post_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {FORBIDDEN+":" + ROLE_USER + ":group=abc",
            FORBIDDEN+":" + ROLE_SECURITY + ":user_id=&group=abc&is_bookable=0",
            UNAUTHORIZED+": abc:user_id=&group=abc&is_bookable=0",
            BAD_REQUEST+":" + ROLE_ADMIN + ":user_id=0",
            BAD_REQUEST+":"+ ROLE_ADMIN +":group=something&is_bookable=0"}, delimiter = ':')
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
            UNAUTHORIZED+": abc",
            FORBIDDEN+":" + ROLE_USER,
            FORBIDDEN+":" + ROLE_SECURITY
    }, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {"user_id=&group=abc&is_bookable=0"}, delimiter = ':')
    public void put_validCall_shouldReturnEntryInDatabase(String params) {
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {FORBIDDEN+":" + ROLE_USER + ":user_id=&group=abc&is_bookable=0",
            FORBIDDEN+":" + ROLE_SECURITY + ":user_id=&group=abc&is_bookable=0",
            UNAUTHORIZED+":abc:user_id=&group=abc&is_bookable=0",
            BAD_REQUEST+":" + ROLE_ADMIN + ":group=&is_bookable=0",
            BAD_REQUEST+":"+ ROLE_ADMIN +":user_id=-1&group=abc&is_bookable=0"}, delimiter = ':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }
}

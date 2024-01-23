import de.cae.XYFleet.Database;
import org.jooq.codegen.XYFleet.tables.Users;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.USERS;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UsersResourceTest extends ResourceTest {

    @BeforeAll
    public static void initAll() {
        UsersRecord user = new UsersRecord(0, "mMustermann", "123", "user", Byte.parseByte("1"));
        uri = "/user";
        testTable = Database.getDSLContext().select(Users.USERS.ID, Users.USERS.NAME, Users.USERS.ROLE, Users.USERS.IS_DRIVER).from(Users.USERS).fetch();
        table = USERS;
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_USER,
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_SECURITY,
            "Unauthorized (401) - The request requires user authentication:abc",
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
            "Method Not Allowed (405) - The method specified in the request is not allowed for the resource identified by the request URI:" + ROLE_USER,
            "Unauthorized (401) - The request requires user authentication:abc",
    }, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "name=mMusterKerl&password=123&role=security&is_driver=1",
            "name=lhelbig3&password=HelloWorld&role=user&is_driver=0"}, delimiter = ':')
    public void put_validCall_shouldReturnEntryInDatabase(String params) {
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:"+ROLE_SECURITY+":name=mMusterKerl&password=123&role=security&is_driver=1",
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:"+ROLE_USER+":name=lhelbig&password=HelloWorld&role=user&is_driver=0",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:"+ROLE_ADMIN+":&password=123&role=security&is_driver=1",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:"+ROLE_ADMIN+":name=password=123&role=security&is_driver=1",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:"+ROLE_ADMIN+":name="+ROLE_ADMIN+"&password=123&role=security&is_driver=1",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:"+ROLE_ADMIN+":"
    }, delimiter=':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }
}

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
        UsersRecord user = new UsersRecord(0, "mMustermann", "123","Max Mustermann","Max.Mustermann@cae.com", "user", Byte.parseByte("1"));
        uri = "/user";
        testTable = Database.getDSLContext().select(Users.USERS.ID, Users.USERS.NAME, Users.USERS.ROLE, Users.USERS.IS_DRIVER).from(Users.USERS).fetch();
        table = USERS;
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {
            FORBIDDEN+":" + ROLE_USER,
            FORBIDDEN+":" + ROLE_SECURITY,
            UNAUTHORIZED+":abc",
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
            METHOD_NOT_ALLOWED+":" + ROLE_USER,
            UNAUTHORIZED+":abc",
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
            FORBIDDEN+":"+ROLE_SECURITY+":name=mMusterKerl&password=123&role=security&is_driver=1",
            FORBIDDEN+":"+ROLE_USER+":name=lhelbig&password=HelloWorld&role=user&is_driver=0",
            BAD_REQUEST+":"+ROLE_ADMIN+":&password=123&role=security&is_driver=1",
            BAD_REQUEST+":"+ROLE_ADMIN+":name=password=123&role=security&is_driver=1",
            BAD_REQUEST+":"+ROLE_ADMIN+":name="+ROLE_ADMIN+"&password=123&role=security&is_driver=1",
            BAD_REQUEST+":"+ROLE_ADMIN+":"
    }, delimiter=':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }
}

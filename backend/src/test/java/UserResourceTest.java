import org.jooq.UpdatableRecord;
import org.jooq.codegen.XYFleet.tables.records.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserResourceTest extends EntryResourceTest {
    @BeforeAll
    public static void initAll() {
        //Arrange
        UsersRecord user = new UsersRecord(0, "mMustermann", "123","Max Mustermann","Max.Mustermann@cae.com", "user", Byte.parseByte("1"));
        user.setId(scenario.add(USERS, user));
        uri = "/user/" + user.getId();
        testRecord = user;
        testTable = testRecord;
        table = USERS;
    }

    @BeforeEach
    public void setupSingle() {
        scenario.merge(USERS, testRecord);
    }

    @ParameterizedTest
    @CsvSource(value = {"name=mMusterKerl&password=123&role=security&is_driver=1&full_name=Max Mustermann&email=Max.Mustermann@cae.com"}, delimiter = ':')
    public void put_validCall_shouldReturnEntryInDatabase(String params) {
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {
            FORBIDDEN+":" + ROLE_USER + ":name=mMusterFrau&password=123&role=security&is_driver=1",
            FORBIDDEN+":" + ROLE_SECURITY + ":name=mMusterFrau&password=123&role=security&is_driver=1",
            UNAUTHORIZED+":abc:name=mMusterFrau&password=123&role=security&is_driver=1",
            BAD_REQUEST+":" + ROLE_ADMIN + ":",
            BAD_REQUEST+":" + ROLE_ADMIN + ":name=" + ROLE_ADMIN + "&password=123&role=security&is_driver=1"}, delimiter = ':')
    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        super.put_invalidCall_shouldThrowResourceException(responseMessage, role, params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {"name=mMusterFrau"}, delimiter = ':')
    public void post_validCall_shouldReturnEntryInDatabase(String params) {
        super.post_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {
            FORBIDDEN+":" + ROLE_USER + ":name=mMusterFrau",
            FORBIDDEN+":" + ROLE_SECURITY + ":name=mMusterFrau",
            UNAUTHORIZED+":abc:name=mMusterFrau",
            BAD_REQUEST+":" + ROLE_ADMIN + ":",
            BAD_REQUEST+":" + ROLE_ADMIN + ":name=" + ROLE_ADMIN}, delimiter = ':')
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
    @CsvSource(value = {FORBIDDEN+":" + ROLE_USER, FORBIDDEN+":" + ROLE_SECURITY, UNAUTHORIZED+": "}, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {UNAUTHORIZED+": abc",
            FORBIDDEN+":" + ROLE_USER,
            FORBIDDEN+":" + ROLE_SECURITY}, delimiter = ':')
    public void get_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.get_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Override
    @Test
    public void get_validCall_shouldReturnEntryInDatabase() {
        super.get_validCall_shouldReturnEntryInDatabase();
    }
}

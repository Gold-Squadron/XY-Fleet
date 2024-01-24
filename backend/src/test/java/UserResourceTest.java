import org.jooq.UpdatableRecord;
import org.jooq.codegen.XYFleet.tables.records.*;
import org.junit.BeforeClass;
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
        System.out.println("before all!");
        UsersRecord user = new UsersRecord(0, "mMustermann", "123", "user", Byte.parseByte("1"));
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
    @CsvSource(value = {"name=mMusterKerl&password=123&role=security&is_driver=1"}, delimiter = ':')
    public void put_validCall_shouldReturnEntryInDatabase(String params) {
        super.put_validCall_shouldReturnEntryInDatabase(params);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_USER + ":name=mMusterFrau&password=123&role=security&is_driver=1",
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_SECURITY + ":name=mMusterFrau&password=123&role=security&is_driver=1",
            "Unauthorized (401) - The request requires user authentication:abc:name=mMusterFrau&password=123&role=security&is_driver=1",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:" + ROLE_ADMIN + ":",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:" + ROLE_ADMIN + ":name=" + ROLE_ADMIN + "&password=123&role=security&is_driver=1"}, delimiter = ':')
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
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_USER + ":name=mMusterFrau",
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_SECURITY + ":name=mMusterFrau",
            "Unauthorized (401) - The request requires user authentication:abc:name=mMusterFrau",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:" + ROLE_ADMIN + ":",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:" + ROLE_ADMIN + ":name=" + ROLE_ADMIN}, delimiter = ':')
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
    @CsvSource(value = {"Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_USER, "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_SECURITY, "Unauthorized (401) - The request requires user authentication: "}, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Override
    @ParameterizedTest
    @CsvSource(value = {"Unauthorized (401) - The request requires user authentication: abc",
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_USER,
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:" + ROLE_SECURITY}, delimiter = ':')
    public void get_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.get_invalidCall_shouldThrowResourceException(responseMessage, role);
    }

    @Override
    @Test
    public void get_validCall_shouldReturnEntryInDatabase() {
        super.get_validCall_shouldReturnEntryInDatabase();
    }
}

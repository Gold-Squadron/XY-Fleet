import de.cae.XYFleet.Database;
import org.jooq.codegen.XYFleet.tables.Users;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;

import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.USERS;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UsersResourceTest extends ResourceTest {

    @BeforeAll
    public static void initAll(){
        UsersRecord user = new UsersRecord(0, "mMustermann", "123", "user", Byte.parseByte("1"));
        ResourceTest.uri = "/user";
        ResourceTest.testRecord =  Database.getDSLContext().select(Users.USERS.ID, Users.USERS.NAME, Users.USERS.ROLE, Users.USERS.IS_DRIVER).from(Users.USERS).fetch();
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
            "Method Not Allowed (405) - The method specified in the request is not allowed for the resource identified by the request URI" + ROLE_USER,
            "Unauthorized (401) - The request requires user authentication:abc",
    }, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }
    @ParameterizedTest
    @CsvSource(value = {}, delimiter = ':')
    public void put_validCall_shouldReturnEntryInDatabase(){
        //Arrange
        //String query = "?name=mMusterFrau&passwort=123&is_driver=0&role=security";
        ClientResource clientResource = new ClientResource(ResourceTest.url + ResourceTest.uri);
        ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, ROLE_ADMIN, ROLE_ADMIN);
        //Query
        clientResource.setQueryValue("name", "mMusterFrau");
        clientResource.setQueryValue("is_driver", "1");
        clientResource.setQueryValue("role", "security");
        clientResource.setQueryValue("password", "123");

        clientResource.setChallengeResponse(challengeResponse);
        clientResource.setRetryAttempts(10);
        try {
            //Assert
            // Send a post request
            assertDoesNotThrow(() -> {
                clientResource.put(null);
            });

            //TODO this is a very unpleasent way to delete the created entry
            Objects.requireNonNull(Objects.requireNonNull(Database.getDSLContext()).fetchOne(USERS, USERS.NAME.eq("mMusterFrau"))).delete();
            //assert clientResource.get(String.class) !=null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Release the resources when done
            clientResource.release();
        }
    }
}

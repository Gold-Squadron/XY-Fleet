import de.cae.XYFleet.Database;
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

import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.ressource.XYServerResource.jSONFormat;
import static org.jooq.codegen.XYFleet.Tables.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserRessourceTest extends RessourceTest {
    @BeforeAll
    public static void initAll(){
        //Arrange
        UsersRecord user = new UsersRecord(0, "mMustermann", "123", "user", Byte.parseByte("1"));
        user.setId(scenario.add(USERS, user));
        uri = "/user/" + user.getId();
        testRecord = user;
    }
    @BeforeEach
    public void setupSingle(){
        scenario.merge(USERS, testRecord);
    }
    @Test
    public void put_validCall_shouldReturnEntryInDatabase(){
        //Arrange
        //String query = "?name=mMusterFrau&passwort=123&is_driver=0&role=security";
        ClientResource clientResource = new ClientResource(url+uri);
        ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, ROLE_ADMIN, ROLE_ADMIN);
        //Query
        clientResource.setQueryValue("name", "mMusterFrau");
        clientResource.setQueryValue("is_driver", "1");
        clientResource.setQueryValue("role", "security");
        clientResource.setQueryValue("passwort", "123");

        clientResource.setChallengeResponse(challengeResponse);
        clientResource.setRetryAttempts(10);
        try {
            //Assert
            // Send a post request
            assertDoesNotThrow(() -> {clientResource.put(null);});

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
    @ParameterizedTest
    @CsvSource(value = {
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:"+ROLE_USER+":mMusterFrau",
            "Forbidden (403) - The server understood the request, but is refusing to fulfill it:"+ROLE_SECURITY+":mMusterFrau",
            "Unauthorized (401) - The request requires user authentication:abc:mMusterFrau",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:"+ROLE_ADMIN+":",
            "Bad Request (400) - The request could not be understood by the server due to malformed syntax:"+ROLE_ADMIN+":"+ROLE_ADMIN}, delimiter = ':')
    public void post_invalidCall_shouldThrowResourceException(String responseMessage, String role, String value){
        //Arrange
        ClientResource clientResource = new ClientResource(url+uri);
        ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, role, role);
        //Query
        clientResource.setQueryValue("name", value);
        clientResource.setQueryValue("is_driver", "1");
        clientResource.setQueryValue("role", "security");
        clientResource.setQueryValue("passwort", "123");

        clientResource.setChallengeResponse(challengeResponse);
        clientResource.setRetryAttempts(10);
        try {
            //Act
            //Assert
            // Send a post request
            ResourceException response = assertThrows(ResourceException.class, () -> {clientResource.put(null);});

            assertEquals(responseMessage,response.getMessage());
            //Objects.requireNonNull(Objects.requireNonNull(Database.getDSLContext()).fetchOne(USERS, USERS.NAME.eq("mMusterFrau"))).delete();
            //assert clientResource.get(String.class) !=null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Release the resources when done
            clientResource.release();
        }
    }
    @Test
    public void delete_validCall_shouldReturnEntryInDatabase() {
       super.delete_validCall_shouldReturnEntryInDatabase();
    }
    @Override
    @ParameterizedTest
    @CsvSource(value = {"Forbidden (403) - The server understood the request, but is refusing to fulfill it:"+ROLE_USER, "Forbidden (403) - The server understood the request, but is refusing to fulfill it:"+ROLE_SECURITY, "Unauthorized (401) - The request requires user authentication: "}, delimiter = ':')
    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.delete_invalidCall_shouldThrowResourceException(responseMessage, role);
    }
    @Override
    @ParameterizedTest
    @CsvSource(value = {"Unauthorized (401) - The request requires user authentication: abc", "Forbidden (403) - The server understood the request, but is refusing to fulfill it:"+ROLE_USER, "Forbidden (403) - The server understood the request, but is refusing to fulfill it:"+ROLE_SECURITY}, delimiter = ':')
    public void get_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        super.get_invalidCall_shouldThrowResourceException(responseMessage, role);
    }
    @Test
    public void get_validCall_shouldReturnEntryInDatabase() {
        super.get_validCall_shouldReturnEntryInDatabase();
    }
}


import de.cae.XYFleet.Database;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import org.jooq.codegen.XYFleet.tables.Users;
import org.jooq.codegen.XYFleet.tables.records.BookingsRecord;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.security.User;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.ressource.XYServerResource.jSONFormat;
import static org.jooq.codegen.XYFleet.tables.Users.USERS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.jooq.codegen.XYFleet.Tables.*;

public class RessourceTest {
    protected static String url = "http://" + "@localhost:8080";
    protected static String uri;
    protected static UpdatableRecord testRecord = null;
    protected static int ADMIN_ID;
    protected static int USER_ID;
    protected static int SECURITY_ID;
    protected static Scenario scenario;

    @BeforeAll
    public static void setUp() {
        scenario = new Scenario();
        UsersRecord test = new UsersRecord();

        UsersRecord admin = new UsersRecord(0, ROLE_ADMIN, ROLE_ADMIN, ROLE_ADMIN, Byte.valueOf("1"));
        UsersRecord user = new UsersRecord(0, ROLE_USER, ROLE_USER, ROLE_USER, Byte.valueOf("1"));
        UsersRecord security = new UsersRecord(0, ROLE_SECURITY, ROLE_SECURITY, ROLE_SECURITY, Byte.valueOf("0"));
        ADMIN_ID = scenario.add(USERS, admin);
        USER_ID = scenario.add(USERS, user);
        SECURITY_ID = scenario.add(USERS, security);
    }

    public void delete_validCall_shouldReturnEntryInDatabase() {
        //Arrange
        ClientResource clientResource = new ClientResource(url + uri);
        ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, ROLE_ADMIN, ROLE_ADMIN);
        clientResource.setChallengeResponse(challengeResponse);
        clientResource.setRetryAttempts(10);
        //Act
        try {
            // Send a DELETE request
            String response = clientResource.delete(String.class);
            // Send a GET request, to look up whether deleted or not
            assertThrows(ResourceException.class, () -> {
                clientResource.get(String.class);
            });
            //Assert
            assertEquals(testRecord.formatJSON(jSONFormat), response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Release the resources when done
            clientResource.release();
        }
    }

    public void get_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        //Arrange
        ClientResource clientResource = new ClientResource(url + uri);
        ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, role, role);
        clientResource.setChallengeResponse(challengeResponse);
        clientResource.setRetryAttempts(10);
        try {
            //Act
            // Send a GET request
            ResourceException response = assertThrows(ResourceException.class, () -> {
                clientResource.get(String.class);
            });
            //Assert
            assertEquals(responseMessage, response.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Release the resources when done
            clientResource.release();
        }
    }

    public void get_validCall_shouldReturnEntryInDatabase() {
        //Arrange
        ClientResource clientResource = new ClientResource(url + uri);
        ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, ROLE_ADMIN, ROLE_ADMIN);
        clientResource.setChallengeResponse(challengeResponse);
        clientResource.setRetryAttempts(10);
        try {
            //Act
            // Send a GET request
            String response = clientResource.get(String.class);
            //Assert
            assertEquals(testRecord.formatJSON(jSONFormat), response);

        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            // Release the resources when done
            clientResource.release();
        }
    }

    public void delete_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        //Arrange
        ClientResource clientResource = new ClientResource(url + uri);
        ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, role, role);
        clientResource.setChallengeResponse(challengeResponse);
        clientResource.setRetryAttempts(10);
        //Act
        try {
            // Send a invalid DELETE request
            ResourceException response = assertThrows(ResourceException.class, () -> {
                clientResource.delete(String.class);
            });
            //Assert
            assertEquals(responseMessage, response.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Release the resources when done
            clientResource.release();
        }
    }

    @AfterAll
    public static void cleanUp() {
        System.out.println("cleanUp");
        scenario.cleanUp();
    }

}

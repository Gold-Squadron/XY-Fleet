import org.jooq.*;
import org.jooq.Record;
import org.jooq.codegen.XYFleet.tables.records.AccessGroupsRecord;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.data.Parameter;
import org.restlet.util.Series;
import org.restlet.data.ChallengeRequest;
import org.restlet.Client;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.security.Role;

import static com.sun.jna.platform.win32.LMAccess.ACCESS_GROUP;
import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.ressource.XYServerResource.jSONFormat;
import static org.jooq.codegen.XYFleet.Tables.ACCESS_GROUPS;
import static org.jooq.codegen.XYFleet.tables.Users.USERS;
import static org.junit.jupiter.api.Assertions.*;

public abstract class ResourceTest {
    protected static String url = "https://" + "localhost:8080";
    protected static String uri;
    protected static Table<?> table;
    protected static Formattable testTable;
    protected static final String BAD_REQUEST = "Bad Request (400) - The request could not be understood by the server due to malformed syntax";
    protected static final String FORBIDDEN = "Forbidden (403) - The server understood the request, but is refusing to fulfill it";
    protected static final String UNAUTHORIZED = "Unauthorized (401) - The request requires user authentication";
    protected static final String METHOD_NOT_ALLOWED = "Method Not Allowed (405) - The method specified in the request is not allowed for the resource identified by the request URI";
    protected static int ADMIN_ID;
    protected static int USER_ID;
    protected static int SECURITY_ID;
    protected static int ACCESS_GROUP_ID;
    protected static Scenario scenario;
    protected static DSLContext dslContext = Database.getDSLContext();
    @BeforeAll
    public static void setUp() {
        scenario = new Scenario();
        UsersRecord test = new UsersRecord();

        UsersRecord admin = new UsersRecord(0, ROLE_ADMIN, ROLE_ADMIN,ROLE_ADMIN , (byte) 1);
        UsersRecord user = new UsersRecord(0, ROLE_USER,ROLE_USER, ROLE_USER, (byte) 1 );
        UsersRecord security = new UsersRecord(0, ROLE_SECURITY, ROLE_SECURITY, ROLE_SECURITY, (byte) 0);

        AccessGroupsRecord accessGroup = new AccessGroupsRecord(0,null, "Pool",(byte) 1);

        ACCESS_GROUP_ID = scenario.add(ACCESS_GROUPS, accessGroup);
        ADMIN_ID = scenario.add(USERS, admin);
        USER_ID = scenario.add(USERS, user);
        SECURITY_ID = scenario.add(USERS, security);
    }

    public void delete_validCall_shouldReturnEntryInDatabase() {
        //Arrange
        ClientResource clientResource = new ClientResource(new Context(),url + uri);
        clientResource.setRetryAttempts(10);
        clientResource.setProtocol(Protocol.HTTPS);
        clientResource.setChallengeResponse(new ChallengeResponse(ChallengeScheme.HTTP_DIGEST, ROLE_ADMIN, ROLE_ADMIN));
        //Client client = new Client(new Context(), Protocol.HTTPS);
        Series<Parameter> clientParameters = clientResource.getContext().getParameters();
        clientParameters.add("truststorePath", "C:\\Users\\Lennard Helbig\\JavaVorkurs\\XY-Fleet\\backend\\SSL\\XYFleetServerTruststore.jks");
        clientParameters.add("truststorePassword", "Uzv2/8EY.X+9dRe<");

        // Send the first request with unsufficient authentication.
        try {
            clientResource.get();
        } catch (ResourceException re) {
        }
        ChallengeRequest c1 = null;
        for (ChallengeRequest challengeRequest : clientResource.getChallengeRequests()) {
            if (ChallengeScheme.HTTP_DIGEST.equals(challengeRequest.getScheme())) {
                c1 = challengeRequest;
                break;
            }
        }
        ChallengeResponse challengeResponse = new ChallengeResponse(c1,
                clientResource.getResponse(),
                ROLE_ADMIN,
                ROLE_ADMIN);
        clientResource.setChallengeResponse(challengeResponse);

        //Act
        try {
            // Send a DELETE request
            String response = clientResource.delete(String.class);
            // Send a GET request, to look up whether deleted or not
            assertThrows(ResourceException.class, () -> {
                clientResource.get(String.class);
            });
            //Assert
            assertEquals(testTable.formatJSON(jSONFormat), response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Release the resources when done
            clientResource.release();
        }
    }

    public void get_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        //Arrange
        if (role == null) role = "";
        System.setProperty("javax.net.ssl.trustStore", "C:/Users/Lennard Helbig/JavaVorkurs/XY-Fleet/backend/SSL/XYFleetServerTruststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword","Uzv2/8EY.X+9dRe<");

        ClientResource clientResource = new ClientResource(new Context(),url + uri);
        clientResource.setRetryAttempts(10);
        clientResource.setProtocol(Protocol.HTTPS);
        clientResource.setChallengeResponse(new ChallengeResponse(ChallengeScheme.HTTP_DIGEST, role, role));
        //Client client = new Client(new Context(), Protocol.HTTPS);
        Series<Parameter> clientParameters = clientResource.getContext().getParameters();
        clientParameters.add("truststorePath", "C:\\Users\\Lennard Helbig\\JavaVorkurs\\XY-Fleet\\backend\\SSL\\XYFleetServerTruststore.jks");
        clientParameters.add("truststorePassword", "Uzv2/8EY.X+9dRe<");

        // Send the first request with unsufficient authentication.
        try {
            clientResource.get();
        } catch (ResourceException re) {
        }
        ChallengeRequest c1 = null;
        for (ChallengeRequest challengeRequest : clientResource.getChallengeRequests()) {
            if (ChallengeScheme.HTTP_DIGEST.equals(challengeRequest.getScheme())) {
                c1 = challengeRequest;
                break;
            }
        }
        ChallengeResponse challengeResponse = new ChallengeResponse(c1,
                clientResource.getResponse(),
                role,
                role);
        clientResource.setChallengeResponse(challengeResponse);

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
        ClientResource clientResource = setupRequest(ROLE_ADMIN);

        try {
            //Act
            // Send a GET request
            String response = clientResource.get(String.class);
            //Assert
            assertEquals(testTable.formatJSON(jSONFormat), response);

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
        if (role == null) role = "";
        ClientResource clientResource = setupRequest(role);

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

    public void put_validCall_shouldReturnEntryInDatabase(String params) {
        //Arrange

        ClientResource clientResource = setupRequest(ROLE_ADMIN);

        //Query
        String paramList[] = processQuery(params, clientResource);

        try {
            //Assert
            // Send a post request
            assertDoesNotThrow(() -> {
                clientResource.put(null);
            });

            //DELETE if wrong error occured or it got falsely accepted.
            Condition condition = DSL.trueCondition(); // Start with a true condition

            for (String param : paramList) {
                String[] temp = param.split("=");
                Field<String> myField = table.field(temp[0], String.class);
                condition = condition.and(myField.eq(DSL.val(temp[1], myField.getDataType())));
            }
            Record result = Database.getDSLContext().deleteFrom(table).where(condition).returning().fetchOne();
            assertNotNull(result);
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            // Release the resources when done
            clientResource.release();
        }
    }

    public void put_invalidCall_shouldThrowResourceException(String responseMessage, String role, String params) {
        //Arrange

        ClientResource clientResource = setupRequest(role);

        //Query
        String[] paramList = processQuery(params, clientResource);

        try {
            //Act
            //Assert
            // Send a put request
            ResourceException response = assertThrows(ResourceException.class, () -> {
                clientResource.put(null);
            });

            assertEquals(responseMessage, response.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //DELETE if wrong error occured or it got falsely accepted.
            Condition condition = DSL.noCondition(); // Start with a true condition

            if (paramList != null) {
                for (String param : paramList) {
                    String[] temp = param.split("=");
                    if (temp.length == 2) {
                        Field<String> myField = table.field(temp[0], String.class);
                        if(myField!=null){
                            condition = condition.and(myField.eq(DSL.val(temp[1], myField.getDataType())));
                        }
                    }
                }
                Database.getDSLContext().deleteFrom(table).where(condition).execute();
            }
            // Release the resources when done
            clientResource.release();
        }
    }

    @AfterAll
    public static void cleanUp() {
        scenario.cleanUp();
    }
    protected String[] processQuery(String params, ClientResource clientResource){
        //Query
        if (params !=null) {
            String[] paramList = params.split("&");
            for (String param : paramList) {
                String[] temp = param.split("=");
                if(temp.length>1){
                    clientResource.setQueryValue(temp[0], temp[1]);
                }else if(temp.length==1){
                     clientResource.setQueryValue(temp[0], null);
                }else{
                    throw new IllegalArgumentException("Query was not setup correctly. Always needs a form of 'column_name'='value/'");
                }
            }
            return paramList;
        }
        return null;
    }
    protected ClientResource setupRequest(String role){
        System.setProperty("javax.net.ssl.trustStore", "C:/Users/Lennard Helbig/JavaVorkurs/XY-Fleet/backend/SSL/XYFleetServerTruststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword","Uzv2/8EY.X+9dRe<");

        ClientResource clientResource = new ClientResource(new Context(),url + uri);
        clientResource.setRetryAttempts(10);
        clientResource.setProtocol(Protocol.HTTPS);
        clientResource.setChallengeResponse(new ChallengeResponse(ChallengeScheme.HTTP_DIGEST, role, role));
        //Client client = new Client(new Context(), Protocol.HTTPS);
        Series<Parameter> clientParameters = clientResource.getContext().getParameters();
        clientParameters.add("truststorePath", "C:\\Users\\Lennard Helbig\\JavaVorkurs\\XY-Fleet\\backend\\SSL\\XYFleetServerTruststore.jks");
        clientParameters.add("truststorePassword", "Uzv2/8EY.X+9dRe<");

        try {
            clientResource.get();
        } catch (ResourceException re) {
        }
        ChallengeRequest c1 = null;
        for (ChallengeRequest challengeRequest : clientResource.getChallengeRequests()) {
            if (ChallengeScheme.HTTP_DIGEST.equals(challengeRequest.getScheme())) {
                c1 = challengeRequest;
                break;
            }
        }
        ChallengeResponse challengeResponse = new ChallengeResponse(c1,
                clientResource.getResponse(),
                role,
                role);
        clientResource.setChallengeResponse(challengeResponse);

        return clientResource;
    }
}

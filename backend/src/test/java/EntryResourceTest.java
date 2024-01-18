import org.jooq.UpdatableRecord;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.USERS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class EntryResourceTest extends ResourceTest {
    protected static UpdatableRecord testRecord = null;
    public void post_validCall_shouldReturnEntryInDatabase(String params) {
        ClientResource clientResource = new ClientResource(url + uri);
        ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, ROLE_ADMIN, ROLE_ADMIN);

        clientResource.setChallengeResponse(challengeResponse);
        clientResource.setRetryAttempts(10);
        //Query
        if (params == null) params = "";
        String[] paramList = params.split("&");
        for (String param : paramList) {
            String[] temp = param.split("=");
            clientResource.setQueryValue(temp[0], temp[1]);
        }
        try {
            //Act
            //Assert
            // Send a post request
            Representation response = clientResource.post(null);
            Representation actual = clientResource.get();
            assertEquals(response.getText(), actual.getText());
            // Release the resources when done
            clientResource.release();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scenario.merge(USERS, testRecord);
            // Release the resources when done
            clientResource.release();
        }
    }
    public void post_invalidCall_shouldReturnEntryInDatabase(String responseMessage, String role, String params) {
        ClientResource clientResource = new ClientResource(url + uri);
        ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, role, role);
        //Query

        clientResource.setChallengeResponse(challengeResponse);
        clientResource.setRetryAttempts(10);

        //Query
        if (params == null) params = "";
        String[] paramList = params.split("&");
        for (String param : paramList) {
            String[] temp = param.split("=");
            clientResource.setQueryValue(temp[0], temp[1]);
        }

        try {
            //Act
            //Assert
            // Send a post request
            ResourceException response = assertThrows(ResourceException.class, () -> {
                clientResource.post(null);
            });

            assertEquals(responseMessage, response.getMessage());
            //Objects.requireNonNull(Objects.requireNonNull(Database.getDSLContext()).fetchOne(USERS, USERS.NAME.eq("mMusterFrau"))).delete();
            //assert clientResource.get(String.class) !=null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scenario.merge(table, testRecord);
            // Release the resources when done
            clientResource.release();
        }
    }
}

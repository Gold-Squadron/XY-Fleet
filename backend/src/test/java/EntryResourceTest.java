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
        //create new Request

        ClientResource clientResource = setupRequest(ROLE_ADMIN);

        //Query
        processQuery(params, clientResource);
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
            scenario.merge(table, testRecord);
            // Release the resources when done
            clientResource.release();
        }
    }
    public void post_invalidCall_shouldReturnEntryInDatabase(String responseMessage, String role, String params) {
        //create new Request

        ClientResource clientResource = setupRequest(role);

        //Query
        processQuery(params, clientResource);
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

import org.jooq.codegen.XYFleet.tables.records.*;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.restlet.data.*;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;

import java.sql.Date;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.ressource.XYServerResource.jSONFormat;
import static org.jooq.codegen.XYFleet.Tables.*;
import static org.junit.jupiter.api.Assertions.*;

public class BookingRessourceTest extends RessourceTest {
    private static String uri;
    private static BookingsRecord testBooking = null;

    @BeforeAll
    public static void initAll(){
        try {
            //Arrange
            PricingRecord pricing = new PricingRecord(0, new Date(1, 1, 1).toLocalDate(), 3000, 5000);
            pricing.setId(scenario.add(PRICING, pricing));
            //dslContext.insertInto(PRICING).values(pricing).onDuplicateKeyIgnore().execute();

            InsurancesRecord insurances = new InsurancesRecord(0, 10, 10, 10);
            insurances.setId(scenario.add(INSURANCES, insurances));
            //dslContext.insertInto(INSURANCES).values(insurances).onDuplicateKeyIgnore().execute();

            VehiclesRecord vehicle = new VehiclesRecord(0, "STO-XY-123", "VW", "Käfer", "123", 2000, 4000, insurances.getId(), pricing.getId());
            vehicle.setId(scenario.add(VEHICLES, vehicle));
            //dslContext.insertInto(VEHICLES).values(vehicle).onDuplicateKeyIgnore().execute();

            BookingsRecord booking = new BookingsRecord(0, ADMIN_ID, vehicle.getId(), new Date(1, 1, 1).toLocalDate(), new Date(1, 1, 2).toLocalDate(), "none");
            booking.setId(scenario.add(BOOKINGS, booking));
            uri = "/booking/" + booking.getId();
            testBooking = booking;
        }catch(Exception e){
            e.printStackTrace();
            cleanUp();
            throw e;
        }
    }
    @Before
    public void setupSingle(){
            testBooking.merge();
    }
    @Test
    public void delete_validCall_shouldReturnEntryInDatabase() {
        //Arrange
        ClientResource clientResource = new ClientResource(url+uri);
        ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, ROLE_ADMIN, ROLE_ADMIN);
        clientResource.setChallengeResponse(challengeResponse);
        clientResource.setRetryAttempts(10);
        //Act
        try {
            // Send a DELETE request
            String response = clientResource.delete(String.class);
            // Send a GET request, to look up whether delted or not
            String response2 = clientResource.get(String.class);

            //Assert
            assertEquals(testBooking.formatJSON(jSONFormat), response);
            assertNull(response2);

        } catch (Exception e) {
            e.printStackTrace();
            cleanUp();
        } finally {
            // Release the resources when done
            clientResource.release();
        }
    }
    @ParameterizedTest
    @CsvSource({"insufficient permission, "+ROLE_USER, "Unauthorized (401) - The request requires user authentication,"+ROLE_SECURITY, "Unauthorized (401) - The request requires user authentication,"})
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
            cleanUp();
        } finally {
            // Release the resources when done
            clientResource.release();
        }
    }
    @ParameterizedTest
    @CsvSource({"Unauthorized (401) - The request requires user authentication,"})
    public void get_invalidCall_shouldThrowResourceException(String responseMessage, String role) {
        //Arrange
        ClientResource clientResource = new ClientResource(url+uri);
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
            assertEquals(testBooking.formatJSON(jSONFormat), response);

        } catch (Exception e) {
            e.printStackTrace();
            cleanUp();
        } finally {
            // Release the resources when done
            clientResource.release();
        }
    }

    @Test
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
            assertEquals(testBooking.formatJSON(jSONFormat), response);

        } catch (Exception e) {
            e.printStackTrace();
            cleanUp();
        } finally {
            // Release the resources when done
            clientResource.release();
        }
    }
}
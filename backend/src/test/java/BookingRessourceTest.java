import org.jooq.codegen.XYFleet.tables.records.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.restlet.data.*;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.sql.Date;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.ressource.XYServerResource.jSONFormat;
import static org.jooq.codegen.XYFleet.Tables.*;
import static org.junit.jupiter.api.Assertions.*;

public class BookingRessourceTest extends RessourceTest {

    @BeforeAll
    public static void initAll(){
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
            testRecord = booking;
    }
    @BeforeEach
    public void initSingle(){

        scenario.merge(BOOKINGS, testRecord);
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
            // Send a GET request, to look up whether deleted or not
            assertThrows(ResourceException.class, () -> {clientResource.get(String.class);});
            //Assert
            assertEquals(testRecord.formatJSON(jSONFormat), response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Release the resources when done
            clientResource.release();
        }
    }
    @ParameterizedTest
    @CsvSource(value = {"Forbidden (403) - The server understood the request, but is refusing to fulfill it:"+ROLE_USER, "Forbidden (403) - The server understood the request, but is refusing to fulfill it:"+ROLE_SECURITY, "Unauthorized (401) - The request requires user authentication: "}, delimiter = ':')
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
    @ParameterizedTest
    @CsvSource(value = {"Unauthorized (401) - The request requires user authentication: abc"}, delimiter = ':')
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
            assertEquals(responseMessage, response.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
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
            assertEquals(testRecord.formatJSON(jSONFormat), response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Release the resources when done
            clientResource.release();
        }
    }
}
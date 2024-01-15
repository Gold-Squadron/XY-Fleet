import org.jooq.codegen.XYFleet.tables.records.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.restlet.data.*;
import org.restlet.resource.ClientResource;

import java.sql.Date;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingTest extends RessourceTest {
    private static String uri = "/booking/102";
    private static BookingsRecord testBooking = null;

    @BeforeAll
    public static void init(){
        //Arrange
        PricingRecord pricing = new PricingRecord(getIndex(), new Date(1, 1, 1).toLocalDate(), 3000, 5000);
        scenario.put( pricing);
        //dslContext.insertInto(PRICING).values(pricing).onDuplicateKeyIgnore().execute();

        InsurancesRecord insurances = new InsurancesRecord(getIndex(), 10, 10, 10);
        scenario.put( insurances);
        //dslContext.insertInto(INSURANCES).values(insurances).onDuplicateKeyIgnore().execute();

        VehiclesRecord vehicle = new VehiclesRecord(getIndex(), "STO-XY-123", "VW", "Käfer", "123", 2000, 4000, insurances.getId(), pricing.getId());
        scenario.put( vehicle);
        //dslContext.insertInto(VEHICLES).values(vehicle).onDuplicateKeyIgnore().execute();

        BookingsRecord booking = new BookingsRecord(getIndex(), ADMIN_ID, vehicle.getId(), new Date(1, 1, 1).toLocalDate(), new Date(1, 1, 2).toLocalDate(), "none");
        scenario.put(booking);
        testBooking = booking;

    }
    @Test
    public void toString_validCall_shouldReturnEntryInDatabase() {
        //Arrange
        ClientResource clientResource = new ClientResource(url+uri);
        ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, ROLE_ADMIN, ROLE_ADMIN);
        clientResource.setChallengeResponse(challengeResponse);
        clientResource.setRetryAttempts(10);
        //Act
        try {
            // Send a GET request
            String response = clientResource.get(String.class);

            // Process the response
            System.out.println("Response from the server: " + response);

            //Assert
            assertEquals(testBooking.formatJSON(), response);
            System.out.println(response);

            // If you need to send other types of requests (POST, PUT, DELETE, etc.), you can use methods like:
            // clientResource.post(), clientResource.put(), clientResource.delete(), etc.
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Release the resources when done
            clientResource.release();
        }
    }
}
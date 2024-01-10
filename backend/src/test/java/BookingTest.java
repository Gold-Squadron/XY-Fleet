import de.cae.XYFleet.Database;
import de.cae.XYFleet.Main;
import de.cae.XYFleet.ressource.Booking;
import de.cae.XYFleet.ressource.test;
import org.jooq.DSLContext;
import org.jooq.codegen.XYFleet.tables.Users;
import org.jooq.codegen.XYFleet.tables.records.*;
import org.junit.jupiter.api.Test;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Delete;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import static org.jooq.codegen.XYFleet.Tables.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingTest {
    private static String url = "https://" +"testPerson" + ":" + "123" + "@localhost:8080/booking/";

    @Test
    public void toString_validCall_shouldReturnEntryInDatabase() {
        //Arrange
        DSLContext dslContext = Database.getDSLContext();
        assert dslContext != null;
        int userId = Index.getIndex();
        int pricingId = Index.getIndex();
        int vehicleId = Index.getIndex();
        int insuranceId = Index.getIndex();
        int bookingId = Index.getIndex();
        UsersRecord user = new UsersRecord(userId, "testPerson", "123", "admin", Byte.valueOf("1"));
        dslContext.executeInsert(user);
        PricingRecord pricing = new PricingRecord(pricingId, new Date(1, 1, 1).toLocalDate(), 3000, 5000);
        dslContext.executeInsert(pricing);

        InsurancesRecord insurances = new InsurancesRecord(insuranceId, 10, 10, 10);
        dslContext.executeInsert(insurances);
        VehiclesRecord vehicle = new VehiclesRecord(vehicleId, "STO-XY-123", "VW", "Käfer", "123", 2000, 4000, insuranceId, pricingId);
        dslContext.executeInsert(vehicle);
        BookingsRecord booking = new BookingsRecord(bookingId, userId, vehicleId, new Date(1, 1, 1).toLocalDate(), new Date(1, 1, 2).toLocalDate(), "none");
        dslContext.executeInsert(booking);
        //Act
try {
    // Instantiate the client connector, and configure it.
    //Client client = new Client(new Context(), Protocol.HTTP);
    //client.getContext().getParameters().add("useForwardedForHeader","false");

    // Instantiate the ClientResource, and set it's client connector.
    //equest request = new Request(Method.DELETE, "https://" +"testPerson" + ":" + "123" + "@localhost:8080/booking/" + bookingId);

    //ClientResource cr = new ClientResource(request);
    //cr.setNext(client);
    ClientResource clientResource = new ClientResource(url);
    Request request = new Request(Method.POST, url);

    clientResource.setRequest(request);

    Form form = new Form();

    form.set("foo", "barValue");

    org.restlet.representation.Representation   response = clientResource.post(form, MediaType.APPLICATION_JSON);
    Representation responseEntity = clientResource.getResponseEntity();


    //test test = cr.wrap(test.class);
    String value = responseEntity.getText();
    //System.out.println("Hallo");
    //Assert
    assertEquals(booking.formatJSON(), value);
    System.out.println(value);
} catch (Exception e) {

}
        //Cleanup
        dslContext.executeDelete(booking);
        dslContext.executeDelete(vehicle);
        dslContext.executeDelete(insurances);
        dslContext.executeDelete(pricing);
        dslContext.executeDelete(user);
    }
}

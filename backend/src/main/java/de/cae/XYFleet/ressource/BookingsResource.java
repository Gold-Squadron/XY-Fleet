package de.cae.XYFleet.ressource;

import de.cae.XYFleet.authentication.XYAuthorizer;
import org.restlet.resource.Get;

public class BookingsResource extends XYServerResource {
    @Get("txt")
    public String toString() {
        isInRole(XYAuthorizer.ROLE_SECURITY);
        Result<org.jooq.Record> result = dslContext.select().from(BOOKINGS).fetch();
        return result.formatJSON(jSONFormat);
    }
    @Put
    public String createBookings(){
        isInRole(XYAuthorizer.ROLE_ADMIN);
        return new Booking().createBooking();
    }
    @Post
    public void postBookings(){
        throw new ResourceException(405);
    }
    @Delete
    public void deleteBookings(){
        throw new ResourceException(405);
    }
}

package de.cae.XYFleet.ressource;

import de.cae.XYFleet.authentication.XYAuthorizer;
import org.jooq.Result;
import org.jooq.codegen.XYFleet.tables.records.BookingsRecord;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.restlet.data.Status;
import org.restlet.resource.*;

import static org.jooq.codegen.XYFleet.tables.Bookings.BOOKINGS;

public class Bookings extends XYServerResource {
    @Get
    public String toString(){
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

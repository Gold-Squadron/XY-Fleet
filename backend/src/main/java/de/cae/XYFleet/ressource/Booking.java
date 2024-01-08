package de.cae.XYFleet.ressource;

import org.jooq.Result;
import org.jooq.Record;
import org.restlet.resource.*;


import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_USER;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;

public class Booking extends XYServerResource {
    private int bookingIdentifier;

    @Get("txt")
    public String toString() {
        checkInRole(ROLE_SECURITY);
        //SELECT * FROM bookings where id = {bookingIdentifier}
        Result<Record> result = dslContext.select().from(BOOKINGS).where(BOOKINGS.ID.eq(bookingIdentifier)).fetch();

        return !result.isEmpty() ? result.get(0).formatJSON(jSONFormat) : null;
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        try {
            bookingIdentifier = Integer.parseInt(getAttribute("bookingIdentifier"));
        } catch (NumberFormatException e) {
            bookingIdentifier = -1;
        }
    }

    @Delete()
    public String deleteBooking() {
        checkInRole(ROLE_USER);

        String result = this.toString();

        //DELETE bookings where id = {bookingIdentifier}
        if(Integer.parseInt(getClientInfo().getUser().getName()) == bookingIdentifier  || isInRole("admin")){
            dslContext.delete(BOOKINGS).where(BOOKINGS.ID.eq(bookingIdentifier)).execute();
        }
        return result;
    }

    @Post()
    public void createBooking() {
        checkInRole(ROLE_USER);
    }

    @Put()
    public void editBooking() {
        checkInRole(ROLE_USER);
    }
}

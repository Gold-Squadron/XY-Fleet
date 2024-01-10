package de.cae.XYFleet.ressource;

import org.jooq.Result;
import org.jooq.Record;
import org.jooq.codegen.XYFleet.tables.records.BookingsRecord;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.restlet.resource.*;


import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_USER;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import static org.jooq.codegen.XYFleet.Tables.USERS;

public class Booking extends XYServerResource {
    private int bookingIdentifier;

    @Get("txt")
    public String toString() {
        checkInRole(ROLE_SECURITY);
        //SELECT * FROM bookings where id = {bookingIdentifier}
        BookingsRecord result = dslContext.fetchOne(BOOKINGS, BOOKINGS.ID.eq(bookingIdentifier));

        return result != null? result.formatJSON(jSONFormat) : null;
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
        }else{
            return "insufficient permission";
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

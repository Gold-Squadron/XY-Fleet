package de.cae.XYFleet.ressource;

import org.jooq.codegen.XYFleet.tables.records.BookingsRecord;
import org.restlet.resource.*;


import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_USER;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;

public class BookingResource extends XYServerResource {
    private int identifier;

    @Get("txt")
    public String toString() throws ResourceException {
        checkInRole(ROLE_SECURITY);
        //SELECT * FROM bookings where id = {identifier}
        BookingsRecord result = dslContext.fetchOne(BOOKINGS, BOOKINGS.ID.eq(identifier));

        if (result == null) throw new ResourceException(404, "not found");
        return result.formatJSON(jSONFormat);
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        try {
            identifier = Integer.parseInt(getAttribute("identifier"));
        } catch (NumberFormatException e) {
            identifier = -1;
        }
    }

    @Delete()
    public String deleteBooking() throws ResourceException {
        checkInRole(ROLE_USER);

        String result = this.toString();


        if (Integer.parseInt(getClientInfo().getUser().getIdentifier()) != identifier && !isInRole("admin"))
            throw new ResourceException(403);
        //DELETE bookings where id = {identifier}
        dslContext.delete(BOOKINGS).where(BOOKINGS.ID.eq(identifier)).execute();
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

package de.cae.XYFleet.ressource;

import org.jooq.codegen.XYFleet.tables.records.BookingsRecord;
import org.restlet.resource.*;


import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_USER;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;

public class BookingRessource extends XYServerResource {
    private int Identifier;

    @Get("txt")
    public String toString() throws ResourceException {
        checkInRole(ROLE_SECURITY);
        //SELECT * FROM bookings where id = {Identifier}
        BookingsRecord result = dslContext.fetchOne(BOOKINGS, BOOKINGS.ID.eq(Identifier));

        if (result == null) throw new ResourceException(404, "not found");
        return result.formatJSON(jSONFormat);
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        try {
            Identifier = Integer.parseInt(getAttribute("Identifier"));
        } catch (NumberFormatException e) {
            Identifier = -1;
        }
    }

    @Delete()
    public String deleteBooking() throws ResourceException {
        checkInRole(ROLE_USER);

        String result = this.toString();


        if (Integer.parseInt(getClientInfo().getUser().getIdentifier()) != Identifier && !isInRole("admin"))
            throw new ResourceException(403);
        //DELETE bookings where id = {Identifier}
        dslContext.delete(BOOKINGS).where(BOOKINGS.ID.eq(Identifier)).execute();
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

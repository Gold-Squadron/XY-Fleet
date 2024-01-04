package de.cae.XYFleet;

import org.jooq.JSONFormat;
import org.jooq.Result;
import org.jooq.Record;
import org.jooq.Query;
import org.jooq.codegen.XYFleet.tables.Bookings;
import org.restlet.resource.*;

import java.util.Map;

import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;

public class Booking extends XYServerResource {
    private int bookingIdentifier;

    @Get("txt")
    public String toString() {

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

        String result = this.toString();

        //DELETE bookings where id = {bookingIdentifier}
        dslContext.delete(BOOKINGS).where(BOOKINGS.ID.eq(bookingIdentifier)).execute();

        return result;
    }

    @Post()
    public void createBooking() {

    }

    @Put()
    public void editBooking() {

    }
}

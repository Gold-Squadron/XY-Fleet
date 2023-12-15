package de.cae.XYFleet;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public class Booking extends XYServerResource {
    //TODO REMOVE THIS METHOD toString() it is only for test purposes
    @Get("txt")
    public String toString(){
        return "this is the booking URI.";
    }

    @Delete("/remove/{bookingIdentifier}")
    public String deleteBooking() {
        String bookingIdentifier = getAttribute("bookingIdentifier");
        return bookingIdentifier;
    }

    @Put("/create")
    public void createBooking() {

    }

    @Put("/edit/{bookingIdentifier")
    public void editBooking() {

    }
}

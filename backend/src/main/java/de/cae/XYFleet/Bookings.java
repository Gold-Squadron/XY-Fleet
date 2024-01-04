package de.cae.XYFleet;

import org.restlet.resource.Get;

public class Bookings extends XYServerResource{
    @Get("txt")
    public String toString(){
        return "this is the bookings URI.";
    }

}

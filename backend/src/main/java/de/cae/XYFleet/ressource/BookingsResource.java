package de.cae.XYFleet.ressource;

import de.cae.XYFleet.authentication.XYAuthorizer;
import org.restlet.resource.Get;

public class BookingsResource extends XYServerResource {
    @Get("txt")
    public String toString(){
        isInRole(XYAuthorizer.ROLE_SECURITY);
        return "this is the bookings URI.";
    }

}

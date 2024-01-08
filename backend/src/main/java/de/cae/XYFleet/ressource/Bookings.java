package de.cae.XYFleet.ressource;

import de.cae.XYFleet.authentication.XYAuthorizer;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

public class Bookings extends XYServerResource {
    @Get("txt")
    public String toString(){
        isInRole(XYAuthorizer.ROLE_SECURITY);
        return "this is the bookings URI.";
    }

}

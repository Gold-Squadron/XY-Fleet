package de.cae.XYFleet.ressource;

import de.cae.XYFleet.authentication.XYAuthorizer;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

public class BookingsResource extends XYServerResource {
    @Override
    public String createEntity() throws ResourceException {
        return null;
    }
    @Override
    @Get("txt")
    public String toString() {
        isInRole(XYAuthorizer.ROLE_SECURITY);
        return "this is the bookings URI.";
    }

}

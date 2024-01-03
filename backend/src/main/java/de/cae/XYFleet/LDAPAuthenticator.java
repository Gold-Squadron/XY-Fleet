package de.cae.XYFleet;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class LDAPAuthenticator extends ServerResource {
    @Get("txt")
    public String toString(){
        return "This is the LDAPAuthenticator!!";
    }
}

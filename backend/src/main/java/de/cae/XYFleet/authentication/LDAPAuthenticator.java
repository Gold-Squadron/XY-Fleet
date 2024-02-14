package de.cae.XYFleet.authentication;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class LDAPAuthenticator extends ServerResource {
    @Get()
    public String toString(){
        return "This is the LDAPAuthenticator!!";
    }
}

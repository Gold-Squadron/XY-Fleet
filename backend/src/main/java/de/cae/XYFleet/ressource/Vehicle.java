package de.cae.XYFleet.ressource;

import org.restlet.resource.Get;

public class Vehicle extends XYServerResource {
    @Get("txt")
    public String toString() {
        // Print the requested URI path
        return "Resource URI  : " + getReference() + '\n' + "Root URI      : "
                + getRootRef() + '\n' + "Routed part   : "
                + getReference().getBaseRef() + '\n' + "Remaining part: "
                + getReference().getRemainingPart();
    }
}

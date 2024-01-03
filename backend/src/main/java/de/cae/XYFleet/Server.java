package de.cae.XYFleet;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;



public class Server extends ServerResource {

    //public static final String ROOT_URI = "file:///c:/restlet/docs/api/";

    public static void initServer(){
        try{
            Component component = new Component();
            org.restlet.Server server = component.getServers().add(Protocol.HTTP, 8080);
            component.getDefaultHost().attach("/xywing", Vehicle.class);
            component.getDefaultHost().attach("/user", User.class);
            component.getDefaultHost().attach("/driver", Driver.class);
            component.getDefaultHost().attach("/booking/", Booking.class);
            component.getDefaultHost().attach("/booking/{bookingIdentifier}", Booking.class);
            component.getDefaultHost().attach("", Server.class);
            component.getDefaultHost().attach("/ldapauthenticator", LDAPAuthenticator.class);
            component.start();
             }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Get("txt")
    public String toString() {
        // Print the requested URI path
        return "hello, world. THis is a TEst";
    }
}

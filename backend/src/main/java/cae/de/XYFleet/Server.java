package cae.de.XYFleet;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class Server extends ServerResource {
    public static void initServer(){
        try{
            new org.restlet.Server(Protocol.HTTP, 8080, Server.class).start();
            Component component = new Component();
            component.getServers().add(Protocol.HTTP, 8080);
            component.getDefaultHost().attach("/xywing", Vehicle.class);
            component.getDefaultHost().attach("/user", User.class);
            component.getDefaultHost().attach("/driver", Driver.class);
            component.getDefaultHost().attach("/booking", Booking.class);
            //component.getDefaultHost().attach("", Server.class);
            component.getDefaultHost().attach("/ldapauthenticator", LDAPAuthenticator.class);
            //component.start();

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

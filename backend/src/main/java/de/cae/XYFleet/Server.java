package de.cae.XYFleet;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;



public class Server extends ServerResource {

    public static final String ROOT_URI = "file:///c:/restlet/docs/api/";

    public static void initServer(){
        try{
            Component component = new Component();
            component.getServers().add(Protocol.HTTP, 8080);
            component.getDefaultHost().attach("/xywing", Vehicle.class);
            component.getDefaultHost().attach("/user", User.class);
            component.getDefaultHost().attach("/driver", Driver.class);
            component.getDefaultHost().attach("/booking", Booking.class);
            component.getDefaultHost().attach("", Server.class);
            component.getDefaultHost().attach("/ldapauthenticator", LDAPAuthenticator.class);
            component.start();



            // Create an application
            Application application = new Application() {
                @Override
                public Restlet createInboundRoot() {
                    return new Directory(getContext(), ROOT_URI);
                }
            };

            // Attach the application to the component and start it
            component.getDefaultHost().attach(application);
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

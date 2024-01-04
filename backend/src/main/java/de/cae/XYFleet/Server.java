package de.cae.XYFleet;

import org.restlet.*;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Directory;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;


public class Server {

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
            component.getDefaultHost().attach("", test.class);
            component.getDefaultHost().attach("/ldapauthenticator", LDAPAuthenticator.class);
            component.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

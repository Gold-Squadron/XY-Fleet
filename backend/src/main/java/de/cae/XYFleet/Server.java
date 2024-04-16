package de.cae.XYFleet;

import de.cae.XYFleet.authentication.XYAuthorizer;
import org.restlet.*;
import org.restlet.data.Protocol;
import org.restlet.service.CorsService;

import java.util.Arrays;
import java.util.HashSet;


public class Server {

    public static void initServer() {
        try {
            Component component = new Component();
            org.restlet.Server server = component.getServers().add(Protocol.HTTP, 8080);

            //component.getDefaultHost().attach("/ldapauthenticator", LDAPAuthenticator.class);
            component.getDefaultHost().attach("/test", TestResource.class);

            //handle CORS to allow frontend to access this server!
            CorsService corsService = new CorsService();
            corsService.setAllowedOrigins( new HashSet(Arrays.asList("http://localhost:5173/", "http://localhost:5174/"))); //TODO: add the URL of the frontend
            corsService.setAllowedCredentials(true);

            //Add to the first Application. The Authorizer the CORS Service handler
            XYAuthorizer xyAuthorizer = new XYAuthorizer();
            xyAuthorizer.getServices().add(corsService);
            component.getDefaultHost().attachDefault(new XYAuthorizer());


            component.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

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


            //Add to the first Application. The Authorizer the CORS Service handler
            XYAuthorizer xyAuthorizer = new XYAuthorizer();
            CorsService corsService = new CorsService();
            corsService.setAllowingAllRequestedHeaders(true);
            corsService.setAllowedOrigins(new HashSet(Arrays.asList("http://localhost:5173")));
            corsService.setAllowedCredentials(true);
            corsService.setSkippingResourceForCorsOptions(true);

            xyAuthorizer.getServices().add(corsService);
            component.getDefaultHost().attachDefault(xyAuthorizer);


            component.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

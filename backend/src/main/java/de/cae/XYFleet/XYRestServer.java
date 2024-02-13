package de.cae.XYFleet;

import de.cae.XYFleet.authentication.LDAPAuthenticator;
import de.cae.XYFleet.authentication.XYAuthorizer;
import org.restlet.*;
import org.restlet.data.Protocol;
import org.restlet.service.CorsService;
import org.restlet.service.Service;


public class XYRestServer {

    //public static final String ROOT_URI = "file:///c:/restlet/docs/api/";

    public static void initServer() {
        try {
            Component component = new Component() {
                @Override
                public void handle(Request request, Response response) {
                    super.handle(request, response);
                    System.out.println("Before: " + response.getAccessControlAllowOrigin());
                    response.setAccessControlAllowOrigin("*");
                    response.setAccessControlAllowCredentials(true);
                    System.out.println(response.toString());
                    System.out.println(response.getAccessControlAllowOrigin());
                }
            };
            Server server = component.getServers().add(Protocol.HTTP, 8080);

            CorsService corsService = new CorsService();
            corsService.getAllowedOrigins().add("localhost:5173");
            corsService.getAllowedOrigins().add("*");
            corsService.setAllowedCredentials(true);

            component.getServices().add(corsService);
            System.out.println(corsService.getAllowedOrigins());

            component.getDefaultHost().attach("/ldapauthenticator", LDAPAuthenticator.class);
            component.getDefaultHost().attach("/test", test.class);

            component.getDefaultHost().attachDefault(new XYAuthorizer());
            component.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

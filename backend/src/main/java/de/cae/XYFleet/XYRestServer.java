package de.cae.XYFleet;

import de.cae.XYFleet.authentication.LDAPAuthenticator;
import de.cae.XYFleet.authentication.XYAuthorizer;
import org.restlet.*;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
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
                    response.getAccessControlAllowMethods().add(Method.GET);
                    response.getAccessControlAllowMethods().add(Method.DELETE);
                    response.getAccessControlAllowMethods().add(Method.PUT);
                    response.getAccessControlAllowMethods().add(Method.POST);
                    response.getAccessControlAllowMethods().add(Method.OPTIONS);
                    response.getAccessControlAllowHeaders().add("Content-Type");
                    response.getAccessControlAllowHeaders().add("Origin");
                    response.getAccessControlAllowHeaders().add("X-Auth-Token");
                    response.getAccessControlAllowHeaders().add("authorization");
                    System.out.println(response.getAccessControlAllowHeaders());
                    System.out.println(response.toString());
                    System.out.println(response.getAccessControlAllowOrigin());
                    response.setStatus(Status.SUCCESS_OK);
                }
            };
            Server server = component.getServers().add(Protocol.HTTP, 8080);


            component.getDefaultHost().attach("/ldapauthenticator", LDAPAuthenticator.class);
            component.getDefaultHost().attach("/test", test.class);

            component.getDefaultHost().attachDefault(new XYAuthorizer());
            component.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

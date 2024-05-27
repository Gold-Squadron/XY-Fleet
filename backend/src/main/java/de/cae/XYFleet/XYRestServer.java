package de.cae.XYFleet;
import org.restlet.Server;
import de.cae.XYFleet.authentication.LDAP.LDAPAuthenticator;
import de.cae.XYFleet.authentication.XYAuthorizer;
import org.restlet.*;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;

import org.restlet.Component;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.util.Series;
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
            Server server = component.getServers().add(Protocol.HTTPS, 8080);

            Series<Parameter> parameters = server.getContext().getParameters();
            parameters.add("keyStorePath", "C:/Users/Lennard Helbig/JavaVorkurs/XY-Fleet/backend/SSL/XYFleetServerKeystore.jks");
            parameters.add("keyStorePassword", "tyJ;~>\"p:x3h(aVS");
            parameters.add("keyPassword", "changeit");
            parameters.add("keyStoreType", "JKS");
            parameters.add("sslContextFactory", "org.restlet.engine.ssl.DefaultSslContextFactory");
            //parameters.add("truststorePath", "C:/Users/Lennard Helbig/JavaVorkurs/XY-Fleet/backend/SSL/XYFleetServerTruststore.jks");
            //parameters.add("truststorePassword", "Uzv2/8EY.X+9dRe<");

            component.getDefaultHost().attach("/ldapauthenticator", LDAPAuthenticator.class);
            component.getDefaultHost().attach("/test", TestResource.class);

            component.getDefaultHost().attachDefault(new XYAuthorizer());
            component.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

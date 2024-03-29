package de.cae.XYFleet;

import de.cae.XYFleet.authentication.XYAuthorizer;
import org.restlet.*;
import org.restlet.data.Protocol;


public class Server {

    public static void initServer() {
        try {
            Component component = new Component();
            org.restlet.Server server = component.getServers().add(Protocol.HTTP, 8080);

            //component.getDefaultHost().attach("/ldapauthenticator", LDAPAuthenticator.class);
            component.getDefaultHost().attach("/test", TestResource.class);

            component.getDefaultHost().attachDefault(new XYAuthorizer());

            component.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

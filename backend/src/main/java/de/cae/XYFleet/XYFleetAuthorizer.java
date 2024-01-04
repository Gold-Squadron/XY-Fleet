package de.cae.XYFleet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.RoleAuthorizer;

public class XYFleetAuthorizer extends Application {
    @Override
    public Restlet createInboundRoot() {
        //Create the authenticator, the authorizer and the router that will be protected
        ChallengeAuthenticator authenticator = createAuthenticator();
        RoleAuthorizer authorizer = createRoleAuthorizer();
        Router router = createRouter();

        a
        return super.createInboundRoot();
    }

    private Router createRouter() {
    }

    private RoleAuthorizer createRoleAuthorizer() {
    }

    private ChallengeAuthenticator createAuthenticator() {
    }

}

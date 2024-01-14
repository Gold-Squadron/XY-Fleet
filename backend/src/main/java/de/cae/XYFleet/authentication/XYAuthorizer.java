package de.cae.XYFleet.authentication;

import de.cae.XYFleet.ressource.User;
import de.cae.XYFleet.test;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.routing.Router;
import org.restlet.security.*;
import de.cae.XYFleet.ressource.*;
public class XYAuthorizer extends Application {

    //Define role names
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_USER = "user";
    public static final String ROLE_SECURITY = "security";

    @Override
    public Restlet createInboundRoot() {
        //Create the authenticator, the authorizer and the router that will be protected
        ChallengeAuthenticator authenticator = createAuthenticator();

        Router baseRouter = createBaseRouter();
        //Protect the resource by enforcing authentication then authorization
        authenticator.setNext(baseRouter);


        return authenticator;
    }

    private Router createBaseRouter() {
        Router router = new Router(getContext());
        router.attach("/booking/{bookingIdentifier}", Booking.class);
        router.attach("/test2", test.class);
        router.attach("/xywing", Vehicle.class);
        router.attach("/user/{bookingIdentifier}", User.class);
        router.attach("/driver", Driver.class);
        router.attach("/booking", Bookings.class);
        return router;
    }

    private ChallengeAuthenticator createAuthenticator() {
        ChallengeAuthenticator guard = new ChallengeAuthenticator(
                getContext(),false,  ChallengeScheme.HTTP_BASIC, "realm");

        //Attach enroler and verifier to determine roles
        LDAPVerifier ldapVerifier = new LDAPVerifier();
        guard.setVerifier(ldapVerifier);
        guard.setEnroler(ldapVerifier);

        return guard;
    }

}

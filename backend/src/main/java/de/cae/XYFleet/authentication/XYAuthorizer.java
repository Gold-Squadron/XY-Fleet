package de.cae.XYFleet.authentication;

import de.cae.XYFleet.ressource.UserResource;
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
        router.attach("/booking/{identifier}", BookingResource.class);
        router.attach("/booking", BookingsResource.class);
        router.attach("/user/{identifier}", UserResource.class);
        router.attach("/user", UsersResource.class);
        router.attach("/xywing/{identifier}", VehicleResource.class);
        router.attach("/test2", test.class);
        router.attach("/xywing", VehiclesResource.class);
        router.attach("/user/{bookingIdentifier}", UserResource.class);
        router.attach("/insurance/{identifier}", InsuranceResource.class);
        router.attach("/insurance", InsurancesResource.class);
        router.attach("/pricing/{identifier}", PricingResource.class);
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

package de.cae.XYFleet.authentication;

import de.cae.XYFleet.ressource.Entry.*;
import de.cae.XYFleet.ressource.Table.*;
import de.cae.XYFleet.ressource.Table.FuelCardsResource;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.ext.crypto.DigestAuthenticator;
import de.cae.XYFleet.ressource.*;
public class XYAuthorizer extends Application {

    //Define role names
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_USER = "user";
    public static final String ROLE_SECURITY = "security";

    @Override
    public Restlet createInboundRoot() {
        //Create the authenticator, the authorizer and the router that will be protected
        DigestAuthenticator authenticator = createAuthenticator();
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
        router.attach("/xywing", VehiclesResource.class);
        router.attach("/user/{bookingIdentifier}", UserResource.class);
        router.attach("/insurance/{identifier}", InsuranceResource.class);
        router.attach("/insurance", InsurancesResource.class);
        router.attach("/pricing/{identifier}", PricingResource.class);
        router.attach("/pricing", PricingsResource.class);
        router.attach("/maintenance/{identifier}", MaintenanceResource.class);
        router.attach("/maintenance", MaintenancesResource.class);
        router.attach("/reschedule", RescheduleResource.class);
        router.attach("/filter", FilterResource.class);
        router.attach("/accessGroup/{identifier}", AccessGroupResource.class);
        router.attach("/accessGroup", AccessGroupsResource.class);
        router.attach("/fuelCard/{identifier}", FuelCardResource.class);
        router.attach("/fuelCard", FuelCardsResource.class);
        return router;
    }

    private DigestAuthenticator createAuthenticator() {
        //Attach enroler and verifier to determine roles
        DigestAuthenticator xyAuthenticator = new DigestAuthenticator(
                getContext(),"xyFleetSecurityRealm", "MyServerKey");
        xyAuthenticator.setVerifier(new XYHttpDigestVerifier(xyAuthenticator, new LDAPLocalVerifier(), null));
        //xyAuthenticator.setWrappedVerifier(new LDAPLocalVerifier());
        xyAuthenticator.setEnroler(new XYEnroler());
        return xyAuthenticator;
    }

}

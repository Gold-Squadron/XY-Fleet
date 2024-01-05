package de.cae.XYFleet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Method;
import org.restlet.routing.Router;
import org.restlet.security.*;

public class XYFleetAuthorizer extends Application {

    //Define role names
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_USER = "user";
    public static final String ROLE_SECURITY = "security";

    @Override
    public Restlet createInboundRoot() {
        //Create the authenticator, the authorizer and the router that will be protected
        ChallengeAuthenticator authenticator = createAuthenticator();

        RoleAuthorizer adminAuthorizer = createRoleAuthorizer();
        //RoleAuthorizer securityAuthorizer = createRoleAuthorizer();
        //RoleAuthorizer userAuthorizer = createRoleAuthorizer();


        Router adminRouter = createAdminRouter();
        Router baseRouter = new Router(getContext());
        //Router securityRouter = createSecurityRouter();
        //Router userRouter = createUserRouter();
        //Protect the resource by enforcing authentication then authorization
        adminAuthorizer.setNext(adminRouter);
        authenticator.setNext(baseRouter);

        //Protect only the private resources with authorizer
        //You could use several different authorizers to authorize different roles
        baseRouter.attachDefault(adminAuthorizer);

        return authenticator;
    }

    //private Router createUserRouter() {
    //}

    //private Router createSecurityRouter() {
    //}

    private Router createAdminRouter() {
        Router adminRouter = new Router();
        adminRouter.attach("/xywing", Vehicle.class);
        adminRouter.attach("/user", User.class);
        adminRouter.attach("/driver", Driver.class);
        adminRouter.attach("/booking", Bookings.class);
        adminRouter.attach("", Server.class);
        return adminRouter;
    }

    private Router createRouter() {
        Router router = new Router();
        router.attach("/booking/{bookingIdentifier}", Booking.class);

        return router;
    }

    private RoleAuthorizer createRoleAuthorizer() {
        RoleAuthorizer roleAuthorizer= new RoleAuthorizer();
        return roleAuthorizer();
    }

    private ChallengeAuthenticator createAuthenticator() {
        ChallengeAuthenticator guard = new ChallengeAuthenticator(
                getContext(),false,  ChallengeScheme.HTTP_BASIC, "realm", new LDAPVerifier());
        //Attach enroler to determine roles
        guard.setEnroler(new Enroler());
        return guard;
    }

}

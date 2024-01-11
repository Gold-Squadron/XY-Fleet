
import de.cae.XYFleet.Database;
import org.jooq.DSLContext;
import org.jooq.codegen.XYFleet.tables.Users;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.restlet.security.User;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.tables.Users.USERS;

import org.jooq.codegen.XYFleet.Tables.*;

public class RessourceTest {
    protected static String url = "http://" + "@localhost:8080";
    protected static final int ADMIN_ID = getIndex();
    protected static final int USER_ID = getIndex();
    protected static final int SECURITY_ID = getIndex();

    private static int index = 99;
    public static int getIndex(){
        index++;
        return index;
    }
    protected static Scenario scenario;
    @BeforeAll
    public static void setUp(){
        System.out.println("setUp Ressource Test");
        scenario = new Scenario();
        UsersRecord admin = new UsersRecord(ADMIN_ID, ROLE_ADMIN,ROLE_ADMIN, ROLE_ADMIN, Byte.valueOf("1"));

        UsersRecord user = new UsersRecord(USER_ID, ROLE_USER,ROLE_USER, ROLE_USER, Byte.valueOf("1"));

        UsersRecord security = new UsersRecord(SECURITY_ID, ROLE_SECURITY,ROLE_SECURITY, ROLE_SECURITY, Byte.valueOf("0"));

        scenario.put(admin);

        scenario.put( user);
        scenario.put( security);
    }
    @AfterClass
    public static void cleanUp(){
        scenario.cleanUp();
    }

}

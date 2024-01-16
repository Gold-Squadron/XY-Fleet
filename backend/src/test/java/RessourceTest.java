
import de.cae.XYFleet.Database;
import org.jooq.DSLContext;
import org.jooq.codegen.XYFleet.tables.Users;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.restlet.security.User;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.tables.Users.USERS;

import org.jooq.codegen.XYFleet.Tables.*;

public class RessourceTest {
    protected static String url = "http://" + "@localhost:8080";
    protected static int ADMIN_ID;
    protected static int USER_ID;
    protected static int SECURITY_ID;
    protected static Scenario scenario;
    @BeforeAll
    public static void setUp(){
        try {
            scenario = new Scenario();
            UsersRecord test = new UsersRecord();

            UsersRecord admin = new UsersRecord(0, ROLE_ADMIN, ROLE_ADMIN, ROLE_ADMIN, Byte.valueOf("1"));
            UsersRecord user = new UsersRecord(0, ROLE_USER, ROLE_USER, ROLE_USER, Byte.valueOf("1"));
            UsersRecord security = new UsersRecord(0, ROLE_SECURITY, ROLE_SECURITY, ROLE_SECURITY, Byte.valueOf("0"));
            ADMIN_ID = scenario.add(USERS, admin);
            USER_ID = scenario.add(USERS, user);
            SECURITY_ID = scenario.add(USERS, security);
        }catch(Exception e){
            e.printStackTrace();
            cleanUp();
            throw e;
        }
    }
    @AfterAll
    public static void cleanUp(){
        System.out.println("cleanUp");
        scenario.cleanUp();
    }

}

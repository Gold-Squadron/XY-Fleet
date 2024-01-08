package de.cae.XYFleet;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import static org.jooq.codegen.XYFleet.Tables.USERS;

public class Database {
    //private static DSLContext dslContext;
    private static final String userName = "root";
    private static final String password = "u2SG7FdmzNE4vZU3kVALCQhPYywfBH5X9raxWJ6T";
    private static final String url = "jdbc:mariadb://localhost:3308";

    public static void initDatabase() {
        // Connection is the only JDBC resource that we need
        // PreparedStatement and ResultSet are handled by JOOQ internally
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            DSLContext dslContext = DSL.using(conn, SQLDialect.MARIADB);
            Result<Record> result = dslContext.select().from(USERS).fetch();
            for (Record r : result) {
                Integer id = r.getValue(USERS.ID);
                String isAdmin = r.getValue(USERS.ROLE);
                Byte isUser = r.getValue(USERS.IS_DRIVER);
                System.out.println("ID: " + id + ", isAdmin: " + isAdmin + ", isUser: " + isUser);
            }

        }
        //TODO specify ExceptionHandling
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DSLContext getDSLContext() {
        try {
            Connection conn = DriverManager.getConnection(url, userName, password);
            DSLContext dslContext = DSL.using(conn, SQLDialect.MARIADB);
            return dslContext;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

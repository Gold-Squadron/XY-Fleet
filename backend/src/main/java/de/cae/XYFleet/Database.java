package de.cae.XYFleet;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import static org.jooq.codegen.XYFleet.Tables.USERS;

public class Database {
    //private static DSLContext dslContext;
    private static final String userName = "root";
    private static final String password = "u2SG7FdmzNE4vZU3kVALCQhPYywfBH5X9raxWJ6T";
    private static final String url = "jdbc:mariadb://localhost:3308";

    public static DSLContext getDSLContext() {
        try {
            Connection conn = DriverManager.getConnection(url, userName, password);
            return DSL.using(conn, SQLDialect.MARIADB);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

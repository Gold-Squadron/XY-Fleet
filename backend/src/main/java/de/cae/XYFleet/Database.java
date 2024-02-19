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
    public static DSLContext getDSLContext() {
        return Main.getDSLContext();
    }
}

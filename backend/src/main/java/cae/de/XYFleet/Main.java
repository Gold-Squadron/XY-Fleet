package cae.de.XYFleet;

import org.jooq.DSLContext;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.SQLDialect;
import static org.jooq.codegen.XYFleet.Tables.*;
import org.jooq
        .impl.DSL;
import org.restlet.data.Protocol;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.sql.*;
public class Main {

    public static void main(String[] args) {
        Database.initDatabase();
        Server.initServer();
    }
}
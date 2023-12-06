package org.example;
import org.jooq.DSLContext;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.SQLDialect;
import static org.jooq.SWT.Tables.*;

import org.jooq.impl.DSL;

import static org.jooq.impl.DSL.*;

import java.sql.*;

public class Main {

    public static void main(String[] args) {
        String userName = "root";
        String password = "u2SG7FdmzNE4vZU3kVALCQhPYywfBH5X9raxWJ6T";
        String url = "jdbc:mariadb://localhost:3308";
        System.out.println("Hello world!");
        // Connection is the only JDBC resource that we need
        // PreparedStatement and ResultSet are handled by JOOQ internally
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            //...
            DSLContext create = DSL.using(conn, SQLDialect.MARIADB);
            Result<Record> result = create.select().from(USERS).fetch();

            for (Record r : result){
                Integer id = r.getValue(USERS.ID);
                Byte isAdmin = r.getValue(USERS.IS_ADMIN);
                Byte isUser = r.getValue(USERS.IS_DRIVER);
                System.out.println("ID: " + id + ", isAdmin: " + isAdmin + ", isUser: " + isUser);
            }

        }

        //TODO specify ExceptionHandling
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
package de.cae.XYFleet;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    //private static DSLContext dslContext;
    private static final String userName = "root";
    private static final String password = "u2SG7FdmzNE4vZU3kVALCQhPYywfBH5X9raxWJ6T";
    private static final String url = "jdbc:mariadb://localhost:3308";
    private static DSLContext dslContext;
    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\Lennard Helbig\\JavaVorkurs\\XY-Fleet\\backend\\SSL\\XYFleetServerTruststore.jks");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        System.setProperty("javax.net.ssl.trustStorePassword","Uzv2/8EY.X+9dRe<");
        System.setProperty("javax.net.ssl.keyStore", "C:\\Users\\Lennard Helbig\\JavaVorkurs\\XY-Fleet\\backend\\SSL\\XYFleetServerKeystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword","tyJ;~>\"p:x3h(aVS");

        try {
            Connection conn = DriverManager.getConnection(url, userName, password);
            dslContext =  DSL.using(conn, SQLDialect.MARIADB);
            Server.initServer();
       } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static DSLContext getDSLContext() {
        return dslContext;
    }
}
package de.cae.XYFleet;

public class Main {

    public static void main(String[] args) {
        Database.initDatabase();
        XYRestServer.initServer();
    }
}
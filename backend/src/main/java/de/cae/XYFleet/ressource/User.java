package de.cae.XYFleet.ressource;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.*;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import static org.jooq.codegen.XYFleet.Tables.USERS;

public class User extends XYServerResource {
    private int bookingIdentifier;
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        try {
            bookingIdentifier = Integer.parseInt(getAttribute("bookingIdentifier"));
        } catch (NumberFormatException e) {
            bookingIdentifier = -1;
        }
    }
    @Put()
    public String createUser(){
        Form form = getQuery();
        for (Parameter parameter : form) {
            System.out.print("parameter " + parameter.getName());
            System.out.println("/" + parameter.getValue());
        }
        return "true";

    }
    @Get("txt")
    public String toString(){
        checkInRole(ROLE_SECURITY);
        //SELECT * FROM users where id = {bookingIdentifier}
        Result<Record> result = dslContext.select().from(USERS).where(USERS.ID.eq(bookingIdentifier)).fetch();
        return !result.isEmpty() ? result.get(0).formatJSON(jSONFormat) : null;
        return null;
        }
    @Post
    public String changeUser(){
        return null;
    }

}

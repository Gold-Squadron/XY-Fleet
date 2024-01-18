package de.cae.XYFleet.ressource;

import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.Result;
import org.restlet.resource.*;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.tables.Users.USERS;

public class UsersResource extends XYServerResource{
    @Get
    public String toString(){
        checkInRole(ROLE_ADMIN);

        Result<Record4<Integer, String, String, Byte>> result =  dslContext.select(USERS.ID, USERS.NAME, USERS.ROLE, USERS.IS_DRIVER).from(USERS).fetch();

        return result.formatJSON(jSONFormat);
    }
    @Put
    public String createUser(){
        isInRole(ROLE_ADMIN);
        return new UserResource().createUser();
    }
    @Post
    public String postUser(){
        throw new ResourceException(405);
    }
    @Delete
    public String deleteUser(){
        throw new ResourceException(405);
    }
}

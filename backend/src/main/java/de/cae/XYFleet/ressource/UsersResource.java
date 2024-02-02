package de.cae.XYFleet.ressource;

import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.Result;
import org.restlet.data.Status;
import org.restlet.resource.*;
import org.restlet.data.Reference;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.tables.Users.USERS;

public class UsersResource extends XYServerResource{

    @Override
    @Get
    public String toString(){
        checkInRole(ROLE_ADMIN);
        //SELECT id, name, role, is_driver FROM users
        Result<Record4<Integer, String, String, Byte>> result =  dslContext.select(USERS.ID, USERS.NAME, USERS.ROLE, USERS.IS_DRIVER).from(USERS).fetch();
        return result.formatJSON(jSONFormat);
    }
    @Override
    @Put
    public String createEntity(){
        checkInRole(ROLE_ADMIN);
        return new UserResource().handlePut(getQuery().getValuesMap());
    }
}

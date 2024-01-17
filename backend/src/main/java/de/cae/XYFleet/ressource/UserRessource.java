package de.cae.XYFleet.ressource;

import org.jooq.*;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.*;
import org.restlet.resource.Delete;

import java.util.Arrays;
import java.util.Map;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.USERS;

public class UserRessource extends XYServerResource {
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

    @Put
    public String createUser() throws ResourceException{
        checkInRole(ROLE_ADMIN);

        //get Query entries
        Form form = getQuery();
        Map<String, String> values = form.getValuesMap();

        //Assert input
        String[] expected = {"name", "passwort", "is_driver", "role"};
        //check if all expected values are given
        for(String a : expected) if(values.get(a) == null) throw new ResourceException(400, "column: "+ a +", missing value.");
        //check whether this name is already used by another user or not
        if(dslContext.fetchOne(USERS, USERS.NAME.eq(values.get("name"))) != null) throw new ResourceException(400, "column: name, user with name:" +values.get("name")+ "already exists");

        //INSERT INTO user (name, passwort, role, is_driver) VALUES ({query values})
        UsersRecord user  = new UsersRecord(0, values.get("name"), values.get("passwort"), values.get("role"), Byte.parseByte(values.get("is_driver")));
        user.setId(null);
        user = dslContext.newRecord(USERS, user);
        user.merge();

        return user.formatJSON(jSONFormat);
        //dslContext.insertInto(USERS, USERS.NAME, USERS.PASSWORT, USERS.ROLE, USERS.IS_DRIVER)
        //        .values(values.get("name"), values.get("passwort"), values.get("role"), Byte.parseByte(values.get("is_driver")))
        //        .execute();
    }

    @Get()
    public String toString() throws ResourceException{
        checkInRole(ROLE_ADMIN);
        //SELECT * FROM users where id = {bookingIdentifier}
        UsersRecord result = dslContext.fetchOne(USERS, USERS.ID.eq(bookingIdentifier));

        if (result == null) throw new ResourceException(404, "not found");
        return result.formatJSON(jSONFormat);
    }

    @Post()
    public void changeUser() throws ResourceException {
        checkInRole(ROLE_ADMIN);

        Form form = getQuery();

        UpdateSetFirstStep<UsersRecord> firstStep = dslContext.update(USERS);
        UpdateSetMoreStep<UsersRecord> moreStep = null;

        for (Parameter param : form) {
            moreStep = switch (param.getName()) {
                case "name" -> firstStep.set(USERS.NAME, param.getValue());
                case "passwort" -> firstStep.set(USERS.PASSWORT, param.getValue());
                case "role" -> firstStep.set(USERS.ROLE, param.getValue());
                case "is_driver" -> firstStep.set(USERS.IS_DRIVER, Byte.parseByte(param.getValue()));
                default -> throw new ResourceException(400, "Invalid param in query: " + param.getName(), "Invalid param in query: " + param.getName());
            };
        }

        if (moreStep == null) throw new ResourceException(400, "nothing to do. no params in query given!!!", "nothing to do. no params in query given");

        //UPDATE users SET ({given values}) WHERE id = {bookingIdentifier}
        moreStep.where(USERS.ID.eq(bookingIdentifier)).execute();
    }

    @Delete
    public String deleteUser() throws ResourceException{
        checkInRole(ROLE_ADMIN);

        String result = this.toString();

        if (Integer.parseInt(getClientInfo().getUser().getName()) == bookingIdentifier)
            throw new ResourceException(400, "you cannot delete the account you are logged in with!");

        //DELETE users WHERE id = {bookingIdentifier}
        dslContext.delete(USERS).where(USERS.ID.eq(bookingIdentifier)).execute();

        return result;
    }
}

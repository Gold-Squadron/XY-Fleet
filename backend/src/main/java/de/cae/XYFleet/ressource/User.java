package de.cae.XYFleet.ressource;

import org.jooq.*;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.*;
import org.restlet.resource.Delete;

import java.util.Map;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.tables.Users.USERS;


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

    @Put
    public void createUser() {
        checkInRole(ROLE_ADMIN);

        Form form = getQuery();
        Map<String, String> values = form.getValuesMap();

        for (Parameter parameter : form) {
            System.out.print("parameter " + parameter.getName());
            System.out.println("/" + parameter.getValue());
        }

        //INSERT INTO user (name, passwort, role, is_driver) VALUES ({query values})
        dslContext.insertInto(USERS, USERS.NAME, USERS.PASSWORD, USERS.ROLE, USERS.IS_DRIVER)
                .values(values.get("name"), values.get("passwort"), values.get("role"), Byte.parseByte(values.get("is_driver")))
                .execute();
    }

    @Get("JSOn")
    public String toString() {
        checkInRole(ROLE_ADMIN);
        //SELECT * FROM users where id = {bookingIdentifier}
        UsersRecord result = dslContext.fetchOne(USERS, USERS.ID.eq(bookingIdentifier));

        return result != null ? result.formatJSON(jSONFormat) : null;
    }

    @Post()
    public void changeUser() throws IllegalArgumentException {
        checkInRole(ROLE_ADMIN);

        Form form = getQuery();

        UpdateSetFirstStep<UsersRecord> firstStep = dslContext.update(USERS);
        UpdateSetMoreStep<UsersRecord> moreStep = null;

        for (Parameter param : form) {
            moreStep = switch (param.getName()) {
                case "name" -> firstStep.set(USERS.NAME, param.getValue());
                case "passwort" -> firstStep.set(USERS.PASSWORD, param.getValue());
                case "role" -> firstStep.set(USERS.ROLE, param.getValue());
                case "is_driver" -> firstStep.set(USERS.IS_DRIVER, Byte.parseByte(param.getValue()));
                default -> throw new IllegalArgumentException("Invalid param in firstStep: " + param.getName());
            };
        }

        if (moreStep == null) throw new IllegalArgumentException("nothing to do. no params in query given!!!");

        //UPDATE users SET ({given values}) WHERE id = {bookingIdentifier}
        moreStep.where(USERS.ID.eq(bookingIdentifier)).execute();
    }

    @Delete
    public String deleteUser() throws IllegalArgumentException{
        checkInRole(ROLE_ADMIN);

        String result = this.toString();

        if (Integer.parseInt(getClientInfo().getUser().getName()) == bookingIdentifier)
            throw new IllegalArgumentException("you cannot delete the account you are logged in with!!");

        //DELETE users WHERE id = {bookingIdentifier}
        dslContext.delete(USERS).where(USERS.ID.eq(bookingIdentifier)).execute();

        return result;
    }
}

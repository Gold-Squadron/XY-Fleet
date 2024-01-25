package de.cae.XYFleet.ressource;

import org.jooq.*;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.*;
import org.restlet.resource.Delete;

import java.util.Map;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.USERS;

public class UserResource extends EntryResource {

    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return handlePut(getQuery().getValuesMap());
    }
    public String handlePut(Map<String, String> values) throws ResourceException{

        //Assert input
        String[] expected = {"name", "password", "is_driver", "role"};
        //check if all expected values are given
        for (String a : expected)
            if (values.get(a) == null) throw new ResourceException(400, "column: " + a + ", missing value.");
        //check whether this name is already used by another user or not
        if (dslContext.fetchOne(USERS, USERS.NAME.eq(values.get("name"))) != null)
            throw new ResourceException(400, "column: name, user with name:" + values.get("name") + "already exists");

        //INSERT INTO user (name, passwort, role, is_driver) VALUES ({query values})
        UsersRecord user  = new UsersRecord(0, values.get("name"), values.get("password"), values.get("role"), Byte.parseByte(values.get("is_driver")));
        user.setId(null);
        user = dslContext.newRecord(USERS, user);
        user.merge();

        return user.formatJSON(jSONFormat);
    }
    @Override
    @Get()
    public String toString() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        //SELECT * FROM users where id = {Identifier}
        UsersRecord result = dslContext.fetchOne(USERS, USERS.ID.eq(identifier));

        if (result == null) throw new ResourceException(404, "not found");
        return result.formatJSON(jSONFormat);
    }
    @Override
    @Post()
    public String editEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);

        Form form = getQuery();

        UpdateSetFirstStep<UsersRecord> firstStep = dslContext.update(USERS);
        UpdateSetMoreStep<UsersRecord> moreStep = null;

        for (Parameter param : form) {
            if (param.getValue() == null)
                throw new ResourceException(400, "no value for given param: " + param.getName());
            moreStep = switch (param.getName()) {
                case "name" -> firstStep.set(USERS.NAME, param.getValue());
                case "password" -> firstStep.set(USERS.PASSWORD, param.getValue());
                case "role" -> firstStep.set(USERS.ROLE, param.getValue());
                case "is_driver" -> firstStep.set(USERS.IS_DRIVER, Byte.parseByte(param.getValue()));
                default ->
                        throw new ResourceException(400, "Invalid param in query: " + param.getName());
            };
        }

        if (moreStep == null)
            throw new ResourceException(400, "nothing to do. no params in query given");

        if (dslContext.fetchOne(USERS, USERS.NAME.eq(form.getValues("name"))) != null)
            throw new ResourceException(400, "column: name, user with name:" + form.getValues("name") + "already exists");


        //UPDATE users SET ({given values}) WHERE id = {Identifier}
        UsersRecord record = moreStep.where(USERS.ID.eq(identifier)).returning().fetchOne();
        if (record == null) throw new ResourceException(500, "internal Server Error");
        return record.formatJSON(jSONFormat);
    }
    @Override
    @Delete
    public String deleteEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);

        String result = this.toString();

        if (Integer.parseInt(getClientInfo().getUser().getIdentifier()) == identifier)
            throw new ResourceException(400, "you cannot delete the account you are logged in with!");

        //DELETE users WHERE id = {Identifier}
        dslContext.delete(USERS).where(USERS.ID.eq(identifier)).execute();

        return result;
    }
}
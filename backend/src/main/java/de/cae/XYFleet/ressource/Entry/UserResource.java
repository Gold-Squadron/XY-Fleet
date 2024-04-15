package de.cae.XYFleet.ressource.Entry;

import de.cae.XYFleet.ressource.EntryResource;
import org.jooq.*;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.resource.*;
import org.restlet.resource.Delete;

import java.util.Map;
import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.USERS;

public class UserResource extends EntryResource {
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = USERS;
    }
    public UserResource(){
        table = USERS;
    }
    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return super.createEntity();
    }

    @Override
    public void validatePutCall(UpdatableRecordImpl record) {
        UsersRecord usersRecord = (UsersRecord) record;

        UsersRecord temp = dslContext.fetchOne(USERS, USERS.NAME.eq(usersRecord.getName()));

        if (temp!=null && !Objects.equals(temp.getId(), usersRecord.getId()))
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "column: name, user with name:" + usersRecord.getName() + "already exists");
    }

    @Override
    @Post
    public String editEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return super.editEntry();
    }

    @Get()
    public String toString() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return super.toString();
    }
    @Override
    @Delete
    public String deleteEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        if (Integer.parseInt(getClientInfo().getUser().getIdentifier()) == identifier)
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "you cannot delete the account you are logged in with!");
        return super.deleteEntry();
    }
}

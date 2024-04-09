package de.cae.XYFleet.ressource.Entry;

import de.cae.XYFleet.ressource.EntryResource;
import org.jooq.codegen.XYFleet.tables.records.AccessGroupsRecord;
import org.jooq.codegen.XYFleet.tables.records.VehiclesRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.data.Status;
import org.restlet.resource.*;

import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static org.jooq.codegen.XYFleet.Tables.*;
import static org.jooq.codegen.XYFleet.Tables.VEHICLES;

public class AccessGroupResource extends EntryResource {
    public AccessGroupResource(){
        table = ACCESS_GROUPS;
    }
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = ACCESS_GROUPS;
    }

    @Override
    public void validatePutCall(UpdatableRecordImpl record) {
        AccessGroupsRecord accessGroupsRecord = (AccessGroupsRecord) record;
        //check USER_ID
        Integer userId = accessGroupsRecord.getUserId();
        if(userId!=null && !dslContext.fetchExists(ACCESS_GROUPS, USERS.ID.eq(userId)))
            throw new ResourceException(org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST, "non existing User id given");

        //check Group name duplicity
        AccessGroupsRecord temp = dslContext.fetchOne(ACCESS_GROUPS, ACCESS_GROUPS.GROUP.eq(accessGroupsRecord.getGroup()));

        if (temp!=null && !Objects.equals(temp.getId(), accessGroupsRecord.getId()))
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "no duplicate groups allowed");
    }
    @Override
    @Delete
    public String deleteEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return super.deleteEntry();
    }

    @Override
    @Post
    public String editEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return super.editEntry();
    }
    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return super.createEntity();
    }
    @Override
    @Get()
    public String toString() {
        checkInRole(ROLE_SECURITY);
        return super.toString();
    }
}

package de.cae.XYFleet.ressource.Entry;

import de.cae.XYFleet.ressource.EntryResource;
import org.jooq.codegen.XYFleet.tables.records.SettingsRecord;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.data.Status;
import org.restlet.resource.*;

import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.*;

public class SettingResource extends EntryResource {
    public SettingResource(){
        table = SETTINGS;
    }

    @Override
    public void validatePutCall(UpdatableRecordImpl record) {
        SettingsRecord settingsRecord = (SettingsRecord) record;
        //check PricingId
        UsersRecord user = dslContext.fetchOne(USERS, USERS.ID.eq(settingsRecord.getUserId()));
        if(user==null)
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "non existing User Id given");

        if(!isInRole(ROLE_ADMIN) && !Objects.equals(getClientInfo().getUser().getIdentifier(), user.getName())){
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "insufficient permission");
        }

        SettingsRecord temp = dslContext.fetchOne(SETTINGS, SETTINGS.KEY.eq(settingsRecord.getKey()));

        if (temp!=null &&  Objects.equals(temp.getUserId(), settingsRecord.getUserId()) && !Objects.equals(temp.getId(), settingsRecord.getId()))
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "column: key, key :" + settingsRecord.getKey() + "already exists for this user");
    }
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = SETTINGS;
    }
    @Override
    @Get
    public String toString() throws ResourceException {
        checkInRole(ROLE_SECURITY);
        return super.toString();
    }
    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(ROLE_SECURITY);
        return super.createEntity();
    }
    @Override
    @Post()
    public String editEntry() {
        checkInRole(ROLE_SECURITY);
        return super.editEntry();
    }
    @Override
    @Delete()
    public String deleteEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return super.deleteEntry();
    }
}

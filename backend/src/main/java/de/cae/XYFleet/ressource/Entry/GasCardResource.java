package de.cae.XYFleet.ressource.Entry;

import de.cae.XYFleet.ressource.EntryResource;
import org.jooq.codegen.XYFleet.tables.records.AccessGroupsRecord;
import org.jooq.codegen.XYFleet.tables.records.GasCardsRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.data.Status;
import org.restlet.resource.*;

import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static org.jooq.codegen.XYFleet.Tables.*;

public class GasCardResource extends EntryResource {
    public GasCardResource(){
        table = GAS_CARDS;
    }
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = GAS_CARDS;
    }

    @Override
    public void validatePutCall(UpdatableRecordImpl record) {
        GasCardsRecord gasCardsRecord = (GasCardsRecord) record;
        //check USER_ID
        if( !dslContext.fetchExists(GAS_CARDS, GAS_CARDS.ID.eq(gasCardsRecord.getVehicleId())))
            throw new ResourceException(org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST, "non existing Vehicle id given");
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

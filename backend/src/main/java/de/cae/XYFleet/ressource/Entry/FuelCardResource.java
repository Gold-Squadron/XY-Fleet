package de.cae.XYFleet.ressource.Entry;

import de.cae.XYFleet.ressource.EntryResource;
import org.jooq.codegen.XYFleet.tables.records.FuelCardRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.data.Status;
import org.restlet.resource.*;

import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static org.jooq.codegen.XYFleet.Tables.*;

public class FuelCardResource extends EntryResource {
    public FuelCardResource() {
        table = FUEL_CARD;
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = FUEL_CARD;
    }

    @Override
    public void validatePutCall(UpdatableRecordImpl record) {
        FuelCardRecord fuelCardRecord = (FuelCardRecord) record;
        //check Group name duplicity
        FuelCardRecord aral = dslContext.fetchOne(FUEL_CARD, FUEL_CARD.ARAL.eq(fuelCardRecord.getAral()));

        if (aral != null && !Objects.equals(aral.getId(), fuelCardRecord.getId()))
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "no duplicate ARAL identifier allowed");

        FuelCardRecord shell = dslContext.fetchOne(FUEL_CARD, FUEL_CARD.SHELL.eq(fuelCardRecord.getShell()));

        if (shell != null && !Objects.equals(shell.getId(), fuelCardRecord.getId()))
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "no duplicate SHELL identifier allowed");
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

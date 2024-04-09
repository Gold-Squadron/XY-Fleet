package de.cae.XYFleet.ressource.Entry;

import de.cae.XYFleet.ressource.EntryResource;
import org.jooq.Field;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.jooq.codegen.XYFleet.tables.records.VehiclesRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.data.Status;
import org.restlet.resource.*;

import java.util.Map;
import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static org.jooq.codegen.XYFleet.Tables.*;

public class VehicleResource extends EntryResource {
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = VEHICLES;
    }

    public VehicleResource() {
        table = VEHICLES;
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
    @Override
    public void validatePutCall(UpdatableRecordImpl record) {
        VehiclesRecord vehiclesRecord = (VehiclesRecord) record;
        //check PricingId
        if(!dslContext.fetchExists(PRICING, PRICING.ID.eq(vehiclesRecord.getPricingId())))
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "non existing Pricing Id given");
        //check InsuranceId
        if(!dslContext.fetchExists(INSURANCES, INSURANCES.ID.eq(vehiclesRecord.getInsuranceId())))
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "non existing Insurance Id given");
        //check FuelCardId
        if(!dslContext.fetchExists(FUEL_CARD, FUEL_CARD.ID.eq(vehiclesRecord.getFuelCardId())))
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "non existing Fuel Card Id given");
        //check FuelCardId
        if(!dslContext.fetchExists(ACCESS_GROUPS, ACCESS_GROUPS.ID.eq(vehiclesRecord.getAccessGroupId())))
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "non existing Access Group Id given");

        VehiclesRecord temp = dslContext.fetchOne(VEHICLES, VEHICLES.LICENSE_PLATE.eq(vehiclesRecord.getLicensePlate()));

        if (temp!=null && !Objects.equals(temp.getId(), vehiclesRecord.getId()))
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "no duplicate license plate allowed");
    }
}

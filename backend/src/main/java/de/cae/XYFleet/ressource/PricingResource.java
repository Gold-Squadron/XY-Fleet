package de.cae.XYFleet.ressource;

import org.jooq.Field;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.codegen.XYFleet.tables.records.PricingRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.data.Status;
import org.restlet.resource.*;

import java.util.Map;
import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.*;

public class PricingResource extends EntryResource {
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = PRICING;
    }

    public PricingResource() {
        table = PRICING;
    }

    @Override
    @Delete
    public String deleteEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return super.deleteEntry();
    }

    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return super.createEntity();
    }

    @Override
    @Get
    public String toString() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return super.toString();
    }

    @Override
    @Post
    public String editEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return super.editEntry();
    }

    @Override
    public void validatePutCall(UpdatableRecordImpl record) {
        PricingRecord pricingRecord = (PricingRecord) record;
        if (pricingRecord.getListPriceGross() < 0)
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "ListPriceGross cannot be negative");
        if(pricingRecord.getLeasingInstallmentNet() <0)
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "LeasingInstallmentNet cannot be negative");
    }
}

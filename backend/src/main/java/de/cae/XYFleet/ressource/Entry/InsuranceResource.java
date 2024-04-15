package de.cae.XYFleet.ressource.Entry;

import de.cae.XYFleet.ressource.EntryResource;
import org.jooq.Field;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.jooq.codegen.XYFleet.tables.records.VehiclesRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.data.Status;
import org.restlet.resource.*;

import java.util.Map;
import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.*;
import static org.jooq.codegen.XYFleet.Tables.USERS;

public class InsuranceResource extends EntryResource {
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = INSURANCES;
    }

    public InsuranceResource() {
        table = INSURANCES;
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
        return handlePut(getQuery().getValuesMap());
    }

    @Override
    public void validatePutCall(UpdatableRecordImpl record) {
        InsurancesRecord insurancesRecord = (InsurancesRecord) record;

        InsurancesRecord temp = dslContext.fetchOne(INSURANCES, INSURANCES.INSURANCE_NUMBER.eq(insurancesRecord.getInsuranceNumber()));

        if (temp!=null && !Objects.equals(temp.getId(), insurancesRecord.getId()))
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "no duplicate Insurancenumber allowed");
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
}

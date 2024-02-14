package de.cae.XYFleet.ressource;

import de.cae.XYFleet.authentication.XYAuthorizer;
import org.jooq.Record;
import org.jooq.Result;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;

public class MaintenancesResource extends XYServerResource{
    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(XYAuthorizer.ROLE_ADMIN);
        return new InsuranceResource().handlePut(getQuery().getValuesMap());
    }

    @Override
    @Get
    public String toString() throws ResourceException {
        checkInRole(XYAuthorizer.ROLE_SECURITY);
        Result<Record> result = dslContext.select().from(BOOKINGS).where(BOOKINGS.DRIVER_ID.isNull()).fetch();
        return result.formatJSON(jSONFormat);
    }
}
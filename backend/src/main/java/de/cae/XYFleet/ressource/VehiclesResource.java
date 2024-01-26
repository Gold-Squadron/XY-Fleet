package de.cae.XYFleet.ressource;

import de.cae.XYFleet.authentication.XYAuthorizer;
import org.jooq.Record;
import org.jooq.Result;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static org.jooq.codegen.XYFleet.Tables.VEHICLES;

public class VehiclesResource extends XYServerResource{
    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return new VehicleResource().handlePut(getQuery().getValuesMap());
    }
    @Override
    @Get
    public String toString() throws ResourceException {
        isInRole(ROLE_SECURITY);
        //SELECT * FROM vehicles
        Result<Record> result = dslContext.select().from(VEHICLES).fetch();
        return result.formatJSON(jSONFormat);
    }
}

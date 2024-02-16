package de.cae.XYFleet.ressource.Table;

import de.cae.XYFleet.ressource.Entry.VehicleResource;
import de.cae.XYFleet.ressource.XYServerResource;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static org.jooq.codegen.XYFleet.Tables.VEHICLES;

public class VehiclesResource extends XYServerResource {
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = VEHICLES;
    }
    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return new VehicleResource().handlePut(getQuery().getValuesMap());
    }
    @Override
    @Get
    public String toString() throws ResourceException {
        checkInRole(ROLE_SECURITY);
        return super.toString();
    }
}

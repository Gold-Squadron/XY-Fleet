package de.cae.XYFleet.ressource.Table;

import de.cae.XYFleet.authentication.XYAuthorizer;
import de.cae.XYFleet.ressource.Entry.AccessGroupResource;
import de.cae.XYFleet.ressource.Entry.BookingResource;
import de.cae.XYFleet.ressource.XYServerResource;
import org.jooq.Record4;
import org.jooq.Result;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static org.jooq.codegen.XYFleet.Tables.ACCESS_GROUPS;
import static org.jooq.codegen.XYFleet.Tables.PRICING;

public class AccessGroupsResource extends XYServerResource {
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = ACCESS_GROUPS;
    }

    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(XYAuthorizer.ROLE_ADMIN);
        return new AccessGroupResource().handlePut(getQuery().getValuesMap());
    }
    @Override
    @Get
    public String toString(){
        checkInRole(ROLE_SECURITY);
        return super.toString();
    }
}

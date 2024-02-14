package de.cae.XYFleet.ressource;

import de.cae.XYFleet.authentication.XYAuthorizer;
import org.jooq.Record;
import org.jooq.Result;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import static org.jooq.codegen.XYFleet.Tables.INSURANCES;
import static org.jooq.codegen.XYFleet.Tables.PRICING;

public class PricingsResource extends XYServerResource {
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = PRICING;
    }

    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(XYAuthorizer.ROLE_ADMIN);
        return new PricingResource().handlePut(getQuery().getValuesMap());
    }

    @Override
    @Get
    public String toString() throws ResourceException {
        checkInRole(XYAuthorizer.ROLE_SECURITY);
        return super.toString();
    }
}

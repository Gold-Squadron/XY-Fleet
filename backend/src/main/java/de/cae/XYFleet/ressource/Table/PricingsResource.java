package de.cae.XYFleet.ressource.Table;

import de.cae.XYFleet.authentication.XYAuthorizer;
import de.cae.XYFleet.ressource.Entry.PricingResource;
import de.cae.XYFleet.ressource.XYServerResource;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

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

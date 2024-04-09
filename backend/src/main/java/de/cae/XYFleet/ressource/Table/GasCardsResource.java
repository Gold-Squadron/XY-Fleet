package de.cae.XYFleet.ressource.Table;

import de.cae.XYFleet.authentication.XYAuthorizer;
import de.cae.XYFleet.ressource.Entry.GasCardResource;
import de.cae.XYFleet.ressource.XYServerResource;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static org.jooq.codegen.XYFleet.Tables.GAS_CARDS;

public class GasCardsResource extends XYServerResource {
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = GAS_CARDS;
    }

    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(XYAuthorizer.ROLE_ADMIN);
        return new GasCardResource().handlePut(getQuery().getValuesMap());
    }
    @Override
    @Get
    public String toString(){
        checkInRole(ROLE_SECURITY);
        return super.toString();
    }
}

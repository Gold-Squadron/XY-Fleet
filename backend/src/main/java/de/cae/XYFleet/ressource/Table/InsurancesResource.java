package de.cae.XYFleet.ressource.Table;

import de.cae.XYFleet.authentication.XYAuthorizer;
import de.cae.XYFleet.ressource.Entry.InsuranceResource;
import de.cae.XYFleet.ressource.XYServerResource;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import static org.jooq.codegen.XYFleet.Tables.INSURANCES;

public class InsurancesResource extends XYServerResource {
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = INSURANCES;
    }

    @Get
    public String toString() {
        checkInRole(XYAuthorizer.ROLE_SECURITY);
        return super.toString();
    }
    @Put
    public String createEntity(){
        checkInRole(XYAuthorizer.ROLE_ADMIN);
        return new InsuranceResource().handlePut(getQuery().getValuesMap());
    }
}

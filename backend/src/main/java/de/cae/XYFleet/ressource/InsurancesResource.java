package de.cae.XYFleet.ressource;

import de.cae.XYFleet.authentication.XYAuthorizer;
import org.jooq.Record;
import org.jooq.Result;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import static org.jooq.codegen.XYFleet.Tables.INSURANCES;

public class InsurancesResource extends XYServerResource{
    @Get
    public String toString() {
        checkInRole(XYAuthorizer.ROLE_SECURITY);
        Result<Record> result = dslContext.select().from(INSURANCES).fetch();
        return result.formatJSON(jSONFormat);
    }
    @Put
    public String createEntity(){
        checkInRole(XYAuthorizer.ROLE_ADMIN);
        return new InsuranceResource().handlePut(getQuery().getValuesMap());
    }
}

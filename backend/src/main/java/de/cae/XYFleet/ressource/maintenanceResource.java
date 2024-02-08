package de.cae.XYFleet.ressource;

import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.restlet.resource.ResourceException;

import java.util.Map;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.INSURANCES;

public class maintenanceResource extends EntryResource{
    @Override
    public String deleteEntry() throws ResourceException {
        return null;
    }

    @Override
    public String createEntity() throws ResourceException {
        return null;
    }

    @Override
    public String toString() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        //SELECT * FROM insurances where id = {identifier}
        InsurancesRecord result = dslContext.fetchOne(INSURANCES, INSURANCES.ID.eq(identifier));

        if (result == null) throw new ResourceException(404, "not found");
        return result.formatJSON(jSONFormat);
    }

    @Override
    public String editEntry() throws ResourceException {
        return null;
    }

    @Override
    public String handlePut(Map<String, String> values) throws ResourceException {
        return null;
    }
}

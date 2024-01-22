package de.cae.XYFleet.ressource;

import org.jooq.Field;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.codegen.XYFleet.tables.Vehicles;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.jooq.codegen.XYFleet.tables.records.VehiclesRecord;
import org.jooq.impl.DSL;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static org.jooq.codegen.XYFleet.Tables.USERS;
import static org.jooq.codegen.XYFleet.Tables.VEHICLES;

public class VehicleResource extends EntryResource {

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        try {
            identifier = Integer.parseInt(getAttribute("Identifier"));
        } catch (NumberFormatException e) {
            identifier = -1;
        }
    }

    @Override
    public String deleteEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return null;
    }

    @Override
    public String editEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);

        java.util.Map<String, String> valuesMap = getQuery().getValuesMap();

        Field<?>[] fields = VEHICLES.fields();

        UpdateSetFirstStep<VehiclesRecord> firstStep = dslContext.update(VEHICLES);
        UpdateSetMoreStep<VehiclesRecord> moreStep = null;

        for (Field<?> field : fields) {
            String value = valuesMap.get(field.getName());
            if (value != null){
                Field<String> myField = DSL.field(field.getName(), String.class);
                moreStep = firstStep.set(myField, value);
            }
        }
        if (moreStep == null)
            throw new ResourceException(400, "nothing to do. no params in query given");

        //UPDATE vehicles SET ({given values}) WHERE id = {Identifier}
        VehiclesRecord record = moreStep.where(VEHICLES.ID.eq(identifier)).returning().fetchOne();

        if (record == null) throw new ResourceException(500, "internal Server Error");

        return record.formatJSON(jSONFormat);
    }

    @Override
    public String createEntity() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        Field<?>[] fields = VEHICLES.fields();

        java.util.Map<String, String> valueMap = getQuery().getValuesMap();

        //check if all expected values are given

        VehiclesRecord user  = dslContext.newRecord(VEHICLES);
        //INSERT INTO user  VALUES ({query values})
        for(Field<?> field : fields) {
            String value = valueMap.get(field.toString());
            if (value == null) throw new ResourceException(405, "Missing value for initialization.");
            Field<String> myField = DSL.field(field.getName(), String.class);
            user.set(myField, value);
        }

        user.setId(null);

        user.merge();

        //CREATE vehicles VALUES ({given values})
        return user.formatJSON(jSONFormat);
    }

    @Override
    @Get("txt")
    public String toString() {
        checkInRole(ROLE_SECURITY);

        //SELECT * FROM vehicles WHERE id=={identifier}
        VehiclesRecord result = dslContext.fetchOne(VEHICLES, VEHICLES.ID.eq(identifier));

        if (result == null) throw new ResourceException(404, "not found");

        return result.formatJSON(jSONFormat);
    }

}

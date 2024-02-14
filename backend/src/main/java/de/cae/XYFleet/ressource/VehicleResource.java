package de.cae.XYFleet.ressource;

import org.jooq.Field;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.codegen.XYFleet.tables.records.VehiclesRecord;
import org.jooq.impl.DSL;
import org.restlet.data.Status;
import org.restlet.resource.*;

import java.util.Map;
import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static org.jooq.codegen.XYFleet.Tables.*;

public class VehicleResource extends EntryResource {
    @Override
    @Delete
    public String deleteEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);

        String result = this.toString();

        //DELETE vehicles where id = {Identifier}
        dslContext.delete(VEHICLES).where(VEHICLES.ID.eq(identifier)).execute();
        return result;
    }

    @Override
    @Post
    public String editEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);

        java.util.Map<String, String> valuesMap = getQuery().getValuesMap();

        Field<?>[] fields = VEHICLES.fields();

        UpdateSetFirstStep<VehiclesRecord> firstStep = dslContext.update(VEHICLES);
        UpdateSetMoreStep<VehiclesRecord> moreStep = null;

        for (Field<?> field : fields) {
            String value = valuesMap.get(field.getUnqualifiedName().first());
            if (value != null) {
                Field<String> myField = DSL.field(field.getName(), String.class);
                moreStep = firstStep.set(myField, value);
            }
        }
        //check if valid call
        if (moreStep == null)
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "nothing to do. no params in query given");
        //check insurance_id
        String insurance_id = VEHICLES.INSURANCE_ID.getUnqualifiedName().first();
        if(valuesMap.containsKey(insurance_id)
                && dslContext.fetchOne(INSURANCES, INSURANCES.ID.eq(Integer.parseInt(valuesMap.get(insurance_id))))==null)
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "non existing Insurance_id given");
        //check pricing_id
        String pricing_id = VEHICLES.PRICING_ID.getUnqualifiedName().first();
        if(valuesMap.containsKey(pricing_id)
                && dslContext.fetchOne(PRICING, PRICING.ID.eq(Integer.parseInt(valuesMap.get(pricing_id))))==null)
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "non existing Insurance_id given");

        //UPDATE vehicles SET ({given values}) WHERE id = {Identifier}
        VehiclesRecord record = moreStep.where(VEHICLES.ID.eq(identifier)).returning().fetchOne();

        if (record == null) throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "internal Server Error");

        return record.formatJSON(jSONFormat);
    }

    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return handlePut(getQuery().getValuesMap());
    }

    @Override
    @Get()
    public String toString() {
        checkInRole(ROLE_SECURITY);

        //SELECT * FROM vehicles WHERE id=={identifier}
        VehiclesRecord result = dslContext.fetchOne(VEHICLES, VEHICLES.ID.eq(identifier));

        if (result == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "not found");

        return result.formatJSON(jSONFormat);
    }

    public String handlePut(Map<String, String> valuesMap) {
        Field<?>[] fields = VEHICLES.fields();

        //check if all expected values are given

        VehiclesRecord vehicle = dslContext.newRecord(VEHICLES);
        //INSERT INTO vehicles  VALUES ({query values})
        for (Field<?> field : fields) {
            String value = valuesMap.get(field.getUnqualifiedName().first());
            if (!Objects.equals(field.getUnqualifiedName().first(), "id")) {
                if (value == null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing value for initialization.");
                Field<String> myField = DSL.field(field.getName(), String.class);
                vehicle.set(myField, value);
            }
        }
        vehicle.setId(null);
        //check correctness of values
        //check PricingId
        String pricing_id = VEHICLES.PRICING_ID.getUnqualifiedName().first();
        if(dslContext.fetchOne(PRICING, PRICING.ID.eq(Integer.parseInt(valuesMap.get(pricing_id))))==null)
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "nob existing Pricing Id given");
        //check InsuranceId
        String insurance_id = VEHICLES.INSURANCE_ID.getUnqualifiedName().first();
        if(dslContext.fetchOne(INSURANCES, INSURANCES.ID.eq(Integer.parseInt(valuesMap.get(insurance_id))))==null)
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "non existing Insurance Id given");

        vehicle.merge();

        //CREATE vehicles VALUES ({given values})
        return vehicle.formatJSON(jSONFormat);
    }
}

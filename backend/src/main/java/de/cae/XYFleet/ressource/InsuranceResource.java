package de.cae.XYFleet.ressource;

import org.jooq.Field;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.jooq.codegen.XYFleet.tables.records.VehiclesRecord;
import org.jooq.impl.DSL;
import org.restlet.resource.*;

import java.util.Map;
import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.*;
import static org.jooq.codegen.XYFleet.Tables.USERS;

public class InsuranceResource extends EntryResource {
    @Override
    @Delete
    public String deleteEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);

        String result = this.toString();

        //DELETE insurance where id = {Identifier}
        dslContext.delete(INSURANCES).where(INSURANCES.ID.eq(identifier)).execute();
        return result;
    }

    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return handlePut(getQuery().getValuesMap());
    }

    public String handlePut(Map<String, String> valuesMap) throws ResourceException {
        Field<?>[] fields = INSURANCES.fields();

        //check if all expected values are given

        InsurancesRecord insurance = dslContext.newRecord(INSURANCES);
        //INSERT INTO insurances  VALUES ({query values})
        for (Field<?> field : fields) {
            String value = valuesMap.get(field.getUnqualifiedName().first());
            if (!Objects.equals(field.getUnqualifiedName().first(), "id")) {
                if (value == null) throw new ResourceException(400, "Missing value for initialization.");
                Field<String> myField = DSL.field(field.getName(), String.class);
                insurance.set(myField, value);
            }
        }
        insurance.setId(null);
        //check correctness of values
        if (dslContext.fetchOne(INSURANCES, INSURANCES.INSURANCE_NUMBER.eq(Integer.parseInt(valuesMap.get(INSURANCES.INSURANCE_NUMBER.getUnqualifiedName().first())))) != null)
            throw new ResourceException(400, "no dublicate insurancenumber allowed");

        insurance.merge();

        //CREATE vehicles VALUES ({given values})
        return insurance.formatJSON(jSONFormat);
    }

    @Override
    @Get
    public String toString() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        //SELECT * FROM insurances where id = {identifier}
        InsurancesRecord result = dslContext.fetchOne(INSURANCES, INSURANCES.ID.eq(identifier));

        if (result == null) throw new ResourceException(404, "not found");
        return result.formatJSON(jSONFormat);
    }

    @Override
    @Post
    public String editEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);


        Map<String, String> valuesMap = getQuery().getValuesMap();

        Field<?>[] fields = INSURANCES.fields();
        //build up query for database
        UpdateSetFirstStep<InsurancesRecord> firstStep = dslContext.update(INSURANCES);
        UpdateSetMoreStep<InsurancesRecord> moreStep = null;

        for (Field<?> field : fields) {
            String value = valuesMap.get(field.getUnqualifiedName().first());
            if (value != null) {
                Field<String> myField = DSL.field(field.getName(), String.class);
                moreStep = firstStep.set(myField, value);
            }
        }
        //check if valid call
        if (moreStep == null)
            throw new ResourceException(400, "nothing to do. no params in query given");
        //check correctness of values
        if (dslContext.fetchOne(INSURANCES, INSURANCES.INSURANCE_NUMBER.eq(Integer.parseInt(valuesMap.get(INSURANCES.INSURANCE_NUMBER.getUnqualifiedName().first())))) != null)
            throw new ResourceException(400, "no dublicate insurancenumber allowed");

        //UPDATE insurances SET ({given values}) WHERE id = {Identifier}
        InsurancesRecord record = moreStep.where(INSURANCES.ID.eq(identifier)).returning().fetchOne();

        if (record == null) throw new ResourceException(500, "internal Server Error");

        return record.formatJSON(jSONFormat);
    }
}

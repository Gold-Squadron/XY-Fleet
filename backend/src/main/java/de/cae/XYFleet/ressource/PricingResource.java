package de.cae.XYFleet.ressource;

import org.jooq.Field;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.codegen.XYFleet.tables.records.PricingRecord;
import org.jooq.impl.DSL;
import org.restlet.resource.*;

import java.util.Map;
import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static org.jooq.codegen.XYFleet.Tables.*;

public class PricingResource extends EntryResource {
    @Override
    @Delete
    public String deleteEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);

        String result = this.toString();

        //DELETE insurance where id = {Identifier}
        dslContext.delete(PRICING).where(PRICING.ID.eq(identifier)).execute();
        return result;
    }

    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        return handlePut(getQuery().getValuesMap());
    }

    @Override
    @Get
    public String toString() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        //SELECT * FROM pricing WHERE id = {identifier}
        PricingRecord result = dslContext.fetchOne(PRICING, PRICING.ID.eq(identifier));

        if (result == null) throw new ResourceException(404, "not found");
        return result.formatJSON(jSONFormat);
    }

    @Override
    @Post
    public String editEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);


        Map<String, String> valuesMap = getQuery().getValuesMap();

        Field<?>[] fields = PRICING.fields();
        //build up query for database
        UpdateSetFirstStep<PricingRecord> firstStep = dslContext.update(PRICING);
        UpdateSetMoreStep<PricingRecord> moreStep = null;

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

        //UPDATE pricing SET ({given values}) WHERE id = {Identifier}
        PricingRecord record = moreStep.where(PRICING.ID.eq(identifier)).returning().fetchOne();

        if (record == null) throw new ResourceException(500, "internal Server Error");

        return record.formatJSON(jSONFormat);
    }

    @Override
    public String handlePut(Map<String, String> valuesMap) throws ResourceException {
        Field<?>[] fields = PRICING.fields();

        //check if all expected values are given

        PricingRecord pricing = dslContext.newRecord(PRICING);
        //INSERT INTO pricing  VALUES ({query values})
        for (Field<?> field : fields) {
            String value = valuesMap.get(field.getUnqualifiedName().first());
            if (!Objects.equals(field.getUnqualifiedName().first(), "id")) {
                if (value == null) throw new ResourceException(400, "Missing value for initialization.");
                Field<String> myField = DSL.field(field.getName(), String.class);
                pricing.set(myField, value);
            }
        }
        pricing.setId(null);
        //check correctness of values

        pricing.merge();

        //CREATE pricing VALUES ({given values})
        return pricing.formatJSON(jSONFormat);
    }
}

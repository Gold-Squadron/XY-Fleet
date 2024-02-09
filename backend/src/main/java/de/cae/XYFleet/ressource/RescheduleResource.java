package de.cae.XYFleet.ressource;

import org.jooq.Result;
import org.restlet.resource.Delete;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import org.jooq.codegen.XYFleet.tables.records.BookingsRecord;

import java.util.Arrays;
import java.util.Map;

public class RescheduleResource extends EntryResource{

    @Override
    @Delete
    public String deleteEntry() throws ResourceException {
        throw new ResourceException(405);
    }

    @Override
    @Put
    public String createEntity() throws ResourceException {
        throw new ResourceException(405);
    }

    @Override
    public String toString() throws ResourceException {
        Map<String, String> valuesMap = getQuery().getValuesMap();
        if(valuesMap.get("id").isBlank())
            throw new ResourceException(400, "no bookings to evaluate given! need \"id\" in query");

        Integer[] ids = Arrays.stream(valuesMap.get("id").split(",")).map(Integer::valueOf).toArray(Integer[]::new);
        Result<BookingsRecord> records = dslContext.fetch(BOOKINGS, BOOKINGS.ID.in(ids));


        return null;
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

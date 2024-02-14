package de.cae.XYFleet.ressource;

import org.jooq.Result;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.data.Status;
import org.restlet.resource.*;

import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import org.jooq.codegen.XYFleet.tables.records.BookingsRecord;

import java.util.Arrays;
import java.util.Map;

public class RescheduleResource extends EntryResource{

    @Override
    @Delete
    public String deleteEntry() throws ResourceException {
        throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
    }

    @Override
    @Put
    public String createEntity() throws ResourceException {
        throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
    }

    @Override
    @Get
    public String toString() throws ResourceException {
        Map<String, String> valuesMap = getQuery().getValuesMap();
        if(valuesMap.get("id").isBlank())
            throw new ResourceException(400, "no bookings to evaluate given! need \"id\" in query");

        Integer[] ids = Arrays.stream(valuesMap.get("id").split(",")).map(Integer::valueOf).toArray(Integer[]::new);
        Result<BookingsRecord> records = dslContext.fetch(BOOKINGS, BOOKINGS.ID.in(ids));
        FilterResource filterResource = new FilterResource();

        Result<BookingsRecord> result = filterResource.filterBooking(records);

        return result.formatJSON(jSONFormat);
    }

    @Override
    @Post
    public String editEntry() throws ResourceException {
        return null;
    }

    @Override
    public String handlePut(Map<String, String> valuesMap) throws ResourceException {
        return null;
    }

    @Override
    public boolean isNotRequiredNull(String name) {
        return false;
    }

    @Override
    public void validatePutCall(UpdatableRecordImpl record) {

    }
}

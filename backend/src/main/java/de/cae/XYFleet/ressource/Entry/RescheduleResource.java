package de.cae.XYFleet.ressource.Entry;


import de.cae.XYFleet.ressource.Entry.BookingResource;

import de.cae.XYFleet.ressource.EntryResource;
import de.cae.XYFleet.ressource.FilterResource;
import org.jooq.Field;
import org.jooq.Result;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.data.Status;
import org.restlet.resource.*;

import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;

import org.jooq.codegen.XYFleet.tables.records.BookingsRecord;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RescheduleResource extends EntryResource {

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
        if (valuesMap.get("id").isBlank())
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
        BookingResource bookingResource = new BookingResource();
        Map<String, String> valuesMap = getQuery().getValuesMap();
        if (valuesMap.isEmpty())
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "nothing to do. No Query given");
        Field<?>[] fields = BOOKINGS.fields();
        HashMap<String, String[]> evaluatedInput = new HashMap<>();
        for (Field<?> field : fields) {
            String fieldName = field.getUnqualifiedName().first();
            String value;
            value = valuesMap.get(fieldName);
            if (!Objects.equals(fieldName, "status")) {
                if (value == null)
                    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing value for initialization: " + fieldName);
                evaluatedInput.put(fieldName, value.split(","));
            }
        }
        for (int i = 0; i < evaluatedInput.get("id").length; i++) {
            BookingsRecord bookingsRecord = dslContext.newRecord(BOOKINGS);
            for (Field<?> field : fields) {
                if (field != BOOKINGS.STATUS)
                    setFieldValueHelper(bookingsRecord, field, evaluatedInput.get(field.getUnqualifiedName().first())[i]);
            }
            if (!dslContext.fetchExists(BOOKINGS, BOOKINGS.ID.eq(bookingsRecord.getId())))
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "given id: " + bookingsRecord.getId() + ", does not exist. perhaps it got deleted or never existed in the first place");
            //check one last time for validity
            try {
                bookingResource.validatePutCall(bookingsRecord);
            } catch (ResourceException e) {
                throw new ResourceException(418, "There were probably changes to the Database during evaluation, so that the reschedule proposal is outdated/cannot be performed anymore");
            }
        }
        return "ok";
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

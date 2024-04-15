package de.cae.XYFleet.ressource.Entry;


import de.cae.XYFleet.ressource.EntryResource;
import de.cae.XYFleet.ressource.FilterResource;
import org.jooq.Field;
import org.jooq.Result;
import org.jooq.codegen.XYFleet.tables.records.VehiclesRecord;
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

    /**
     * calculates reschedule suggestions for each bookings.
     *
     * @return returns JSON package containing reschedule suggestions for each booking in a list.
     * @throws ResourceException
     */
    @Override
    @Get
    public String toString() throws ResourceException {
        Map<String, String> valuesMap = getQuery().getValuesMap();
        if (valuesMap.get("id").isBlank())
            throw new ResourceException(400, "no bookings to evaluate given! need \"id\" in query");

        Integer[] ids = Arrays.stream(valuesMap.get("id").split(",")).map(Integer::valueOf).toArray(Integer[]::new);
        Result<BookingsRecord> records = dslContext.fetch(BOOKINGS, BOOKINGS.ID.in(ids));

        Result<BookingsRecord> result = calculateRescheduling(records);

        return result.formatJSON(jSONFormat);
    }

    @Override
    @Post
    public String editEntry() throws ResourceException {
        return null;
    }

    @Override
    public String handlePut(Map<String, String> values) throws ResourceException {
        return null;
    }

    @Override
    public void validatePutCall(UpdatableRecordImpl record) {

    }

    /**
     * calculate possible rescheduling for given bookingsrecords. This Method is designed to handle for one specific vehicle only.
     *
     * @param records bookingRecordList of all records that need to be rescheduled
     * @return calculated rescheduling for given bookingsrecords
     */
    public Result<BookingsRecord> calculateRescheduling(Result<BookingsRecord> records) {
        Result<BookingsRecord> tempBookingsTable = dslContext.fetch(BOOKINGS);
        FilterResource filterResource = new FilterResource();
        for (BookingsRecord record : records) {
            Result<VehiclesRecord> result = filterResource.filterVehicle(tempBookingsTable, record.getLeasingStart(), record.getLeasingEnd());

            int i = result.get(0).getId();
            tempBookingsTable.remove(record);
            record.setVehicleId(i);
            //add expected rescheduled Booking to temporary table, so it gets considered for next calculation
            tempBookingsTable.add(record);
        }
        return records;
    }
}

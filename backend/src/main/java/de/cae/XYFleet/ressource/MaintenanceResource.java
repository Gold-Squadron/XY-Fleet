package de.cae.XYFleet.ressource;

import org.jooq.Field;
import org.jooq.Result;
import org.jooq.codegen.XYFleet.tables.records.BookingsRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.resource.*;

import java.util.Map;
import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_ADMIN;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_USER;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;

public class MaintenanceResource extends EntryResource {
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = BOOKINGS;
    }

    @Override
    @Delete
    public String deleteEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);

        String result = this.toString();

        //DELETE insurance where id = {Identifier}
        dslContext.delete(BOOKINGS).where(BOOKINGS.ID.eq(identifier)).execute();
        return result;
    }

    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(ROLE_USER);
        return handlePut(getQuery().getValuesMap());
    }

    @Override
    @Get
    public String toString() throws ResourceException {
        checkInRole(ROLE_ADMIN);
        //SELECT * FROM bookings where id = {identifier}
        BookingsRecord result = dslContext.fetchOne(BOOKINGS, BOOKINGS.ID.eq(identifier));

        if (result == null) throw new ResourceException(404, "not found");
        if (result.getStatus() == null)
            throw new ResourceException(400, "Requested Id is supposedly maintenance entry, got booking entry instead");

        return result.formatJSON(jSONFormat);
    }

    @Override
    @Post
    public String editEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);


        Map<String, String> valuesMap = getQuery().getValuesMap();
        valuesMap.remove("id");

        Field<?>[] fields = BOOKINGS.fields();
        //build up query for database
        BookingsRecord booking = dslContext.fetchOne(BOOKINGS, BOOKINGS.ID.eq(identifier));

        if (booking == null) throw new ResourceException(404, "given Maintenance Id is missing");

        for (Field<?> field : fields) {
            //checking whether amendments have been requested or not. Setting all types to String type safty in tests
            String name = field.getUnqualifiedName().first();
            String value = valuesMap.get(field.getUnqualifiedName().first());
            if(value != null){
                setFieldValueHelper(booking, field, value);
            }
        }
        //check if valid call
        if (!booking.changed())
            throw new ResourceException(400, "nothing to do. no params in query given");
        //check correctness of values
        validateCall(booking);

        //UPDATE insurances SET ({given values}) WHERE id = {Identifier}
        booking.update();


        return booking.formatJSON(jSONFormat);
    }

    @Override
    public String handlePut(Map<String, String> valuesMap) throws ResourceException {
        Field<?>[] fields = BOOKINGS.fields();
        //check if all expected values are given
        BookingsRecord booking = dslContext.newRecord(BOOKINGS);
        //INSERT INTO booking  VALUES ({query values})
        for (Field<?> field : fields) {
            String name = field.getUnqualifiedName().first();
            String value = valuesMap.get(name);
            if (!Objects.equals(name, "id") && !Objects.equals(name, "driver_id")) {
                if (value == null) throw new ResourceException(400, "Missing value for initialization: "+name);
                setFieldValueHelper(booking, field, value);
            }
        }
        booking.setId(null);
        //check correctness of values
        validateCall(booking);
        booking.merge();
        //CREATE vehicles VALUES ({given values})
        return booking.formatJSON(jSONFormat);
    }

    public void validateCall(UpdatableRecordImpl record) {
        BookingsRecord booking = (BookingsRecord) record;
        if (booking.getDriverId() != null)
            throw new ResourceException(400, "maintenance entries do not have a driver");
        //return all conflicting bookings
        Result<BookingsRecord> temp = dslContext.fetch(BOOKINGS, BOOKINGS.LEASING_START.lessOrEqual(
                booking.getLeasingEnd()).
                and(BOOKINGS.LEASING_END.greaterOrEqual(booking.getLeasingStart())).
                and(BOOKINGS.VEHICLE_ID.eq(booking.getVehicleId())).
                and(BOOKINGS.ID.notEqual(booking.getId())));
        if (temp.isNotEmpty()) {
            StringBuilder rescheduleMessage = new StringBuilder();
            StringBuilder errorMessage = new StringBuilder();
            for (BookingsRecord bookingsRecord : temp) {
                //maintenance cannot be rescheduled for now
                if (bookingsRecord.getStatus() != null){
                    errorMessage.append(bookingsRecord.getId()).append(", ");
                }else{
                    rescheduleMessage.append(bookingsRecord.getId()).append(", ");
                }
            }
            if(!errorMessage.isEmpty())
                throw new ResourceException(418, "Maintenance can not be rescheduled. Following are conflicting: " + errorMessage);
            //returning all IDs, so that the user can determine how or if he wants to rescedule the conflicting bookings.
            throw new ResourceException(418, rescheduleMessage.toString());
        }

    }
}

package de.cae.XYFleet.ressource.Entry;

import de.cae.XYFleet.ressource.EntryResource;
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
    public MaintenanceResource(){
        table = BOOKINGS;
    }
    @Override
    @Delete
    public String deleteEntry() throws ResourceException {
        checkInRole(ROLE_ADMIN);

        return super.deleteEntry();
    }

    @Override
    @Put
    public String createEntity() throws ResourceException {
        checkInRole(ROLE_USER);
        return super.createEntity();
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
        return super.editEntry();
    }

    @Override
    public boolean isNotRequiredNull(String name) {
        return super.isNotRequiredNull(name) && !Objects.equals(name, "driver_id");
    }

    public void validatePutCall(UpdatableRecordImpl record) {
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

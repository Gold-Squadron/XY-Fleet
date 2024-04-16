package de.cae.XYFleet.ressource.Entry;

import de.cae.XYFleet.ressource.EntryResource;


import org.jooq.Field;
import org.jooq.Result;
import org.jooq.codegen.XYFleet.tables.records.BookingsRecord;
import org.jooq.codegen.XYFleet.tables.records.InsurancesRecord;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.data.Status;
import org.restlet.resource.*;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import static org.jooq.codegen.XYFleet.Tables.INSURANCES;
import static org.jooq.codegen.XYFleet.tables.Users.USERS;
import static org.jooq.codegen.XYFleet.tables.Vehicles.VEHICLES;
import org.restlet.data.Status;

public class BookingResource extends EntryResource {
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = BOOKINGS;
    }
    public BookingResource (){
        table = BOOKINGS;
    }

    @Override
    @Get()
    public String toString() throws ResourceException {
        checkInRole(ROLE_SECURITY);
        return super.toString();
    }

    @Override
    @Delete()
    public String deleteEntry() throws ResourceException {
        checkInRole(ROLE_USER);
        return super.deleteEntry();
    }

    @Override
    @Put()
    public String createEntity() {
        checkInRole(ROLE_USER);
        return super.createEntity();
    }

    @Override
    @Post()
    public String editEntry() {
        checkInRole(ROLE_USER);
        return super.editEntry();
    }

    @Override
    public boolean isNotRequiredNull(String name) {
        return super.isNotRequiredNull(name) && (!Objects.equals(name, "status") && !Objects.equals(name, "driver_id"));
    }

    @Override
    public void validatePutCall(UpdatableRecordImpl record) {
        BookingsRecord  booking = (BookingsRecord) record;
        System.out.println("booking = " + booking);
        if (booking.getStatus() == null && booking.getDriverId() == null)
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "status and driver_id cant both be null");

        if (dslContext.fetchOne(VEHICLES, VEHICLES.ID.equal(booking.getVehicleId()))==null)
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "vehicle id does not exist");
        if (dslContext.fetch(BOOKINGS, BOOKINGS.VEHICLE_ID.eq(booking.getVehicleId())
                .and(BOOKINGS.LEASING_START.lessOrEqual(booking.getLeasingEnd()))
                .and(BOOKINGS.LEASING_END.greaterOrEqual(booking.getLeasingStart()))).isNotEmpty()) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "booking date conflict");
        }
        if (booking.getStatus()==null) {
            //InsurancesRecord temp = dslContext.fetchOne(INSURANCES, INSURANCES.INSURANCE_NUMBER.eq(insurancesRecord.getInsuranceNumber()));
            UsersRecord temp = dslContext.fetchOne(USERS, USERS.ID.eq(booking.getDriverId()));
            if (temp==null || temp.getIsDriver()!=(byte) 1)
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "driver id does not exist");
        } else {
            if (booking.getDriverId()!=null)
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "car can't be driven while in maintenance");
        }
    }

}

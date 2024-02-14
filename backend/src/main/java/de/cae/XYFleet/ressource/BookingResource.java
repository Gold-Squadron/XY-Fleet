package de.cae.XYFleet.ressource;

import org.jooq.Field;
import org.jooq.Result;
import org.jooq.codegen.XYFleet.tables.records.BookingsRecord;
import org.jooq.impl.DSL;
import org.restlet.data.Status;
import org.restlet.resource.*;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import static org.jooq.codegen.XYFleet.tables.Users.USERS;
import static org.jooq.codegen.XYFleet.tables.Vehicles.VEHICLES;

import org.restlet.data.Status;

public class BookingResource extends EntryResource {
    @Override
    @Get("txt")
    public String toString() throws ResourceException {
        checkInRole(ROLE_SECURITY);
        //SELECT * FROM bookings where id = {Identifier}
        BookingsRecord result = dslContext.fetchOne(BOOKINGS, BOOKINGS.ID.eq(identifier));
        if (result == null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "not found");
        return result.formatJSON(jSONFormat);
    }

    @Override
    @Delete()
    public String deleteEntry() throws ResourceException {
        checkInRole(ROLE_USER);

        String result = this.toString();


        if (Integer.parseInt(getClientInfo().getUser().getIdentifier()) != identifier && !isInRole(ROLE_ADMIN))
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
        //DELETE bookings where id = {Identifier}
        dslContext.delete(BOOKINGS).where(BOOKINGS.ID.eq(identifier)).execute();
        return result;
    }

    @Override
    @Put()
    public String createEntity() {
        checkInRole(ROLE_USER);
        return handlePut(getQuery().getValuesMap());
    }

    @Override
    @Post()
    public String editEntry() {
        checkInRole(ROLE_USER);
        return null;
    }

    @Override
    public String handlePut(Map<String, String> valuesMap) throws ResourceException {

        Field<?>[] fields = BOOKINGS.fields();

        //check if all expected values are given

        BookingsRecord booking = dslContext.newRecord(BOOKINGS);
        //INSERT INTO insurances  VALUES ({query values})
        for (Field<?> field : fields) {
            String key = field.getUnqualifiedName().first();
            String value = valuesMap.get(key);
            if (Objects.equals(key, "id")) continue;

            if (value == null && !(key.equals("status") || key.equals("driver_id")))
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, key + " cant be null");

            Field<String> myField = DSL.field(field.getName(), String.class);
            booking.set(myField, value);

        }
        booking.setId(null);

        //check correctness of values

        if (booking.getStatus() == null && booking.getDriverId() == null)
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "status and driver_id cant both be null");

        if (dslContext.fetchExists(VEHICLES, VEHICLES.ID.eq(booking.getVehicleId())))
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "vehicle id does not exist");
        if (dslContext.fetch(BOOKINGS, BOOKINGS.VEHICLE_ID.eq(booking.getVehicleId())
                .and(BOOKINGS.LEASING_START.greaterThan(booking.getLeasingEnd()))
                .and(BOOKINGS.LEASING_END.lessThan(booking.getLeasingStart()))).isNotEmpty()) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "booking date conflict");
        }
        if (valuesMap.get("status").isBlank()) {
            if (dslContext.fetchExists(USERS, USERS.ID.eq(booking.getDriverId()).and(USERS.IS_DRIVER.eq((byte) 1))))
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "driver id does not exist");
        } else {
            if (dslContext.fetchExists(USERS, USERS.ID.isNotNull()))
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "car can't be driven while in maintenance");
        }

        //merge into database
        booking.merge();

        //CREATE vehicles VALUES ({given values})
        return booking.formatJSON(jSONFormat);
    }

}

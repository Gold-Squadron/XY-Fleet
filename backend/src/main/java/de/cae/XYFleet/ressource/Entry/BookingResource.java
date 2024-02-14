package de.cae.XYFleet.ressource.Entry;

import de.cae.XYFleet.ressource.EntryResource;
import org.jooq.codegen.XYFleet.tables.records.BookingsRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.restlet.resource.*;


import java.util.Map;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;

import org.restlet.data.Status;

public class BookingResource extends EntryResource {
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
        return editEntry();
    }

    @Override
    public String handlePut(Map<String, String> valuesMap) throws ResourceException {
        //TODO must be implemented for create Booking
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

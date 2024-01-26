package de.cae.XYFleet.ressource;

import org.jooq.codegen.XYFleet.tables.records.BookingsRecord;
import org.restlet.resource.*;


import java.time.LocalDate;
import java.util.Map;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;

public class BookingResource extends EntryResource {
    @Override
    @Get("txt")
    public String toString() throws ResourceException {
        checkInRole(ROLE_SECURITY);
        //SELECT * FROM bookings where id = {Identifier}
        BookingsRecord result = dslContext.fetchOne(BOOKINGS, BOOKINGS.ID.eq(identifier));

        if (result == null) throw new ResourceException(404, "not found");
        return result.formatJSON(jSONFormat);
    }
    @Override
    @Delete()
    public String deleteEntry() throws ResourceException {
        checkInRole(ROLE_USER);

        String result = this.toString();


        if (Integer.parseInt(getClientInfo().getUser().getIdentifier()) != identifier && !isInRole(ROLE_ADMIN))
            throw new ResourceException(403);
        //DELETE bookings where id = {Identifier}
        dslContext.delete(BOOKINGS).where(BOOKINGS.ID.eq(identifier)).execute();
        return result;
    }
    @Override
    @Put()
    public String createEntity() {
        checkInRole(ROLE_USER);
        return null;
    }
    @Override
    @Post()
    public String editEntry() {
        checkInRole(ROLE_USER);
        return null;
    }

    @Override
    public String handlePut(Map<String, String> values) throws ResourceException {

        //INSERT INTO BOOKINGS VALUES {given values}
        //dslContext.insertInto(BOOKINGS);
        if ((values.get("driver_id"))==null){
            throw new ResourceException(400,"driver_id is null");
        }
        if (Integer.parseInt(values.get("vehicle_id")) ==0){

        }
        if (values.get("reasoning").isBlank()){

        }
        BookingsRecord record = new BookingsRecord(0, Integer.parseInt(values.get("driver_id")), Integer.parseInt(values.get("vehicle_id")), LocalDate.parse(values.get("leasing_start")), LocalDate.parse(values.get("leasing_end")), values.get("reasoning"));
        record.setId(null);
        //dslContext.insertInto(BOOKINGS);
        record = dslContext.newRecord(BOOKINGS, record);
        record.insert();



        return null;
    }

}

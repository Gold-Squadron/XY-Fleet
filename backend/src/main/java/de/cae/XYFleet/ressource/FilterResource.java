package de.cae.XYFleet.ressource;
import de.cae.XYFleet.Database;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.codegen.XYFleet.tables.records.BookingsRecord;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static de.cae.XYFleet.ressource.XYServerResource.jSONFormat;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import static org.jooq.codegen.XYFleet.Tables.VEHICLES;
import static org.jooq.impl.DSL.field;

public class FilterResource extends ServerResource {

    protected DSLContext dslContext = Database.getDSLContext();
    @Get()
    public String filterBooking() throws ResourceException {
        if (!isInRole(ROLE_SECURITY))
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);

        Form form = getQuery();
        if(form.getValues("start")== null || form.getValues("end")==null)
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "no start or end date given");

        LocalDate start;
        LocalDate end;
        try{
            start =  LocalDate.parse(form.getValues("start"));
            end = LocalDate.parse(form.getValues("end"));
        }catch(DateTimeParseException e){
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
        }

        Result<Record> result = filterBooking(start, end);

        return result.formatJSON(jSONFormat);
    }
    public Result<Record> filterBooking(Result<BookingsRecord> tempTable, LocalDate start, LocalDate end){
        Integer[] possibleVehicleIds = tempTable.stream().
                filter(e ->!(e.getLeasingStart().isBefore(end)&&e.getLeasingEnd().isAfter(start)))
                .map(e -> Integer.valueOf(e.getVehicleId())).toArray(Integer[]::new);
        if(possibleVehicleIds.length==0){
            throw new ResourceException(Status.CLIENT_ERROR_CONFLICT);
        }
        //SELECT * FROM vehicles WHERE vehicles.id = subquery ORDERBY (vehicles.annual_performance - (vehicle.expected_mileage + vehicle.mileage))
        Result<Record> result = dslContext.select().from(VEHICLES).where(VEHICLES.ID.in(possibleVehicleIds).isFalse()).
                orderBy((VEHICLES.ANNUAL_PERFORMANCE.subtract(VEHICLES.EXPECTED_MILEAGE.subtract(VEHICLES.MILEAGE))).desc()).fetch();
        return result;
    }

    public Result<Record> filterBooking(LocalDate start, LocalDate end){
        //WITH subquery AS (SELECT * FROM bookings WHERE bookings.leasing_start <= {end} AND bookings.leasing_end >= {start})
        Field<Integer> subquery = field(dslContext.select(BOOKINGS.VEHICLE_ID).from(BOOKINGS).
                where(BOOKINGS.LEASING_START.lessOrEqual(end).and(BOOKINGS.LEASING_END.greaterOrEqual(start))));
        //SELECT * FROM vehicles WHERE vehicles.id = subquery ORDERBY (vehicles.annual_performance - (vehicle.expected_mileage + vehicle.mileage))
        Result<Record> result = dslContext.select().from(VEHICLES).where(VEHICLES.ID.in(subquery).isFalse()).
                orderBy((VEHICLES.ANNUAL_PERFORMANCE.subtract(VEHICLES.EXPECTED_MILEAGE.subtract(VEHICLES.MILEAGE))).desc()).fetch();
        return result;
    }
    public Result<BookingsRecord> filterBooking(Result<BookingsRecord> records){
        Result<BookingsRecord> tempBookingsTable = dslContext.fetch(BOOKINGS);
        for(BookingsRecord record : records){
            int i = (int) filterBooking(tempBookingsTable, record.getLeasingStart(), record.getLeasingEnd()).().get("id");
            tempBookingsTable.remove(record);
            //add expected rescheduled Booking to temporary table, so it gets considered for next calculation
            record.setVehicleId(i);
            tempBookingsTable.add(record);
        }
        return records;
    }
}

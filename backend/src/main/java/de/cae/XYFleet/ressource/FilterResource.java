package de.cae.XYFleet.ressource;

import de.cae.XYFleet.Database;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.codegen.XYFleet.tables.Vehicles;
import org.jooq.codegen.XYFleet.tables.records.BookingsRecord;
import org.jooq.codegen.XYFleet.tables.records.VehiclesRecord;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.stream.Collectors;
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
        if (form.getValues("start") == null || form.getValues("end") == null)
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "no start or end date given");

        LocalDate start;
        LocalDate end;
        try {
            start = LocalDate.parse(form.getValues("start"));
            end = LocalDate.parse(form.getValues("end"));
        } catch (DateTimeParseException e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
        }

        Result<VehiclesRecord> result = filterBooking(start, end);

        return result.formatJSON(jSONFormat);
    }

    public Result<VehiclesRecord> filterBooking(Result<BookingsRecord> tempTable, LocalDate start, LocalDate end) {
        Set<Integer> possibleVehicleIds = tempTable.stream().
                filter(e -> (e.getLeasingStart().isBefore(end.plusDays(1)) && e.getLeasingEnd().isAfter(start.minusDays(1))))
                .map(BookingsRecord::getVehicleId).collect(Collectors.toSet());
        if (possibleVehicleIds.size() == dslContext.fetchCount(VEHICLES)) {
            throw new ResourceException(Status.CLIENT_ERROR_CONFLICT);
        }
        //SELECT * FROM vehicles WHERE vehicles.id = subquery ORDERBY (vehicles.annual_performance - (vehicle.expected_mileage + vehicle.mileage))
        Result<VehiclesRecord> result = dslContext.select().from(VEHICLES).where(VEHICLES.ID.in(possibleVehicleIds).isFalse()).
                orderBy((VEHICLES.ANNUAL_PERFORMANCE.subtract(VEHICLES.EXPECTED_MILEAGE.subtract(VEHICLES.MILEAGE))).desc()).fetchInto(VEHICLES);
        return result;
    }

    public Result<VehiclesRecord> filterBooking(LocalDate start, LocalDate end) {
        return filterBooking(dslContext.fetch(BOOKINGS), start, end);
    }

    public Result<BookingsRecord> filterBooking(Result<BookingsRecord> records) {
        Result<BookingsRecord> tempBookingsTable = dslContext.fetch(BOOKINGS);
        for (BookingsRecord record : records) {
            Result<VehiclesRecord> result = filterBooking(tempBookingsTable, record.getLeasingStart(), record.getLeasingEnd());

            int i = result.get(0).getId();
            tempBookingsTable.remove(record);
            //add expected rescheduled Booking to temporary table, so it gets considered for next calculation
            record.setVehicleId(i);
            tempBookingsTable.add(record);
        }
        return records;
    }
}

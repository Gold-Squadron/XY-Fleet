package de.cae.XYFleet.ressource;

import de.cae.XYFleet.Main;
import org.jooq.*;
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

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static de.cae.XYFleet.ressource.XYServerResource.jSONFormat;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import static org.jooq.codegen.XYFleet.Tables.VEHICLES;

public class FilterResource extends ServerResource {

    protected DSLContext dslContext = Main.getDSLContext();

    /***
     * filters all bookingsrecords that are not within given start and end date
     * @return JSON package with all bookings within given start and end date
     * @throws ResourceException
     */
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

        Result<VehiclesRecord> result = filterVehicle(start, end);

        return result.formatJSON(jSONFormat);
    }

    /**
     * filters all vehicles that cannot be booked for start to end date.
     * @param tempTable the List of all bookings
     * @param start start date
     * @param end end date
     * @return list of all vehicles that can be booked for start to end date.
     */
    public Result<VehiclesRecord> filterVehicle(Result<BookingsRecord> tempTable, LocalDate start, LocalDate end) {
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

    /**
     * filters all vehicles that ca
     * @param start
     * @param end
     * @return
     */
    public Result<VehiclesRecord> filterVehicle(LocalDate start, LocalDate end) {
        return filterVehicle(dslContext.fetch(BOOKINGS), start, end);
    }


}

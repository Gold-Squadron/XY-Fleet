package de.cae.XYFleet.ressource;
import de.cae.XYFleet.Database;
import org.jooq.*;
import org.jooq.Record;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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

        //WITH subquery AS (SELECT * FROM bookings WHERE bookings.leasing_start <= {end} AND bookings.leasing_end >= {start})
        Field<Integer> subquery = field(dslContext.select(BOOKINGS.VEHICLE_ID).from(BOOKINGS).
                where(BOOKINGS.LEASING_START.lessOrEqual(end).and(BOOKINGS.LEASING_END.greaterOrEqual(start))));
        //SELECT * FROM vehicles WHERE vehicles.id = subquery ORDERBY (vehicles.annual_performance - (vehicle.expected_mileage + vehicle.mileage))
        Result<Record> result = dslContext.select().from(VEHICLES).where(VEHICLES.ID.eq(subquery)).
                orderBy((VEHICLES.ANNUAL_PERFORMANCE.subtract(VEHICLES.EXPECTED_MILEAGE.subtract(VEHICLES.MILEAGE)))).fetch();

        return result.formatJSON(jSONFormat);
    }
}

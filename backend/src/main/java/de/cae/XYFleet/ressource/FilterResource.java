package de.cae.XYFleet.ressource;
import de.cae.XYFleet.Database;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.codegen.XYFleet.tables.records.VehiclesRecord;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;


import java.time.LocalDate;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static de.cae.XYFleet.ressource.XYServerResource.jSONFormat;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import static org.jooq.codegen.XYFleet.Tables.VEHICLES;
import static org.jooq.impl.DSL.field;

public class FilterResource extends ServerResource {

    protected DSLContext dslContext = Database.getDSLContext();

    @Get("?start")
    public String filterBooking() throws ResourceException {
        if (!isInRole(ROLE_SECURITY)) {
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
        }
        Form form = getQuery();
        LocalDate start =  LocalDate.parse(form.getValues("start"));
        LocalDate end = LocalDate.parse(form.getValues("end"));
        //LocalDate type = LocalDate.parse(form.getValues("type"));

        //WITH subquery AS (SELECT * FROM bookings WHERE bookings.leasing_start <= {end} AND bookings.leasing_end >= {start})
        Field<Integer> subquery = field(dslContext.select(BOOKINGS.VEHICLE_ID).from(BOOKINGS).where(BOOKINGS.LEASING_START.lessOrEqual(end).and(BOOKINGS.LEASING_END.greaterOrEqual(start))));
        //SELECT * FROM vehicles WHERE vehicles.id = subquery
        Result<Record> result = dslContext.select().from(VEHICLES).where(VEHICLES.ID.eq(subquery)).orderBy(VEHICLES.ID.asc()).fetch();
        return result.formatJSON(jSONFormat);
    }
}

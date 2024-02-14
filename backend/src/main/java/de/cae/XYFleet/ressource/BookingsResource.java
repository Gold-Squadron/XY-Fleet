package de.cae.XYFleet.ressource;


import org.jooq.JSONFormat;
import org.jooq.Record;
import de.cae.XYFleet.authentication.XYAuthorizer;
import org.jooq.Result;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import org.restlet.resource.ResourceException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class BookingsResource extends XYServerResource {
    @Get()
    public String toString() {
        checkInRole(XYAuthorizer.ROLE_SECURITY);

        //SELECT * FROM bookings
        Result<Record> result = dslContext.select().from(BOOKINGS).fetch();
        return result.formatJSON(jSONFormat);
    }
    //TODO missing jUnit Tests for this method
    @Get("?")
    public String getFromTo(){
        checkInRole(XYAuthorizer.ROLE_SECURITY);
        Form form = getQuery();

        if(form.getValues("start")== null || form.getValues("end")==null){
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "no start or end date given");
        }

        LocalDate start;
        LocalDate end;

        try{

            start = LocalDate.parse(form.getValues("start"));
            end = LocalDate.parse(form.getValues("end"));
        }catch(DateTimeParseException e){
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
        }

        //SELECT * FROM bookings WHERE bookings.leasing_start >= {start} and bookings.leasing_end <= {end}
        Result<Record> result = dslContext.select().from(BOOKINGS).where(BOOKINGS.LEASING_START.greaterOrEqual(start).and(BOOKINGS.LEASING_END.lessOrEqual(end))).fetch();
        return result.formatJSON(jSONFormat);
    }

    @Put
    public String createEntity(){
        checkInRole(XYAuthorizer.ROLE_ADMIN);
        return null;
    }
}

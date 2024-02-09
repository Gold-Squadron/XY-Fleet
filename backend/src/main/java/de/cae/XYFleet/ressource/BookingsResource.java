package de.cae.XYFleet.ressource;


import org.jooq.JSONFormat;
import org.jooq.Record;
import de.cae.XYFleet.authentication.XYAuthorizer;
import org.jooq.Result;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import static org.jooq.codegen.XYFleet.Tables.BOOKINGS;
import org.restlet.resource.ResourceException;

public class BookingsResource extends XYServerResource {
    @Get("txt")
    public String toString() {
        checkInRole(XYAuthorizer.ROLE_SECURITY);

        //SELECT * FROM bookings
        Result<Record> result = dslContext.select().from(BOOKINGS).fetch();
        return result.formatJSON(jSONFormat);
    }
    @Put
    public String createEntity(){
        checkInRole(XYAuthorizer.ROLE_ADMIN);
        return null;
    }
}

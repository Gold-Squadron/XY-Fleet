package de.cae.XYFleet.ressource;


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
        isInRole(XYAuthorizer.ROLE_SECURITY);
        Result<Record> result = dslContext.select().from(BOOKINGS).fetch();
        return result.formatJSON(jSONFormat);
    }
    @Put
    public String createEntity(){
        isInRole(XYAuthorizer.ROLE_ADMIN);
        return null;
    }
}

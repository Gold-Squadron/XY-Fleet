package de.cae.XYFleet.ressource;

import de.cae.XYFleet.Main;
import org.jooq.*;
import org.jooq.Record;
import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public abstract class XYServerResource extends ServerResource {

    public static final JSONFormat jSONFormat = new JSONFormat().recordFormat(JSONFormat.RecordFormat.OBJECT).header(false);
    protected static DSLContext dslContext = Main.getDSLContext();
    protected static Table<?> table;
    protected void checkInRole(String roleName) {
        if (!isInRole(roleName)) {
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
        }
    }
    abstract  public String createEntity() throws ResourceException;
    @Override
    public String toString() throws ResourceException{
        Result<Record> result = dslContext.select().from(table).fetch();
        return result.formatJSON(jSONFormat);
    }
    @Post
    public String editEntry(){
        throw new ResourceException(405);
    }
    @Delete
    public String deleteEntry(){
        throw new ResourceException(405);
    }
}

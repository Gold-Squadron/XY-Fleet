package de.cae.XYFleet.ressource;

import de.cae.XYFleet.Database;
import org.jooq.DSLContext;
import org.jooq.JSONFormat;
import org.jooq.Record;
import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public abstract class XYServerResource extends ServerResource {

    public static final JSONFormat jSONFormat = new JSONFormat().recordFormat(JSONFormat.RecordFormat.OBJECT);
    protected DSLContext dslContext = Database.getDSLContext();;

    protected void checkInRole(String roleName) {
        if (!isInRole(roleName)) {
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
        }
    }
    abstract  public String createEntity() throws ResourceException;
    abstract public String toString() throws ResourceException;
}

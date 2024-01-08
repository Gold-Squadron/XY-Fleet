package de.cae.XYFleet;

import org.jooq.DSLContext;
import org.jooq.JSONFormat;
import org.jooq.Record;
import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.sql.ResultSet;

import static de.cae.XYFleet.Database.initDatabase;

public abstract class XYServerResource extends ServerResource {

    protected static final JSONFormat jSONFormat = new JSONFormat().recordFormat(JSONFormat.RecordFormat.OBJECT);
    protected DSLContext dslContext;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        dslContext = Database.getDSLContext();
    }

}

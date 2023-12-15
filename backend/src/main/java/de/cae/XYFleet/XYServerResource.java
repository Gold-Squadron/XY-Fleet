package de.cae.XYFleet;

import org.jooq.DSLContext;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public abstract class XYServerResource extends ServerResource {
    protected DSLContext dslContext;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        DSLContext dslContext = Database.getDSLContext();
    }
}

package de.cae.XYFleet.ressource;

import org.restlet.resource.ResourceException;

public  abstract class EntryResource extends XYServerResource{
    protected int identifier;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        try {
            identifier = Integer.parseInt(getAttribute("identifier"));

        } catch (NumberFormatException e) {
            identifier = -1;
        }
    }
    @Override
    abstract public String deleteEntry() throws ResourceException;
    @Override
    abstract public String editEntry() throws ResourceException;
}

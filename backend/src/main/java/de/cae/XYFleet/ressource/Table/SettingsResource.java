package de.cae.XYFleet.ressource.Table;

import de.cae.XYFleet.authentication.XYAuthorizer;
import de.cae.XYFleet.ressource.Entry.SettingResource;
import de.cae.XYFleet.ressource.XYServerResource;
import org.jooq.Result;
import org.restlet.resource.*;
import org.restlet.routing.Redirector;

import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static org.jooq.codegen.XYFleet.Tables.SETTINGS;

/**
 * this class inherits from SettingsResource because otherwise the put handle is not implementable without redirection and a 2 step request
 */
public class SettingsResource extends SettingResource {
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        table = SETTINGS;
    }
    @Get("?user_id")
    public String getAllSettingsOfOneUser(){
        checkInRole(ROLE_SECURITY);
        String userID = getQuery().getValuesMap().get("user_id");
        Result result = dslContext.select().from(SETTINGS).where(SETTINGS.USER_ID.eq(Integer.parseInt(userID))).fetch();
        return result.formatJSON(jSONFormat);
    }

    @Override
    @Get
    public String toString(){
        checkInRole(ROLE_SECURITY);
        return super.toString();
    }
    @Override
    @Post
    public String editEntry(){
        throw new ResourceException(405);
    }
    @Delete
    public String deleteEntry(){
        throw new ResourceException(405);
    }
}

package de.cae.XYFleet.authentication;

import de.cae.XYFleet.Main;
import org.jooq.DSLContext;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.restlet.data.ClientInfo;
import org.restlet.security.Enroler;
import org.restlet.security.Role;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static de.cae.XYFleet.authentication.XYAuthorizer.ROLE_SECURITY;
import static org.jooq.codegen.XYFleet.Tables.USERS;
import static org.restlet.Application.getCurrent;

public class XYEnroler implements Enroler {
    @Override
    public void enrole(ClientInfo clientInfo) {
        if(clientInfo.getUser()==null)
            return;
        //usersRecord.refresh();
        //create RoleList
        DSLContext dslContext = Main.getDSLContext();
        UsersRecord usersRecord = dslContext.fetchOne(USERS, USERS.NAME.eq(clientInfo.getUser().getIdentifier()));
        switch (usersRecord.getRole()) {
            case ROLE_ADMIN:
                clientInfo.getRoles().add(Role.get(getCurrent(), ROLE_ADMIN));
                //fallthrough
            case ROLE_USER:
                clientInfo.getRoles().add(Role.get(getCurrent(), ROLE_USER));
                //fallthrough
            case ROLE_SECURITY:
                clientInfo.getRoles().add(Role.get(getCurrent(), ROLE_SECURITY));
        }

        clientInfo.setUser(new org.restlet.security.User(usersRecord.getId().toString(), ""));
    }
}

package de.cae.XYFleet.authentication;

import de.cae.XYFleet.Database;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.restlet.Request;
import org.restlet.data.ClientInfo;
import org.restlet.resource.ResourceException;
import org.restlet.security.Enroler;
import org.restlet.security.Role;
import org.restlet.security.SecretVerifier;

import java.util.Arrays;

import static de.cae.XYFleet.authentication.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.USERS;
import static org.restlet.Application.getCurrent;

public class LDAPVerifier extends SecretVerifier implements Enroler {
    private UsersRecord record;

    @Override
    public int verify(String identifier, char[] secret)
            throws ResourceException {
        //TODO check whether null is a default value, that should be allowed or not
        //if(identifier == null) throw new ResourceException(400, "username cannot be null");
        //SELECT * FROM USERS WHERE USERS.NAME = identifier

        System.out.println("identifier = " + identifier + ", secret = " + Arrays.toString(secret));

        record = Database.getDSLContext().fetchOne(USERS, USERS.NAME.eq(identifier));

        return (record != null && compare(record.getPassword().toCharArray(), secret))? SecretVerifier.RESULT_VALID : SecretVerifier.RESULT_INVALID;
    }

    @Override
    public void enrole(ClientInfo clientInfo) {
        System.out.println("science");
        //record.refresh();
        //create RoleList
        switch (record.getRole()) {
            case ROLE_ADMIN:
                clientInfo.getRoles().add(Role.get(getCurrent(), ROLE_ADMIN));
                //fallthrough
            case ROLE_USER:
                clientInfo.getRoles().add(Role.get(getCurrent(), ROLE_USER));
                //fallthrough
            case ROLE_SECURITY:
                clientInfo.getRoles().add(Role.get(getCurrent(), ROLE_SECURITY));
        }

        clientInfo.setUser(new org.restlet.security.User(record.getId().toString(), ""));
    }
}

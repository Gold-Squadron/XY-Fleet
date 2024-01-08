package de.cae.XYFleet;

import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.restlet.Request;
import org.restlet.security.Role;
import org.restlet.security.SecretVerifier;

import static de.cae.XYFleet.XYAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.USERS;
import static org.restlet.Application.getCurrent;

public class LDAPVerifier extends SecretVerifier {
    @Override
    public int verify(String identifier, char[] secret)
            throws IllegalArgumentException {


        //SELECT * FROM USERS WHERE USERS.NAME = identifier
        UsersRecord record = Database.getDSLContext().fetchOne(USERS, USERS.NAME.eq(identifier));

        if (record!=null) {
            if (compare(record.getPasswort().toCharArray(),secret)){
                Request request = Request.getCurrent();
                //create RoleList
                switch(record.getRole()){
                    case ROLE_ADMIN:
                        request.getClientInfo().getRoles().add(Role.get(getCurrent(), ROLE_ADMIN));
                        //fallthrough
                    case ROLE_USER:
                        request.getClientInfo().getRoles().add(Role.get(getCurrent(), ROLE_USER));
                        //fallthrough
                    case ROLE_SECURITY:
                        request.getClientInfo().getRoles().add(Role.get(getCurrent(), ROLE_SECURITY));
                }
                org.restlet.security.User user = new org.restlet.security.User(record.getId().toString(), secret);
                request.getClientInfo().setUser(user);
                return SecretVerifier.RESULT_VALID;
            }
        }

        return SecretVerifier.RESULT_INVALID;
    }
}

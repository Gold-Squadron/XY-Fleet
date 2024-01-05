package de.cae.XYFleet;

import org.jooq.Result;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.restlet.Request;
import org.restlet.security.Role;
import org.restlet.security.SecretVerifier;
import org.jooq.Record;

import java.util.ArrayList;
import java.util.List;

import static de.cae.XYFleet.XYFleetAuthorizer.*;
import static org.jooq.codegen.XYFleet.Tables.USERS;
import static org.restlet.Application.getCurrent;

public class LDAPVerifier extends SecretVerifier {
    @Override
    public int verify(String identifier, char[] secret)
            throws IllegalArgumentException {

        //user contains both user hints and roles

        //SELECT * FROM USERS WHERE USERS.NAME = identifier
        UsersRecord record = Database.getDSLContext().fetchOne(USERS, USERS.NAME.eq(identifier));
        if (record!=null) {
            if (compare(record.getPasswort().toCharArray(),secret)){
                Request request = Request.getCurrent();
                //create RoleList
                if(record.getIsAdmin()==1){
                    request.getClientInfo().getRoles().add(Role.get(getCurrent(), ROLE_ADMIN));
                }
                //TODO Add the other roles if this shows success
                //Role.get(getCurrent(), ROLE_SECURITY);
                //Role.get(getCurrent(), ROLE_USER);
                org.restlet.security.User user = new org.restlet.security.User(identifier, secret);
                request.getClientInfo().setUser(user);
                return SecretVerifier.RESULT_VALID;
            }
        }
        return SecretVerifier.RESULT_INVALID;
    }
}

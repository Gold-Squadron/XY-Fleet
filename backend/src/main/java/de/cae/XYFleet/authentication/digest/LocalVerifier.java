package de.cae.XYFleet.authentication.digest;

import de.cae.XYFleet.Main;
import org.jooq.DSLContext;
import org.jooq.codegen.XYFleet.tables.records.UsersRecord;
import org.restlet.resource.ResourceException;

import static org.jooq.codegen.XYFleet.Tables.USERS;

/**
 * searches for the Password of the given User for Password Verification
 */
public class LocalVerifier extends org.restlet.security.LocalVerifier {
    @Override
    public char[] getLocalSecret(String s) {
        DSLContext dslContext = Main.getDSLContext();
        UsersRecord usersRecord = dslContext.fetchOne(USERS, USERS.NAME.eq(s));
       if(usersRecord == null)
           throw new ResourceException(401, "insufficient permission");
        return usersRecord.getPassword().toCharArray();
    }
}

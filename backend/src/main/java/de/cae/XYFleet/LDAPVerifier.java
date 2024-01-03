package de.cae.XYFleet;

import org.restlet.security.LocalVerifier;

public class LDAPVerifier extends LocalVerifier {

    @Override
    public char[] getLocalSecret(String identifier) {
        // Could have a look into a database, LDAP directory, etc.
        if ("lhelbig".equals(identifier)) {
            return "TestPasswort".toCharArray();
        }
        return null;
    }
}

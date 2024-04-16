package de.cae.XYFleet.authentication;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.ext.crypto.DigestAuthenticator;
import org.restlet.security.Enroler;
import org.restlet.security.SecretVerifier;

public class XYAuthenticator extends DigestAuthenticator {

    public XYAuthenticator(Context context, String realm, String serverKey) {
        super(context, realm, serverKey);
    }


    @Override
    protected boolean authenticate(Request request, Response response) {
        int res = getVerifier().verify(request, response);
        getEnroler().enrole(request.getClientInfo());
        return res == SecretVerifier.RESULT_VALID;
    }

}

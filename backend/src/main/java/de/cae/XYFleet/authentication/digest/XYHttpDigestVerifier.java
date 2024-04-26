package de.cae.XYFleet.authentication.digest;

import de.cae.XYFleet.Main;
import de.cae.XYFleet.authentication.Token.XYToken;
import org.jooq.DSLContext;
import org.jooq.codegen.XYFleet.tables.records.TokensRecord;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.CookieSetting;
import org.restlet.ext.crypto.DigestAuthenticator;
import org.restlet.ext.crypto.internal.HttpDigestVerifier;
import org.restlet.security.LocalVerifier;
import org.restlet.security.SecretVerifier;
import org.restlet.security.User;

import static org.jooq.codegen.XYFleet.Tables.TOKENS;

public class XYHttpDigestVerifier extends HttpDigestVerifier {


    public XYHttpDigestVerifier(DigestAuthenticator digestAuthenticator, LocalVerifier wrappedVerifier, String wrappedAlgorithm) {
        super(digestAuthenticator, wrappedVerifier, wrappedAlgorithm);
    }
    @Override
    public int verify(Request request, Response response) {
        ChallengeResponse challengeResponse = request.getChallengeResponse();
        DSLContext dslContext = Main.getDSLContext();
        //STEP ONE: handle TOKEN
        //get cookie "token"
        String token = Request.getCurrent().getCookies().getValues("token");
        //check whether a token is set in COOKIES or not
        if (token != null) {
            //check for existence of token
            TokensRecord tokensRecord = dslContext.fetchOne(TOKENS, TOKENS.TOKEN.eq(token));
            if (tokensRecord != null) {
                //token exists set Username to set Roles for further request handling
                request.getClientInfo().setUser(new User(tokensRecord.getUserName()));
                //valid token automatically gives you permission to access website
                return SecretVerifier.RESULT_VALID;
            }
        }
        //STEP TWO: handle possible login request:
        //check for correct login data
        int res = super.verify(request, response);
        if (SecretVerifier.RESULT_VALID == res){
            //generate token for easier access to the website
            TokensRecord tokensRecord = dslContext.newRecord(TOKENS);
            token = XYToken.generateNewToken();
            tokensRecord.setToken(token);
            tokensRecord.setUserName(challengeResponse.getIdentifier());
            //UPDATE tokens SET token = XYToken.generateNewToken() AND user_id = usersRecord.getId();
            tokensRecord.merge();
            Response.getCurrent().getCookieSettings().add(new CookieSetting(
                    1,
                    "token",
                    token
            ));
        }
        if(challengeResponse!=null) request.getClientInfo().setUser(new User(challengeResponse.getIdentifier(), ""));

        return res;
    }
}

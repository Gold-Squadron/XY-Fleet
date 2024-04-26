package de.cae.XYFleet.authentication.Token;

import java.security.SecureRandom;
import java.util.Base64;

public class XYToken {
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe
    /**
    Generate token for session-based communication between client and server
     */
    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}

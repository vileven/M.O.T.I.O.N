package utils;

import java.security.SecureRandom;
import java.math.BigInteger;

@SuppressWarnings("FieldCanBeLocal")
public final class SessionIdGenerator {

    private final SecureRandom random = new SecureRandom();
    private final int numBits = 130;
    private final int radix = 32;

    public String nextSessionId() {
        return new BigInteger(numBits, random).toString(radix);
    }
}
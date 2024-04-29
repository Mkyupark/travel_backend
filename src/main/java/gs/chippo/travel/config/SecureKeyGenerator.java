package gs.chippo.travel.config;

import java.security.SecureRandom;
import java.util.Base64;

public class SecureKeyGenerator {
    public static String generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[32]; // 256 bits are equal to 32 bytes.
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}

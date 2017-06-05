package ly.generalassemb.de.datafeeds.americanExpress.ingress.util;

/**
 * Created by dashirov on 6/3/17.
 */

import java.security.SecureRandom;
import java.util.UUID;

public class RunID {
    // Maxim: Copied from UUID implementation :)
    private static volatile SecureRandom numberGenerator = null;
    private static final long MSB = 0x8000000000000000L;

    public static String unique() {
        SecureRandom ng = numberGenerator;
        if (ng == null) {
            numberGenerator = ng = new SecureRandom();
        }
        return Long.toHexString(MSB | ng.nextLong()) + Long.toHexString(MSB | ng.nextLong());
    }
}
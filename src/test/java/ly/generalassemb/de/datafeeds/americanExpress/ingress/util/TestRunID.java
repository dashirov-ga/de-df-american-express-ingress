package ly.generalassemb.de.datafeeds.americanExpress.ingress.util;

import org.junit.Test;

import java.util.UUID;

/**
 * Created by dashirov on 6/3/17.
 */
public class TestRunID {
    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
        System.out.println(RunID.unique());

        System.out.println();
        System.out.println(Long.toHexString(0x8000000000000000L | 21));
        System.out.println(Long.toBinaryString(0x8000000000000000L | 21));
        System.out.println(Long.toHexString(Long.MAX_VALUE + 1));
    }

    @Test
    public void testRunID() {
        String runId = RunID.unique();
    }
}

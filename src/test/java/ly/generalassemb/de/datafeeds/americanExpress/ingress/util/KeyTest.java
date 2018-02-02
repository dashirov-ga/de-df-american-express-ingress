package ly.generalassemb.de.datafeeds.americanExpress.ingress.util;

import org.junit.Test;
import org.apache.commons.codec.binary.Base64;

import java.util.Arrays;


/**
 * Created by dashirov on 5/13/17.
 */
public class KeyTest {
    @Test
    public void testBase64ToBytes() {
        String openSSHKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIIEpAIBAAKCAQEAv3QSnDzS1ci6jUtGM5jXUQ/PPj0eo3ouKBOpFiSRl1FMSXCy\n" +
                "dLHVfqikO+AIZr2R4Qd7mJsLPygs0NnEcAUaKb3gs2UJR7EspdQA59ioFFdbpuMx\n" +
                "TxeID4/mY8+7VXwHhkd9l0uf6r8om5jIJYr9C8dwDnvN3rRTIgO89iA5kEBjoKhX\n" +
                "xXowPKiJ6W/T5qzZiJrJH8CDwU0WnsiqzELbYxRoe7AGxINDQK2GMXp390+PpLAm\n" +
                "FGEwG7AU5c4qte8fSLN7RXqGvrztagXeCwIcRLeUxV7XTmvynN+p8Ww6cqg9krKl\n" +
                "tlFVApgDSvUS9B7K/nZ7LDAfdYKrPlrTkozBDQIDAQABAoIBAD98Wf99G9zQZywb\n" +
                "XfgWIrA6sdpNoi9LgKrBejxhe/XVe5qOyF5x6f3KJkvEioSnMAyS0qVUJHKY++N1\n" +
                "kvIifv6AfFaAloczdT64AmTfLz0virDYfa1gfDnM2fUXGWPLcSNR6Y+WAD1zjh+q\n" +
                "lhP89tRFGh9zZl0HJtoMq0hifzQZ0w90MR21QBYnf44DBZWiq2uPN1Qs4z8XTm2P\n" +
                "9S0OizM/kimcSvtrHr8hdGZNct8EbY0Te5k5Kbx7pdG/xmwtDvdg6kba6si1jwme\n" +
                "cU6saF5bkxNAZSdb5pU8KxgNsTjG4lT+VOgJbl8XSKlX3Di3F8rM8WnJaUGNnUgU\n" +
                "COMSSXECgYEA40De4538ZnMkXNPdyAihNhs5XS0+sEunrBZiK1j4mWcsALTbdUv7\n" +
                "3sEeKO+W6IL9kZbBlQhUtu7QN/h6KKcgz/FIOccIXxAHNTkCh+IZpug2m3sht14A\n" +
                "QpUljqeCBjcGHCZL3nGzr3cs1IUzdLccNyYGpolv4uMxPScDq3fcZCsCgYEA16vk\n" +
                "xrt3N60/4UBTGXomM4oXpUyAs7OH8tbTgAD7SFLzsoqVL4NLYk3kzCHffohy0/WD\n" +
                "CDQvfs9rl5kPTkw1atql5morpR5tEHS0zRN+4AHYvY5tRqyadFEDL1vnvSIL1HqT\n" +
                "nR2Gt94wJRsJUMRkcEuKf2lfjwr1CuKBCNPUu6cCgYEArTN67SYzAW5OT4WWaefR\n" +
                "3qf/H3nlZn4yy+C/b2Q/DMPqcJtnC84eqBatwutgK9PyKK0q20hfMJFxFseFa0h9\n" +
                "pJ6zun1sbu+LXtAVkrULcf9X50aAWv4E2rpLxp76ZXw4Zun1NxOXrGr7OvpOTm1k\n" +
                "zs9nkDP9l4Iqu/3pOR2AVsMCgYEArC0ulvlSU8CcHtebf9tkZAD6n5xToyLQha27\n" +
                "a5VdBoAyzXaVYzkOFYzZ+52PLxT/lveJhCMlR7ePzZcEAR4WBxhHGYvpDAkeSOTi\n" +
                "8lV+FJdk0O/DjYh+Rh3mzQgyGWv0xcyVy/imdGONTBIpfbFL/dpQ1yboguncdrQd\n" +
                "D08NpkECgYAKbvpIUCs2yR4ukC6+8P92zxWdG77bx4jqpIIiZvCSyZWt1WyGqEgK\n" +
                "rqTk0mSIq9NN7o2XhCWBQWlMET/tNyt3iWZisVKhVmLwJq2zb38ibwpCLV3JVPCF\n" +
                "kxaLgXTSUyPeezwoqin/lN3swU4LY2rlee1N/8cOodA6nYthsLNRpg==\n" +
                "-----END RSA PRIVATE KEY-----";
        openSSHKey = openSSHKey
                .replace("-----BEGIN RSA PRIVATE KEY-----\n", "")
                .replace("-----END RSA PRIVATE KEY-----", "");
        byte[] privateKey = Base64.decodeBase64(openSSHKey);
        System.out.println(Arrays.toString(privateKey));


        String public_key = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC/dBKcPNLVyLqNS0YzmNdRD88+PR6jei4oE6kWJJGXUUxJcLJ0sdV+qKQ74AhmvZHhB3uYmws/KCzQ2cRwBRopveCzZQlHsSyl1ADn2KgUV1um4zFPF4gPj+Zjz7tVfAeGR32XS5/qvyibmMgliv0Lx3AOe83etFMiA7z2IDmQQGOgqFfFejA8qInpb9PmrNmImskfwIPBTRaeyKrMQttjFGh7sAbEg0NArYYxenf3T4+ksCYUYTAbsBTlziq17x9Is3tFeoa+vO1qBd4LAhxEt5TFXtdOa/Kc36nxbDpyqD2SsqW2UVUCmANK9RL0Hsr+dnssMB91gqs+WtOSjMEN root@8dd297bf7a66";
        public_key = public_key.replaceAll("^ssh-rsa\\p{Blank}", "").replaceAll("\\p{Blank}\\p{all}*$", "");
        System.out.println(public_key);
    }
}

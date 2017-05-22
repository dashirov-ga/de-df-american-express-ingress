package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.regex.Matcher;

/**
 * Created by dashirov on 5/19/17.
 */
public class TestROCDetail {
    private static final String line = "63189795316318979531          2017137B032131120171362017136136009000000187398D000000002995}371704XXXXX9004           78GEWWDW                     201713678GEWWDW                                           000000              8699371704XXXXX9004                                                                                                                                                                                                               ";

    @Test
    public void testParser() throws ParseException {
        Matcher m = ROCDetail.pattern.matcher(line);
        Assert.assertEquals(true, m.matches());

        ROCDetail record = ROCDetail.parse(line);

        Assert.assertNotNull(record);
    }

}

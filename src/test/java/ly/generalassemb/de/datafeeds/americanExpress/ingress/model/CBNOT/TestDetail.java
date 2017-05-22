package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by dashirov on 5/16/17.
 */
public class TestDetail {
    @Test
    public void testParser() {
        String input = "D     1234567890              371713XXXXX3009 1234567890X01X098765432110Y   20171011201710111234567     FRAUD 123456789012345A01-1234567890123.01201A34123456DDFFRRTT                 -1234567890123.01-1234567890123.01234001234021000000000000000CADNIGOR LEE                                                    1 MAIN ST.                                                  OAKLAND,CA                    123450293IGOR                    LEE                 1234567890     IGOR LEE                      IGOR                    LEE                 Note 1                                                                                                                                                                                                                                                                                                                                                                                                                                                      12                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ";
        Assert.assertEquals(2202, input.length());
        Detail record = null;
        try {
            record = Detail.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(record);
        Assert.assertEquals("D", record.getDataFileRecordType());

        try {
            Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2017-10-11"), record.getAdjustmentDate());
            Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2017-10-11"), record.getChargeDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(record.toString());

    }
}

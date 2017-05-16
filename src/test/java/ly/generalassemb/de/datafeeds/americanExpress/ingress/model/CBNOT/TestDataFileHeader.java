package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by dashirov on 5/16/17.
 */
public class TestDataFileHeader {
    @Test
    public void testParser() {
        String input = "HXXFF20170515                                                                                        123456CBNOT20172380224012                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            ";
        DataFileHeader record = null;
        try {
            record = DataFileHeader.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(record);
        Assert.assertEquals("H", record.getDataFileHeaderRecordType());
        Assert.assertEquals(2202, input.length());
        Assert.assertEquals("XX", record.getAmexApplication().getApplicationSytemCode());
        Assert.assertEquals("FF", record.getAmexApplication().getFileTypeCode());
        try {
            Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2017-05-15"), record.getAmexApplication().getFileCreationDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("{\"applicationSytemCode\":\"XX\",\"fileTypeCode\":\"FF\",\"fileCreationDate\":\"2017-05-15\"}", record.getAmexApplication().toString());
        Assert.assertEquals("CBNOT", record.getDataType());
        try {
            Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-08-26 22:40:12"), record.getOriginalFileTransmissionDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("123456", record.getServiceAccessId());

        Assert.assertEquals("{\"dataFileHeaderRecordType\":\"H\",\"amexApplication\":{\"applicationSytemCode\":\"XX\",\"fileTypeCode\":\"FF\",\"fileCreationDate\":\"2017-05-15\"},\"serviceAccessId\":\"123456\",\"dataType\":\"CBNOT\",\"originalFileTransmissionDateTime\":\"2017-08-26 22:40:12.000-0400\"}", record.toString());

    }
}

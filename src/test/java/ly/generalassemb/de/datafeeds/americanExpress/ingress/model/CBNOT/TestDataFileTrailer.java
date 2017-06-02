package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by dashirov on 5/16/17.
 */
public class TestDataFileTrailer {
    @Test
    public void testParser() {
        String input = "T010120170515      0000000000000001000000001                                                         123456CBNOT20172380224012001                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         ";
        Assert.assertEquals(2202, input.length());
        DataFileTrailer record = null;
        try {
            record = DataFileTrailer.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(record);
        Assert.assertEquals("T", record.getDataFileTrailerRecordType());

        Assert.assertEquals("01", record.getAmexApplication().getApplicationSytemCode());
        Assert.assertEquals("01", record.getAmexApplication().getFileTypeCode());
        try {
            Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2017-05-15"), record.getAmexApplication().getFileCreationDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(new Long(1L), record.getAmexApplication().getRecordCount());
        Assert.assertEquals("{\"applicationSytemCode\":\"01\",\"fileTypeCode\":\"01\",\"fileCreationDate\":\"2017-05-15\",\"recordCount\":1}", record.getAmexApplication().toString());
        Assert.assertEquals("CBNOT", record.getDataType());
        try {
            Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-08-26 22:40:12"), record.getOriginalFileTransmissionDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("123456", record.getServiceAccessId());
        Assert.assertEquals("{\"dataFileTrailerRecordType\":\"T\",\"amexApplication\":{\"applicationSytemCode\":\"01\",\"fileTypeCode\":\"01\",\"fileCreationDate\":\"2017-05-15\",\"recordCount\":1},\"serviceAccessId\":\"123456\",\"dataType\":\"CBNOT\",\"originalFileTransmissionDateTime\":\"2017-08-26 22:40:12.000-0400\"}", record.toString());

    }
}

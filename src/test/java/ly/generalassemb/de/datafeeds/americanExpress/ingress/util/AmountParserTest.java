package ly.generalassemb.de.datafeeds.americanExpress.ingress.util;

import com.amazonaws.services.s3.AmazonS3URI;
import org.junit.Test;
import org.junit.Assert;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.TimeZone;

/**
 * Created by davidashirov on 3/12/17.
 */
public class AmountParserTest {

    @Test
    public void testSplitString(){
        Assert.assertEquals(+1231L, AmountParser.toLong("123","A").longValue());
        Assert.assertEquals(+1232L, AmountParser.toLong("123","B").longValue());
        Assert.assertEquals(+1233L, AmountParser.toLong("123","C").longValue());
        Assert.assertEquals(+1234L, AmountParser.toLong("123","D").longValue());
        Assert.assertEquals(+1235L, AmountParser.toLong("123","E").longValue());
        Assert.assertEquals(+1236L, AmountParser.toLong("123","F").longValue());
        Assert.assertEquals(+1237L, AmountParser.toLong("123","G").longValue());
        Assert.assertEquals(+1238L, AmountParser.toLong("123","H").longValue());
        Assert.assertEquals(+1239L, AmountParser.toLong("123","I").longValue());
        Assert.assertEquals(+1230L, AmountParser.toLong("123","{").longValue());
        Assert.assertEquals(-1231L, AmountParser.toLong("123","J").longValue());
        Assert.assertEquals(-1232L, AmountParser.toLong("123","K").longValue());
        Assert.assertEquals(-1233L, AmountParser.toLong("123","L").longValue());
        Assert.assertEquals(-1234L, AmountParser.toLong("123","M").longValue());
        Assert.assertEquals(-1235L, AmountParser.toLong("123","N").longValue());
        Assert.assertEquals(-1236L, AmountParser.toLong("123","O").longValue());
        Assert.assertEquals(-1237L, AmountParser.toLong("123","P").longValue());
        Assert.assertEquals(-1238L, AmountParser.toLong("123","Q").longValue());
        Assert.assertEquals(-1239L, AmountParser.toLong("123","R").longValue());
        Assert.assertEquals(-1230L, AmountParser.toLong("123","}").longValue());

        Assert.assertEquals(+1L, AmountParser.toLong("","A").longValue());
        Assert.assertEquals(+2L, AmountParser.toLong("","B").longValue());
        Assert.assertEquals(+3L, AmountParser.toLong("","C").longValue());
        Assert.assertEquals(+4L, AmountParser.toLong("","D").longValue());
        Assert.assertEquals(+5L, AmountParser.toLong("","E").longValue());
        Assert.assertEquals(+6L, AmountParser.toLong("","F").longValue());
        Assert.assertEquals(+7L, AmountParser.toLong("","G").longValue());
        Assert.assertEquals(+8L, AmountParser.toLong("","H").longValue());
        Assert.assertEquals(+9L, AmountParser.toLong("","I").longValue());
        Assert.assertEquals(+0L, AmountParser.toLong("","{").longValue());
        Assert.assertEquals(-1L, AmountParser.toLong("","J").longValue());
        Assert.assertEquals(-2L, AmountParser.toLong("","K").longValue());
        Assert.assertEquals(-3L, AmountParser.toLong("","L").longValue());
        Assert.assertEquals(-4L, AmountParser.toLong("","M").longValue());
        Assert.assertEquals(-5L, AmountParser.toLong("","N").longValue());
        Assert.assertEquals(-6L, AmountParser.toLong("","O").longValue());
        Assert.assertEquals(-7L, AmountParser.toLong("","P").longValue());
        Assert.assertEquals(-8L, AmountParser.toLong("","Q").longValue());
        Assert.assertEquals(-9L, AmountParser.toLong("","R").longValue());
        Assert.assertEquals(-0L, AmountParser.toLong("","}").longValue());

        Assert.assertEquals(+1L, AmountParser.toLong(null,"A").longValue());
        Assert.assertEquals(+2L, AmountParser.toLong(null,"B").longValue());
        Assert.assertEquals(+3L, AmountParser.toLong(null,"C").longValue());
        Assert.assertEquals(+4L, AmountParser.toLong(null,"D").longValue());
        Assert.assertEquals(+5L, AmountParser.toLong(null,"E").longValue());
        Assert.assertEquals(+6L, AmountParser.toLong(null,"F").longValue());
        Assert.assertEquals(+7L, AmountParser.toLong(null,"G").longValue());
        Assert.assertEquals(+8L, AmountParser.toLong(null,"H").longValue());
        Assert.assertEquals(+9L, AmountParser.toLong(null,"I").longValue());
        Assert.assertEquals(+0L, AmountParser.toLong(null,"{").longValue());
        Assert.assertEquals(-1L, AmountParser.toLong(null,"J").longValue());
        Assert.assertEquals(-2L, AmountParser.toLong(null,"K").longValue());
        Assert.assertEquals(-3L, AmountParser.toLong(null,"L").longValue());
        Assert.assertEquals(-4L, AmountParser.toLong(null,"M").longValue());
        Assert.assertEquals(-5L, AmountParser.toLong(null,"N").longValue());
        Assert.assertEquals(-6L, AmountParser.toLong(null,"O").longValue());
        Assert.assertEquals(-7L, AmountParser.toLong(null,"P").longValue());
        Assert.assertEquals(-8L, AmountParser.toLong(null,"Q").longValue());
        Assert.assertEquals(-9L, AmountParser.toLong(null,"R").longValue());
        Assert.assertEquals(-0L, AmountParser.toLong(null,"}").longValue());
    }

    @Test
    public void testWholeString(){
        Assert.assertEquals(+1231L, AmountParser.toLong("123A").longValue());
        Assert.assertEquals(+1232L, AmountParser.toLong("123B").longValue());
        Assert.assertEquals(+1233L, AmountParser.toLong("123C").longValue());
        Assert.assertEquals(+1234L, AmountParser.toLong("123D").longValue());
        Assert.assertEquals(+1235L, AmountParser.toLong("123E").longValue());
        Assert.assertEquals(+1236L, AmountParser.toLong("123F").longValue());
        Assert.assertEquals(+1237L, AmountParser.toLong("123G").longValue());
        Assert.assertEquals(+1238L, AmountParser.toLong("123H").longValue());
        Assert.assertEquals(+1239L, AmountParser.toLong("123I").longValue());
        Assert.assertEquals(+1230L, AmountParser.toLong("123{").longValue());
        Assert.assertEquals(-1231L, AmountParser.toLong("123J").longValue());
        Assert.assertEquals(-1232L, AmountParser.toLong("123K").longValue());
        Assert.assertEquals(-1233L, AmountParser.toLong("123L").longValue());
        Assert.assertEquals(-1234L, AmountParser.toLong("123M").longValue());
        Assert.assertEquals(-1235L, AmountParser.toLong("123N").longValue());
        Assert.assertEquals(-1236L, AmountParser.toLong("123O").longValue());
        Assert.assertEquals(-1237L, AmountParser.toLong("123P").longValue());
        Assert.assertEquals(-1238L, AmountParser.toLong("123Q").longValue());
        Assert.assertEquals(-1239L, AmountParser.toLong("123R").longValue());
        Assert.assertEquals(-1230L, AmountParser.toLong("123}").longValue());

        Assert.assertEquals(+1L, AmountParser.toLong("A").longValue());
        Assert.assertEquals(+2L, AmountParser.toLong("B").longValue());
        Assert.assertEquals(+3L, AmountParser.toLong("C").longValue());
        Assert.assertEquals(+4L, AmountParser.toLong("D").longValue());
        Assert.assertEquals(+5L, AmountParser.toLong("E").longValue());
        Assert.assertEquals(+6L, AmountParser.toLong("F").longValue());
        Assert.assertEquals(+7L, AmountParser.toLong("G").longValue());
        Assert.assertEquals(+8L, AmountParser.toLong("H").longValue());
        Assert.assertEquals(+9L, AmountParser.toLong("I").longValue());
        Assert.assertEquals(+0L, AmountParser.toLong("{").longValue());
        Assert.assertEquals(-1L, AmountParser.toLong("J").longValue());
        Assert.assertEquals(-2L, AmountParser.toLong("K").longValue());
        Assert.assertEquals(-3L, AmountParser.toLong("L").longValue());
        Assert.assertEquals(-4L, AmountParser.toLong("M").longValue());
        Assert.assertEquals(-5L, AmountParser.toLong("N").longValue());
        Assert.assertEquals(-6L, AmountParser.toLong("O").longValue());
        Assert.assertEquals(-7L, AmountParser.toLong("P").longValue());
        Assert.assertEquals(-8L, AmountParser.toLong("Q").longValue());
        Assert.assertEquals(-9L, AmountParser.toLong("R").longValue());
        Assert.assertEquals(-0L, AmountParser.toLong("}").longValue());
    }

    @Test
    public void testNullReturnValue() {

        Assert.assertNull(AmountParser.toLong("1233"));
        Assert.assertNull(AmountParser.toLong("")); // IndexOutOfBoundsException
        Assert.assertNull(AmountParser.toLong(null));     // NullPointerException

        Assert.assertNull(AmountParser.toLong("123",null));
        Assert.assertNull(AmountParser.toLong("",""));
        Assert.assertNull(AmountParser.toLong(null,null));
    }


}

package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT;

import org.junit.Assert;
import org.junit.Test;


import javax.money.*;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by dashirov on 5/17/17.
 */
public class TestMoney {
    @Test
    public void testMonetaryClasses() {
        MonetaryAmountFormat defaultFormat = MonetaryFormats.getAmountFormat(Locale.US);
        Assert.assertNotNull(defaultFormat);
        Assert.assertEquals("USD12.50", defaultFormat.format(Monetary.getDefaultAmountFactory().setCurrency("USD").setNumber(12.50).create()));
        MonetaryAmount a = Monetary.getDefaultAmountFactory().setCurrency("USD").setNumber(12.50).create();
        Assert.assertNotNull(a);

    }

}

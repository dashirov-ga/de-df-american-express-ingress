package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;
import org.skyscreamer.jsonassert.JSONAssert;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class PricingRecordTest {
    private static final FixedFormatManager fixedFormatManager = new FixedFormatManagerImpl();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String testString1 = "8918979531USD                   110Charge                                                          000330000000000000000{0000E00000000022740F00000000026191{00000000003450M00000000000750N00000000000000{00000000021990A                                                                                                                                                                                                                                ";
    private static final String testString2 = "8918979531USD                   110Conversion fee                                                  000000000000000000007}0000{00000000000000{00000000000000{00000000000000{00000000000000{00000000000159K00000000000159K                                                                                                                                                                                                                                ";
    private static final Logger LOGGER = LoggerFactory.getLogger(PricingRecordTest.class);
    private static PricingRecord pricingRecord1;
    private static PricingRecord pricingRecord2;
    private static boolean setUpIsDone = false;

    @Before
    public void init() {
        if (!setUpIsDone) {
            LOGGER.debug("⟱⟱⟱⟱⟱⟱⟱⟱⟱⟱");
            setUpIsDone = true;
            pricingRecord1 = fixedFormatManager.load(PricingRecord.class, testString1);
            pricingRecord2 = fixedFormatManager.load(PricingRecord.class, testString2);
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            try {
                LOGGER.debug(mapper.writer().writeValueAsString(pricingRecord1));
                LOGGER.debug(mapper.writer().writeValueAsString(pricingRecord2));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            LOGGER.debug("⟰⟰⟰⟰⟰⟰⟰⟰⟰⟰");
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getSettlementSeAccountNumber() {
        assertEquals("8918979531", pricingRecord1.getSettlementSeAccountNumber());
        assertEquals("8918979531", pricingRecord2.getSettlementSeAccountNumber());
    }

    @Test
    public void getSettlementCurrencyCode() {
        assertEquals("USD", pricingRecord1.getSettlementCurrencyCode());
        assertEquals("USD", pricingRecord2.getSettlementCurrencyCode());
    }

    @Test
    public void getRecordCode() {
        assertEquals("1", pricingRecord1.getRecordCode ());
    }

    @Test
    public void getRecordSubCode() {
        assertEquals("10", pricingRecord1.getRecordSubCode());

    }

    @Test
    public void getRecordCodeId() {
        assertEquals("110", pricingRecord2.getRecordCode() + pricingRecord2.getRecordSubCode());

    }


    @Test
    public void getPricingDescription() {
        assertEquals("Charge", pricingRecord1.getPricingDescription());
        assertEquals("Conversion fee", pricingRecord2.getPricingDescription());
    }

    @Test
    public void getDiscountRate() {
        assertEquals(BigDecimal.valueOf(3300).movePointLeft(5), pricingRecord1.getDiscountRate());
    }

    @Test
    public void getFeePerCharge() {
        assertEquals(BigDecimal.valueOf( 0).setScale(2, RoundingMode.HALF_UP),
                pricingRecord1.getFeePerCharge());
        assertEquals(BigDecimal.valueOf( -0.7 ).setScale(2, RoundingMode.HALF_UP),
                pricingRecord2.getFeePerCharge());
    }

    @Test
    public void getNumberOfCharges() {
        assertEquals(BigDecimal.valueOf( 5).setScale(0, RoundingMode.HALF_UP),
                pricingRecord1.getNumberOfCharges());
        assertEquals(BigDecimal.valueOf( 0 ).setScale(0, RoundingMode.HALF_UP),
                pricingRecord2.getNumberOfCharges());
    }

    @Test
    public void getGrossAmount() {
        assertEquals(BigDecimal.valueOf( 2274.06).setScale(2, RoundingMode.HALF_UP),
                pricingRecord1.getGrossAmount());
        assertEquals(BigDecimal.valueOf( 0 ).setScale(2, RoundingMode.HALF_UP),
                pricingRecord2.getGrossAmount());
    }

    @Test
    public void getGrossDebitAmount() {
        assertEquals(BigDecimal.valueOf( 2619.10).setScale(2, RoundingMode.HALF_UP),
                pricingRecord1.getGrossDebitAmount());
        assertEquals(BigDecimal.valueOf( 0 ).setScale(2, RoundingMode.HALF_UP),
                pricingRecord2.getGrossDebitAmount());
    }

    @Test
    public void getGrossCredeitAmount() {
        assertEquals(BigDecimal.valueOf( -345.04 ).setScale(2, RoundingMode.HALF_UP),
                pricingRecord1.getGrossCredeitAmount());
        assertEquals(BigDecimal.valueOf( 0 ).setScale(2, RoundingMode.HALF_UP),
                pricingRecord2.getGrossCredeitAmount());

    }

    @Test
    public void getDiscountFee() {
        assertEquals(BigDecimal.valueOf(-75.05).setScale(2, RoundingMode.HALF_UP),
                pricingRecord1.getDiscountFee());

        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP),
                pricingRecord2.getDiscountFee());

    }

    @Test
    public void getServiceFee() {
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP),
                pricingRecord1.getServiceFee());

        assertEquals(BigDecimal.valueOf(-15.92).setScale(2, RoundingMode.HALF_UP),
                pricingRecord2.getServiceFee());

    }

    @Test
    public void getNetAmount() {
        assertEquals(BigDecimal.valueOf(2199.01).setScale(2, RoundingMode.HALF_UP),
                pricingRecord1.getNetAmount());

        assertEquals(BigDecimal.valueOf(-15.92).setScale(2, RoundingMode.HALF_UP),
                pricingRecord2.getNetAmount());
}

    @Test
    public void toJson() {
    }
}
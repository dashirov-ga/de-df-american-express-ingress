package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class AdjustmentRecordTest {
    private static final FixedFormatManager fixedFormatManager = new FixedFormatManagerImpl();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String testString = "89189795318918979531XXXXXXXXXX2017206A1111230201720699911100000900}00000000{000000{000000{00000900}0987000000000000000000000{00371294XXXXXXXXXCHARGEBACK - CARDMEMBER DISPUTE                                                                                                                                                                                                                                                         560037                      ";
    private static final Logger LOGGER = LoggerFactory.getLogger(AdjustmentRecordTest.class);
    private static AdjustmentRecord adjustmentRecord;
    private static boolean setUpIsDone = false;

    @Before
    public void init() {
        if (!setUpIsDone) {
            LOGGER.debug("⟱⟱⟱⟱⟱⟱⟱⟱⟱⟱");
            setUpIsDone = true;
            adjustmentRecord = fixedFormatManager.load(AdjustmentRecord.class, testString);
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            try {
                LOGGER.debug(mapper.writer().writeValueAsString(adjustmentRecord));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            LOGGER.debug("⟰⟰⟰⟰⟰⟰⟰⟰⟰⟰");
        }
    }

    @After
    public void finalyze() {

    }

    @Test
    public void testRecordJsonStructure() throws JSONException, JsonProcessingException {
        String exactMatchRequired = "{\"AMEX_PAYEE_NUMBER\":8918979531,\"AMEX_SE_NUMBER\":8918979531,\"SE_UNIT_NUMBER\":\"XXXXXXXXXX\",\"PAYMENT_YEAR\":2017,\"PAYMENT_NUMBER\":\"206A1111\",\"RECORD_TYPE\":\"2\",\"DETAIL_RECORD_TYPE\":\"30\",\"AMEX_PROCESS_DATE\":\"2017-07-25\",\"ADJUSTMENT_NUMBER\":999111,\"ADJUSTMENT_AMOUNT\":-90.00,\"DISCOUNT_AMOUNT\":0.00,\"SERVICE_FEE_AMOUNT\":0.00,\"NET_ADJUSTMENT_AMOUNT\":-90.00,\"DISCOUNT_RATE\":0.0987,\"SERVICE_FEE_RATE\":0.0,\"CARDMEMBER_NUMBER\":\"371294XXXXXXXXX\",\"ADJUSTMENT_REASON\":\"CHARGEBACK - CARDMEMBER DISPUTE\"}";
        JSONObject test = new JSONObject(mapper.writeValueAsString(adjustmentRecord));
        JSONAssert.assertEquals(exactMatchRequired, test, true);
    }

    @Test
    public void testRecordType() {
        String amexDetailRecordCode = adjustmentRecord.getRecordType() + adjustmentRecord.getRecordSubType();
        assertEquals("230", amexDetailRecordCode);
    }

    @Test
    public void testAmexPayeeNumber() {
        assertEquals(BigDecimal.valueOf(8918979531L), adjustmentRecord.getPayeeNumber());
    }

    @Test
    public void testAmexServiceEstablishmentNumber() {
        assertEquals(BigDecimal.valueOf(8918979531L), adjustmentRecord.getServiceEstablishmentNumber());
    }




    @Test
    public void testAmexProcessingDate() {
        assertEquals(LocalDate.of(2017, Month.JULY, 25), adjustmentRecord.getProcessingDate());
    }

    @Test
    public void testAdjustmentAmount() {
        assertEquals(new BigDecimal("-90").setScale(2, RoundingMode.HALF_UP), adjustmentRecord.getAdjustmentAmount());
    }

    @Test
    public void testDiscountAmount() {
        assertEquals(new BigDecimal("0").setScale(2, RoundingMode.HALF_UP), adjustmentRecord.getDiscountAmount());
    }

    @Test
    public void testServiceFeeAmount() {
        assertEquals(new BigDecimal("0").setScale(2, RoundingMode.HALF_UP), adjustmentRecord.getServiceFeeAmount());
    }

    @Test
    public void testAdjustmentReason() {
        assertEquals("CHARGEBACK - CARDMEMBER DISPUTE", adjustmentRecord.getAdjustmentReason());
    }

    @Test
    public void testServiceEstablishmentUnitNumber() {
        assertEquals(String.join("", Collections.nCopies(10, "X")), adjustmentRecord.getServiceEstablishmentUnitNumber());
    }

    @Test
    public void testDiscountRate() {
        assertEquals(new BigDecimal("9870").movePointLeft(5).setScale(5, RoundingMode.HALF_UP), adjustmentRecord.getDiscountRate());
    }

    @Test
    public void testPaymentYear() {
        assertEquals(Integer.valueOf(2017), adjustmentRecord.getPaymentYear());
    }

    // TODO: Check JSON/CSV serialization. What are the rules?
    // ➥ keys in documentation must match keys in JSON object?
    //    ➥ not going to work 100% of the time: documentation is convoluted and often misleading
    @Test
    public void testPaymentNumber() {
        assertEquals("206A1111", adjustmentRecord.getPaymentNumber());
        LocalDate paymentDate = LocalDate.ofYearDay(adjustmentRecord.getPaymentYear(), Integer.valueOf(adjustmentRecord.getPaymentNumber().substring(0, 3)));
        assertEquals("A", adjustmentRecord.getPaymentNumber().substring(3, 4));
        assertEquals("1111", adjustmentRecord.getPaymentNumber().substring(4, 8));
    }
}


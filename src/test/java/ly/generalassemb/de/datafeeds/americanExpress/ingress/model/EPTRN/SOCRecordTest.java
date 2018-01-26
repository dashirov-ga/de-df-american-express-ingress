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

public class SOCRecordTest {
    private static final FixedFormatManager fixedFormatManager = new FixedFormatManagerImpl();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String testString = "89189795318918979531XXXXXXXXXX2017170D1111210201716920171690494020000191780{00005542D000018{000000{0000186219F0739000000000000000000000{0000{0000191780{0001B169123123                000001B-000000000055424 000000000000000 000000000000000 00000000 00000000-000000000000180-00015000                                                                                                                                                                          ";
    private static final Logger LOGGER = LoggerFactory.getLogger(SOCRecordTest.class);
    private static SOCRecord socRecord;
    private static boolean setUpIsDone = false;

    @Before
    public void init() {
        if (!setUpIsDone) {
            LOGGER.debug("⟱⟱⟱⟱⟱⟱⟱⟱⟱⟱");
            setUpIsDone = true;
            socRecord = fixedFormatManager.load(SOCRecord.class, testString);
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            try {
                LOGGER.debug(mapper.writer().writeValueAsString(socRecord));
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
        String exactMatchRequired = "{\"AMEX_PAYEE_NUMBER\":8918979531,\"AMEX_SE_NUMBER\":8918979531,\"SE_UNIT_NUMBER\":\"XXXXXXXXXX\",\"PAYMENT_YEAR\":2017,\"PAYMENT_NUMBER\":\"170D1111\",\"RECORD_TYPE\":\"2\",\"DETAIL_RECORD_TYPE\":\"10\",\"SE_BUSINESS_DATE\":\"2017-06-18\",\"AMEX_PROCESS_DATE\":\"2017-06-18\",\"SOC_INVOICE_NUMBER\":49402,\"SOC_AMOUNT\":19178.00,\"DISCOUNT_AMOUNT\":554.24,\"SERVICE_FEE_AMOUNT\":1.80,\"NET_SOC_AMOUNT\":18621.96,\"DISCOUNT_RATE\":0.07390,\"SERVICE_FEE_RATE\":0.00000,\"AMEX_GROSS_AMOUNT\":19178.00,\"AMEX_ROC_COUNT\":12,\"TRACKING_ID\":169123123,\"CPC_INDICATOR\":\"\",\"AMEX_ROC_COUNT_POA\":12}";
        JSONObject test = new JSONObject(mapper.writeValueAsString(socRecord));
        JSONAssert.assertEquals(exactMatchRequired, test, true);
    }

    @Test
    public void testSocRecordType() {
        String amexDetailRecordCode = socRecord.getRecordType() + socRecord.getRecordSubType();
        assertEquals("210", amexDetailRecordCode);
    }

    @Test
    public void testAmexPayeeNumber() {
        assertEquals(BigDecimal.valueOf(8918979531L), socRecord.getPayeeNumber());
    }

    @Test
    public void testAmexServiceEstablishmentNumber() {
        assertEquals(BigDecimal.valueOf(8918979531L), socRecord.getServiceEstablishmentNumber());
    }

    @Test
    public void testProcessDate() {
        assertEquals(LocalDate.of(2017, Month.JUNE, 18), socRecord.getAmexProcessDate());
    }

    @Test
    public void testServiceEstablishmentBusinessDate() {
        assertEquals(LocalDate.of(2017, Month.JUNE, 18), socRecord.getServiceEstablishmentBusinessDate());
    }

    @Test
    public void testServiceEstablishmentUnitNumber() {
        assertEquals(String.join("", Collections.nCopies(10, "X")), socRecord.getServiceEstablishmentUnitNumber());
    }

    @Test
    public void testPaymentYear() {
        assertEquals(Integer.valueOf(2017), socRecord.getPaymentYear());
    }

    // TODO: Check JSON/CSV serialization. What are the rules?
    // ➥ keys in documentation must match keys in JSON object?
    //    ➥ not going to work 100% of the time: documentation is convoluted and often misleading
    @Test
    public void testPaymentNumber() {
        assertEquals("170D1111", socRecord.getPaymentNumber());
        LocalDate paymentDate = LocalDate.ofYearDay(socRecord.getPaymentYear(), Integer.valueOf(socRecord.getPaymentNumber().substring(0, 3)));
        assertEquals(socRecord.getAmexProcessDate().plusDays(1), paymentDate);
        assertEquals("D", socRecord.getPaymentNumber().substring(3, 4));
        assertEquals("1111", socRecord.getPaymentNumber().substring(4, 8));
        assertEquals(Integer.valueOf(1111), socRecord.getPaymentCheckNumber());
    }


    @Test
    public void testSocInvoiceNumber() {
        assertEquals(Integer.valueOf(49402), socRecord.getSocInvoiceNumber());
    }

    @Test
    public void testSocAmount() {
        assertEquals(BigDecimal.valueOf(19178.00).setScale(2, RoundingMode.HALF_UP), socRecord.getSocAmount());
    }

    @Test
    public void testDiscountAmount() {
        assertEquals(BigDecimal.valueOf(554.24).setScale(2, RoundingMode.HALF_UP), socRecord.getDiscountAmount());
    }

    @Test
    public void testServiceFeeAmount() {
        assertEquals(BigDecimal.valueOf(1.80).setScale(2, RoundingMode.HALF_UP), socRecord.getServiceFeeAmount());
    }

    @Test
    public void testNetSocAmount() {
        assertEquals(BigDecimal.valueOf(18621.96).setScale(2, RoundingMode.HALF_UP), socRecord.getNetSocAmount());
        // NetSocAmount is SocAmount less Discount and Service Fees
        assertEquals(socRecord.getNetSocAmount(),
                socRecord.getSocAmount().subtract(
                        socRecord.getDiscountAmount().add(socRecord.getServiceFeeAmount())
                )
        );
    }

    @Test
    public void testGrossSocAmount() {
        assertEquals(BigDecimal.valueOf(19178.00).setScale(2, RoundingMode.HALF_UP), socRecord.getAmexGrossAmount());
    }

    @Test
    public void testTrackingId() {
        assertEquals(Integer.valueOf(169123123), socRecord.getTrackingId());
    }

    @Test
    public void testRocCount() {
        assertEquals(BigDecimal.valueOf(12).setScale(0, RoundingMode.FLOOR), socRecord.getAmexRocCount());
    }

    @Test
    public void testRocCountPointOfAcceptance() {
        assertEquals(BigDecimal.valueOf(12).setScale(0, RoundingMode.FLOOR), socRecord.getAmexRocCountPoa());
    }

    @Test
    public void testServiceFeeRate() {
        assertEquals(BigDecimal.valueOf(0).setScale(5, RoundingMode.HALF_UP), socRecord.getServiceFeeRate());
    }

    @Test
    public void testDiscountRate() {
        assertEquals(BigDecimal.valueOf(7390).movePointLeft(5).setScale(5, RoundingMode.HALF_UP), socRecord.getDiscountRate());
    }
}


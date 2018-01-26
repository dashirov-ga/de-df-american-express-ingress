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

public class ROCRecordTest {
    private static final FixedFormatManager fixedFormatManager = new FixedFormatManagerImpl();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String testString = "89189795318918979531XXXXXXXXXX2017170D111131120171692017169049402000000191780{000000001300{376773XXXXXXXXX           ABCDEGFZ                     2017169ABCDEGFZ                                           000000175870        8699376773XXXXXXXXX                                                                                                                                                                                                               ";
    private static final Logger LOGGER = LoggerFactory.getLogger(ROCRecordTest.class);
    private static ROCRecord rocRecord;
    private static boolean setUpIsDone = false;

    @Before
    public void init() {
        if (!setUpIsDone) {
            LOGGER.debug("⟱⟱⟱⟱⟱⟱⟱⟱⟱⟱");
            setUpIsDone = true;
            rocRecord = fixedFormatManager.load(ROCRecord.class, testString);
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            try {
                LOGGER.debug(mapper.writer().writeValueAsString(rocRecord));
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
        String exactMatchRequired = "{\"TLRR_AMEX_PAYEE_NUMBER\":8918979531,\"TLRR_AMEX_SE_NUMBER\":8918979531,\"TLRR_SE_UNIT_NUMBER\":\"XXXXXXXXXX\",\"TLRR_PAYMENT_YEAR\":2017,\"TLRR_PAYMENT_NUMBER\":\"170D1111\",\"TLRR_RECORD_TYPE\":\"3\",\"TLRR_DETAIL_RECORD_TYPE\":\"11\",\"TLRR_SE_BUSINESS_DATE\":\"2017-06-18\",\"TLRR_AMEX_PROCESS_DATE\":\"2017-06-18\",\"TLRR_SOC_INVOICE_NUMBER\":49402,\"TLRR_SOC_AMOUNT\":19178.00,\"TLRR_ROC_AMOUNT\":130.00,\"TLRR_CM_NUMBER\":\"376773XXXXXXXXX\",\"TLRR_CM_REF_NO\":\"\",\"TLRR_TRAN_DATE\":\"2017-06-18\",\"TLRR_SE_REF_POA\":\"ABCDEGFZ\",\"NON-COMPLIANT_INDICATOR\":\"\",\"NON-COMPLIANT_ERROR_CODE_1\":\"\",\"NON-COMPLIANT_ERROR_CODE_2\":\"\",\"NON-COMPLIANT_ERROR_CODE_3\":\"\",\"NON-COMPLIANT_ERROR_CODE_4\":\"\",\"NON-SWIPED_INDICATOR\":\"\",\"TLRR_CM_NUMBER_EXD\":\"376773XXXXXXXXX\"}";
        JSONObject test = new JSONObject(mapper.writeValueAsString(rocRecord));
        JSONAssert.assertEquals(exactMatchRequired, test, true);
    }

    @Test
    public void testRecordType() {
        String amexDetailRecordCode = rocRecord.getRecordType() + rocRecord.getRecordSubType();
        assertEquals("311", amexDetailRecordCode);
    }

    @Test
    public void testAmexPayeeNumber() {
        assertEquals(BigDecimal.valueOf(8918979531L), rocRecord.getPayeeNumber());
    }

    @Test
    public void testAmexServiceEstablishmentNumber() {
        assertEquals(BigDecimal.valueOf(8918979531L), rocRecord.getServiceEstablishmentNumber());
    }


    @Test
    public void testServiceEstablishmentBusinessDate() {
        assertEquals(LocalDate.of(2017, Month.JUNE, 18), rocRecord.getServiceEstablishmentBusinessDate());
    }

    @Test
    public void testAmexProcessingDate() {
        assertEquals(LocalDate.of(2017, Month.JUNE, 18), rocRecord.getProcessingDate());
    }

    @Test
    public void testTransactionDate() {
        assertEquals(LocalDate.of(2017, Month.JUNE, 18), rocRecord.getTransactionDate());
    }

    @Test
    public void testServiceEstablishmentUnitNumber() {
        assertEquals(String.join("", Collections.nCopies(10, "X")), rocRecord.getServiceEstablishmentUnitNumber());
    }

    @Test
    public void testPaymentYear() {
        assertEquals(Integer.valueOf(2017), rocRecord.getPaymentYear());
    }

    // TODO: Check JSON/CSV serialization. What are the rules?
    // ➥ keys in documentation must match keys in JSON object?
    //    ➥ not going to work 100% of the time: documentation is convoluted and often misleading
    @Test
    public void testPaymentNumber() {
        assertEquals("170D1111", rocRecord.getPaymentNumber());
        LocalDate paymentDate = LocalDate.ofYearDay(rocRecord.getPaymentYear(), Integer.valueOf(rocRecord.getPaymentNumber().substring(0, 3)));
        assertEquals("D", rocRecord.getPaymentNumber().substring(3, 4));
        assertEquals("1111", rocRecord.getPaymentNumber().substring(4, 8));
    }


    @Test
    public void testSocInvoiceNumber() {
        assertEquals(Integer.valueOf(49402), rocRecord.getSocInvoiceNumber());
    }

    @Test
    public void testSocAmount() {
        assertEquals(BigDecimal.valueOf(19178.00).setScale(2, RoundingMode.HALF_UP), rocRecord.getSocAmount());
    }


    @Test
    public void testRocAmount() {
        assertEquals(BigDecimal.valueOf(130).setScale(2, RoundingMode.HALF_UP), rocRecord.getRocAmount());
    }

    @Test
    public void testServiceEstablishmentReference() {
        assertEquals("ABCDEGFZ", rocRecord.getServiceEstablishmentReference());
    }

    @Test
    public void testCardmemberNumber() {
        assertEquals("376773XXXXXXXXX", rocRecord.getCardmemberNumber());
        assertEquals("376773XXXXXXXXX", rocRecord.getCardmemberNumberExtended());
    }
}


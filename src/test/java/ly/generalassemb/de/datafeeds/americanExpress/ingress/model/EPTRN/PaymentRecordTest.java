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

public class PaymentRecordTest {
    private static final FixedFormatManager fixedFormatManager = new FixedFormatManagerImpl();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String testString = "8918979531000000000000000000002017170D111110020171720000196770F00000000{098765431234567                                                                                                                                                                                                                                                                                                                                                                          ";
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentRecordTest.class);
    private static PaymentRecord paymentRecord;
    private static boolean setUpIsDone = false;

    @Before
    public void init() {
        if (!setUpIsDone) {
            LOGGER.debug("⟱⟱⟱⟱⟱⟱⟱⟱⟱⟱");
            setUpIsDone = true;
            paymentRecord = fixedFormatManager.load(PaymentRecord.class, testString);
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            try {
                LOGGER.debug(mapper.writer().writeValueAsString(paymentRecord));
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
        String exactMatchRequired = "{\"AMEX_PAYEE_NUMBER\":8918979531,\"PAYMENT_YEAR\":2017,\"PAYMENT_NUMBER\":\"170D1111\",\"RECORD_TYPE\":\"1\",\"DETAIL_RECORD_TYPE\":\"00\",\"PAYMENT_DATE\":\"2017-06-21\",\"PAYMENT_AMOUNT\":19677.06,\"DEBIT_BALANCE_AMOUNT\":0.00,\"ABA_BANK_NUMBER\":98765431,\"SE_DDA_NUMBER\":\"234567\"}";
        JSONObject test = new JSONObject(mapper.writeValueAsString(paymentRecord));
        JSONAssert.assertEquals(exactMatchRequired, test, true);
    }

    @Test
    public void testRecordType() {
        String amexDetailRecordCode = paymentRecord.getRecordType() + paymentRecord.getRecordSubType();
        assertEquals("100", amexDetailRecordCode);
    }

    @Test
    public void testAmexPayeeNumber() {
        assertEquals(BigDecimal.valueOf(8918979531L), paymentRecord.getPayeeNumber());
    }


    @Test
    public void testPaymentYear() {
        assertEquals(Integer.valueOf(2017), paymentRecord.getPaymentYear());
    }

    // TODO: Check JSON/CSV serialization. What are the rules?
    // ➥ keys in documentation must match keys in JSON object?
    //    ➥ not going to work 100% of the time: documentation is convoluted and often misleading
    @Test
    public void testPaymentNumber() {
        assertEquals("170D1111", paymentRecord.getPaymentNumber());
        LocalDate paymentDate = LocalDate.ofYearDay(paymentRecord.getPaymentYear(), Integer.valueOf(paymentRecord.getPaymentNumber().substring(0, 3)));
        assertEquals("D", paymentRecord.getPaymentNumber().substring(3, 4));
        assertEquals("1111", paymentRecord.getPaymentNumber().substring(4, 8));
    }


}


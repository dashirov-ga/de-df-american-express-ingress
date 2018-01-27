package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

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

import static org.junit.Assert.assertEquals;

public class PaymentRecordTest {
    private static final FixedFormatManager fixedFormatManager = new FixedFormatManagerImpl();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String testString = "891897953100220180110000000000001000000000000000021830I000931365      9999999             00000000022740F00000000000000{000000000000000000909P00000000000000{000000000000000000000{0000{00000000000000{0000{00000000000000{00000000000000{                              ACME CORPORATION PAINT INC            PRIMARY             USD00000000000000{                                                                                                    ";
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
    public void getSettlementServiceEstablishmentAccountNumber(){
        assertEquals("8918979531", paymentRecord.getSettlementSeAccountNumber());
    }

    @Test
    public void testRecordJsonStructure() throws JSONException, JsonProcessingException {
        String exactMatchRequired = "{\"SETTLEMENT_SE_ACCOUNT_NUMBER\":\"8918979531\",\"SETTLEMENT_ACCOUNT_NAME_CODE\":\"002\",\"SETTLEMENT_DATE\":\"2018-01-10\",\"RECORD_CODE\":1,\"SOC_RECORD_CODE\":\"00\",\"SETTLEMENT_AMOUNT\":2183.09,\"SE_BANK_SORT_CODE\":\"000931365\",\"SE_BANK_ACCOUNT_NUMBER\":\"9999999\",\"SETTLEMENT_GROSS_AMOUNT\":2274.06,\"TAX_AMOUNT\":0.00,\"TAX_RATE\":0,\"SERVICE_FEE_AMOUNT\":-90.97,\"SERVICE_FEE_RATE\":0,\"SETTLEMENT_ADJUSTMENT_AMOUNT\":0.00,\"PAY_PLAN_SHORT_NAME\":\"\",\"PAYEE_NAME\":\"ACME CORPORATION PAINT INC\",\"SETTLEMENT_ACCOUNT_NAME_CODE_EXTENDED\":\"PRIMARY\",\"SETTLEMENT_CURRENCY_CODE\":\"USD\",\"PREVIOUS_DEBIT_BALANCE\":0.00,\"IBAN\":\"\",\"BIC\":\"\",\"GENERATED_PAYMENT_NUMBER\":\"8c63154a-81b1-3247-9241-6c60eec8e3d5\"}";
        JSONObject test = new JSONObject(mapper.writeValueAsString(paymentRecord));
        JSONAssert.assertEquals(exactMatchRequired, test, true);
    }

    @Test
    public void testRecordType() {
        String amexDetailRecordCode = paymentRecord.getRecordCode() + paymentRecord.getRecordSubCode();
        assertEquals("100", amexDetailRecordCode);
    }

    @Test
    public void testAmexPayeeName() {
        assertEquals("ACME CORPORATION PAINT INC", paymentRecord.getPayeeName());
    }


    // Bank sort code is a british thing, encoded in IBANs, strongly correlated to BIC
    @Test
    public void testServiceEstablishmentBankSortCode(){
        assertEquals("000931365", paymentRecord.getSeBankSortCode());
    }

    @Test
    public void testServiceEstablishmentBankAccountNumber(){
        assertEquals("9999999", paymentRecord.getSeBankAccountNumber());
    }


    @Test
    public void testPaymentCurrencyCode(){
        assertEquals("USD", paymentRecord.getSettlementCurrencyCode());
    }

    // "SETTLEMENT_GROSS_AMOUNT":2274.06,"TAX_AMOUNT":0.00,"TAX_RATE":0,"SERVICE_FEE_AMOUNT":-90.97,"SERVICE_FEE_RATE":0,"SETTLEMENT_ADJUSTMENT_AMOUNT":0.00

    @Test
    public void testTaxRate(){
        assertEquals(BigDecimal.valueOf(0).setScale(5, RoundingMode.HALF_UP), paymentRecord.getTaxRate());
    }
    @Test
    public void testTaxAmount(){
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP), paymentRecord.getTaxAmount());
    }
    @Test
    public void testSettlementGrossAmount(){
        assertEquals(BigDecimal.valueOf(2274.06).setScale(2, RoundingMode.HALF_UP), paymentRecord.getSettlementGrossAmount());
    }

    @Test
    public void testPaymentYear() {
        assertEquals(Integer.valueOf(2018),paymentRecord.getPaymentYear());
    }

    @Test
    public void testPaymentDate() {
        assertEquals(LocalDate.of(2018, Month.JANUARY, 10),paymentRecord.getPaymentDate());
    }

    @Test
    public void testSettlementDate() {
        assertEquals(LocalDate.of(2018, Month.JANUARY, 10),paymentRecord.getSettlementDate());
    }

    // TODO: Check JSON/CSV serialization. What are the rules?
    // ➥ keys in documentation must match keys in JSON object?
    //    ➥ not going to work 100% of the time: documentation is convoluted and often misleading
    @Test
    public void testPaymentNumber() {
        assertEquals("8c63154a-81b1-3247-9241-6c60eec8e3d5", paymentRecord.getPaymentId());
    }


}


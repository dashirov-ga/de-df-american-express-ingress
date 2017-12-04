package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by davidashirov on 12/2/17.
 */
public final class PaymentRecordBuilder {
    private String  SettlementSeAccountNumber;
    private String  SettlementAccountNameCode;
    private Date SettlementDate;
    //private Integer filler1_1;
    //private String  filler2_10;
    private Integer RecordCode;
    private String SOCRecordCode;
    private BigDecimal SettlementAmount;
    private String  SEBankSortCode;
    private String  SEBankAccountNumber;
    private BigDecimal SettlementGrossAmount;
    private BigDecimal  TaxAmount;
    private Integer  TaxRate;//121	127	7
    private BigDecimal ServiceFeeAmount;//	128	142	15
    private Integer  ServiceFeeRate;//	158	164	7
    private BigDecimal  SettlementAdjustmentAmount;//	220 234 15
    private String  PayPlanShortName;// 	235	264	30
    private String  PayeeName;// 	265	302	38
    private String  SettlementAccountNameCodeExtended;//	303 322 20
    private String  SettlementCurrencyCode;//	323	325	3
    private BigDecimal  PreviousDebitBalance;//	326	340	15
    private String  IBAN;//	341	374	34
    private String  BIC;//	375	385	12

    private PaymentRecordBuilder() {
    }

    public static PaymentRecordBuilder aPaymentRecord() {
        return new PaymentRecordBuilder();
    }

    public PaymentRecordBuilder withSettlementSeAccountNumber(String SettlementSeAccountNumber) {
        this.SettlementSeAccountNumber = SettlementSeAccountNumber;
        return this;
    }

    public PaymentRecordBuilder withSettlementAccountNameCode(String SettlementAccountNameCode) {
        this.SettlementAccountNameCode = SettlementAccountNameCode;
        return this;
    }

    public PaymentRecordBuilder withSettlementDate(Date SettlementDate) {
        this.SettlementDate = SettlementDate;
        return this;
    }

    public PaymentRecordBuilder withRecordCode(Integer RecordCode) {
        this.RecordCode = RecordCode;
        return this;
    }

    public PaymentRecordBuilder withSOCRecordCode(String SOCRecordCode) {
        this.SOCRecordCode = SOCRecordCode;
        return this;
    }

    public PaymentRecordBuilder withSettlementAmount(BigDecimal SettlementAmount) {
        this.SettlementAmount = SettlementAmount;
        return this;
    }

    public PaymentRecordBuilder withSEBankSortCode(String SEBankSortCode) {
        this.SEBankSortCode = SEBankSortCode;
        return this;
    }

    public PaymentRecordBuilder withSEBankAccountNumber(String SEBankAccountNumber) {
        this.SEBankAccountNumber = SEBankAccountNumber;
        return this;
    }

    public PaymentRecordBuilder withSettlementGrossAmount(BigDecimal SettlementGrossAmount) {
        this.SettlementGrossAmount = SettlementGrossAmount;
        return this;
    }

    public PaymentRecordBuilder withTaxAmount(BigDecimal TaxAmount) {
        this.TaxAmount = TaxAmount;
        return this;
    }

    public PaymentRecordBuilder withTaxRate(Integer TaxRate) {
        this.TaxRate = TaxRate;
        return this;
    }

    public PaymentRecordBuilder withServiceFeeAmount(BigDecimal ServiceFeeAmount) {
        this.ServiceFeeAmount = ServiceFeeAmount;
        return this;
    }

    public PaymentRecordBuilder withServiceFeeRate(Integer ServiceFeeRate) {
        this.ServiceFeeRate = ServiceFeeRate;
        return this;
    }

    public PaymentRecordBuilder withSettlementAdjustmentAmount(BigDecimal SettlementAdjustmentAmount) {
        this.SettlementAdjustmentAmount = SettlementAdjustmentAmount;
        return this;
    }

    public PaymentRecordBuilder withPayPlanShortName(String PayPlanShortName) {
        this.PayPlanShortName = PayPlanShortName;
        return this;
    }

    public PaymentRecordBuilder withPayeeName(String PayeeName) {
        this.PayeeName = PayeeName;
        return this;
    }

    public PaymentRecordBuilder withSettlementAccountNameCodeExtended(String SettlementAccountNameCodeExtended) {
        this.SettlementAccountNameCodeExtended = SettlementAccountNameCodeExtended;
        return this;
    }

    public PaymentRecordBuilder withSettlementCurrencyCode(String SettlementCurrencyCode) {
        this.SettlementCurrencyCode = SettlementCurrencyCode;
        return this;
    }

    public PaymentRecordBuilder withPreviousDebitBalance(BigDecimal PreviousDebitBalance) {
        this.PreviousDebitBalance = PreviousDebitBalance;
        return this;
    }

    public PaymentRecordBuilder withIBAN(String IBAN) {
        this.IBAN = IBAN;
        return this;
    }

    public PaymentRecordBuilder withBIC(String BIC) {
        this.BIC = BIC;
        return this;
    }

    public PaymentRecord build() {
        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setSettlementSeAccountNumber(SettlementSeAccountNumber);
        paymentRecord.setSettlementAccountNameCode(SettlementAccountNameCode);
        paymentRecord.setSettlementDate(SettlementDate);
        paymentRecord.setRecordCode(RecordCode);
        paymentRecord.setRecordSubCode(SOCRecordCode);
        paymentRecord.setSettlementAmount(SettlementAmount);
        paymentRecord.setSeBankSortCode(SEBankSortCode);
        paymentRecord.setSeBankAccountNumber(SEBankAccountNumber);
        paymentRecord.setSettlementGrossAmount(SettlementGrossAmount);
        paymentRecord.setTaxAmount(TaxAmount);
        paymentRecord.setTaxRate(TaxRate);
        paymentRecord.setServiceFeeAmount(ServiceFeeAmount);
        paymentRecord.setServiceFeeRate(ServiceFeeRate);
        paymentRecord.setSettlementAdjustmentAmount(SettlementAdjustmentAmount);
        paymentRecord.setPayPlanShortName(PayPlanShortName);
        paymentRecord.setPayeeName(PayeeName);
        paymentRecord.setSettlementAccountNameCodeExtended(SettlementAccountNameCodeExtended);
        paymentRecord.setSettlementCurrencyCode(SettlementCurrencyCode);
        paymentRecord.setPreviousDebitBalance(PreviousDebitBalance);
        paymentRecord.setIban(IBAN);
        paymentRecord.setBic(BIC);
        return paymentRecord;
    }
}

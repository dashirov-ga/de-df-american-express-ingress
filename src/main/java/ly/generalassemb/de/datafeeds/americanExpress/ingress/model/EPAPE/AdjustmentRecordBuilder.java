package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by davidashirov on 12/2/17.
 */
public final class AdjustmentRecordBuilder {
    private String SettlementSeAccountNumber;
    private String SettlementAccountNameCode;
    private Date SettlementDate;
    private String SubmissionSeAccountNumber;
    private Integer RecordCode;
    private String AdjustmentRecordCode;
    private BigDecimal SupportingReferenceNumber;
    private BigDecimal SettlementGrossAmount;
    private BigDecimal SettlementDiscountAmount;
    private BigDecimal SettlementNetAmount;
    private Integer ServiceFeeRate;
    private BigDecimal SettlementTaxAmount;
    private Integer SettlementTaxRate;
    private Integer CardMemberAccountNumber;
    private String AdjustmentRecordMessageCode;
    private String AdjustmentMessageDescription;
    private String SubmissionSeBranchNumber;
    private BigDecimal SubmissionGrossAmount;
    private String SubmissionCurrencyCode;
    private String AdjustmentMessageReference;

    private AdjustmentRecordBuilder() {
    }

    public static AdjustmentRecordBuilder anAdjustmentRecord() {
        return new AdjustmentRecordBuilder();
    }

    public AdjustmentRecordBuilder withSettlementSeAccountNumber(String SettlementSeAccountNumber) {
        this.SettlementSeAccountNumber = SettlementSeAccountNumber;
        return this;
    }

    public AdjustmentRecordBuilder withSettlementAccountNameCode(String SettlementAccountNameCode) {
        this.SettlementAccountNameCode = SettlementAccountNameCode;
        return this;
    }

    public AdjustmentRecordBuilder withSettlementDate(Date SettlementDate) {
        this.SettlementDate = SettlementDate;
        return this;
    }

    public AdjustmentRecordBuilder withSubmissionSeAccountNumber(String SubmissionSeAccountNumber) {
        this.SubmissionSeAccountNumber = SubmissionSeAccountNumber;
        return this;
    }

    public AdjustmentRecordBuilder withRecordCode(Integer RecordCode) {
        this.RecordCode = RecordCode;
        return this;
    }

    public AdjustmentRecordBuilder withAdjustmentRecordCode(String AdjustmentRecordCode) {
        this.AdjustmentRecordCode = AdjustmentRecordCode;
        return this;
    }

    public AdjustmentRecordBuilder withSupportingReferenceNumber(BigDecimal SupportingReferenceNumber) {
        this.SupportingReferenceNumber = SupportingReferenceNumber;
        return this;
    }

    public AdjustmentRecordBuilder withSettlementGrossAmount(BigDecimal SettlementGrossAmount) {
        this.SettlementGrossAmount = SettlementGrossAmount;
        return this;
    }

    public AdjustmentRecordBuilder withSettlementDiscountAmount(BigDecimal SettlementDiscountAmount) {
        this.SettlementDiscountAmount = SettlementDiscountAmount;
        return this;
    }

    public AdjustmentRecordBuilder withSettlementNetAmount(BigDecimal SettlementNetAmount) {
        this.SettlementNetAmount = SettlementNetAmount;
        return this;
    }

    public AdjustmentRecordBuilder withServiceFeeRate(Integer ServiceFeeRate) {
        this.ServiceFeeRate = ServiceFeeRate;
        return this;
    }

    public AdjustmentRecordBuilder withSettlementTaxAmount(BigDecimal SettlementTaxAmount) {
        this.SettlementTaxAmount = SettlementTaxAmount;
        return this;
    }

    public AdjustmentRecordBuilder withSettlementTaxRate(Integer SettlementTaxRate) {
        this.SettlementTaxRate = SettlementTaxRate;
        return this;
    }

    public AdjustmentRecordBuilder withCardMemberAccountNumber(Integer CardMemberAccountNumber) {
        this.CardMemberAccountNumber = CardMemberAccountNumber;
        return this;
    }

    public AdjustmentRecordBuilder withAdjustmentRecordMessageCode(String AdjustmentRecordMessageCode) {
        this.AdjustmentRecordMessageCode = AdjustmentRecordMessageCode;
        return this;
    }

    public AdjustmentRecordBuilder withAdjustmentMessageDescription(String AdjustmentMessageDescription) {
        this.AdjustmentMessageDescription = AdjustmentMessageDescription;
        return this;
    }

    public AdjustmentRecordBuilder withSubmissionSeBranchNumber(String SubmissionSeBranchNumber) {
        this.SubmissionSeBranchNumber = SubmissionSeBranchNumber;
        return this;
    }

    public AdjustmentRecordBuilder withSubmissionGrossAmount(BigDecimal SubmissionGrossAmount) {
        this.SubmissionGrossAmount = SubmissionGrossAmount;
        return this;
    }

    public AdjustmentRecordBuilder withSubmissionCurrencyCode(String SubmissionCurrencyCode) {
        this.SubmissionCurrencyCode = SubmissionCurrencyCode;
        return this;
    }

    public AdjustmentRecordBuilder withAdjustmentMessageReference(String AdjustmentMessageReference) {
        this.AdjustmentMessageReference = AdjustmentMessageReference;
        return this;
    }

    public AdjustmentRecordBuilder but() {
        return anAdjustmentRecord().withSettlementSeAccountNumber(SettlementSeAccountNumber).withSettlementAccountNameCode(SettlementAccountNameCode).withSettlementDate(SettlementDate).withSubmissionSeAccountNumber(SubmissionSeAccountNumber).withRecordCode(RecordCode).withAdjustmentRecordCode(AdjustmentRecordCode).withSupportingReferenceNumber(SupportingReferenceNumber).withSettlementGrossAmount(SettlementGrossAmount).withSettlementDiscountAmount(SettlementDiscountAmount).withSettlementNetAmount(SettlementNetAmount).withServiceFeeRate(ServiceFeeRate).withSettlementTaxAmount(SettlementTaxAmount).withSettlementTaxRate(SettlementTaxRate).withCardMemberAccountNumber(CardMemberAccountNumber).withAdjustmentRecordMessageCode(AdjustmentRecordMessageCode).withAdjustmentMessageDescription(AdjustmentMessageDescription).withSubmissionSeBranchNumber(SubmissionSeBranchNumber).withSubmissionGrossAmount(SubmissionGrossAmount).withSubmissionCurrencyCode(SubmissionCurrencyCode).withAdjustmentMessageReference(AdjustmentMessageReference);
    }

    public AdjustmentRecord build() {
        AdjustmentRecord adjustmentRecord = new AdjustmentRecord();
        adjustmentRecord.setSettlementSeAccountNumber(SettlementSeAccountNumber);
        adjustmentRecord.setSettlementAccountNameCode(SettlementAccountNameCode);
        adjustmentRecord.setSettlementDate(SettlementDate);
        adjustmentRecord.setSubmissionSeAccountNumber(SubmissionSeAccountNumber);
        adjustmentRecord.setRecordCode(RecordCode);
        adjustmentRecord.setRecordSubCode(AdjustmentRecordCode);
        adjustmentRecord.setSupportingReferenceNumber(SupportingReferenceNumber);
        adjustmentRecord.setSettlementGrossAmount(SettlementGrossAmount);
        adjustmentRecord.setSettlementDiscountAmount(SettlementDiscountAmount);
        adjustmentRecord.setSettlementNetAmount(SettlementNetAmount);
        adjustmentRecord.setServiceFeeRate(ServiceFeeRate);
        adjustmentRecord.setSettlementTaxAmount(SettlementTaxAmount);
        adjustmentRecord.setSettlementTaxRate(SettlementTaxRate);
        adjustmentRecord.setCardMemberAccountNumber(CardMemberAccountNumber);
        adjustmentRecord.setAdjustmentRecordMessageCode(AdjustmentRecordMessageCode);
        adjustmentRecord.setAdjustmentMessageDescription(AdjustmentMessageDescription);
        adjustmentRecord.setSubmissionSeBranchNumber(SubmissionSeBranchNumber);
        adjustmentRecord.setSubmissionGrossAmount(SubmissionGrossAmount);
        adjustmentRecord.setSubmissionCurrencyCode(SubmissionCurrencyCode);
        adjustmentRecord.setAdjustmentMessageReference(AdjustmentMessageReference);
        return adjustmentRecord;
    }
}

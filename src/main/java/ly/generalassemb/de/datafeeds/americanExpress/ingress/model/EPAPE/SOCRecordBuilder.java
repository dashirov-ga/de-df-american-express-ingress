package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by davidashirov on 12/3/17.
 */
public final class SOCRecordBuilder {
    private String settlementSeAccountNumber;
    private String settlementAccountNameCode;
    private Date settlementDate;
    private String submissionSeAccountNumber;

    private Integer recordCode;
    private String recordSubCode;
    private Date socDate;
    private BigDecimal submissionCalculatedGrossAmount;
    private BigDecimal submissionDeclaredGrossAmount;
    private BigDecimal discountAmount;

    private BigDecimal settlementNetAmount;
    private Integer serviceFeeRate;
    private BigDecimal settlementGrossAmount;
    private BigDecimal rocCalculatedCount;
    private BigDecimal settlementTaxAmount;
    private Integer settlementTaxRate;
    private String submissionCurrencyCode;
    private Integer submissionNumber;
    private String submissionSeBranchNumber;
    private String submissionMethodCode;
    private BigDecimal exchangeRate;

    private SOCRecordBuilder() {
    }

    public static SOCRecordBuilder aSOCRecord() {
        return new SOCRecordBuilder();
    }

    public SOCRecordBuilder withSettlementSeAccountNumber(String settlementSeAccountNumber) {
        this.settlementSeAccountNumber = settlementSeAccountNumber;
        return this;
    }

    public SOCRecordBuilder withSettlementAccountNameCode(String settlementAccountNameCode) {
        this.settlementAccountNameCode = settlementAccountNameCode;
        return this;
    }

    public SOCRecordBuilder withSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
        return this;
    }

    public SOCRecordBuilder withSubmissionSeAccountNumber(String submissionSeAccountNumber) {
        this.submissionSeAccountNumber = submissionSeAccountNumber;
        return this;
    }

    public SOCRecordBuilder withRecordCode(Integer recordCode) {
        this.recordCode = recordCode;
        return this;
    }

    public SOCRecordBuilder withRecordSubCode(String recordSubCode) {
        this.recordSubCode = recordSubCode;
        return this;
    }

    public SOCRecordBuilder withSocDate(Date socDate) {
        this.socDate = socDate;
        return this;
    }

    public SOCRecordBuilder withSubmissionCalculatedGrossAmount(BigDecimal submissionCalculatedGrossAmount) {
        this.submissionCalculatedGrossAmount = submissionCalculatedGrossAmount;
        return this;
    }

    public SOCRecordBuilder withSubmissionDeclaredGrossAmount(BigDecimal submissionDeclaredGrossAmount) {
        this.submissionDeclaredGrossAmount = submissionDeclaredGrossAmount;
        return this;
    }

    public SOCRecordBuilder withDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public SOCRecordBuilder withSettlementNetAmount(BigDecimal settlementNetAmount) {
        this.settlementNetAmount = settlementNetAmount;
        return this;
    }

    public SOCRecordBuilder withServiceFeeRate(Integer serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
        return this;
    }

    public SOCRecordBuilder withSettlementGrossAmount(BigDecimal settlementGrossAmount) {
        this.settlementGrossAmount = settlementGrossAmount;
        return this;
    }

    public SOCRecordBuilder withRocCalculatedCount(BigDecimal rocCalculatedCount) {
        this.rocCalculatedCount = rocCalculatedCount;
        return this;
    }

    public SOCRecordBuilder withSettlementTaxAmount(BigDecimal settlementTaxAmount) {
        this.settlementTaxAmount = settlementTaxAmount;
        return this;
    }

    public SOCRecordBuilder withSettlementTaxRate(Integer settlementTaxRate) {
        this.settlementTaxRate = settlementTaxRate;
        return this;
    }

    public SOCRecordBuilder withSubmissionCurrencyCode(String submissionCurrencyCode) {
        this.submissionCurrencyCode = submissionCurrencyCode;
        return this;
    }

    public SOCRecordBuilder withSubmissionNumber(Integer submissionNumber) {
        this.submissionNumber = submissionNumber;
        return this;
    }

    public SOCRecordBuilder withSubmissionSeBranchNumber(String submissionSeBranchNumber) {
        this.submissionSeBranchNumber = submissionSeBranchNumber;
        return this;
    }

    public SOCRecordBuilder withSubmissionMethodCode(String submissionMethodCode) {
        this.submissionMethodCode = submissionMethodCode;
        return this;
    }

    public SOCRecordBuilder withExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public SOCRecordBuilder but() {
        return aSOCRecord().withSettlementSeAccountNumber(settlementSeAccountNumber).withSettlementAccountNameCode(settlementAccountNameCode).withSettlementDate(settlementDate).withSubmissionSeAccountNumber(submissionSeAccountNumber).withRecordCode(recordCode).withRecordSubCode(recordSubCode).withSocDate(socDate).withSubmissionCalculatedGrossAmount(submissionCalculatedGrossAmount).withSubmissionDeclaredGrossAmount(submissionDeclaredGrossAmount).withDiscountAmount(discountAmount).withSettlementNetAmount(settlementNetAmount).withServiceFeeRate(serviceFeeRate).withSettlementGrossAmount(settlementGrossAmount).withRocCalculatedCount(rocCalculatedCount).withSettlementTaxAmount(settlementTaxAmount).withSettlementTaxRate(settlementTaxRate).withSubmissionCurrencyCode(submissionCurrencyCode).withSubmissionNumber(submissionNumber).withSubmissionSeBranchNumber(submissionSeBranchNumber).withSubmissionMethodCode(submissionMethodCode).withExchangeRate(exchangeRate);
    }

    public SOCRecord build() {
        SOCRecord sOCRecord = new SOCRecord();
        sOCRecord.setSettlementSeAccountNumber(settlementSeAccountNumber);
        sOCRecord.setSettlementAccountNameCode(settlementAccountNameCode);
        sOCRecord.setSettlementDate(settlementDate);
        sOCRecord.setSubmissionSeAccountNumber(submissionSeAccountNumber);
        sOCRecord.setRecordCode(recordCode);
        sOCRecord.setRecordSubCode(recordSubCode);
        sOCRecord.setSocDate(socDate);
        sOCRecord.setSubmissionCalculatedGrossAmount(submissionCalculatedGrossAmount);
        sOCRecord.setSubmissionDeclaredGrossAmount(submissionDeclaredGrossAmount);
        sOCRecord.setDiscountAmount(discountAmount);
        sOCRecord.setSettlementNetAmount(settlementNetAmount);
        sOCRecord.setServiceFeeRate(serviceFeeRate);
        sOCRecord.setSettlementGrossAmount(settlementGrossAmount);
        sOCRecord.setRocCalculatedCount(rocCalculatedCount);
        sOCRecord.setSettlementTaxAmount(settlementTaxAmount);
        sOCRecord.setSettlementTaxRate(settlementTaxRate);
        sOCRecord.setSubmissionCurrencyCode(submissionCurrencyCode);
        sOCRecord.setSubmissionNumber(submissionNumber);
        sOCRecord.setSubmissionSeBranchNumber(submissionSeBranchNumber);
        sOCRecord.setSubmissionMethodCode(submissionMethodCode);
        sOCRecord.setExchangeRate(exchangeRate);
        return sOCRecord;
    }
}

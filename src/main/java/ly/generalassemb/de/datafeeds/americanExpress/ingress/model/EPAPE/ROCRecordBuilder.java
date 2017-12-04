package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by davidashirov on 12/3/17.
 */
public final class ROCRecordBuilder {
    private String settlementSeAccountNumber;
    private String settlementAccountNameCode;
    private String submissionSeAccountNumber;
    private Integer recordCode;
    private String recordSubCode;
    private BigDecimal chargeAmount;
    private Date chargeDate;
    private String rocReferenceNumber;
    private String rocReferenceNumberCpcOnly;
    private String threeDigitChargeAuthorizationCode;
    private String cardMemberAccountNumber;
    private String airlineTicketNumber;
    private String sixDigitChargeAuthorizationCode;

    private ROCRecordBuilder() {
    }

    public static ROCRecordBuilder aROCRecord() {
        return new ROCRecordBuilder();
    }

    public ROCRecordBuilder withSettlementSeAccountNumber(String settlementSeAccountNumber) {
        this.settlementSeAccountNumber = settlementSeAccountNumber;
        return this;
    }

    public ROCRecordBuilder withSettlementAccountNameCode(String settlementAccountNameCode) {
        this.settlementAccountNameCode = settlementAccountNameCode;
        return this;
    }

    public ROCRecordBuilder withSubmissionSeAccountNumber(String submissionSeAccountNumber) {
        this.submissionSeAccountNumber = submissionSeAccountNumber;
        return this;
    }

    public ROCRecordBuilder withRecordCode(Integer recordCode) {
        this.recordCode = recordCode;
        return this;
    }

    public ROCRecordBuilder withRecordSubCode(String recordSubCode) {
        this.recordSubCode = recordSubCode;
        return this;
    }

    public ROCRecordBuilder withChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
        return this;
    }

    public ROCRecordBuilder withChargeDate(Date chargeDate) {
        this.chargeDate = chargeDate;
        return this;
    }

    public ROCRecordBuilder withRocReferenceNumber(String rocReferenceNumber) {
        this.rocReferenceNumber = rocReferenceNumber;
        return this;
    }

    public ROCRecordBuilder withRocReferenceNumberCpcOnly(String rocReferenceNumberCpcOnly) {
        this.rocReferenceNumberCpcOnly = rocReferenceNumberCpcOnly;
        return this;
    }

    public ROCRecordBuilder withThreeDigitChargeAuthorizationCode(String threeDigitChargeAuthorizationCode) {
        this.threeDigitChargeAuthorizationCode = threeDigitChargeAuthorizationCode;
        return this;
    }

    public ROCRecordBuilder withCardMemberAccountNumber(String cardMemberAccountNumber) {
        this.cardMemberAccountNumber = cardMemberAccountNumber;
        return this;
    }

    public ROCRecordBuilder withAirlineTicketNumber(String airlineTicketNumber) {
        this.airlineTicketNumber = airlineTicketNumber;
        return this;
    }

    public ROCRecordBuilder withSixDigitChargeAuthorizationCode(String sixDigitChargeAuthorizationCode) {
        this.sixDigitChargeAuthorizationCode = sixDigitChargeAuthorizationCode;
        return this;
    }

    public ROCRecordBuilder but() {
        return aROCRecord().withSettlementSeAccountNumber(settlementSeAccountNumber).withSettlementAccountNameCode(settlementAccountNameCode).withSubmissionSeAccountNumber(submissionSeAccountNumber).withRecordCode(recordCode).withRecordSubCode(recordSubCode).withChargeAmount(chargeAmount).withChargeDate(chargeDate).withRocReferenceNumber(rocReferenceNumber).withRocReferenceNumberCpcOnly(rocReferenceNumberCpcOnly).withThreeDigitChargeAuthorizationCode(threeDigitChargeAuthorizationCode).withCardMemberAccountNumber(cardMemberAccountNumber).withAirlineTicketNumber(airlineTicketNumber).withSixDigitChargeAuthorizationCode(sixDigitChargeAuthorizationCode);
    }

    public ROCRecord build() {
        ROCRecord rOCRecord = new ROCRecord();
        rOCRecord.setSettlementSeAccountNumber(settlementSeAccountNumber);
        rOCRecord.setSettlementAccountNameCode(settlementAccountNameCode);
        rOCRecord.setSubmissionSeAccountNumber(submissionSeAccountNumber);
        rOCRecord.setRecordCode(recordCode);
        rOCRecord.setRecordSubCode(recordSubCode);
        rOCRecord.setChargeAmount(chargeAmount);
        rOCRecord.setChargeDate(chargeDate);
        rOCRecord.setRocReferenceNumber(rocReferenceNumber);
        rOCRecord.setRocReferenceNumberCpcOnly(rocReferenceNumberCpcOnly);
        rOCRecord.setThreeDigitChargeAuthorizationCode(threeDigitChargeAuthorizationCode);
        rOCRecord.setCardMemberAccountNumber(cardMemberAccountNumber);
        rOCRecord.setAirlineTicketNumber(airlineTicketNumber);
        rOCRecord.setSixDigitChargeAuthorizationCode(sixDigitChargeAuthorizationCode);
        return rOCRecord;
    }
}

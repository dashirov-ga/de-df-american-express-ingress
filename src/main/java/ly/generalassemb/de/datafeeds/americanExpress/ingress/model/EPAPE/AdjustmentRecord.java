package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import com.ancientprogramming.fixedformat4j.annotation.*;
import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.AmexSignedNumericFixedFormatter;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.LocalDateFormatter;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by davidashirov on 12/2/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "SETTLEMENT_SE_ACCOUNT_NUMBER",
        "SETTLEMENT_ACCOUNT_NAME_CODE",
        "SETTLEMENT_DATE",

        "SUBMISSION_SE_ACCOUNT_NUMBER",
        "RECORD_CODE",
        "ADJUSTMENT_RECORD_CODE",

        "SUPPORTING_REFERENCE_NUMBER",
        "SETTLEMENT_GROSS_AMOUNT",
        "SETTLEMENT_DISCOUNT_AMOUNT",

        "SETTLEMENT_NET_AMOUNT",
        "SERVICE_FEE_RATE",

        "SETTLEMENT_TAX_AMOUNT",
        "SETTLEMENT_TAX_RATE",
        "FILLER",
        "CARD_MEMBER_ACCOUNT_NUMBER",
        "ADJUSTMENT_RECORD_MESSAGE_CODE",
        "ADJUSTMENT_MESSAGE_DESCRIPTION",

        "SUBMISSION_SE_BRANCH_NUMBER",
        "SUBMISSION_GROSS_AMOUNT",
        "SUBMISSION_CURRENCY_CODE",
        "ADJUSTMENT_MESSAGE_REFERENCE"

})
@Record(length = 441)
public class AdjustmentRecord {
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final CsvMapper csvMapper = new CsvMapper();

    private String paymentId;
    @JsonProperty("GENERATED_PAYMENT_NUMBER")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    @JsonProperty("SETTLEMENT_SE_ACCOUNT_NUMBER")
    @Size(max = 10)
    @javax.validation.constraints.NotNull
    private String settlementSeAccountNumber;

    @JsonProperty("SETTLEMENT_ACCOUNT_NAME_CODE")
    @Size(max = 3)
    @javax.validation.constraints.NotNull
    private String settlementAccountNameCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("SETTLEMENT_DATE")
    @Size(max = 8)
    @javax.validation.constraints.NotNull
    private LocalDate settlementDate;

    @JsonProperty("SUBMISSION_SE_ACCOUNT_NUMBER")
    @Size(max = 10)
    @javax.validation.constraints.NotNull
    private String submissionSeAccountNumber;

    @JsonProperty("RECORD_CODE")
    @Size(max = 1)
    @javax.validation.constraints.NotNull
    private Integer recordCode;

    @JsonProperty("ADJUSTMENT_RECORD_CODE")
    @Size(max = 2)
    @javax.validation.constraints.NotNull
    private String recordSubCode;

    @JsonProperty("SUPPORTING_REFERENCE_NUMBER")
    @Size(max = 11)	@javax.validation.constraints.NotNull
    private BigDecimal supportingReferenceNumber;

    @JsonProperty("SETTLEMENT_GROSS_AMOUNT")
    @Size(max = 15)	@javax.validation.constraints.NotNull
    private BigDecimal settlementGrossAmount;

    @JsonProperty("SETTLEMENT_DISCOUNT_AMOUNT")
    @Size(max = 15)	@javax.validation.constraints.NotNull
    private BigDecimal settlementDiscountAmount;

    @JsonProperty("SETTLEMENT_NET_AMOUNT")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal settlementNetAmount;

    @JsonProperty("SERVICE_FEE_RATE")
    @Size(max = 7)
    @javax.validation.constraints.NotNull
    private Integer serviceFeeRate;

    @JsonProperty("SETTLEMENT_TAX_AMOUNT")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal settlementTaxAmount;

    @JsonProperty("SETTLEMENT_TAX_RATE")
    @Size(max = 7)
    @javax.validation.constraints.NotNull
    private Integer settlementTaxRate;

    @JsonProperty("CARD_MEMBER_ACCOUNT_NUMBER")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal cardMemberAccountNumber;

    @JsonProperty("ADJUSTMENT_RECORD_MESSAGE_CODE")
    @Size(max = 10)
    @javax.validation.constraints.NotNull
    private String adjustmentRecordMessageCode;

    @JsonProperty("ADJUSTMENT_MESSAGE_DESCRIPTION")
    @Size(max = 64)
    @javax.validation.constraints.NotNull
    private String adjustmentMessageDescription;

    @JsonProperty("SUBMISSION_SE_BRANCH_NUMBER")
    @Size(max = 10)
    @javax.validation.constraints.NotNull
    private String submissionSeBranchNumber;

    @JsonProperty("SUBMISSION_GROSS_AMOUNT")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal submissionGrossAmount;

    @JsonProperty("SUBMISSION_CURRENCY_CODE")
    @Size(max = 3)
    @javax.validation.constraints.NotNull
    private String submissionCurrencyCode;

    @JsonProperty("ADJUSTMENT_MESSAGE_REFERENCE")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private String adjustmentMessageReference;


    @Field(offset=1,length=10,align=Align.LEFT,paddingChar = ' ')
    public String getSettlementSeAccountNumber() {
        return settlementSeAccountNumber;
    }
    public void setSettlementSeAccountNumber(String settlementSeAccountNumber) {
        this.settlementSeAccountNumber = settlementSeAccountNumber;
    }
    @Field(offset=11,length=3,align=Align.LEFT,paddingChar = ' ')
    public String getSettlementAccountNameCode() {
        return settlementAccountNameCode;
    }
    public void setSettlementAccountNameCode(String settlementAccountNameCode) {
        this.settlementAccountNameCode = settlementAccountNameCode;
    }

    @FixedFormatPattern("yyyyMMdd")
    @Field(offset=14,length=8,align=Align.RIGHT,paddingChar = '0', formatter = LocalDateFormatter.class)
    public LocalDate getSettlementDate() {
        return settlementDate;
    }
    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    @Field(offset=23,length=10,align=Align.LEFT,paddingChar = ' ')
    public String getSubmissionSeAccountNumber() {
        return submissionSeAccountNumber;
    }
    public void setSubmissionSeAccountNumber(String submissionSeAccountNumber) {
        this.submissionSeAccountNumber = submissionSeAccountNumber;
    }

    @Field(offset=33,length=1,align=Align.RIGHT,paddingChar = '0')
    public Integer getRecordCode() {
        return recordCode;
    }
    public void setRecordCode(Integer recordCode) {
        this.recordCode = recordCode;
    }

    @Field(offset=34,length=2,align=Align.LEFT,paddingChar = ' ')
    public String getRecordSubCode() {
        return recordSubCode;
    }
    public void setRecordSubCode(String recordSubCode) {
        this.recordSubCode = recordSubCode;
    }

    @Field(offset=41,length=11,align=Align.RIGHT,paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    public BigDecimal getSupportingReferenceNumber() {
        return supportingReferenceNumber;
    }

    public void setSupportingReferenceNumber(BigDecimal supportingReferenceNumber) {
        this.supportingReferenceNumber = supportingReferenceNumber;
    }

    @Field(offset=52,length=15,align=Align.RIGHT,paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    public BigDecimal getSettlementGrossAmount() {
        return settlementGrossAmount;
    }

    public void setSettlementGrossAmount(BigDecimal settlementGrossAmount) {
        this.settlementGrossAmount = settlementGrossAmount;
    }

    @Field(offset=67,length=15,align=Align.RIGHT,paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    public BigDecimal getSettlementDiscountAmount() {
        return settlementDiscountAmount;
    }
    public void setSettlementDiscountAmount(BigDecimal settlementDiscountAmount) {
        this.settlementDiscountAmount = settlementDiscountAmount;
    }

    @Field(offset=97,length=15,align=Align.RIGHT,paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    public BigDecimal getSettlementNetAmount() {
        return settlementNetAmount;
    }
    public void setSettlementNetAmount(BigDecimal settlementNetAmount) {
        this.settlementNetAmount = settlementNetAmount;
    }

    @Field(offset=112,length=7,align=Align.RIGHT,paddingChar = '0')
    public Integer getServiceFeeRate() {
        return serviceFeeRate;
    }
    public void setServiceFeeRate(Integer serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
    }

    @Field(offset=154,length=15,align=Align.RIGHT,paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    public BigDecimal getSettlementTaxAmount() {
        return settlementTaxAmount;
    }
    public void setSettlementTaxAmount(BigDecimal settlementTaxAmount) {
        this.settlementTaxAmount = settlementTaxAmount;
    }

    @Field(offset=169,length=7,align=Align.RIGHT,paddingChar = '0')
    public Integer getSettlementTaxRate() {
        return settlementTaxRate;
    }
    public void setSettlementTaxRate(Integer settlementTaxRate) {
        this.settlementTaxRate = settlementTaxRate;
    }

    @Field(offset=191,length=15,align=Align.RIGHT,paddingChar = '0')
    @FixedFormatNumber(sign = Sign.NOSIGN)
    @FixedFormatDecimal(decimals = 0)
    public BigDecimal getCardMemberAccountNumber() {
        return cardMemberAccountNumber;
    }
    public void setCardMemberAccountNumber(BigDecimal cardMemberAccountNumber) {
        this.cardMemberAccountNumber = cardMemberAccountNumber;
    }

    @Field(offset=206,length=10,align=Align.LEFT,paddingChar = ' ')
    public String getAdjustmentRecordMessageCode() {
        return adjustmentRecordMessageCode;
    }
    public void setAdjustmentRecordMessageCode(String adjustmentRecordMessageCode) {
        this.adjustmentRecordMessageCode = adjustmentRecordMessageCode;
    }

    @Field(offset=216,length=64,align=Align.LEFT,paddingChar = ' ')
    public String getAdjustmentMessageDescription() {
        return adjustmentMessageDescription;
    }

    public void setAdjustmentMessageDescription(String adjustmentMessageDescription) {
        this.adjustmentMessageDescription = adjustmentMessageDescription;
    }

    @Field(offset=283,length=10,align=Align.LEFT,paddingChar = ' ')
    public String getSubmissionSeBranchNumber() {
        return submissionSeBranchNumber;
    }

    public void setSubmissionSeBranchNumber(String submissionSeBranchNumber) {
        this.submissionSeBranchNumber = submissionSeBranchNumber;
    }

    @Field(offset=293,length=15,align=Align.RIGHT,paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    public BigDecimal getSubmissionGrossAmount() {
        return submissionGrossAmount;
    }
    public void setSubmissionGrossAmount(BigDecimal submissionGrossAmount) {
        this.submissionGrossAmount = submissionGrossAmount;
    }

    @Field(offset=308,length=3,align=Align.LEFT,paddingChar = ' ')
    public String getSubmissionCurrencyCode() {
        return submissionCurrencyCode;
    }
    public void setSubmissionCurrencyCode(String submissionCurrencyCode) {
        this.submissionCurrencyCode = submissionCurrencyCode;
    }

    @JsonGetter("ADJUSTMENT_MESSAGE_REFERENCE")
    @Field(offset=311,length=15,align=Align.LEFT,paddingChar = ' ')
    public String getAdjustmentMessageReference() {
        return adjustmentMessageReference;
    }

    public void setAdjustmentMessageReference(String adjustmentMessageReference) {
        this.adjustmentMessageReference = adjustmentMessageReference;
    }

    @Override
    public String toString() {
        try {
            return jsonMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ((Object)this).toString();
        }
    }

    public String toCsv() {
        ObjectWriter myObjectWriter = csvMapper.writer(
                csvMapper.schemaFor(AdjustmentRecord.class)
                        .withColumnSeparator(',')
                        .withHeader()
        );
        try {
            return myObjectWriter.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public AdjustmentRecord parse(FixedFormatManager manager, String line){
        return manager.load(AdjustmentRecord.class,line);
    }

    public void put(MerchantSubmission submission){
        submission.put(this);
    }

    public static final class Builder {
        private String paymentId;
        private String settlementSeAccountNumber;
        private String settlementAccountNameCode;
        private LocalDate settlementDate;
        private String submissionSeAccountNumber;
        private Integer recordCode;
        private String recordSubCode;
        private BigDecimal supportingReferenceNumber;
        private BigDecimal settlementGrossAmount;
        private BigDecimal settlementDiscountAmount;
        private BigDecimal settlementNetAmount;
        private Integer serviceFeeRate;
        private BigDecimal settlementTaxAmount;
        private Integer settlementTaxRate;
        private BigDecimal cardMemberAccountNumber;
        private String adjustmentRecordMessageCode;
        private String adjustmentMessageDescription;
        private String submissionSeBranchNumber;
        private BigDecimal submissionGrossAmount;
        private String submissionCurrencyCode;
        private String adjustmentMessageReference;

        private Builder() {
        }

        public static Builder anAdjustmentRecord() {
            return new Builder();
        }

        public Builder withPaymentId(String paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public Builder withSettlementSeAccountNumber(String settlementSeAccountNumber) {
            this.settlementSeAccountNumber = settlementSeAccountNumber;
            return this;
        }

        public Builder withSettlementAccountNameCode(String settlementAccountNameCode) {
            this.settlementAccountNameCode = settlementAccountNameCode;
            return this;
        }

        public Builder withSettlementDate(LocalDate settlementDate) {
            this.settlementDate = settlementDate;
            return this;
        }

        public Builder withSubmissionSeAccountNumber(String submissionSeAccountNumber) {
            this.submissionSeAccountNumber = submissionSeAccountNumber;
            return this;
        }

        public Builder withRecordCode(Integer recordCode) {
            this.recordCode = recordCode;
            return this;
        }

        public Builder withRecordSubCode(String recordSubCode) {
            this.recordSubCode = recordSubCode;
            return this;
        }

        public Builder withSupportingReferenceNumber(BigDecimal supportingReferenceNumber) {
            this.supportingReferenceNumber = supportingReferenceNumber;
            return this;
        }

        public Builder withSettlementGrossAmount(BigDecimal settlementGrossAmount) {
            this.settlementGrossAmount = settlementGrossAmount;
            return this;
        }

        public Builder withSettlementDiscountAmount(BigDecimal settlementDiscountAmount) {
            this.settlementDiscountAmount = settlementDiscountAmount;
            return this;
        }

        public Builder withSettlementNetAmount(BigDecimal settlementNetAmount) {
            this.settlementNetAmount = settlementNetAmount;
            return this;
        }

        public Builder withServiceFeeRate(Integer serviceFeeRate) {
            this.serviceFeeRate = serviceFeeRate;
            return this;
        }

        public Builder withSettlementTaxAmount(BigDecimal settlementTaxAmount) {
            this.settlementTaxAmount = settlementTaxAmount;
            return this;
        }

        public Builder withSettlementTaxRate(Integer settlementTaxRate) {
            this.settlementTaxRate = settlementTaxRate;
            return this;
        }

        public Builder withCardMemberAccountNumber(BigDecimal cardMemberAccountNumber) {
            this.cardMemberAccountNumber = cardMemberAccountNumber;
            return this;
        }

        public Builder withAdjustmentRecordMessageCode(String adjustmentRecordMessageCode) {
            this.adjustmentRecordMessageCode = adjustmentRecordMessageCode;
            return this;
        }

        public Builder withAdjustmentMessageDescription(String adjustmentMessageDescription) {
            this.adjustmentMessageDescription = adjustmentMessageDescription;
            return this;
        }

        public Builder withSubmissionSeBranchNumber(String submissionSeBranchNumber) {
            this.submissionSeBranchNumber = submissionSeBranchNumber;
            return this;
        }

        public Builder withSubmissionGrossAmount(BigDecimal submissionGrossAmount) {
            this.submissionGrossAmount = submissionGrossAmount;
            return this;
        }

        public Builder withSubmissionCurrencyCode(String submissionCurrencyCode) {
            this.submissionCurrencyCode = submissionCurrencyCode;
            return this;
        }

        public Builder withAdjustmentMessageReference(String adjustmentMessageReference) {
            this.adjustmentMessageReference = adjustmentMessageReference;
            return this;
        }

        public Builder but() {
            return anAdjustmentRecord().withPaymentId(paymentId).withSettlementSeAccountNumber(settlementSeAccountNumber).withSettlementAccountNameCode(settlementAccountNameCode).withSettlementDate(settlementDate).withSubmissionSeAccountNumber(submissionSeAccountNumber).withRecordCode(recordCode).withRecordSubCode(recordSubCode).withSupportingReferenceNumber(supportingReferenceNumber).withSettlementGrossAmount(settlementGrossAmount).withSettlementDiscountAmount(settlementDiscountAmount).withSettlementNetAmount(settlementNetAmount).withServiceFeeRate(serviceFeeRate).withSettlementTaxAmount(settlementTaxAmount).withSettlementTaxRate(settlementTaxRate).withCardMemberAccountNumber(cardMemberAccountNumber).withAdjustmentRecordMessageCode(adjustmentRecordMessageCode).withAdjustmentMessageDescription(adjustmentMessageDescription).withSubmissionSeBranchNumber(submissionSeBranchNumber).withSubmissionGrossAmount(submissionGrossAmount).withSubmissionCurrencyCode(submissionCurrencyCode).withAdjustmentMessageReference(adjustmentMessageReference);
        }

        public AdjustmentRecord build() {
            AdjustmentRecord adjustmentRecord = new AdjustmentRecord();
            adjustmentRecord.setPaymentId(paymentId);
            adjustmentRecord.setSettlementSeAccountNumber(settlementSeAccountNumber);
            adjustmentRecord.setSettlementAccountNameCode(settlementAccountNameCode);
            adjustmentRecord.setSettlementDate(settlementDate);
            adjustmentRecord.setSubmissionSeAccountNumber(submissionSeAccountNumber);
            adjustmentRecord.setRecordCode(recordCode);
            adjustmentRecord.setRecordSubCode(recordSubCode);
            adjustmentRecord.setSupportingReferenceNumber(supportingReferenceNumber);
            adjustmentRecord.setSettlementGrossAmount(settlementGrossAmount);
            adjustmentRecord.setSettlementDiscountAmount(settlementDiscountAmount);
            adjustmentRecord.setSettlementNetAmount(settlementNetAmount);
            adjustmentRecord.setServiceFeeRate(serviceFeeRate);
            adjustmentRecord.setSettlementTaxAmount(settlementTaxAmount);
            adjustmentRecord.setSettlementTaxRate(settlementTaxRate);
            adjustmentRecord.setCardMemberAccountNumber(cardMemberAccountNumber);
            adjustmentRecord.setAdjustmentRecordMessageCode(adjustmentRecordMessageCode);
            adjustmentRecord.setAdjustmentMessageDescription(adjustmentMessageDescription);
            adjustmentRecord.setSubmissionSeBranchNumber(submissionSeBranchNumber);
            adjustmentRecord.setSubmissionGrossAmount(submissionGrossAmount);
            adjustmentRecord.setSubmissionCurrencyCode(submissionCurrencyCode);
            adjustmentRecord.setAdjustmentMessageReference(adjustmentMessageReference);
            return adjustmentRecord;
        }
    }


}
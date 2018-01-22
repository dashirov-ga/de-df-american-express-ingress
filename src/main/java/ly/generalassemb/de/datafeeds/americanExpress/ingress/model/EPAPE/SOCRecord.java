package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import com.ancientprogramming.fixedformat4j.annotation.*;
import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.AmexSignedNumericFixedFormatter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

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
        "RECORD_SUB_CODE",

        "SOC_DATE",
        "SUBMISSION_CALCULATED_GROSS_AMOUNT",
        "SUBMISSION_DECLARED_GROSS_AMOUNT",
        "DISCOUNT_AMOUNT",

        "SETTLEMENT_NET_AMOUNT",
        "SERVICE_FEE_RATE",

        "SETTLEMENT_GROSS_AMOUNT",
        "ROC_CALCULATED_COUNT",


        "SETTLEMENT_TAX_AMOUNT",
        "SETTLEMENT_TAX_RATE",
        "SUBMISSION_CURRENCY_CODE",
        "SUBMISSION_NUMBER",
        "SUBMISSION_SE_BRANCH_NUMBER",
        "SUBMISSION_METHOD_CODE",

        "EXCHANGE_RATE"
})
@Record(length = 441)
public class SOCRecord {
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final CsvMapper csvMapper = new CsvMapper();

    @JsonProperty("GENERATED_SOC_NUMBER")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public String getSocId() {
        return uniqueCode().toString();
    }

    @JsonProperty("GENERATED_PAYMENT_NUMBER")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String paymentId;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    @JsonProperty("SETTLEMENT_SE_ACCOUNT_NUMBER")
    @Size(max = 10)
    @NotNull
    private String settlementSeAccountNumber;

    @JsonProperty("SETTLEMENT_ACCOUNT_NAME_CODE")
    @Size(max = 3)
    @NotNull
    private String settlementAccountNameCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("SETTLEMENT_DATE")
    @Size(max = 8)
    @NotNull
    private Date settlementDate;

    @JsonProperty("SUBMISSION_SE_ACCOUNT_NUMBER")
    @Size(max = 10)
    @NotNull
    private String submissionSeAccountNumber;

    @JsonProperty("RECORD_CODE")
    @Size(max = 1)
    @NotNull

    private Integer recordCode;
    @JsonProperty("RECORD_SUB_CODE")
    @Size(max = 2)
    @NotNull
    private String recordSubCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("SOC_DATE")
    @Size(max = 8)
    @NotNull
    private Date socDate;

    @JsonProperty("SUBMISSION_CALCULATED_GROSS_AMOUNT")
    @Size(max = 15)
    @NotNull
    private BigDecimal submissionCalculatedGrossAmount;

    @JsonProperty("SUBMISSION_DECLARED_GROSS_AMOUNT")
    @Size(max = 15)
    @NotNull
    private BigDecimal submissionDeclaredGrossAmount;

    @JsonProperty("DISCOUNT_AMOUNT")
    @Size(max = 15)
    @NotNull
    private BigDecimal discountAmount;

    @JsonProperty("SETTLEMENT_NET_AMOUNT")
    @Size(max = 15)
    @NotNull

    private BigDecimal settlementNetAmount;
    @JsonProperty("SERVICE_FEE_RATE")
    @Size(max = 7)
    @NotNull
    private Integer serviceFeeRate;

    @JsonProperty("SETTLEMENT_GROSS_AMOUNT")
    @Size(max = 15)
    @NotNull
    private BigDecimal settlementGrossAmount;

    @JsonProperty("ROC_CALCULATED_COUNT")
    @Size(max = 5)
    @NotNull
    private BigDecimal rocCalculatedCount;


    @JsonProperty("SETTLEMENT_TAX_AMOUNT")
    @Size(max = 15)
    @NotNull
    private BigDecimal settlementTaxAmount;

    @JsonProperty("SETTLEMENT_TAX_RATE")
    @Size(max = 7)
    @NotNull
    private Integer settlementTaxRate;

    @JsonProperty("SUBMISSION_CURRENCY_CODE")
    @Size(max = 3)
    @NotNull
    private String submissionCurrencyCode;

    @JsonProperty("SUBMISSION_NUMBER")
    @Size(max = 15)
    @NotNull
    private Integer submissionNumber;

    @JsonProperty("SUBMISSION_SE_BRANCH_NUMBER")
    @Size(max = 10)
    @NotNull
    private String submissionSeBranchNumber;

    @JsonProperty("SUBMISSION_METHOD_CODE")
    @Size(max = 2)
    @NotNull
    private String submissionMethodCode;

    @JsonProperty("EXCHANGE_RATE")
    @Size(max = 15)
    @NotNull
    private BigDecimal exchangeRate;


    @Field(offset = 1, length = 10, align = Align.LEFT, paddingChar = ' ')        //  getSettlementSeAccountNumber

    public String getSettlementSeAccountNumber() {
        return settlementSeAccountNumber;
    }

    public void setSettlementSeAccountNumber(String settlementSeAccountNumber) {
        this.settlementSeAccountNumber = settlementSeAccountNumber;
    }

    @Field(offset = 11, length = 3, align = Align.LEFT, paddingChar = ' ')        //  getSettlementAccountNameCode
    public String getSettlementAccountNameCode() {
        return settlementAccountNameCode;
    }

    public void setSettlementAccountNameCode(String settlementAccountNameCode) {
        this.settlementAccountNameCode = settlementAccountNameCode;
    }

    @FixedFormatPattern("yyyyMMdd")
    @Field(offset = 14, length = 8, align = Align.RIGHT, paddingChar = '0')        //  getSettlementDate
    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    @Field(offset = 23, length = 10, align = Align.LEFT, paddingChar = ' ')        //  getSubmissionSeAccountNumber
    public String getSubmissionSeAccountNumber() {
        return submissionSeAccountNumber;
    }

    public void setSubmissionSeAccountNumber(String submissionSeAccountNumber) {
        this.submissionSeAccountNumber = submissionSeAccountNumber;
    }

    @Field(offset = 33, length = 1, align = Align.RIGHT, paddingChar = '0')        //  getRecordCode
    public Integer getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(Integer recordCode) {
        this.recordCode = recordCode;
    }

    @Field(offset = 34, length = 2, align = Align.LEFT, paddingChar = ' ')        //  getRecordSubCode
    public String getRecordSubCode() {
        return recordSubCode;
    }

    public void setRecordSubCode(String recordSubCode) {
        this.recordSubCode = recordSubCode;
    }


    @FixedFormatPattern("yyyyMMdd")
    @Field(offset = 41, length = 8, align = Align.RIGHT, paddingChar = '0')        //  getSocDate
    public Date getSocDate() {
        return socDate;
    }

    public void setSocDate(Date socDate) {
        this.socDate = socDate;
    }

    @Field(offset = 49, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    //  getSubmissionCalculatedGrossAmount
    public BigDecimal getSubmissionCalculatedGrossAmount() {
        return submissionCalculatedGrossAmount;
    }

    public void setSubmissionCalculatedGrossAmount(BigDecimal submissionCalculatedGrossAmount) {
        this.submissionCalculatedGrossAmount = submissionCalculatedGrossAmount;
    }

    @Field(offset = 64, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    //  getSubmissionDeclaredGrossAmount
    public BigDecimal getSubmissionDeclaredGrossAmount() {
        return submissionDeclaredGrossAmount;
    }

    public void setSubmissionDeclaredGrossAmount(BigDecimal submissionDeclaredGrossAmount) {
        this.submissionDeclaredGrossAmount = submissionDeclaredGrossAmount;
    }

    @Field(offset = 79, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    //  getDiscountAmount
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Field(offset = 109, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    //  getSettlementNetAmount
    public BigDecimal getSettlementNetAmount() {
        return settlementNetAmount;
    }

    public void setSettlementNetAmount(BigDecimal settlementNetAmount) {
        this.settlementNetAmount = settlementNetAmount;
    }

    @Field(offset = 124, length = 7, align = Align.RIGHT, paddingChar = '0')        //  getServiceFeeRate
    public Integer getServiceFeeRate() {
        return serviceFeeRate;
    }

    public void setServiceFeeRate(Integer serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
    }

    @Field(offset = 171, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    //  getSettlementGrossAmount
    public BigDecimal getSettlementGrossAmount() {
        return settlementGrossAmount;
    }

    public void setSettlementGrossAmount(BigDecimal settlementGrossAmount) {
        this.settlementGrossAmount = settlementGrossAmount;
    }

    @Field(offset = 186, length = 5, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    @FixedFormatDecimal(decimals = 0)
    //  getRocCalculatedCount
    public BigDecimal getRocCalculatedCount() {
        return rocCalculatedCount;
    }

    public void setRocCalculatedCount(BigDecimal rocCalculatedCount) {
        this.rocCalculatedCount = rocCalculatedCount;
    }


    @Field(offset = 216, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    //  getSettlementTaxAmount
    public BigDecimal getSettlementTaxAmount() {
        return settlementTaxAmount;
    }

    public void setSettlementTaxAmount(BigDecimal settlementTaxAmount) {
        this.settlementTaxAmount = settlementTaxAmount;
    }

    @Field(offset = 231, length = 7, align = Align.RIGHT, paddingChar = '0')        //  getSettlementTaxRate
    public Integer getSettlementTaxRate() {
        return settlementTaxRate;
    }

    public void setSettlementTaxRate(Integer settlementTaxRate) {
        this.settlementTaxRate = settlementTaxRate;
    }

    @Field(offset = 238, length = 3, align = Align.LEFT, paddingChar = ' ')        //  getSubmissionCurrencyCode
    public String getSubmissionCurrencyCode() {
        return submissionCurrencyCode;
    }

    public void setSubmissionCurrencyCode(String submissionCurrencyCode) {
        this.submissionCurrencyCode = submissionCurrencyCode;
    }

    @Field(offset = 241, length = 15, align = Align.RIGHT, paddingChar = '0')        //  getSubmissionNumber
    public Integer getSubmissionNumber() {
        return submissionNumber;
    }

    public void setSubmissionNumber(Integer submissionNumber) {
        this.submissionNumber = submissionNumber;
    }

    @Field(offset = 256, length = 10, align = Align.LEFT, paddingChar = ' ')        //  getSubmissionSeBranchNumber
    public String getSubmissionSeBranchNumber() {
        return submissionSeBranchNumber;
    }

    public void setSubmissionSeBranchNumber(String submissionSeBranchNumber) {
        this.submissionSeBranchNumber = submissionSeBranchNumber;
    }

    @Field(offset = 266, length = 2, align = Align.LEFT, paddingChar = ' ')        //  getSubmissionMethodCode
    public String getSubmissionMethodCode() {
        return submissionMethodCode;
    }

    public void setSubmissionMethodCode(String submissionMethodCode) {
        this.submissionMethodCode = submissionMethodCode;
    }

    @Field(offset = 293, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    //  getExchangeRate
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        try {
            return jsonMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ((Object) this).toString();
        }
    }

    public String toCsv() {
        CsvSchema schema = csvMapper.schemaFor(SOCRecord.class).withColumnSeparator(',').withHeader();
        ObjectWriter myObjectWriter = csvMapper.writer(schema);
        try {
            return myObjectWriter.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    public UUID uniqueCode() {
        return UUID.nameUUIDFromBytes(
                new StringBuilder()
                        .append(settlementSeAccountNumber)
                        .append(settlementAccountNameCode)
                        .append(settlementDate)
                        .append(submissionSeAccountNumber)
                        .append(recordCode)
                        .append(recordSubCode)
                        .append(socDate)
                        .append(submissionCalculatedGrossAmount)
                        .append(submissionDeclaredGrossAmount)
                        .append(discountAmount)
                        .append(settlementNetAmount)
                        .append(serviceFeeRate)
                        .append(settlementGrossAmount)
                        .append(rocCalculatedCount)
                        .append(settlementTaxAmount)
                        .append(settlementTaxRate)
                        .append(submissionCurrencyCode)
                        .append(submissionNumber)
                        .append(submissionSeBranchNumber)
                        .append(submissionMethodCode)
                        .append(exchangeRate)
                        .toString().getBytes()
        );
    }

    public SOCRecord parse(FixedFormatManager manager, String line) {
        return manager.load(SOCRecord.class, line);
    }

    public static final class Builder {
        private String paymentId;
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

        private Builder() {
        }

        public static Builder aSOCRecord() {
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

        public Builder withSettlementDate(Date settlementDate) {
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

        public Builder withSocDate(Date socDate) {
            this.socDate = socDate;
            return this;
        }

        public Builder withSubmissionCalculatedGrossAmount(BigDecimal submissionCalculatedGrossAmount) {
            this.submissionCalculatedGrossAmount = submissionCalculatedGrossAmount;
            return this;
        }

        public Builder withSubmissionDeclaredGrossAmount(BigDecimal submissionDeclaredGrossAmount) {
            this.submissionDeclaredGrossAmount = submissionDeclaredGrossAmount;
            return this;
        }

        public Builder withDiscountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
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

        public Builder withSettlementGrossAmount(BigDecimal settlementGrossAmount) {
            this.settlementGrossAmount = settlementGrossAmount;
            return this;
        }

        public Builder withRocCalculatedCount(BigDecimal rocCalculatedCount) {
            this.rocCalculatedCount = rocCalculatedCount;
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

        public Builder withSubmissionCurrencyCode(String submissionCurrencyCode) {
            this.submissionCurrencyCode = submissionCurrencyCode;
            return this;
        }

        public Builder withSubmissionNumber(Integer submissionNumber) {
            this.submissionNumber = submissionNumber;
            return this;
        }

        public Builder withSubmissionSeBranchNumber(String submissionSeBranchNumber) {
            this.submissionSeBranchNumber = submissionSeBranchNumber;
            return this;
        }

        public Builder withSubmissionMethodCode(String submissionMethodCode) {
            this.submissionMethodCode = submissionMethodCode;
            return this;
        }

        public Builder withExchangeRate(BigDecimal exchangeRate) {
            this.exchangeRate = exchangeRate;
            return this;
        }

        public Builder but() {
            return aSOCRecord().withPaymentId(paymentId).withSettlementSeAccountNumber(settlementSeAccountNumber).withSettlementAccountNameCode(settlementAccountNameCode).withSettlementDate(settlementDate).withSubmissionSeAccountNumber(submissionSeAccountNumber).withRecordCode(recordCode).withRecordSubCode(recordSubCode).withSocDate(socDate).withSubmissionCalculatedGrossAmount(submissionCalculatedGrossAmount).withSubmissionDeclaredGrossAmount(submissionDeclaredGrossAmount).withDiscountAmount(discountAmount).withSettlementNetAmount(settlementNetAmount).withServiceFeeRate(serviceFeeRate).withSettlementGrossAmount(settlementGrossAmount).withRocCalculatedCount(rocCalculatedCount).withSettlementTaxAmount(settlementTaxAmount).withSettlementTaxRate(settlementTaxRate).withSubmissionCurrencyCode(submissionCurrencyCode).withSubmissionNumber(submissionNumber).withSubmissionSeBranchNumber(submissionSeBranchNumber).withSubmissionMethodCode(submissionMethodCode).withExchangeRate(exchangeRate);
        }

        public SOCRecord build() {
            SOCRecord sOCRecord = new SOCRecord();
            sOCRecord.setPaymentId(paymentId);
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
}

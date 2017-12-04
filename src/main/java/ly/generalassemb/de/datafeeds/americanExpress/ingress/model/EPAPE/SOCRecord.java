package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;
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
import java.util.List;

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

    public static String toCsv(List<SOCRecord> list) {
        CsvSchema schema = csvMapper.schemaFor(SOCRecord.class).withColumnSeparator(',').withHeader();
        ObjectWriter myObjectWriter = csvMapper.writer(schema);
        try {
            return myObjectWriter.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}

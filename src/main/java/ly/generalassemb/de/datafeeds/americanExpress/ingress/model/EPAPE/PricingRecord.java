package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.AmexSignedNumericFixedFormatter;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by davidashirov on 12/1/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "SETTLEMENT_SE_ACCOUNT_NUMBER",
        "SETTLEMENT_CURRENCY_CODE",

        "RECORD_CODE",
        "RECORD_SUB_CODE",
        "PRICING_DESCRIPTION",
        "DISCOUNT_RATE",
        "FEE_PER_CHARGE",
        "NUMBER_OF_CHARGES",
        "GROSS_AMOUNT",
        "GROSS_DEBIT_AMOUNT",
        "GROSS_CREDEIT_AMOUNT",
        "DISCOUNT_FEE",
        "SERVICE_FEE",
        "NET_AMOUNT"

})
@Record(length = 441)
public class PricingRecord {
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final CsvMapper csvMapper = new CsvMapper();

    @JsonProperty("SETTLEMENT_SE_ACCOUNT_NUMBER")
    @Size(max = 10)
    @javax.validation.constraints.NotNull
    private String settlementSeAccountNumber;

    @JsonProperty("SETTLEMENT_CURRENCY_CODE")
    @Size(max = 3)
    @javax.validation.constraints.NotNull
    private String settlementCurrencyCode;

    @JsonProperty("RECORD_CODE")
    @Size(max = 1)
    @javax.validation.constraints.NotNull
    private Integer recordCode;

    @JsonProperty("RECORD_SUB_CODE")
    @Size(max = 2)
    @javax.validation.constraints.NotNull
    private String recordSubCode;

    @JsonProperty("PRICING_DESCRIPTION")
    @Size(max = 64)
    @javax.validation.constraints.NotNull
    private String pricingDescription;

    @JsonProperty("DISCOUNT_RATE")
    @Size(max = 7)
    @javax.validation.constraints.NotNull
    private Integer discountRate;

    @JsonProperty("FEE_PER_CHARGE")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal feePerCharge;

    @JsonProperty("NUMBER_OF_CHARGES")
    @Size(max = 5)
    @javax.validation.constraints.NotNull
    private BigDecimal numberOfCharges;

    @JsonProperty("GROSS_AMOUNT")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal grossAmount;

    @JsonProperty("GROSS_DEBIT_AMOUNT")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal grossDebitAmount;

    @JsonProperty("GROSS_CREDEIT_AMOUNT")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal grossCredeitAmount;

    @JsonProperty("DISCOUNT_FEE")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal discountFee;

    @JsonProperty("SERVICE_FEE")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal serviceFee;

    @JsonProperty("NET_AMOUNT")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal netAmount;

    @Field(offset = 1, length = 10, align = Align.RIGHT, paddingChar = '0')//  getSettlementSeAccountNumber
    public String getSettlementSeAccountNumber() {
        return settlementSeAccountNumber;
    }

    public void setSettlementSeAccountNumber(String settlementSeAccountNumber) {
        this.settlementSeAccountNumber = settlementSeAccountNumber;
    }

    @Field(offset = 11, length = 3, align = Align.RIGHT, paddingChar = '0')//  getSettlementCurrencyCode
    public String getSettlementCurrencyCode() {
        return settlementCurrencyCode;
    }

    public void setSettlementCurrencyCode(String settlementCurrencyCode) {
        this.settlementCurrencyCode = settlementCurrencyCode;
    }

    @Field(offset = 33, length = 1, align = Align.RIGHT, paddingChar = '0')//  getRecordCode
    public Integer getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(Integer recordCode) {
        this.recordCode = recordCode;
    }

    @Field(offset = 34, length = 2, align = Align.RIGHT, paddingChar = '0')//  getRecordSubCode
    public String getRecordSubCode() {
        return recordSubCode;
    }

    public void setRecordSubCode(String recordSubCode) {
        this.recordSubCode = recordSubCode;
    }

    @Field(offset = 36, length = 64, align = Align.RIGHT, paddingChar = '0')//  getPricingDescription
    public String getPricingDescription() {
        return pricingDescription;
    }

    public void setPricingDescription(String pricingDescription) {
        this.pricingDescription = pricingDescription;
    }

    @Field(offset = 100, length = 7, align = Align.RIGHT, paddingChar = '0')//  getDiscountRate
    public Integer getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
    }

    @Field(offset = 107, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
//  getFeePerCharge
    public BigDecimal getFeePerCharge() {
        return feePerCharge;
    }

    public void setFeePerCharge(BigDecimal feePerCharge) {
        this.feePerCharge = feePerCharge;
    }

    @Field(offset = 122, length = 5, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
//  getNumberOfCharges
    public BigDecimal getNumberOfCharges() {
        return numberOfCharges;
    }

    public void setNumberOfCharges(BigDecimal numberOfCharges) {
        this.numberOfCharges = numberOfCharges;
    }

    @Field(offset = 127, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
//  getGrossAmount
    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    @Field(offset = 142, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
//  getGrossDebitAmount
    public BigDecimal getGrossDebitAmount() {
        return grossDebitAmount;
    }

    public void setGrossDebitAmount(BigDecimal grossDebitAmount) {
        this.grossDebitAmount = grossDebitAmount;
    }

    @Field(offset = 157, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
//  getGrossCredeitAmount
    public BigDecimal getGrossCredeitAmount() {
        return grossCredeitAmount;
    }

    public void setGrossCredeitAmount(BigDecimal grossCredeitAmount) {
        this.grossCredeitAmount = grossCredeitAmount;
    }

    @Field(offset = 172, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
//  getDiscountFee
    public BigDecimal getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(BigDecimal discountFee) {
        this.discountFee = discountFee;
    }


    @Field(offset = 187, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
//  getServiceFee
    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }


    @Field(offset = 202, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
//  getNetAmount
    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
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
        CsvSchema schema = csvMapper.schemaFor(PricingRecord.class).withColumnSeparator(',').withHeader();
        ObjectWriter myObjectWriter = csvMapper.writer(schema);
        try {
            return myObjectWriter.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String toCsv(List<PricingRecord> list) {
        CsvSchema schema = csvMapper.schemaFor(PricingRecord.class).withColumnSeparator(',').withHeader();
        ObjectWriter myObjectWriter = csvMapper.writer(schema);
        try {
            return myObjectWriter.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}

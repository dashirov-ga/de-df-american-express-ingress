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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by davidashirov on 12/1/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "SETTLEMENT_SE_ACCOUNT_NUMBER",
        "SETTLEMENT_ACCOUNT_NAME_CODE",
        "SETTLEMENT_DATE",


        "RECORD_CODE",
        "SOC_RECORD_CODE",

        "SETTLEMENT_AMOUNT",
        "SE_BANK_SORT_CODE",
        "SE_BANK_ACCOUNT_NUMBER",
        "SETTLEMENT_GROSS_AMOUNT",
        "TAX_AMOUNT",
        "TAX_RATE",
        "SERVICE_FEE_AMOUNT",

        "SERVICE_FEE_RATE",

        "SETTLEMENT_ADJUSTMENT_AMOUNT",
        "PAY_PLAN_SHORT_NAME",
        "PAYEE_NAME",
        "SETTLEMENT_ACCOUNT_NAME_CODE_EXTENDED",
        "SETTLEMENT_CURRENCY_CODE",
        "PREVIOUS_DEBIT_BALANCE",
        "IBAN",
        "BIC"
})
@Record(length = 441)
public class PaymentRecord {
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final CsvMapper csvMapper = new CsvMapper();

    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty("GENERATED_PAYMENT_NUMBER")
    public String getPaymentId() {
        return uniqueCode().toString();
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
    private Date settlementDate;


    @JsonProperty("RECORD_CODE")
    @Size(max = 1)
    @javax.validation.constraints.NotNull

    private Integer recordCode;
    @JsonProperty("SOC_RECORD_CODE")
    @Size(max = 2)
    @javax.validation.constraints.NotNull
    private String recordSubCode;

    @JsonProperty("SETTLEMENT_AMOUNT")
    @Size(max = 15)
    @javax.validation.constraints.NotNull

    private BigDecimal settlementAmount;
    @JsonProperty("SE_BANK_SORT_CODE")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private String seBankSortCode;

    @JsonProperty("SE_BANK_ACCOUNT_NUMBER")
    @Size(max = 20)
    @javax.validation.constraints.NotNull
    private String seBankAccountNumber;
    @JsonProperty("SETTLEMENT_GROSS_AMOUNT")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal settlementGrossAmount;
    @JsonProperty("TAX_AMOUNT")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal taxAmount;
    @JsonProperty("TAX_RATE")
    @Size(max = 7)
    @javax.validation.constraints.NotNull
    private Integer taxRate;
    @JsonProperty("SERVICE_FEE_AMOUNT")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal serviceFeeAmount;

    @JsonProperty("SERVICE_FEE_RATE")
    @Size(max = 7)
    @javax.validation.constraints.NotNull
    private Integer serviceFeeRate;

    @JsonProperty("SETTLEMENT_ADJUSTMENT_AMOUNT")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal settlementAdjustmentAmount;
    @JsonProperty("PAY_PLAN_SHORT_NAME")
    @Size(max = 30)
    @javax.validation.constraints.NotNull
    private String payPlanShortName;
    @JsonProperty("PAYEE_NAME")
    @Size(max = 38)
    @javax.validation.constraints.NotNull
    private String payeeName;
    @JsonProperty("SETTLEMENT_ACCOUNT_NAME_CODE_EXTENDED")
    @Size(max = 20)
    @javax.validation.constraints.NotNull
    private String settlementAccountNameCodeExtended;
    @JsonProperty("SETTLEMENT_CURRENCY_CODE")
    @Size(max = 3)
    @javax.validation.constraints.NotNull
    private String settlementCurrencyCode;
    @JsonProperty("PREVIOUS_DEBIT_BALANCE")
    @Size(max = 15)
    @javax.validation.constraints.NotNull
    private BigDecimal previousDebitBalance;
    @JsonProperty("IBAN")
    @Size(max = 34)
    @javax.validation.constraints.NotNull
    private String iban;
    @JsonProperty("BIC")
    @Size(max = 12)
    @javax.validation.constraints.NotNull
    private String bic;


    @Field(offset = 1, length = 10, align = Align.LEFT, paddingChar = ' ')
    public String getSettlementSeAccountNumber() {
        return settlementSeAccountNumber;
    }

    @Field(offset = 11, length = 3, align = Align.LEFT, paddingChar = ' ')
    public String getSettlementAccountNameCode() {
        return settlementAccountNameCode;
    }

    @Field(offset = 14, length = 8, align = Align.RIGHT, paddingChar = '0')
    @FixedFormatPattern("yyyyMMdd")
    public Date getSettlementDate() {
        return settlementDate;
    }

    @Field(offset = 33, length = 1, align = Align.RIGHT, paddingChar = '0')
    public Integer getRecordCode() {
        return recordCode;
    }

    @Field(offset = 34, length = 2, align = Align.LEFT, paddingChar = ' ')
    public String getRecordSubCode() {
        return recordSubCode;
    }

    @Field(offset = 41, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    public BigDecimal getSettlementAmount() {
        return settlementAmount;
    }

    @Field(offset = 56, length = 15, align = Align.LEFT, paddingChar = ' ')
    public String getSeBankSortCode() {
        return seBankSortCode;
    }

    @Field(offset = 71, length = 20, align = Align.LEFT, paddingChar = ' ')
    public String getSeBankAccountNumber() {
        return seBankAccountNumber;
    }

    @Field(offset = 91, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    public BigDecimal getSettlementGrossAmount() {
        return settlementGrossAmount;
    }

    @Field(offset = 106, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    @Field(offset = 121, length = 7, align = Align.RIGHT, paddingChar = '0')
    public Integer getTaxRate() {
        return taxRate;
    }

    @Field(offset = 128, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    public BigDecimal getServiceFeeAmount() {
        return serviceFeeAmount;
    }

    @Field(offset = 158, length = 7, align = Align.RIGHT, paddingChar = '0')
    public Integer getServiceFeeRate() {
        return serviceFeeRate;
    }

    @Field(offset = 220, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    public BigDecimal getSettlementAdjustmentAmount() {
        return settlementAdjustmentAmount;
    }

    @Field(offset = 235, length = 30, align = Align.LEFT, paddingChar = ' ')
    public String getPayPlanShortName() {
        return payPlanShortName;
    }

    @Field(offset = 265, length = 38, align = Align.LEFT, paddingChar = ' ')
    public String getPayeeName() {
        return payeeName;
    }

    @Field(offset = 303, length = 20, align = Align.LEFT, paddingChar = ' ')
    public String getSettlementAccountNameCodeExtended() {
        return settlementAccountNameCodeExtended;
    }

    @Field(offset = 323, length = 3, align = Align.LEFT, paddingChar = ' ')
    public String getSettlementCurrencyCode() {
        return settlementCurrencyCode;
    }

    @Field(offset = 326, length = 15, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    public BigDecimal getPreviousDebitBalance() {
        return previousDebitBalance;
    }

    @Field(offset = 341, length = 34, align = Align.LEFT, paddingChar = ' ')
    public String getIban() {
        return iban;
    }

    @Field(offset = 375, length = 12, align = Align.LEFT, paddingChar = ' ')
    public String getBic() {
        return bic;
    }

    public void setSettlementSeAccountNumber(String settlementSeAccountNumber) {
        this.settlementSeAccountNumber = settlementSeAccountNumber;
    }

    public void setSettlementAccountNameCode(String settlementAccountNameCode) {
        this.settlementAccountNameCode = settlementAccountNameCode;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    public void setRecordCode(Integer recordCode) {
        this.recordCode = recordCode;
    }

    public void setRecordSubCode(String recordSubCode) {
        this.recordSubCode = recordSubCode;
    }

    public void setSettlementAmount(BigDecimal settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public void setSeBankSortCode(String seBankSortCode) {
        this.seBankSortCode = seBankSortCode;
    }

    public void setSeBankAccountNumber(String seBankAccountNumber) {
        this.seBankAccountNumber = seBankAccountNumber;
    }

    public void setSettlementGrossAmount(BigDecimal settlementGrossAmount) {
        this.settlementGrossAmount = settlementGrossAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public void setTaxRate(Integer taxRate) {
        this.taxRate = taxRate;
    }

    public void setServiceFeeAmount(BigDecimal serviceFeeAmount) {
        this.serviceFeeAmount = serviceFeeAmount;
    }

    public void setServiceFeeRate(Integer serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
    }

    public void setSettlementAdjustmentAmount(BigDecimal settlementAdjustmentAmount) {
        this.settlementAdjustmentAmount = settlementAdjustmentAmount;
    }

    public void setPayPlanShortName(String payPlanShortName) {
        this.payPlanShortName = payPlanShortName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public void setSettlementAccountNameCodeExtended(String settlementAccountNameCodeExtended) {
        this.settlementAccountNameCodeExtended = settlementAccountNameCodeExtended;
    }

    public void setSettlementCurrencyCode(String settlementCurrencyCode) {
        this.settlementCurrencyCode = settlementCurrencyCode;
    }

    public void setPreviousDebitBalance(BigDecimal previousDebitBalance) {
        this.previousDebitBalance = previousDebitBalance;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setBic(String bic) {
        this.bic = bic;
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
        CsvSchema schema = csvMapper.schemaFor(PaymentRecord.class).withColumnSeparator(',').withHeader();
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
                .append(recordCode)
                .append(recordSubCode)
                .append(settlementAmount)
                .append(seBankSortCode)
                .append(seBankAccountNumber)
                .append(settlementGrossAmount)
                .append(taxAmount)
                .append(taxRate)
                .append(serviceFeeAmount)
                .append(serviceFeeRate)
                .append(settlementAdjustmentAmount)
                .append(payPlanShortName)
                .append(payeeName)
                .append(settlementAccountNameCodeExtended)
                .append(settlementCurrencyCode)
                .append(previousDebitBalance)
                .append(iban)
                .append(bic)
                .toString().getBytes()
        );
    }

    public static final class Builder {
        private String settlementSeAccountNumber;
        private String settlementAccountNameCode;
        private Date settlementDate;

        private Integer recordCode;
        private String recordSubCode;

        private BigDecimal settlementAmount;
        private String seBankSortCode;
        private String seBankAccountNumber;
        private BigDecimal settlementGrossAmount;
        private BigDecimal taxAmount;
        private Integer taxRate;
        private BigDecimal serviceFeeAmount;
        private Integer serviceFeeRate;
        private BigDecimal settlementAdjustmentAmount;
        private String payPlanShortName;
        private String payeeName;
        private String settlementAccountNameCodeExtended;
        private String settlementCurrencyCode;
        private BigDecimal previousDebitBalance;
        private String iban;
        private String bic;

        private Builder() {
        }

        public static Builder aPaymentRecord() {
            return new Builder();
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

        public Builder withRecordCode(Integer recordCode) {
            this.recordCode = recordCode;
            return this;
        }

        public Builder withRecordSubCode(String recordSubCode) {
            this.recordSubCode = recordSubCode;
            return this;
        }

        public Builder withSettlementAmount(BigDecimal settlementAmount) {
            this.settlementAmount = settlementAmount;
            return this;
        }

        public Builder withSeBankSortCode(String seBankSortCode) {
            this.seBankSortCode = seBankSortCode;
            return this;
        }

        public Builder withSeBankAccountNumber(String seBankAccountNumber) {
            this.seBankAccountNumber = seBankAccountNumber;
            return this;
        }

        public Builder withSettlementGrossAmount(BigDecimal settlementGrossAmount) {
            this.settlementGrossAmount = settlementGrossAmount;
            return this;
        }

        public Builder withTaxAmount(BigDecimal taxAmount) {
            this.taxAmount = taxAmount;
            return this;
        }

        public Builder withTaxRate(Integer taxRate) {
            this.taxRate = taxRate;
            return this;
        }

        public Builder withServiceFeeAmount(BigDecimal serviceFeeAmount) {
            this.serviceFeeAmount = serviceFeeAmount;
            return this;
        }

        public Builder withServiceFeeRate(Integer serviceFeeRate) {
            this.serviceFeeRate = serviceFeeRate;
            return this;
        }

        public Builder withSettlementAdjustmentAmount(BigDecimal settlementAdjustmentAmount) {
            this.settlementAdjustmentAmount = settlementAdjustmentAmount;
            return this;
        }

        public Builder withPayPlanShortName(String payPlanShortName) {
            this.payPlanShortName = payPlanShortName;
            return this;
        }

        public Builder withPayeeName(String payeeName) {
            this.payeeName = payeeName;
            return this;
        }

        public Builder withSettlementAccountNameCodeExtended(String settlementAccountNameCodeExtended) {
            this.settlementAccountNameCodeExtended = settlementAccountNameCodeExtended;
            return this;
        }

        public Builder withSettlementCurrencyCode(String settlementCurrencyCode) {
            this.settlementCurrencyCode = settlementCurrencyCode;
            return this;
        }

        public Builder withPreviousDebitBalance(BigDecimal previousDebitBalance) {
            this.previousDebitBalance = previousDebitBalance;
            return this;
        }

        public Builder withIban(String iban) {
            this.iban = iban;
            return this;
        }

        public Builder withBic(String bic) {
            this.bic = bic;
            return this;
        }

        public Builder but() {
            return aPaymentRecord().withSettlementSeAccountNumber(settlementSeAccountNumber).withSettlementAccountNameCode(settlementAccountNameCode).withSettlementDate(settlementDate).withRecordCode(recordCode).withRecordSubCode(recordSubCode).withSettlementAmount(settlementAmount).withSeBankSortCode(seBankSortCode).withSeBankAccountNumber(seBankAccountNumber).withSettlementGrossAmount(settlementGrossAmount).withTaxAmount(taxAmount).withTaxRate(taxRate).withServiceFeeAmount(serviceFeeAmount).withServiceFeeRate(serviceFeeRate).withSettlementAdjustmentAmount(settlementAdjustmentAmount).withPayPlanShortName(payPlanShortName).withPayeeName(payeeName).withSettlementAccountNameCodeExtended(settlementAccountNameCodeExtended).withSettlementCurrencyCode(settlementCurrencyCode).withPreviousDebitBalance(previousDebitBalance).withIban(iban).withBic(bic);
        }

        public PaymentRecord build() {
            PaymentRecord paymentRecord = new PaymentRecord();
            paymentRecord.setSettlementSeAccountNumber(settlementSeAccountNumber);
            paymentRecord.setSettlementAccountNameCode(settlementAccountNameCode);
            paymentRecord.setSettlementDate(settlementDate);
            paymentRecord.setRecordCode(recordCode);
            paymentRecord.setRecordSubCode(recordSubCode);
            paymentRecord.setSettlementAmount(settlementAmount);
            paymentRecord.setSeBankSortCode(seBankSortCode);
            paymentRecord.setSeBankAccountNumber(seBankAccountNumber);
            paymentRecord.setSettlementGrossAmount(settlementGrossAmount);
            paymentRecord.setTaxAmount(taxAmount);
            paymentRecord.setTaxRate(taxRate);
            paymentRecord.setServiceFeeAmount(serviceFeeAmount);
            paymentRecord.setServiceFeeRate(serviceFeeRate);
            paymentRecord.setSettlementAdjustmentAmount(settlementAdjustmentAmount);
            paymentRecord.setPayPlanShortName(payPlanShortName);
            paymentRecord.setPayeeName(payeeName);
            paymentRecord.setSettlementAccountNameCodeExtended(settlementAccountNameCodeExtended);
            paymentRecord.setSettlementCurrencyCode(settlementCurrencyCode);
            paymentRecord.setPreviousDebitBalance(previousDebitBalance);
            paymentRecord.setIban(iban);
            paymentRecord.setBic(bic);
            return paymentRecord;
        }
    }
}
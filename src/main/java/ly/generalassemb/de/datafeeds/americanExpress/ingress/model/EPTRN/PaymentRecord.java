package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN;

import com.ancientprogramming.fixedformat4j.annotation.*;
import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.AmexSignedNumericFixedFormatter;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.LocalDateFormatter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Record(length = 450)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "AMEX_PAYEE_NUMBER",
        "PAYMENT_YEAR",
        "PAYMENT_NUMBER",
        "RECORD_TYPE",
        "DETAIL_RECORD_TYPE",
        "PAYMENT_DATE",
        "PAYMENT_AMOUNT",
        "DEBIT_BALANCE_AMOUNT",
        "ABA_BANK_NUMBER",
        "SE_DDA_NUMBER"
})
public class PaymentRecord {
    @NotNull
    @JsonProperty("AMEX_PAYEE_NUMBER")
    private BigDecimal payeeNumber;
    @Field(offset=1,length=10,align=Align.RIGHT,paddingChar = '0')        //  getPayeeNumber()
    @FixedFormatNumber(sign = Sign.NOSIGN)
    @FixedFormatDecimal(decimals = 0, useDecimalDelimiter = false)
    public BigDecimal getPayeeNumber() {
        return payeeNumber;
    }
    public void setPayeeNumber(BigDecimal payeeNumber) {
        this.payeeNumber = payeeNumber;
    }

    @NotNull
    @JsonProperty("PAYMENT_YEAR")
    private Integer paymentYear;
    @Field(offset=31,length=4,align=Align.RIGHT,paddingChar = '0')        //  getPaymentYear()
    @FixedFormatNumber(sign = Sign.NOSIGN)
    @FixedFormatDecimal(decimals = 0, useDecimalDelimiter = false)
    public Integer getPaymentYear() {
        return paymentYear;
    }
    public void setPaymentYear(Integer paymentYear) {
        this.paymentYear = paymentYear;
    }

    @NotNull
    @JsonProperty("PAYMENT_NUMBER")
    private String paymentNumber;
    @Field(offset=35,length=8,align=Align.LEFT,paddingChar = ' ')        //  getPaymentNumber()
    public String getPaymentNumber() {
        return paymentNumber;
    }
    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    @NotNull
    @JsonProperty("RECORD_TYPE")
    private String recordType;
    @Field(offset=43,length=1,align=Align.LEFT,paddingChar = ' ')        //  getRecordType()
    public String getRecordType() {
        return recordType;
    }
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @NotNull
    @JsonProperty("DETAIL_RECORD_TYPE")
    private String recordSubType;
    @Field(offset=44,length=2,align=Align.LEFT,paddingChar = ' ')        //  getRecordSubType()
    public String getRecordSubType() {
        return recordSubType;
    }
    public void setRecordSubType(String recordSubType) {
        this.recordSubType = recordSubType;
    }

    @NotNull
    @JsonProperty("PAYMENT_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;
    @FixedFormatPattern("yyyyDDD")
    @Field(offset=46,length=7,align=Align.RIGHT,paddingChar = '0', formatter = LocalDateFormatter.class)        //  getPaymentDate()
    public LocalDate getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    @NotNull
    @JsonProperty("PAYMENT_AMOUNT")
    private BigDecimal paymentAmount;
    @Field(offset=53,length=11,align=Align.RIGHT,paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)        //  getPaymentAmount()
    @FixedFormatNumber(sign = Sign.NOSIGN)
    @FixedFormatDecimal(decimals = 2, useDecimalDelimiter = false)
    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }
    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    @NotNull
    @JsonProperty("DEBIT_BALANCE_AMOUNT")
    private BigDecimal debitBalanceAmount;
    @Field(offset=64,length=9,align=Align.RIGHT,paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)        //  getDebitBalanceAmount()
    @FixedFormatNumber(sign = Sign.NOSIGN)
    @FixedFormatDecimal(decimals = 2, useDecimalDelimiter = false)
    public BigDecimal getDebitBalanceAmount() {
        return debitBalanceAmount;
    }
    public void setDebitBalanceAmount(BigDecimal debitBalanceAmount) {
        this.debitBalanceAmount = debitBalanceAmount;
    }

    @NotNull
    @JsonProperty("ABA_BANK_NUMBER")
    // American Banking Association assigned unique bank identification number
    private BigDecimal abaBankNumber;
    @Field(offset=73,length=9,align=Align.RIGHT,paddingChar = '0')        //  getAbaBankNumber()
    @FixedFormatNumber(sign = Sign.NOSIGN)
    @FixedFormatDecimal(decimals = 0, useDecimalDelimiter = false)
    public BigDecimal getAbaBankNumber() {
        return abaBankNumber;
    }
    public void setAbaBankNumber(BigDecimal abaBankNumber) {
        this.abaBankNumber = abaBankNumber;
    }

    @NotNull
    @JsonProperty("SE_DDA_NUMBER")
    private String serviceEstablishmentDirectDepositAccountNumber;
    @Field(offset=82,length=17,align=Align.LEFT,paddingChar = ' ')        //  getServiceEstablishmentDirectDepositAccountNumber()
    public String getServiceEstablishmentDirectDepositAccountNumber() {
        return serviceEstablishmentDirectDepositAccountNumber;
    }
    public void setServiceEstablishmentDirectDepositAccountNumber(String serviceEstablishmentDirectDepositAccountNumber) {
        this.serviceEstablishmentDirectDepositAccountNumber = serviceEstablishmentDirectDepositAccountNumber;
    }


    public PaymentRecord parse(FixedFormatManager manager, String line){
        return manager.load(PaymentRecord.class,line);
    }
}

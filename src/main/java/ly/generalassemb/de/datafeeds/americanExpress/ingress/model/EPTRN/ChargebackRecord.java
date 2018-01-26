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
        "AMEX_SE_NUMBER",
        "SE_UNIT_NUMBER",
        "PAYMENT_YEAR",
        "PAYMENT_NUMBER",
        "RECORD_TYPE",
        "DETAIL_RECORD_TYPE",
        "SE_BUSINESS_DATE",
        "AMEX_PROCESS_DATE",
        "SOC_INVOICE_NUMBER",
        "SOC_AMOUNT",
        "CHARGEBACK_AMOUNT",
        "DISCOUNT_AMOUNT",
        "SERVICE_FEE_AMOUNT",
        "NET_CHARGEBACK_AMOUNT",
        "DISCOUNT_RATE",
        "SERVICE_FEE_RATE",
        "CHARGEBACK_REASON"
})
public class ChargebackRecord {
    @JsonProperty("AMEX_PAYEE_NUMBER")
    @NotNull
    private BigDecimal payeeNumber;
    @Field(offset=1,length=10,align= Align.RIGHT,paddingChar = '0')        //  getPayeeNumber()
    @FixedFormatDecimal(decimals = 0)
    public BigDecimal getPayeeNumber() {
        return payeeNumber;
    }
    public void setPayeeNumber(BigDecimal payeeNumber) {
        this.payeeNumber = payeeNumber;
    }

    @JsonProperty("AMEX_SE_NUMBER")
    @NotNull
    private BigDecimal serviceEstablishmentNumber;
    @Field(offset=11,length=10,align=Align.RIGHT,paddingChar = '0')        //  getServiceEstablishmentNumber()
    @FixedFormatDecimal(decimals = 0)
    public BigDecimal getServiceEstablishmentNumber() {
        return serviceEstablishmentNumber;
    }
    public void setServiceEstablishmentNumber(BigDecimal serviceEstablishmentNumber) {
        this.serviceEstablishmentNumber = serviceEstablishmentNumber;
    }

    @JsonProperty("SE_UNIT_NUMBER")
    private String serviceEstablishmentUnitNumber;
    @Field(offset=21,length=10,align=Align.LEFT,paddingChar = ' ')        //  getServiceEstablishmentUnitNumber()
    public String getServiceEstablishmentUnitNumber() {
        return serviceEstablishmentUnitNumber;
    }

    public void setServiceEstablishmentUnitNumber(String serviceEstablishmentUnitNumber) {
        this.serviceEstablishmentUnitNumber = serviceEstablishmentUnitNumber;
    }

    @JsonProperty("PAYMENT_YEAR")
    @NotNull
    private BigDecimal paymentYear;
    @Field(offset=31,length=4,align=Align.RIGHT,paddingChar = '0')        //  getPaymentYear()
    @FixedFormatDecimal(decimals = 0)
    public BigDecimal getPaymentYear() {
        return paymentYear;
    }
    public void setPaymentYear(BigDecimal paymentYear) {
        this.paymentYear = paymentYear;
    }

    @JsonProperty("PAYMENT_NUMBER")
    @NotNull
    private String paymentNumber;
    @Field(offset=35,length=8,align=Align.LEFT,paddingChar = ' ')        //  getPaymentNumber()
    public String getPaymentNumber() {
        return paymentNumber;
    }
    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    @JsonProperty("RECORD_TYPE")
    @NotNull
    private String recordType;
    @Field(offset=43,length=1,align=Align.LEFT,paddingChar = ' ')        //  getRecordType()
    public String getRecordType() {
        return recordType;
    }
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @JsonProperty("DETAIL_RECORD_TYPE")
    @NotNull
    private String recordSubType;
    @Field(offset=44,length=2,align=Align.LEFT,paddingChar = ' ')        //  getRecordSubType()
    public String getRecordSubType() {
        return recordSubType;
    }
    public void setRecordSubType(String recordSubType) {
        this.recordSubType = recordSubType;
    }

    @JsonProperty("SE_BUSINESS_DATE")
    @NotNull

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate serviceEstablishmentBusinessDate;
    @Field(offset=46,length=7,align=Align.RIGHT,paddingChar = '0', formatter = LocalDateFormatter.class)
    @FixedFormatPattern("yyyyDDD")
    public LocalDate getServiceEstablishmentBusinessDate() {
        return serviceEstablishmentBusinessDate;
    }
    public void setServiceEstablishmentBusinessDate(LocalDate serviceEstablishmentBusinessDate) {
        this.serviceEstablishmentBusinessDate = serviceEstablishmentBusinessDate;
    }

    @JsonProperty("AMEX_PROCESS_DATE")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate processingDate;
    @Field(offset=53,length=7,align=Align.RIGHT,paddingChar = '0', formatter = LocalDateFormatter.class)        //  getProcessingDate()
    @FixedFormatPattern("yyyyDDD")
    public LocalDate getProcessingDate() {
        return processingDate;
    }
    public void setProcessingDate(LocalDate processingDate) {
        this.processingDate = processingDate;
    }

    @JsonProperty("SOC_INVOICE_NUMBER")
    @NotNull
    private Integer socInvoiceNumber;
    @Field(offset=60,length=6,align=Align.RIGHT,paddingChar = '0')        //  getSocInvoiceNumber()
    @FixedFormatDecimal(decimals = 0)
    public Integer getSocInvoiceNumber() {
        return socInvoiceNumber;
    }
    public void setSocInvoiceNumber(Integer socInvoiceNumber) {
        this.socInvoiceNumber = socInvoiceNumber;
    }

    @JsonProperty("SOC_AMOUNT")
    @NotNull
    private BigDecimal socAmount;
    @Field(offset=66,length=11,align=Align.RIGHT,paddingChar = '0', formatter=AmexSignedNumericFixedFormatter.class)        //  getSocAmount()
    @FixedFormatDecimal(decimals = 2)
    public BigDecimal getSocAmount() {
        return socAmount;
    }
    public void setSocAmount(BigDecimal socAmount) {
        this.socAmount = socAmount;
    }

    @JsonProperty("CHARGEBACK_AMOUNT")
    @NotNull
    private BigDecimal chargebackAmount;
    @Field(offset=77,length=9,align=Align.RIGHT,paddingChar = '0', formatter=AmexSignedNumericFixedFormatter.class)        //  getChargebackAmount()
    @FixedFormatDecimal(decimals = 2)
    public BigDecimal getChargebackAmount() {
        return chargebackAmount;
    }
    public void setChargebackAmount(BigDecimal chargebackAmount) {
        this.chargebackAmount = chargebackAmount;
    }

    @JsonProperty("DISCOUNT_AMOUNT")
    @NotNull
    private BigDecimal discountAmount;
    @Field(offset=86,length=9,align=Align.RIGHT,paddingChar = '0', formatter=AmexSignedNumericFixedFormatter.class)        //  getDiscountAmount()
    @FixedFormatDecimal(decimals = 2)
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    @JsonProperty("SERVICE_FEE_AMOUNT")
    @NotNull
    private BigDecimal serviceFeeAmount;
    @Field(offset=95,length=7,align=Align.RIGHT,paddingChar = '0', formatter=AmexSignedNumericFixedFormatter.class)        //  getServiceFeeAmount()
    @FixedFormatDecimal(decimals = 2)
    public BigDecimal getServiceFeeAmount() {
        return serviceFeeAmount;
    }
    public void setServiceFeeAmount(BigDecimal serviceFeeAmount) {
        this.serviceFeeAmount = serviceFeeAmount;
    }

    @JsonProperty("NET_CHARGEBACK_AMOUNT")
    @NotNull
    private BigDecimal netChargebackAmount;
    @Field(offset=109,length=9,align=Align.RIGHT,paddingChar = '0', formatter=AmexSignedNumericFixedFormatter.class)        //  getNetChargebackAmount()
    @FixedFormatDecimal(decimals = 2)
    public BigDecimal getNetChargebackAmount() {
        return netChargebackAmount;
    }
    public void setNetChargebackAmount(BigDecimal netChargebackAmount) {
        this.netChargebackAmount = netChargebackAmount;
    }

    @JsonProperty("DISCOUNT_RATE")
    @NotNull
    private Double discountRate;
    @Field(offset=118,length=5,align=Align.RIGHT,paddingChar = '0')        //  getDiscountRate()
    @FixedFormatDecimal(decimals = 5)
    public Double getDiscountRate() {
        return discountRate;
    }
    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    @JsonProperty("SERVICE_FEE_RATE")
    @NotNull
    private Double serviceFeeRate;
    @Field(offset=123,length=5,align=Align.RIGHT,paddingChar = '0')        //  getServiceFeeRate()
    @FixedFormatDecimal(decimals = 5)
    public Double getServiceFeeRate() {
        return serviceFeeRate;
    }
    public void setServiceFeeRate(Double serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
    }

    @JsonProperty("CHARGEBACK_REASON")
    @NotNull
    private String chargebackReason;
    @Field(offset=144,length=280,align=Align.LEFT,paddingChar = ' ')        //  getChargebackReason()

    public String getChargebackReason() {
        return chargebackReason;
    }
    public void setChargebackReason(String chargebackReason) {
        this.chargebackReason = chargebackReason;
    }

    public ChargebackRecord parse(FixedFormatManager manager, String line){
        return manager.load(ChargebackRecord.class,line);
    }
}

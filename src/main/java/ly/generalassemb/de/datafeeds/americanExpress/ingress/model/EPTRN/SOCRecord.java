package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN;

import com.ancientprogramming.fixedformat4j.annotation.*;
import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.fasterxml.jackson.annotation.*;
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
        "DISCOUNT_AMOUNT",
        "SERVICE_FEE_AMOUNT",

        "NET_SOC_AMOUNT",
        "DISCOUNT_RATE",
        "SERVICE_FEE_RATE",


        "AMEX_GROSS_AMOUNT",
        "AMEX_ROC_COUNT",
        "TRACKING_ID",
        "CPC_INDICATOR",


        "AMEX_ROC_COUNT_POA"
})
public class SOCRecord {

    @NotNull
    @JsonProperty("AMEX_PAYEE_NUMBER")
    private BigDecimal payeeNumber;
    @Field(offset=1,length=10,align= Align.RIGHT,paddingChar = '0')        //  getPayeeNumber()
    @FixedFormatDecimal(decimals = 0)
    @FixedFormatNumber(sign = Sign.NOSIGN)
    public BigDecimal getPayeeNumber() {
        return payeeNumber;
    }
    public void setPayeeNumber(BigDecimal payeeNumber) {
        this.payeeNumber = payeeNumber;
    }

    @NotNull
    @JsonProperty("AMEX_SE_NUMBER")
    private BigDecimal serviceEstablishmentNumber;
    @Field(offset=11,length=10,align=Align.RIGHT,paddingChar = '0')        //  getServiceEstablishmentNumber()
    @FixedFormatDecimal(decimals = 0)
    @FixedFormatNumber(sign = Sign.NOSIGN)
    public BigDecimal getServiceEstablishmentNumber() {
        return serviceEstablishmentNumber;
    }
    public void setServiceEstablishmentNumber(BigDecimal serviceEstablishmentNumber) {
        this.serviceEstablishmentNumber = serviceEstablishmentNumber;
    }

    // Optional
    @JsonProperty("SE_UNIT_NUMBER")
    private String serviceEstablishmentUnitNumber;
    @Field(offset=21,length=10,align=Align.LEFT,paddingChar = ' ')        //  getServiceEstablishmentUnitNumber()
    public String getServiceEstablishmentUnitNumber() {
        return serviceEstablishmentUnitNumber;
    }
    public void setServiceEstablishmentUnitNumber(String serviceEstablishmentUnitNumber) {
        this.serviceEstablishmentUnitNumber = serviceEstablishmentUnitNumber;
    }

    @NotNull
    @JsonProperty("PAYMENT_YEAR")
    private Integer paymentYear;
    @Field(offset=31,length=4,align=Align.RIGHT,paddingChar = '0')        //  getPaymentYear()
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
    @JsonProperty("SE_BUSINESS_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate serviceEstablishmentBusinessDate;
    @Field(offset=46,length=7,align=Align.RIGHT,paddingChar = '0',formatter = LocalDateFormatter.class)        //  getServiceEstablishmentBusinessDate()
    @FixedFormatPattern("yyyyDDD")
    public LocalDate getServiceEstablishmentBusinessDate() {
        return serviceEstablishmentBusinessDate;
    }
    public void setServiceEstablishmentBusinessDate(LocalDate serviceEstablishmentBusinessDate) {
        this.serviceEstablishmentBusinessDate = serviceEstablishmentBusinessDate;
    }

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("AMEX_PROCESS_DATE")
    private LocalDate amexProcessDate;
    @Field(offset=53,length=7,align=Align.RIGHT,paddingChar = '0',formatter = LocalDateFormatter.class)        //  getAmexProcessDate()
    @FixedFormatPattern("yyyyDDD")
    public LocalDate getAmexProcessDate() {
        return amexProcessDate;
    }
    public void setAmexProcessDate(LocalDate amexProcessDate) {
        this.amexProcessDate = amexProcessDate;
    }

    @JsonProperty("SOC_INVOICE_NUMBER")
    private Integer socInvoiceNumber;
    @Field(offset=60,length=6,align=Align.RIGHT,paddingChar = '0')        //  getSocInvoiceNumber()
    @FixedFormatDecimal(decimals = 0)
    @FixedFormatNumber(sign = Sign.NOSIGN)
    public Integer getSocInvoiceNumber() {
        return socInvoiceNumber;
    }
    public void setSocInvoiceNumber(Integer socInvoiceNumber) {
        this.socInvoiceNumber = socInvoiceNumber;
    }

    @NotNull
    @JsonProperty("SOC_AMOUNT")
    private BigDecimal socAmount;
    @Field(offset=66,length=11,align=Align.RIGHT,paddingChar = '0', formatter=AmexSignedNumericFixedFormatter.class)        //  getSocAmount()
    @FixedFormatDecimal(decimals = 2, useDecimalDelimiter = false)
    @FixedFormatNumber(sign = Sign.NOSIGN)
    public BigDecimal getSocAmount() {
        return socAmount;
    }
    public void setSocAmount(BigDecimal socAmount) {
        this.socAmount = socAmount;
    }

    @NotNull
    @JsonProperty("DISCOUNT_AMOUNT")
    private BigDecimal discountAmount;
    @Field(offset=77,length=9,align=Align.RIGHT,paddingChar = '0', formatter=AmexSignedNumericFixedFormatter.class)        //  getDiscountAmount()
    @FixedFormatDecimal(decimals = 2, useDecimalDelimiter = false)
    @FixedFormatNumber(sign = Sign.NOSIGN)
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    @NotNull
    @JsonProperty("SERVICE_FEE_AMOUNT")
    private BigDecimal serviceFeeAmount;
    @Field(offset=86,length=7,align=Align.RIGHT,paddingChar = '0', formatter=AmexSignedNumericFixedFormatter.class)        //  getServiceFeeAmount()
    @FixedFormatDecimal(decimals = 2, useDecimalDelimiter = false)
    @FixedFormatNumber(sign = Sign.NOSIGN)
    public BigDecimal getServiceFeeAmount() {
        return serviceFeeAmount;
    }
    public void setServiceFeeAmount(BigDecimal serviceFeeAmount) {
        this.serviceFeeAmount = serviceFeeAmount;
    }

    @JsonProperty("NET_SOC_AMOUNT")
    private BigDecimal netSocAmount;
    @Field(offset=100,length=11,align=Align.RIGHT,paddingChar = '0', formatter=AmexSignedNumericFixedFormatter.class)        //  getNetSocAmount()
    @FixedFormatDecimal(decimals = 2, useDecimalDelimiter = false)
    @FixedFormatNumber(sign = Sign.NOSIGN)
    public BigDecimal getNetSocAmount() {
        return netSocAmount;
    }
    public void setNetSocAmount(BigDecimal netSocAmount) {
        this.netSocAmount = netSocAmount;
    }


    // WARNING: Due to a bug in fixedformat4j that causes decimal point to not be introduced properly during parsing
    // [ https://github.com/jeyben/fixedformat4j/issues/44 ]
    // Padding Character is changed from '0' to ' ' as a temporary workaround
    @JsonProperty("DISCOUNT_RATE")
    private BigDecimal discountRate;
    @Field(offset=111,length=5,align=Align.RIGHT,paddingChar = '0')        //  getDiscountRate()
    @FixedFormatDecimal(decimals = 5,roundingMode = 4)
    @FixedFormatNumber(sign = Sign.NOSIGN)
    public BigDecimal getDiscountRate() {
        return discountRate;
    }
    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    // WARNING: Due to a bug in fixedformat4j that causes decimal point to not be introduced properly during parsing
    // [ https://github.com/jeyben/fixedformat4j/issues/44 ]
    // Padding Character is changed from '0' to ' ' as a temporary workaround
    @JsonProperty("SERVICE_FEE_RATE")
    private BigDecimal serviceFeeRate;
    @Field(offset=116,length=5,align=Align.RIGHT,paddingChar = '0')        //  getServiceFeeRate()
    @FixedFormatDecimal(decimals = 5,roundingMode = 4)
    @FixedFormatNumber(sign = Sign.NOSIGN)
    public BigDecimal getServiceFeeRate() {
        return serviceFeeRate;
    }
    public void setServiceFeeRate(BigDecimal serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
    }

    @JsonProperty("AMEX_GROSS_AMOUNT")
    private BigDecimal amexGrossAmount;
    @Field(offset=142,length=11,align=Align.RIGHT,paddingChar = '0', formatter=AmexSignedNumericFixedFormatter.class)        //  getAmexGrossAmount()
    @FixedFormatDecimal(decimals = 2, useDecimalDelimiter = false)
    @FixedFormatNumber(sign = Sign.NOSIGN)
    public BigDecimal getAmexGrossAmount() {
        return amexGrossAmount;
    }
    public void setAmexGrossAmount(BigDecimal amexGrossAmount) {
        this.amexGrossAmount = amexGrossAmount;
    }

    @JsonProperty("AMEX_ROC_COUNT")
    private BigDecimal amexRocCount;
    @Field(offset=153,length=5,align=Align.RIGHT,paddingChar = '0', formatter=AmexSignedNumericFixedFormatter.class)        //  getAmexRocCount()
    @FixedFormatDecimal(decimals = 0)
    @FixedFormatNumber(sign = Sign.NOSIGN)
    public BigDecimal getAmexRocCount() {
        return amexRocCount;
    }
    public void setAmexRocCount(BigDecimal amexRocCount) {
        this.amexRocCount = amexRocCount;
    }

    @JsonProperty("TRACKING_ID")
    private Integer trackingId;
    @Field(offset=158,length=9,align=Align.RIGHT,paddingChar = '0')        //  getTrackingId()
    @FixedFormatDecimal(decimals = 0)
    @FixedFormatNumber(sign = Sign.NOSIGN)
    public Integer getTrackingId() {
        return trackingId;
    }
    public void setTrackingId(Integer trackingId) {
        this.trackingId = trackingId;
    }

    @JsonProperty("CPC_INDICATOR")
    private String cpcIndicator;
    @Field(offset=167,length=1,align=Align.LEFT,paddingChar = ' ')        //  getCpcIndicator()
    public String getCpcIndicator() {
        return cpcIndicator;
    }
    public void setCpcIndicator(String cpcIndicator) {
        this.cpcIndicator = cpcIndicator;
    }

    @JsonProperty("AMEX_ROC_COUNT_POA")
    private BigDecimal amexRocCountPoa;
    @Field(offset=183,length=7,align=Align.RIGHT,paddingChar = '0', formatter=AmexSignedNumericFixedFormatter.class)        //  getAmexRocCountPoa()
    @FixedFormatDecimal(decimals = 0, useDecimalDelimiter = false)
    @FixedFormatNumber(sign = Sign.NOSIGN)
    public BigDecimal getAmexRocCountPoa() {
        return amexRocCountPoa;
    }
    public void setAmexRocCountPoa(BigDecimal amexRocCountPoa) {
        this.amexRocCountPoa = amexRocCountPoa;
    }

    @JsonIgnore
    public LocalDate getPaymentDate(){
        return LocalDate.ofYearDay(this.getPaymentYear(),Integer.valueOf(this.getPaymentNumber().substring(0,3)));
    }

    @JsonIgnore
    public Integer getPaymentCheckNumber(){
        return Integer.valueOf(this.getPaymentNumber().substring(4,8));
    }

    public SOCRecord parse(FixedFormatManager manager, String line){
        return manager.load(SOCRecord.class,line);
    }
}

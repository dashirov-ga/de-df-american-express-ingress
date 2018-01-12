package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN;

import com.ancientprogramming.fixedformat4j.annotation.*;
import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.AmexSignedNumericFixedFormatter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

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
        "AMEX_PROCESS_DATE",
        "ADJUSTMENT_NUMBER",
        "ADJUSTMENT_AMOUNT",
        "DISCOUNT_AMOUNT",
        "SERVICE_FEE_AMOUNT",
        "NET_ADJUSTMENT_AMOUNT",
        "DISCOUNT_RATE",
        "SERVICE_FEE_RATE",
        "CARDMEMBER_NUMBER",
        "ADJUSTMENT_REASON"
})
public class AdjustmentRecord {
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
    @Field(offset=11,length=10,align=Align.RIGHT,paddingChar = '0')
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
    private Integer paymentYear;
    @Field(offset=31,length=4,align=Align.RIGHT,paddingChar = '0')
    @FixedFormatDecimal(decimals = 0)
    public Integer getPaymentYear() {
        return paymentYear;
    }
    public void setPaymentYear(Integer paymentYear) {
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

    @JsonProperty("AMEX_PROCESS_DATE")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date processingDate;
    @Field(offset=46,length=7,align=Align.RIGHT,paddingChar = '0')
    @FixedFormatPattern("yyyyDDD")
    public Date getProcessingDate() {
        return processingDate;
    }
    public void setProcessingDate(Date processingDate) {
        this.processingDate = processingDate;
    }

    @JsonProperty("ADJUSTMENT_NUMBER")
    @NotNull
    private Integer adjustmentNumber;
    @Field(offset=53,length=6,align=Align.RIGHT,paddingChar = '0')        //  getAdjustmentNumber()
    @FixedFormatDecimal(decimals = 0)
    public Integer getAdjustmentNumber() {
        return adjustmentNumber;
    }
    public void setAdjustmentNumber(Integer adjustmentNumber) {
        this.adjustmentNumber = adjustmentNumber;
    }

    @JsonProperty("ADJUSTMENT_AMOUNT")
    @NotNull
    private BigDecimal adjustmentAmount;
    @Field(offset=59,length=9,align=Align.LEFT,paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)        //  getAdjustmentAmount()
    @FixedFormatDecimal(decimals = 2)
    public BigDecimal getAdjustmentAmount() {
        return adjustmentAmount;
    }
    public void setAdjustmentAmount(BigDecimal adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    @JsonProperty("DISCOUNT_AMOUNT")
    @NotNull
    private BigDecimal discountAmount;
    @FixedFormatDecimal(decimals = 2)
    @Field(offset=68,length=9,align=Align.LEFT,paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)        //  getDiscountAmount()
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    @JsonProperty("SERVICE_FEE_AMOUNT")
    @NotNull
    private BigDecimal serviceFeeAmount;
    @Field(offset=77,length=7,align=Align.LEFT,paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)        //  getServiceFeeAmount()
    @FixedFormatDecimal(decimals = 2)
    public BigDecimal getServiceFeeAmount() {
        return serviceFeeAmount;
    }
    public void setServiceFeeAmount(BigDecimal serviceFeeAmount) {
        this.serviceFeeAmount = serviceFeeAmount;
    }

    @JsonProperty("NET_ADJUSTMENT_AMOUNT")
    @NotNull
    private BigDecimal netChargebackAmount;
    @Field(offset=91,length=9,align=Align.LEFT,paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
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
    @Field(offset=100,length=5,align=Align.RIGHT,paddingChar = '0')        //  getDiscountRate()
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
    @Field(offset=105,length=5,align=Align.RIGHT,paddingChar = '0')        //  getServiceFeeRate()
    @FixedFormatDecimal(decimals = 5)
    public Double getServiceFeeRate() {
        return serviceFeeRate;
    }
    public void setServiceFeeRate(Double serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
    }

    @JsonProperty("CARDMEMBER_NUMBER")
    @NotNull
    private String cardmemberNumber;
    @Field(offset=126,length=17,align=Align.RIGHT,paddingChar = '0')        //  getCardmemberNumber()
    public String getCardmemberNumber() {
        return cardmemberNumber;
    }
    public void setCardmemberNumber(String cardmemberNumber) {
        this.cardmemberNumber = cardmemberNumber;
    }

    @JsonProperty("ADJUSTMENT_REASON")
    @NotNull
    private String adjustmentReason;
    @Field(offset=143,length=280,align=Align.LEFT,paddingChar = ' ')        //  getAdjustmentReason()
    public String getAdjustmentReason() {
        return adjustmentReason;
    }
    public void setAdjustmentReason(String adjustmentReason) {
        this.adjustmentReason = adjustmentReason;
    }

    public AdjustmentRecord parse(FixedFormatManager manager, String line){
        return manager.load(AdjustmentRecord.class,line);
    }
}

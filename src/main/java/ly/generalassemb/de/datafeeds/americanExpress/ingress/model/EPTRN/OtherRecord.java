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
        "AMEX_PROCESS_DATE",
        "ASSET_BILLING_AMOUNT",
        "ASSET_BILLING_DESCRIPTION",
        "TAKE_ONE_COMMISSION_AMOUNT",
        "TAKE_ONE_DESCRIPTION",
        "OTHER_FEE_AMOUNT",
        "OTHER_FEE_DESCRIPTION",
        "ASSET_BILLING_TAX",
        "PAY_IN_GROSS_INDICATOR"

})
public class OtherRecord {
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
    private Integer paymentYear;
    @Field(offset=31,length=4,align=Align.RIGHT,paddingChar = '0'
    )        //  getPaymentYear()
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
    private LocalDate processDate;
    @Field(offset=46,length=7,align=Align.RIGHT,paddingChar = '0', formatter = LocalDateFormatter.class)        //  getProcessDate()
    @FixedFormatPattern("yyyyDDD")
    public LocalDate getProcessDate() {
        return processDate;
    }
    public void setProcessDate(LocalDate processDate) {
        this.processDate = processDate;
    }

    @JsonProperty("ASSET_BILLING_AMOUNT")
    @NotNull
    private BigDecimal assetBillingAmount;
    @Field(offset=53,length=9,align=Align.RIGHT,paddingChar = '0',  formatter=AmexSignedNumericFixedFormatter.class)        //  getAssetBillingAmount()
    @FixedFormatDecimal(decimals = 2)
    public BigDecimal getAssetBillingAmount() {
        return assetBillingAmount;
    }
    public void setAssetBillingAmount(BigDecimal assetBillingAmount) {
        this.assetBillingAmount = assetBillingAmount;
    }

    @JsonProperty("ASSET_BILLING_DESCRIPTION")
    @NotNull
    private String assetBillingDescription;
    @Field(offset=62,length=65,align=Align.LEFT,paddingChar = ' ')        //  getAssetBillingDescription()
    public String getAssetBillingDescription() {
        return assetBillingDescription;
    }
    public void setAssetBillingDescription(String assetBillingDescription) {
        this.assetBillingDescription = assetBillingDescription;
    }

    @JsonProperty("TAKE_ONE_COMMISSION_AMOUNT")
    @NotNull
    private BigDecimal takeOneCommissionAmount;
    @Field(offset=127,length=9,align=Align.RIGHT,paddingChar = '0',  formatter=AmexSignedNumericFixedFormatter.class)        //  getTakeOneCommissionAmount()
    public BigDecimal getTakeOneCommissionAmount() {
        return takeOneCommissionAmount;
    }
    public void setTakeOneCommissionAmount(BigDecimal takeOneCommissionAmount) {
        this.takeOneCommissionAmount = takeOneCommissionAmount;
    }

    @JsonProperty("TAKE_ONE_DESCRIPTION")
    @NotNull
    private String takeOneDescription;
    @Field(offset=136,length=80,align=Align.LEFT,paddingChar = ' ')        //  getTakeOneDescription()
    public String getTakeOneDescription() {
        return takeOneDescription;
    }
    public void setTakeOneDescription(String takeOneDescription) {
        this.takeOneDescription = takeOneDescription;
    }

    @JsonProperty("OTHER_FEE_AMOUNT")
    @NotNull
    private BigDecimal otherFeeAmount;
    @Field(offset=216,length=9,align=Align.RIGHT,paddingChar = '0',  formatter=AmexSignedNumericFixedFormatter.class)        //  getOtherFeeAmount()
    @FixedFormatDecimal(decimals = 2)
    public BigDecimal getOtherFeeAmount() {
        return otherFeeAmount;
    }
    public void setOtherFeeAmount(BigDecimal otherFeeAmount) {
        this.otherFeeAmount = otherFeeAmount;
    }

    @JsonProperty("OTHER_FEE_DESCRIPTION")
    @NotNull
    private String otherFeeDescription;
    @Field(offset=225,length=80,align=Align.LEFT,paddingChar = ' ')        //  getOtherFeeDescription()

    public String getOtherFeeDescription() {
        return otherFeeDescription;
    }

    public void setOtherFeeDescription(String otherFeeDescription) {
        this.otherFeeDescription = otherFeeDescription;
    }

    @JsonProperty("ASSET_BILLING_TAX")
    @NotNull
    private BigDecimal assetBillingTax;
    @Field(offset=305,length=9,align=Align.LEFT,paddingChar =  '0',  formatter=AmexSignedNumericFixedFormatter.class)        //  getAssetBillingTax()
    @FixedFormatDecimal(decimals = 2)
    public BigDecimal getAssetBillingTax() {
        return assetBillingTax;
    }
    public void setAssetBillingTax(BigDecimal assetBillingTax) {
        this.assetBillingTax = assetBillingTax;
    }

    @JsonProperty("PAY_IN_GROSS_INDICATOR")
    private String payInGrossIndicator;
    @Field(offset=314,length=1,align=Align.LEFT,paddingChar = ' ')        //  getPayInGrossIndicator()
    public String getPayInGrossIndicator() {
        return payInGrossIndicator;
    }
    public void setPayInGrossIndicator(String payInGrossIndicator) {
        this.payInGrossIndicator = payInGrossIndicator;
    }
    public OtherRecord parse(FixedFormatManager manager, String line){
        return manager.load(OtherRecord.class,line);
    }
}

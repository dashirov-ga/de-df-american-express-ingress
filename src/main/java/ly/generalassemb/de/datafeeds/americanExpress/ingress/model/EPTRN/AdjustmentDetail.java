package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by dashirov on 5/12/17.
 */
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
public class AdjustmentDetail {
    public final static Pattern pattern = Pattern.compile("^(?<amexPayeeNumber>\\p{Digit}{10})(?<amexSeNumber>[\\p{Alnum}]{10})(?<seUnitNumber>[\\p{Alnum}\\p{Blank}]{10})(?<paymentYear>\\p{Digit}{4})(?<paymentNumber>(?<paymentNumberJulianDate>\\p{Digit}{3})(?<paymentNumberRecordTypeIndicator>\\p{Alnum}{1})(?<paymentNumberSequence>\\p{Digit}{4}))(?<recordType>2)(?<detailRecordType>30)(?<amexProcessDate>(?<amexProcessDateYear>\\p{Digit}{4})(?<amexProcessJulianDate>\\p{Digit}{3}))(?<adjustmentNumber>\\p{Digit}{6})(?<adjustmentAmount>(?<adjustmentAmountPrefix>\\p{Digit}{8})(?<adjustmentAmountSuffix>[A-R}{]{1}))(?<discountAmount>(?<discountAmountPrefix>\\p{Digit}{8})(?<discountAmountSuffix>[A-R}{]{1}))(?<serviceFeeAmount>(?<serviceFeeAmountPrefix>\\p{Digit}{6})(?<serviceFeeAmountSuffix>[A-R}{]{1}))(?<filler13>000000\\{)(?<netAdjustmentAmount>(?<netAdjustmentAmountPrefix>\\p{Digit}{8})(?<netAdjustmentAmountSuffix>[A-R}{]{1}))(?<discountRate>\\p{Digit}{5})(?<serviceFeeRate>\\p{Digit}{5})(?<filler17>00000)(?<filler18>0000000000\\{)(?<cardmemberNumber>\\p{Alnum}{17})(?<adjustmentReason>[\\p{ASCII}]{280})(?<filler21>\\p{ASCII}{3})(?<filler22>\\p{ASCII}{3})(?<filler23>\\p{Blank}{15})(?<filler24>\\p{ASCII}{1})(?<filler25>\\p{ASCII}{6})$");


    /**
     * amexPayeeNumber
     * Numeric String 10 bytes long, required
     * This field contains the Service Establishment (SE) Number of the merchant that received the payment from American Express.
     * Note: SE Numbers are assigned by American Express.
     */
    @JsonProperty("AMEX_PAYEE_NUMBER")
    @Size(max = 10)
    @NotNull
    Long amexPayeeNumber;

    /**
     * amexSeNumber
     * Numeric String 10 bytes long, required
     * This field contains the Service Establishment (SE) Number of the merchant being reconciled, which may not necessarily be the same SE receiving payment (see Field 1, AMEX_PAYEE_NUMBER).
     * This is the SE Number under which the transactions were sub-mitted, which usually corresponds to the physical location.
     */
    @JsonProperty("AMEX_SE_NUMBER")
    @Size(max = 10)
    @NotNull
    Long amexSeNumber;
    /**
     * seUnitNumber
     * This field contains the merchant-assigned SE Unit Number (usually an internal, store identifier code) that corresponds to a specific store or location.
     * If no value is assigned, this field is character space filled.
     */
    @JsonProperty("SE_UNIT_NUMBER")
    @Size(max = 10)
    @NotNull
    String seUnitNumber;

    @JsonProperty("PAYMENT_YEAR")
    @Size(max = 4)
    @NotNull
    Long paymentYear;

    /**
     * paymentNumber
     * This field contains the Payment Number, a reference number used by the American Express Payee to reconcile the daily settlement to the daily payment.
     */
    @JsonProperty("PAYMENT_NUMBER")
    @Size(max = 8)
    @NotNull
    String paymentNumber;

    /**
     * recordType
     * This field contains the constant literal “2”, a Record Type code that indicates that this is a Detail Record.
     */
    @JsonProperty("RECORD_TYPE")
    @Size(max = 1)
    @NotNull
    private Long recordType;

    /**
     * detailRecordType
     * This field contains the Detail Record Type code that indicates the type of record used in this transaction. For Adjustment Detail Records, this entry is always “30”.
     */
    @JsonProperty("DETAIL_RECORD_TYPE")
    @Size(max = 2)
    @NotNull
    private Long detailRecordType;

    /**
     * amexProcessDate
     * This field contains the American Express Transaction Processing Date, which is used to determine the payment date.
     */
    @JsonProperty("AMEX_PROCESS_DATE")
    @Size(max = 8)
    @NotNull
    Date amexProcessDate;


    /**
     * adjustmentNumber
     * This field contains the Summary of Charge (SOC) Invoice Number.
     * <p>
     * For electronically submitted SOCs where the TBT_IDENTIFICATION_NUMBER (field 5 in the GFSG
     * Submission file) is populated with all zeros, this value is the concatenation of the Julian
     * Date (positions 60-62) and the last three digits of the PCID number under which this SOC was
     * submitted (positions 63-65).
     * <p>
     * For electronically submitted SOCs where the TBT_IDENTIFICATION_NUMBER (field 5 in the GFSG
     * Submission file) is populated with any numeric value other than zero, this value will be the
     * last six digits of the TBT_IDENTIFICATION_NUMBER under which this SOC was submitted
     * (positions 60-65).
     */
    @JsonProperty("ADJUSTMENT_NUMBER")
    @Size(max = 6)
    @NotNull
    Long adjustmentNumber;

    /**
     * socAmount
     * This field contains the Summary of Charge (SOC) Amount originally submitted for payment.
     * Note: For US Dollar (USD) and Canadian Dollar (CAD) trans-actions, two decimal places are implied.
     */
    @JsonProperty("ADJUSTMENT_AMOUNT")
    @Size(max = 9)
    @NotNull
    Long adjustmentAmount;

    /**
     * discountAmount
     * This field contains the total Discount Amount, based on socAmount and discountRate
     */
    @JsonProperty("DISCOUNT_AMOUNT")
    @Size(max = 9)
    @NotNull
    Long discountAmount;

    /**
     * serviceFeeAmount
     * This field contains the total Service Fee Amount, based on socamount and serviceFeeRate
     */
    @JsonProperty("SERVICE_FEE_AMOUNT")
    @Size(max = 9)
    @NotNull
    Long serviceFeeAmount;

    /**
     * netAdjustmentAmount
     * This field contains the Net SOC (Summary of Charge) Amount submitted to American Express for
     * payment, which is the sum total of  socAmount, less discountAmount and serviceFeeAmount
     */
    @JsonProperty("NET_ADJUSTMENT_AMOUNT")
    @Size(max = 9)
    @NotNull
    Long netAdjustmentAmount;

    /**
     * discountRate
     * This field contains the Discount Rate (decimal place value) used to calculate the amount
     * American Express charges a merchant for services provided per the American Express Card
     * Acceptance Agreement.
     */
    @JsonProperty("DISCOUNT_RATE")
    @Size(max = 5)
    @NotNull
    Long discountRate;

    /**
     * serviceFeeRate
     * This field contains the Service Fee Rate (decimal place value) used to calculate the amount
     * American Express charges a merchant as service fees.
     * Service fees are assessed only in certain situations and may not apply to all SEs.
     */
    @JsonProperty("SERVICE_FEE_RATE")
    @Size(max = 5)
    @NotNull
    Long serviceFeeRate;

    /**
     * This field contains the Card member (Account) Number that corresponds to Field 10, ADJUSTMENT_AMOUNT.
     * (Please note that if Card number masking is enabled this field is required to accept alphanumeric characters.)
     */
    @JsonProperty("CARDMEMBER_NUMBER")
    @Size(max = 17)
    @NotNull
    String cardMemberNumber;


    @JsonProperty("ADJUSTMENT_REASON")
    @Size(max = 280)
    @NotNull
    String adjustmentReason;


    public AdjustmentDetail() {
    }

    public AdjustmentDetail withAmexPayeeNumber(Long amexPayeeNumber) {
        this.amexPayeeNumber = amexPayeeNumber;
        return this;
    }

    public AdjustmentDetail withAmexSeNumber(Long amexSeNumber) {
        this.amexSeNumber = amexSeNumber;
        return this;
    }

    public AdjustmentDetail withSeUnitNumber(String seUnitNumber) {
        this.seUnitNumber = seUnitNumber;
        return this;
    }

    public AdjustmentDetail withPaymentYear(Long paymentYear) {
        this.paymentYear = paymentYear;
        return this;
    }

    public AdjustmentDetail withPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
        return this;
    }

    public AdjustmentDetail withRecordType(Long recordType) {
        this.recordType = recordType;
        return this;
    }

    public AdjustmentDetail withDetailRecordType(Long detailRecordType) {
        this.detailRecordType = detailRecordType;
        return this;
    }

    public AdjustmentDetail withAmexProcessDate(Date amexProcessDate) {
        this.amexProcessDate = amexProcessDate;
        return this;
    }

    public AdjustmentDetail withAdjustmentNumber(Long adjustmentNumber) {
        this.adjustmentNumber = adjustmentNumber;
        return this;
    }

    public AdjustmentDetail withAdjustmentAmount(Long adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
        return this;
    }

    public AdjustmentDetail withDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public AdjustmentDetail withServiceFeeAmount(Long serviceFeeAmount) {
        this.serviceFeeAmount = serviceFeeAmount;
        return this;
    }

    public AdjustmentDetail withNetAdjustmentAmount(Long netAdjustmentAmount) {
        this.netAdjustmentAmount = netAdjustmentAmount;
        return this;
    }

    public AdjustmentDetail withDiscountRate(Long discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public AdjustmentDetail withServiceFeeRate(Long serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
        return this;
    }

    public AdjustmentDetail withCardMemberNumber(String cardMemberNumber) {
        this.cardMemberNumber = cardMemberNumber;
        return this;
    }

    public AdjustmentDetail withAdjustmentReason(String adjustmentReason) {
        this.adjustmentReason = adjustmentReason;
        return this;
    }

    public static Pattern getPattern() {
        return pattern;
    }

    public Long getAmexPayeeNumber() {
        return amexPayeeNumber;
    }

    public void setAmexPayeeNumber(Long amexPayeeNumber) {
        this.amexPayeeNumber = amexPayeeNumber;
    }

    public Long getAmexSeNumber() {
        return amexSeNumber;
    }

    public void setAmexSeNumber(Long amexSeNumber) {
        this.amexSeNumber = amexSeNumber;
    }

    public String getSeUnitNumber() {
        return seUnitNumber;
    }

    public void setSeUnitNumber(String seUnitNumber) {
        this.seUnitNumber = seUnitNumber;
    }

    public Long getPaymentYear() {
        return paymentYear;
    }

    public void setPaymentYear(Long paymentYear) {
        this.paymentYear = paymentYear;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public Long getRecordType() {
        return recordType;
    }

    public void setRecordType(Long recordType) {
        this.recordType = recordType;
    }

    public Long getDetailRecordType() {
        return detailRecordType;
    }

    public void setDetailRecordType(Long detailRecordType) {
        this.detailRecordType = detailRecordType;
    }

    public Date getAmexProcessDate() {
        return amexProcessDate;
    }

    public void setAmexProcessDate(Date amexProcessDate) {
        this.amexProcessDate = amexProcessDate;
    }

    public Long getAdjustmentNumber() {
        return adjustmentNumber;
    }

    public void setAdjustmentNumber(Long adjustmentNumber) {
        this.adjustmentNumber = adjustmentNumber;
    }

    public Long getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(Long adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public Long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Long getServiceFeeAmount() {
        return serviceFeeAmount;
    }

    public void setServiceFeeAmount(Long serviceFeeAmount) {
        this.serviceFeeAmount = serviceFeeAmount;
    }

    public Long getNetAdjustmentAmount() {
        return netAdjustmentAmount;
    }

    public void setNetAdjustmentAmount(Long netAdjustmentAmount) {
        this.netAdjustmentAmount = netAdjustmentAmount;
    }

    public Long getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Long discountRate) {
        this.discountRate = discountRate;
    }

    public Long getServiceFeeRate() {
        return serviceFeeRate;
    }

    public void setServiceFeeRate(Long serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
    }

    public String getCardMemberNumber() {
        return cardMemberNumber;
    }

    public void setCardMemberNumber(String cardMemberNumber) {
        this.cardMemberNumber = cardMemberNumber;
    }

    public String getAdjustmentReason() {
        return adjustmentReason;
    }

    public void setAdjustmentReason(String adjustmentReason) {
        this.adjustmentReason = adjustmentReason;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AdjustmentDetail that = (AdjustmentDetail) o;

        return new EqualsBuilder()
                .append(getAmexPayeeNumber(), that.getAmexPayeeNumber())
                .append(getAmexSeNumber(), that.getAmexSeNumber())
                .append(getSeUnitNumber(), that.getSeUnitNumber())
                .append(getPaymentYear(), that.getPaymentYear())
                .append(getPaymentNumber(), that.getPaymentNumber())
                .append(getRecordType(), that.getRecordType())
                .append(getDetailRecordType(), that.getDetailRecordType())
                .append(getAmexProcessDate(), that.getAmexProcessDate())
                .append(getAdjustmentNumber(), that.getAdjustmentNumber())
                .append(getAdjustmentAmount(), that.getAdjustmentAmount())
                .append(getDiscountAmount(), that.getDiscountAmount())
                .append(getServiceFeeAmount(), that.getServiceFeeAmount())
                .append(getNetAdjustmentAmount(), that.getNetAdjustmentAmount())
                .append(getDiscountRate(), that.getDiscountRate())
                .append(getServiceFeeRate(), that.getServiceFeeRate())
                .append(getCardMemberNumber(), that.getCardMemberNumber())
                .append(getAdjustmentReason(), that.getAdjustmentReason())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getAmexPayeeNumber())
                .append(getAmexSeNumber())
                .append(getSeUnitNumber())
                .append(getPaymentYear())
                .append(getPaymentNumber())
                .append(getRecordType())
                .append(getDetailRecordType())
                .append(getAmexProcessDate())
                .append(getAdjustmentNumber())
                .append(getAdjustmentAmount())
                .append(getDiscountAmount())
                .append(getServiceFeeAmount())
                .append(getNetAdjustmentAmount())
                .append(getDiscountRate())
                .append(getServiceFeeRate())
                .append(getCardMemberNumber())
                .append(getAdjustmentReason())
                .toHashCode();
    }
}

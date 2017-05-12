package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by dashirov on 5/12/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "TLRR_AMEX_PAYEE_NUMBER",
        "TLRR_AMEX_SE_NUMBER",
        "TLRR_SE_UNIT_NUMBER",
        "TLRR_PAYMENT_YEAR",
        "TLRR_PAYMENT_NUMBER",
        "TLRR_RECORD_TYPE",
        "TLRR_DETAIL_RECORD_TYPE",
        "TLRR_SE_BUSINESS_DATE",
        "TLRR_AMEX_PROCESS_DATE",
        "TLRR_SOC_INVOICE_NUMBER",
        "TLRR_SOC_AMOUNT",
        "TLRR_ROC_AMOUNT",
        "TLRR_CM_NUMBER",
        "TLRR_CM_REF_NO",

        "TLRR_TRAN_DATE",
        "TLRR_SE_REF_POA",
        "NON_COMPLIANT_INDICATOR",
        "NON_COMPLIANT_ERROR_CODE_1",
        "NON_COMPLIANT_ERROR_CODE_2",
        "NON_COMPLIANT_ERROR_CODE_3",
        "NON_COMPLIANT_ERROR_CODE_4",
        "NON_SWIPED_INDICATOR",
        "TLRR_CM_NUMBER_EXD"

})
public class ROCDetail {
    public final static Pattern pattern = Pattern.compile("^(?<amexPayeeNumber>\\p{Digit}{10})(?<amexSeNumber>[\\p{Alnum}]{10})(?<seUnitNumber>[\\p{Digit}\\p{Blank}]{10})(?<paymentYear>\\p{Digit}{4})(?<paymentNumber>(?<paymentNumberJulianDate>\\p{Digit}{3})(?<paymentNumberRecordTypeIndicator>\\p{Alnum})(?<paymentNumberSequence>\\p{Digit}{4}))(?<recordType>3)(?<detailRecordType>11)(?<seBusinessDate>(?<seBusinessDateYear>\\p{Digit}{4})(?<seBuisnessDateJulianDate>\\p{Digit}{3}))(?<amexProcessingDate>(?<amexProcessingDateYear>\\p{Digit}{4})(?<amexProcessingDateJulianDate>\\p{Digit}{3}))(?<socInvoiceNumber>\\p{Digit}{6})(?<socAmount>(?<socAmountPrefix>\\p{Digit}{12})(?<socAmountSuffix>[A-R}{]{1}))(?<rocAmount>(?<rocAmountPrefix>\\p{Digit}{12})(?<rocAmountSuffix>[A-R}{]{1}))(?<cardMemberNumber>[\\p{Blank}\\p{Alnum}]{15})(?<cardMemberReferenceNumber>[\\p{Blank}\\p{Alnum}]{11}).{9}\\p{Blank}{10}(?<rocNumber>[\\p{Alnum}\\p{Blank}]{10})(?<transactionDate>(?<transactionDateYear>\\p{Digit}{4})(?<transactionJulianDate>\\p{Digit}{3}))(?<invoiceReferenceNumber>[\\p{Alnum}\\p{Blank}]{30})(?<nonCompliantIndicator>[ AN~]{1})(?<nonCompliantErrorCode1>[\\p{Alnum}\\p{Blank}]{4})(?<nonCompliantErrorCode2>[\\p{Alnum}\\p{Blank}]{4})(?<nonCompliantErrorCode3>[\\p{Alnum}\\p{Blank}]{4})(?<nonCompliantErrorCode4>[\\p{Alnum}\\p{Blank}]{4})(?<nonSwipedIndicator>[ CH~Z]{1})[\\p{Blank}\\p{Alnum}]{1}.{4}.{22}(?<cardMemberNumberUsedInTransaction>[\\p{Blank}\\p{Alnum}]{19})(?<filler30>\\p{Blank}{203})$");

    @JsonProperty("TLRR_AMEX_PAYEE_NUMBER")
    @Size(max = 10)
    @NotNull
    private Long amexPayeeNumber;

    @JsonProperty("TLRR_AMEX_SE_NUMBER")
    @Size(max = 10)
    @NotNull
    private Long amexSeNumber;

    @JsonProperty("TLRR_SE_UNIT_NUMBER")
    @Size(max = 10)
    @NotNull
    private String seUnitNumber;

    @JsonProperty("TLRR_PAYMENT_YEAR")
    @Size(max = 4)
    @NotNull
    private Long paymentYear;

    @JsonProperty("TLRR_PAYMENT_NUMBER")
    @Size(max = 8)
    @NotNull
    private String paymentNumber;

    @JsonProperty("TLRR_RECORD_TYPE")
    @Size(max = 1)
    @NotNull
    private Long recordType;

    @JsonProperty("TLRR_DETAIL_RECORD_TYPE")
    @Size(max = 2)
    @NotNull
    private Long detailRecordType;

    @JsonProperty("TLRR_SE_BUSINESS_DATE")
    @NotNull
    private Date seBusinessDate;

    @JsonProperty("TLRR_AMEX_PROCESS_DATE")
    @NotNull
    private Date amexProcessDate;

    @JsonProperty("TLRR_SOC_INVOICE_NUMBER")
    @Size(max = 6)
    @NotNull
    private Long socInvoiceNumber;

    @JsonProperty("TLRR_SOC_AMOUNT")
    @Size(max = 13)
    @NotNull
    private Long socAmount;

    @JsonProperty("TLRR_ROC_AMOUNT")
    @Size(max = 13)
    @NotNull
    private Long rocAmount;

    /**
     * cardMemberNumber
     * This field contains the Card member (Account) Number that corresponds to this transaction. (Please note that if
     * Card number masking is enabled this field is required to accept alphanumeric characters.)
     */
    @JsonProperty("TLRR_CM_NUMBER")
    @Size(max = 15)
    @NotNull
    private String cardMemberNumber;


    /**
     * cardMemberReferenceNumber
     * OPTIONAL PROPERTY
     * This field contains the Cardmember Reference Number assigned to this transaction by the Cardmember, at the
     * time the sale was executed. This data is primarily used in the CPC/Corporate Purchasing Card (a.k.a.,
     * CPS/Corporate Purchasing Solutions Card) environment. If this field is populated, this value is used by the
     * Cardmember’s organization for tracking and accounting purposes.
     */
    @JsonProperty("TLRR_CM_REF_NO")
    @Size(max = 11)
    @Null
    private String cardMemberReferenceNumber;

    /**
     * This field contains the Transaction Date, which is the date the transaction took place (from the TRANSACTION_DATE
     * field in the financial settlement file).
     */
    @JsonProperty("TLRR_TRAN_DATE")
    @NotNull
    private Date transactionDate;

    /**
     * invoiceReferenceNumber
     * OPTIONAL PROPERTY
     * <p>
     * This field contains the SE (Invoice) Reference Number assigned to this transaction by the merchant, at the time
     * the sale was executed.
     * This entry may be a reference to the Record of Charge (ROC), order number, invoice number, or any other merchant-
     * designated combination of letters and numerals that was intended to aid the merchant in the retrieval of
     * supporting documentation, in case of inquiry or other post-transaction correspondence.
     */
    @JsonProperty("TLRR_SE_REF_POA")
    @Size(max = 30)
    @Null
    private String invoiceReferenceNumber;

    /**
     * nonCompliantIndicator
     * OPTIONAL PROPERTY
     * <p>
     * This field contains the Non-Compliant Indicator. Valid values include the following:
     * A = Settlement and/or authorization file did not comply with American Express standards.
     * See NON_COMPLIANT_ ERROR_CODE fields that follow, on next page.
     * N = Settlement and/or authorization file did not comply with American Express standards.
     * ~ = None assessed.
     * Note: Tilde (~) represents a character space.
     */
    @JsonProperty("NON_COMPLIANT_INDICATOR")
    @Null
    @Size(max = 1)
    private String nonCompliantIndicator;


    /**
     * These fields contain field-level Non-compliant Error Code(s) applicable to this Record of Charge (ROC). Valid values include the following:
     * 2014 = Point of Service Data Code invalid
     * 2015 = Approval Code non-numeric
     * 2022 = Transaction Identifier Invalid
     * 2036 = Approval Code not equal to required length
     * If unused, this field is character space filled.
     * Note: One or more of these fields may be populated only if this Record of Charge (ROC) is non-compliant as indicated by the value “A” in the preceding NON-COMPLIANT_INDICATOR field. For more information, see page 5
     */
    @JsonProperty("NON_COMPLIANT_ERROR_CODE_1")
    @Null
    @Size(max = 4)
    private String nonCompliantErrorCode1;
    @JsonProperty("NON_COMPLIANT_ERROR_CODE_2")
    @Null
    @Size(max = 4)
    private String nonCompliantErrorCode2;
    @JsonProperty("NON_COMPLIANT_ERROR_CODE_3")
    @Null
    @Size(max = 4)
    private String nonCompliantErrorCode3;
    @JsonProperty("NON_COMPLIANT_ERROR_CODE_4")
    @Null
    @Size(max = 4)
    private String nonCompliantErrorCode4;

    /**
     * This field contains the Non-Swiped Indicator. This entry indicates if the American Express or American Express
     * Partner’s Cardmem- ber Account Number for this transaction was manually entered; and either the Card was not
     * present, or the Card’s magnetic stripe or chip could not be read by the POS device. Transactions are reviewed
     * utilizing the Point of Sale Data Code (value “C”) or Authorization Code (value “H”) or Non-Swipe ADJ App-In Code
     * (value “Z”).
     * Valid values include the following:
     * C = Non-Swiped
     * H = Non-Swiped
     * ~ = Non assessed
     * Z =Non-SwipeADJApp-In
     * Note: Tilde (~) represents a character space.
     */
    @JsonProperty("NON_SWIPED_INDICATOR")
    @Null
    @Size(max = 1)
    private String nonSwipedIndicator;

    @JsonProperty("TLRR_CM_NUMBER_EXD")
    @NotNull
    @Size(max = 19)
    private String cardmemberNumberExtended;


    public ROCDetail() {
    }

    public Long getAmexPayeeNumber() {
        return amexPayeeNumber;
    }

    public void setAmexPayeeNumber(Long amexPayeeNumber) {
        this.amexPayeeNumber = amexPayeeNumber;
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

    public Date getSeBusinessDate() {
        return seBusinessDate;
    }

    public void setSeBusinessDate(Date seBusinessDate) {
        this.seBusinessDate = seBusinessDate;
    }

    public Date getAmexProcessDate() {
        return amexProcessDate;
    }

    public void setAmexProcessDate(Date amexProcessDate) {
        this.amexProcessDate = amexProcessDate;
    }

    public Long getSocInvoiceNumber() {
        return socInvoiceNumber;
    }

    public void setSocInvoiceNumber(Long socInvoiceNumber) {
        this.socInvoiceNumber = socInvoiceNumber;
    }

    public Long getSocAmount() {
        return socAmount;
    }

    public void setSocAmount(Long socAmount) {
        this.socAmount = socAmount;
    }

    public Long getRocAmount() {
        return rocAmount;
    }

    public void setRocAmount(Long rocAmount) {
        this.rocAmount = rocAmount;
    }

    public String getCardMemberNumber() {
        return cardMemberNumber;
    }

    public void setCardMemberNumber(String cardMemberNumber) {
        this.cardMemberNumber = cardMemberNumber;
    }

    public String getCardMemberReferenceNumber() {
        return cardMemberReferenceNumber;
    }

    public void setCardMemberReferenceNumber(String cardMemberReferenceNumber) {
        this.cardMemberReferenceNumber = cardMemberReferenceNumber;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getInvoiceReferenceNumber() {
        return invoiceReferenceNumber;
    }

    public void setInvoiceReferenceNumber(String invoiceReferenceNumber) {
        this.invoiceReferenceNumber = invoiceReferenceNumber;
    }

    public String getNonCompliantIndicator() {
        return nonCompliantIndicator;
    }

    public void setNonCompliantIndicator(String nonCompliantIndicator) {
        this.nonCompliantIndicator = nonCompliantIndicator;
    }

    public String getNonCompliantErrorCode1() {
        return nonCompliantErrorCode1;
    }

    public void setNonCompliantErrorCode1(String nonCompliantErrorCode1) {
        this.nonCompliantErrorCode1 = nonCompliantErrorCode1;
    }

    public String getNonCompliantErrorCode2() {
        return nonCompliantErrorCode2;
    }

    public void setNonCompliantErrorCode2(String nonCompliantErrorCode2) {
        this.nonCompliantErrorCode2 = nonCompliantErrorCode2;
    }

    public String getNonCompliantErrorCode3() {
        return nonCompliantErrorCode3;
    }

    public void setNonCompliantErrorCode3(String nonCompliantErrorCode3) {
        this.nonCompliantErrorCode3 = nonCompliantErrorCode3;
    }

    public String getNonCompliantErrorCode4() {
        return nonCompliantErrorCode4;
    }

    public void setNonCompliantErrorCode4(String nonCompliantErrorCode4) {
        this.nonCompliantErrorCode4 = nonCompliantErrorCode4;
    }

    public String getNonSwipedIndicator() {
        return nonSwipedIndicator;
    }

    public void setNonSwipedIndicator(String nonSwipedIndicator) {
        this.nonSwipedIndicator = nonSwipedIndicator;
    }

    public String getCardmemberNumberExtended() {
        return cardmemberNumberExtended;
    }

    public ROCDetail withAmexPayeeNumber(Long amexPayeeNumber) {
        this.amexPayeeNumber = amexPayeeNumber;
        return this;
    }

    public ROCDetail withAmexSeNumber(Long amexSeNumber) {
        this.amexSeNumber = amexSeNumber;
        return this;
    }

    public ROCDetail withSeUnitNumber(String seUnitNumber) {
        this.seUnitNumber = seUnitNumber;
        return this;
    }

    public ROCDetail withPaymentYear(Long paymentYear) {
        this.paymentYear = paymentYear;
        return this;
    }

    public ROCDetail withPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
        return this;
    }

    public ROCDetail withRecordType(Long recordType) {
        this.recordType = recordType;
        return this;
    }

    public ROCDetail withDetailRecordType(Long detailRecordType) {
        this.detailRecordType = detailRecordType;
        return this;
    }

    public ROCDetail withSeBusinessDate(Date seBusinessDate) {
        this.seBusinessDate = seBusinessDate;
        return this;
    }

    public ROCDetail withAmexProcessDate(Date amexProcessDate) {
        this.amexProcessDate = amexProcessDate;
        return this;
    }

    public ROCDetail withSocInvoiceNumber(Long socInvoiceNumber) {
        this.socInvoiceNumber = socInvoiceNumber;
        return this;
    }

    public ROCDetail withSocAmount(Long socAmount) {
        this.socAmount = socAmount;
        return this;
    }

    public ROCDetail withRocAmount(Long rocAmount) {
        this.rocAmount = rocAmount;
        return this;
    }

    public ROCDetail withCardMemberNumber(String cardMemberNumber) {
        this.cardMemberNumber = cardMemberNumber;
        return this;
    }

    public ROCDetail withCardMemberReferenceNumber(String cardMemberReferenceNumber) {
        this.cardMemberReferenceNumber = cardMemberReferenceNumber;
        return this;
    }

    public ROCDetail withTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public ROCDetail withInvoiceReferenceNumber(String invoiceReferenceNumber) {
        this.invoiceReferenceNumber = invoiceReferenceNumber;
        return this;
    }

    public ROCDetail withNonCompliantIndicator(String nonCompliantIndicator) {
        this.nonCompliantIndicator = nonCompliantIndicator;
        return this;
    }

    public ROCDetail withNonCompliantErrorCode1(String nonCompliantErrorCode1) {
        this.nonCompliantErrorCode1 = nonCompliantErrorCode1;
        return this;
    }

    public ROCDetail withNonCompliantErrorCode2(String nonCompliantErrorCode2) {
        this.nonCompliantErrorCode2 = nonCompliantErrorCode2;
        return this;
    }

    public ROCDetail withNonCompliantErrorCode3(String nonCompliantErrorCode3) {
        this.nonCompliantErrorCode3 = nonCompliantErrorCode3;
        return this;
    }

    public ROCDetail withNonCompliantErrorCode4(String nonCompliantErrorCode4) {
        this.nonCompliantErrorCode4 = nonCompliantErrorCode4;
        return this;
    }

    public ROCDetail withNonSwipedIndicator(String nonSwipedIndicator) {
        this.nonSwipedIndicator = nonSwipedIndicator;
        return this;
    }

    public ROCDetail withCardmemberNumberExtended(String cardmemberNumberExtended) {
        this.cardmemberNumberExtended = cardmemberNumberExtended;
        return this;
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

        ROCDetail rocDetail = (ROCDetail) o;

        return new EqualsBuilder()
                .append(getAmexPayeeNumber(), rocDetail.getAmexPayeeNumber())
                .append(getAmexSeNumber(), rocDetail.getAmexSeNumber())
                .append(getSeUnitNumber(), rocDetail.getSeUnitNumber())
                .append(getPaymentYear(), rocDetail.getPaymentYear())
                .append(getPaymentNumber(), rocDetail.getPaymentNumber())
                .append(getRecordType(), rocDetail.getRecordType())
                .append(getDetailRecordType(), rocDetail.getDetailRecordType())
                .append(getSeBusinessDate(), rocDetail.getSeBusinessDate())
                .append(getAmexProcessDate(), rocDetail.getAmexProcessDate())
                .append(getSocInvoiceNumber(), rocDetail.getSocInvoiceNumber())
                .append(getSocAmount(), rocDetail.getSocAmount())
                .append(getRocAmount(), rocDetail.getRocAmount())
                .append(getCardMemberNumber(), rocDetail.getCardMemberNumber())
                .append(getCardMemberReferenceNumber(), rocDetail.getCardMemberReferenceNumber())
                .append(getTransactionDate(), rocDetail.getTransactionDate())
                .append(getInvoiceReferenceNumber(), rocDetail.getInvoiceReferenceNumber())
                .append(getNonCompliantIndicator(), rocDetail.getNonCompliantIndicator())
                .append(getNonCompliantErrorCode1(), rocDetail.getNonCompliantErrorCode1())
                .append(getNonCompliantErrorCode2(), rocDetail.getNonCompliantErrorCode2())
                .append(getNonCompliantErrorCode3(), rocDetail.getNonCompliantErrorCode3())
                .append(getNonCompliantErrorCode4(), rocDetail.getNonCompliantErrorCode4())
                .append(getNonSwipedIndicator(), rocDetail.getNonSwipedIndicator())
                .append(getCardmemberNumberExtended(), rocDetail.getCardmemberNumberExtended())
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
                .append(getSeBusinessDate())
                .append(getAmexProcessDate())
                .append(getSocInvoiceNumber())
                .append(getSocAmount())
                .append(getRocAmount())
                .append(getCardMemberNumber())
                .append(getCardMemberReferenceNumber())
                .append(getTransactionDate())
                .append(getInvoiceReferenceNumber())
                .append(getNonCompliantIndicator())
                .append(getNonCompliantErrorCode1())
                .append(getNonCompliantErrorCode2())
                .append(getNonCompliantErrorCode3())
                .append(getNonCompliantErrorCode4())
                .append(getNonSwipedIndicator())
                .append(getCardmemberNumberExtended())
                .toHashCode();
    }
}

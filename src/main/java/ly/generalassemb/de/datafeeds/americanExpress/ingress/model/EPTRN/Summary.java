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
public class Summary {
    private final static Pattern pattern = Pattern.compile("^(?<amexPayeeNumber>\\p{Digit}{10})(?<amexSortField1>[0]{10})(?<amexSortField2>[0]{10})(?<paymentYear>\\p{Digit}{4})(?<paymentNumber>(?<paymentNumberJulianDate>\\p{Digit}{3})(?<paymentNumberRecordTypeIndicator>\\p{Alnum}{1})(?<paymentNumberSequence>\\p{Digit}{4}))(?<recordType>1)(?<detailRecordType>00)(?<paymentDate>(?<paymentDateYear>\\p{Digit}{4})(?<paymentDateJulianDate>\\p{Digit}{3}))(?<paymentAmount>(?<paymentAmountPrefix>\\p{Digit}{10})(?<paymentAmountSuffix>[A-R}{]{1}))(?<debitBalanceAmount>(?<debitBalanceAmountPrefix>\\p{Digit}{8})(?<debitBalanceAmountSuffix>[A-R}{]{1}))(?<abaBankNumber>\\p{Digit}{9})(?<seDDANumber>\\p{Digit}{1,17})");

    @JsonProperty("AMEX_PAYEE_NUMBER")
    @Size(max = 10)
    @NotNull
    private Long amexPayeeNumber;

    @JsonProperty("PAYMENT_YEAR")
    @Size(max = 4)
    @NotNull
    private Long paymentYear;

    @JsonProperty("PAYMENT_NUMBER")
    @Size(max = 10)
    @NotNull
    private String paymentNumber;

    @JsonProperty("RECORD_TYPE")
    @Size(max = 1)
    @NotNull
    private Long recordType;

    @JsonProperty("DETAIL_RECORD_TYPE")
    @Size(max = 2)
    @NotNull
    private Long detailRecordType;

    @JsonProperty("PAYMENT_DATE")
    @NotNull
    private Date paymentDate;

    @JsonProperty("PAYMENT_AMOUNT")
    @Size(max = 11)
    @NotNull
    private Long paymentAmount;

    @JsonProperty("DEBIT_BALANCE_AMOUNT")
    @Size(max = 9)
    @NotNull
    private Long debitBalanceAmount;

    @JsonProperty("ABA_BANK_NUMBER")
    @Size(max = 9)
    @NotNull
    private Long abaBankNumber;

    @JsonProperty("SE_DDA_NUMBER")
    @Size(max = 17)
    @NotNull
    private String payeeDirectDepositAccountNumber;


    public Summary() {
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

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Long paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Long getDebitBalanceAmount() {
        return debitBalanceAmount;
    }

    public void setDebitBalanceAmount(Long debitBalanceAmount) {
        this.debitBalanceAmount = debitBalanceAmount;
    }

    public Long getAbaBankNumber() {
        return abaBankNumber;
    }

    public void setAbaBankNumber(Long abaBankNumber) {
        this.abaBankNumber = abaBankNumber;
    }

    public String getPayeeDirectDepositAccountNumber() {
        return payeeDirectDepositAccountNumber;
    }

    public void setPayeeDirectDepositAccountNumber(String payeeDirectDepositAccountNumber) {
        this.payeeDirectDepositAccountNumber = payeeDirectDepositAccountNumber;
    }

    public Summary withAmexPayeeNumber(Long amexPayeeNumber) {
        this.amexPayeeNumber = amexPayeeNumber;
        return this;
    }

    public Summary withPaymentYear(Long paymentYear) {
        this.paymentYear = paymentYear;
        return this;
    }

    public Summary withPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public Summary withPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
        return this;
    }
    public Summary withRecordType(Long recordType){
        this.recordType = recordType;
        return this;
    }
    public Summary withDetailRecordType(Long detailRecordType){
        this.detailRecordType = detailRecordType;
        return this;
    }
    public Summary withPaymentAmount(Long paymentAmount){
        this.paymentAmount = paymentAmount;
        return this;
    }
    public Summary withDebitBalanceAmount(Long debitBalanceAmount){
        this.debitBalanceAmount = debitBalanceAmount;
        return this;
    }

    public Summary withAbaBankNumber(Long abaBankNumber){
        this.abaBankNumber = abaBankNumber;
        return this;
    }
    public Summary withPayeeDirectDepositAccountNumber(String payeeDirectDepositAccountNumber){
        this.payeeDirectDepositAccountNumber = payeeDirectDepositAccountNumber;
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

        Summary summary = (Summary) o;

        return new EqualsBuilder()
                .append(getAmexPayeeNumber(), summary.getAmexPayeeNumber())
                .append(getPaymentYear(), summary.getPaymentYear())
                .append(getPaymentNumber(), summary.getPaymentNumber())
                .append(getRecordType(), summary.getRecordType())
                .append(getDetailRecordType(), summary.getDetailRecordType())
                .append(getPaymentDate(), summary.getPaymentDate())
                .append(getPaymentAmount(), summary.getPaymentAmount())
                .append(getDebitBalanceAmount(), summary.getDebitBalanceAmount())
                .append(getAbaBankNumber(), summary.getAbaBankNumber())
                .append(getPayeeDirectDepositAccountNumber(), summary.getPayeeDirectDepositAccountNumber())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getAmexPayeeNumber())
                .append(getPaymentYear())
                .append(getPaymentNumber())
                .append(getRecordType())
                .append(getDetailRecordType())
                .append(getPaymentDate())
                .append(getPaymentAmount())
                .append(getDebitBalanceAmount())
                .append(getAbaBankNumber())
                .append(getPayeeDirectDepositAccountNumber())
                .toHashCode();
    }
}

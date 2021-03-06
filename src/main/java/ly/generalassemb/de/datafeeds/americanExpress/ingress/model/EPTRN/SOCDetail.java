package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.AmountParser;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.quote.AlwaysQuoteMode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
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
public class SOCDetail {
    public final static Pattern pattern = Pattern.compile("^(?<amexPayeeNumber>\\p{Digit}{10})(?<amexSeNumber>[\\p{Alnum}]{10})(?<seUnitNumber>[\\p{Alnum}\\p{Blank}]{10})(?<paymentYear>\\p{Digit}{4})(?<paymentNumber>(?<paymentNumberJulianDate>\\p{Digit}{3})(?<paymentNumberRecordTypeIndicator>\\p{Alnum})(?<paymentNumberSequence>\\p{Digit}{4}))(?<recordType>2)(?<detailRecordType>10)(?<seBusinessDate>(?<seBusinessDateYear>\\p{Digit}{4})(?<seBuisnessDateJulianDate>\\p{Digit}{3}))(?<amexProcessDate>(?<amexProcessDateYear>\\p{Digit}{4})(?<amexProcessDateJulianDate>\\p{Digit}{3}))(?<socInvoiceNumber>\\p{Digit}{6})(?<socAmount>(?<socAmountPrefix>\\p{Digit}{10})(?<socAmountSuffix>[A-R}{]{1}))(?<discountAmount>(?<discountAmountPrefix>\\p{Digit}{8})(?<discountAmountSuffix>[A-R}{]{1}))(?<serviceFeeAmount>(?<serviceFeeAmountPrefix>\\p{Digit}{6})(?<serviceFeeAmountSuffix>[A-R}{]{1}))[0{]{7}(?<netSOCAmount>(?<netSOCAmountPrefix>\\p{Digit}{10})(?<netSOCAmountSuffix>[A-R}{]{1}))(?<discountRate>\\p{Digit}{5})(?<serviceFeeRate>\\p{Digit}{5})0{5}[0{]{11}[0{]{5}(?<amexGrossAmount>(?<amexGrossAmountPrefix>\\p{Digit}{10})(?<amexGrossAmountSuffix>[A-R}{]{1}))(?<amexROCCount>(?<amexROCCountPrefix>\\p{Digit}{4})(?<amexROCCountSuffix>[A-I{]{1}))(?<trackingId>[\\p{Digit}\\p{Blank}]{9})(?<cpcIndicator>[\\p{Alnum}\\p{Blank}]{1})\\p{Blank}{7}\\p{Blank}{8}(?<amexROCCountPOA>(?<amexROCCountPOAPrefix>\\p{Digit}{6})(?<amexROCCountPOASuffix>[A-I{]{1})).{0,261}$");
    private static final DateFormat julianDate = new SimpleDateFormat("yyyyDDD");
    /**
     * amexPayeeNumber
     * Numeric String 10 bytes long, required
     * This field contains the Service Establishment (SE) Number of the merchant that received the payment from American Express.
     * Note: SE Numbers are assigned by American Express.
     */
    @JsonProperty("AMEX_PAYEE_NUMBER")
    @Size(max = 10)
    @NotNull
    private Long amexPayeeNumber;

    /**
     *  amexSeNumber
     *    Numeric String 10 bytes long, required
     *    This field contains the Service Establishment (SE) Number of the merchant being reconciled, which may not necessarily be the same SE receiving payment (see Field 1, AMEX_PAYEE_NUMBER).
     *    This is the SE Number under which the transactions were sub-mitted, which usually corresponds to the physical location.
     */
    @JsonProperty("AMEX_SE_NUMBER")
    @Size(max = 10)
    @NotNull
    private Long amexSeNumber;

    /**
     *  seUnitNumber
     *    This field contains the merchant-assigned SE Unit Number (usually an internal, store identifier code) that corresponds to a specific store or location.
     *    If no value is assigned, this field is character space filled.
     */
    @JsonProperty("SE_UNIT_NUMBER")
    @Size(max = 10)
    @NotNull
    private String seUnitNumber;

    @JsonProperty("PAYMENT_YEAR")
    @Size(max = 4)
    @NotNull
    private Long paymentYear;

    /**
     *  paymentNumber
     *    This field contains the Payment Number, a reference number used by the American Express Payee to reconcile the daily settlement to the daily payment.
     */
    @JsonProperty("PAYMENT_NUMBER")
    @Size(max = 8)
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

    /**
     *  seBusinessDate
     *     This field contains the SE Business Date assigned to this submission by the submitting merchant location.
     */
    @JsonProperty("SE_BUSINESS_DATE")
    @NotNull
    private Date seBusinessDate;

    /**
     *  amexProcessingDate
     *    This field contains the American Express Transaction Processing Date, which is used to determine the payment date.
     */
    @JsonProperty("AMEX_PROCESS_DATE")
    @NotNull
    private Date amexProcessDate;

    /**
     *  socInvoiceNumber
     *  This field contains the Summary of Charge (SOC) Invoice Number.
     *
     *  For electronically submitted SOCs where the TBT_IDENTIFICATION_NUMBER (field 5 in the GFSG
     *          Submission file) is populated with all zeros, this value is the concatenation of the Julian
     *  Date (positions 60-62) and the last three digits of the PCID number under which this SOC was
     *  submitted (positions 63-65).
     *
     *          For electronically submitted SOCs where the TBT_IDENTIFICATION_NUMBER (field 5 in the GFSG
     *  Submission file) is populated with any numeric value other than zero, this value will be the
     *  last six digits of the TBT_IDENTIFICATION_NUMBER under which this SOC was submitted
     *          (positions 60-65).
     *
     */
    @JsonProperty("SOC_INVOICE_NUMBER")
    @Size(max = 6)
    @NotNull
    private Long socInvoiceNumber;

    /**
     * socAmount
     *    This field contains the Summary of Charge (SOC) Amount originally submitted for payment.
     *    Note: For US Dollar (USD) and Canadian Dollar (CAD) trans-actions, two decimal places are implied.
     */
    @JsonProperty("SOC_AMOUNT")
    @Size(max = 11)
    @NotNull
    private Long socAmount;

    /**
     * discountAmount
     *   This field contains the total Discount Amount, based on socAmount and discountRate
     */
    @JsonProperty("DISCOUNT_AMOUNT")
    @Size(max = 9)
    @NotNull
    private Long discountAmount;

    /**
     * serviceFeeAmount
     *   This field contains the total Service Fee Amount, based on socamount and serviceFeeRate
     */
    @JsonProperty("SERVICE_FEE_AMOUNT")
    @Size(max = 7)
    @NotNull
    private Long serviceFeeAmount;

    /**
     * netSOCAmount
     *   This field contains the Net SOC (Summary of Charge) Amount submitted to American Express for
     *   payment, which is the sum total of  socAmount, less discountAmount and serviceFeeAmount
     */
    @JsonProperty("NET_SOC_AMOUNT")
    @Size(max = 11)
    @NotNull
    private Long netSOCAmount;

    /**
     * discountRate
     *   This field contains the Discount Rate (decimal place value) used to calculate the amount
     *   American Express charges a merchant for services provided per the American Express Card
     *   Acceptance Agreement.
     */
    @JsonProperty("DISCOUNT_RATE")
    @Size(max = 5)
    @NotNull
    private Long discountRate;

    /**
     *  serviceFeeRate
     *    This field contains the Service Fee Rate (decimal place value) used to calculate the amount
     *    American Express charges a merchant as service fees.
     *    Service fees are assessed only in certain situations and may not apply to all SEs.
     */
    @JsonProperty("SEVICE_FEE_RATE")
    @Size(max = 5)
    @NotNull
    private Long serviceFeeRate;

    /**
     * amexGrossAmount
     *    This field contains the gross amount of American Express charges submitted in the original
     *    SOC amount.
     */
    @JsonProperty("AMEX_GROSS_AMOUNT")
    @Size(max = 11)
    @NotNull
    private Long amexGrossAmount;

    /**
     *  amexROCCount
     *     This field contains the quantity of American Express charges submitted in this Summary of
     *     Charge (SOC). This entry is always positive, which is indicated by an upper-case alpha
     *     code used in place of the last (least significant) digit.
     */
    @JsonProperty("AMEX_ROC_COUNT")
    @Size(max = 5)
    @NotNull
    private Long amexROCCount;

    /**
     *  trackingId
     *     This field contains the Tracking ID, which holds SOC processing information.
     */
    @JsonProperty("TRACKING_ID")
    @Size(max = 9)
    @NotNull
    private Long trackingId;
    /**
     * cpcIndicator
     * This field contains the CPC Indicator, which indicates whether the batch that corresponds
     * to this SOC detail record contains CPC/Corporate Purchasing Card (a.k.a., CPS/Corporate
     * Purchasing Solutions Card) transactions. Valid entries include:
     * P = CPC/CPS Card transactions (special pricing applied)
     * ~ = Non-CPC/CPS Card transactions
     * Note: Tilde (~) represents a character space.
     */
    @JsonProperty("CPC_INDICATOR")
    @Size(max = 1)
    @NotNull
    private Boolean cpcIndicator;
    /**
     * amexROCCountPOA
     * This field contains the quantity of American Express charges submitted in this Summary of
     * Charge (SOC). This entry is always positive
     */
    @JsonProperty("AMEX_ROC_COUNT_POA")
    @Size(max = 7)
    @NotNull
    private Long amexROCCountPOA;

    public SOCDetail() {
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

    public Long getNetSOCAmount() {
        return netSOCAmount;
    }

    public void setNetSOCAmount(Long netSOCAmount) {
        this.netSOCAmount = netSOCAmount;
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

    public Long getAmexGrossAmount() {
        return amexGrossAmount;
    }

    public void setAmexGrossAmount(Long amexGrossAmount) {
        this.amexGrossAmount = amexGrossAmount;
    }

    public Long getAmexROCCount() {
        return amexROCCount;
    }

    public void setAmexROCCount(Long amexROCCount) {
        this.amexROCCount = amexROCCount;
    }

    public Long getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(Long trackingId) {
        this.trackingId = trackingId;
    }

    public Boolean getCpcIndicator() {
        return cpcIndicator;
    }

    public void setCpcIndicator(Boolean cpcIndicator) {
        this.cpcIndicator = cpcIndicator;
    }

    public Long getAmexROCCountPOA() {
        return amexROCCountPOA;
    }


    public void setAmexROCCountPOA(Long amexROCCountPOA) {
        this.amexROCCountPOA = amexROCCountPOA;
    }


    public SOCDetail withAmexPayeeNumber(Long amexPayeeNumber) {
        this.amexPayeeNumber = amexPayeeNumber;
        return this;
    }

    public SOCDetail withAmexSeNumber(Long amexSeNumber) {
        this.amexSeNumber = amexSeNumber;
        return this;
    }

    public SOCDetail withSeUnitNumber(String seUnitNumber) {
        this.seUnitNumber = seUnitNumber;
        return this;
    }

    public SOCDetail withPaymentYear(Long paymentYear) {
        this.paymentYear = paymentYear;
        return this;
    }

    public SOCDetail withPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
        return this;
    }

    public SOCDetail withRecordType(Long recordType) {
        this.recordType = recordType;
        return this;
    }

    public SOCDetail withDetailRecordType(Long detailRecordType) {
        this.detailRecordType = detailRecordType;
        return this;
    }

    public SOCDetail withSeBusinessDate(Date seBusinessDate) {
        this.seBusinessDate = seBusinessDate;
        return this;
    }

    public SOCDetail withAmexProcessDate(Date amexProcessDate) {
        this.amexProcessDate = amexProcessDate;
        return this;
    }

    public SOCDetail withSocInvoiceNumber(Long socInvoiceNumber) {
        this.socInvoiceNumber = socInvoiceNumber;
        return this;
    }

    public SOCDetail withSocAmount(Long socAmount) {
        this.socAmount = socAmount;
        return this;
    }

    public SOCDetail withDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public SOCDetail withServiceFeeAmount(Long serviceFeeAmount) {
        this.serviceFeeAmount = serviceFeeAmount;
        return this;
    }

    public SOCDetail withNetSOCAmount(Long netSOCAmount) {
        this.netSOCAmount = netSOCAmount;
        return this;
    }

    public SOCDetail withDiscountRate(Long discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public SOCDetail withServiceFeeRate(Long serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
        return this;
    }

    public SOCDetail withAmexGrossAmount(Long amexGrossAmount) {
        this.amexGrossAmount = amexGrossAmount;
        return this;
    }

    public SOCDetail withAmexROCCount(Long amexROCCount) {
        this.amexROCCount = amexROCCount;
        return this;
    }

    public SOCDetail withTrackingId(Long trackingId) {
        this.trackingId = trackingId;
        return this;
    }

    public SOCDetail withCpcIndicator(Boolean cpcIndicator) {
        this.cpcIndicator = cpcIndicator;
        return this;
    }

    public SOCDetail withAmexROCCountPOA(Long amexROCCountPOA) {
        this.amexROCCountPOA = amexROCCountPOA;
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

        SOCDetail socDetail = (SOCDetail) o;

        return new EqualsBuilder()
                .append(getAmexPayeeNumber(), socDetail.getAmexPayeeNumber())
                .append(getAmexSeNumber(), socDetail.getAmexSeNumber())
                .append(getSeUnitNumber(), socDetail.getSeUnitNumber())
                .append(getPaymentYear(), socDetail.getPaymentYear())
                .append(getPaymentNumber(), socDetail.getPaymentNumber())
                .append(getRecordType(), socDetail.getRecordType())
                .append(getDetailRecordType(), socDetail.getDetailRecordType())
                .append(getSeBusinessDate(), socDetail.getSeBusinessDate())
                .append(getAmexProcessDate(), socDetail.getAmexProcessDate())
                .append(getSocInvoiceNumber(), socDetail.getSocInvoiceNumber())
                .append(getSocAmount(), socDetail.getSocAmount())
                .append(getDiscountAmount(), socDetail.getDiscountAmount())
                .append(getServiceFeeAmount(), socDetail.getServiceFeeAmount())
                .append(getNetSOCAmount(), socDetail.getNetSOCAmount())
                .append(getDiscountRate(), socDetail.getDiscountRate())
                .append(getServiceFeeRate(), socDetail.getServiceFeeRate())
                .append(getAmexGrossAmount(), socDetail.getAmexGrossAmount())
                .append(getAmexROCCount(), socDetail.getAmexROCCount())
                .append(getTrackingId(), socDetail.getTrackingId())
                .append(getCpcIndicator(), socDetail.getCpcIndicator())
                .append(getAmexROCCountPOA(), socDetail.getAmexROCCountPOA())
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
                .append(getDiscountAmount())
                .append(getServiceFeeAmount())
                .append(getNetSOCAmount())
                .append(getDiscountRate())
                .append(getServiceFeeRate())
                .append(getAmexGrossAmount())
                .append(getAmexROCCount())
                .append(getTrackingId())
                .append(getCpcIndicator())
                .append(getAmexROCCountPOA())
                .toHashCode();
    }

    public static SOCDetail parse(String lineOfText) throws java.text.ParseException {
        Matcher m = pattern.matcher(lineOfText);
        if (m.matches()) {
            return new SOCDetail()
                    .withAmexPayeeNumber(Long.valueOf(m.group("amexPayeeNumber")))
                    .withAmexSeNumber(Long.valueOf(m.group("amexSeNumber")))
                    .withSeUnitNumber(m.group("seUnitNumber").trim())
                    .withPaymentYear(Long.valueOf(m.group("paymentYear")))
                    .withPaymentNumber(m.group("paymentNumber"))
                    .withRecordType(Long.valueOf(m.group("recordType")))
                    .withDetailRecordType(Long.valueOf(m.group("detailRecordType")))
                    .withSeBusinessDate(julianDate.parse(m.group("seBusinessDate")))
                    .withAmexProcessDate(julianDate.parse(m.group("amexProcessDate")))
                    .withSocInvoiceNumber(Long.valueOf(m.group("socInvoiceNumber")))
                    .withSocAmount(AmountParser.toLong(m.group("socAmountPrefix"), m.group("socAmountSuffix")))
                    .withDiscountAmount(AmountParser.toLong(m.group("discountAmountPrefix"), m.group("discountAmountSuffix")))
                    .withServiceFeeAmount(AmountParser.toLong(m.group("serviceFeeAmountPrefix"), m.group("serviceFeeAmountSuffix")))
                    .withNetSOCAmount(AmountParser.toLong(m.group("netSOCAmountPrefix"), m.group("netSOCAmountSuffix")))
                    .withDiscountRate(Long.valueOf(m.group("discountRate")))
                    .withServiceFeeRate(Long.valueOf(m.group("serviceFeeRate")))
                    .withAmexGrossAmount(AmountParser.toLong(m.group("amexGrossAmountPrefix"), m.group("amexGrossAmountSuffix")))
                    .withAmexROCCount(AmountParser.toLong(m.group("amexROCCountPrefix"), m.group("amexROCCountSuffix")))
                    .withTrackingId(Long.valueOf(m.group("trackingId")))
                    .withCpcIndicator(m.group("cpcIndicator").equals("P"))
                    .withAmexROCCountPOA(AmountParser.toLong(m.group("amexROCCountPOAPrefix"), m.group("amexROCCountPOASuffix")));
        } else {
            return null;
        }
    }
    public static void writeCSVFile(String csvFileName, List<SOCDetail> records) {
        ICsvBeanWriter beanWriter = null;
        CellProcessor[] processors = new CellProcessor[]{
                new ParseLong(), // amexPayeeNumber
                new ParseLong(), // amexSeNumber
                new Trim(), // seUnitNumber
                new ParseLong(), // paymentYear
                new org.supercsv.cellprocessor.constraint.NotNull(), // paymentNumber
                new org.supercsv.cellprocessor.constraint.NotNull(), // recordType
                new Optional(new Trim()), // detailRecordType
                new FmtDate("yyyy-MM-dd"), // seBusinessDate
                new FmtDate("yyyy-MM-dd"), // amexProcessDate
                new ParseLong(), // socInvoiceNumber
                new ParseLong(), // socAmount
                new ParseLong(), // discountAmount
                new ParseLong(), // serviceFeeAmount
                new ParseLong(), // netSOCAmount
                new ParseLong(), // discountRate
                new ParseLong(), // serviceFeeRate
                new ParseLong(), // amexGrossAmount
                new ParseLong(), // amexROCCount
                new ParseLong(), // trackingId
                new Optional(new Trim()), // cpcIndicator
                new ParseLong(), // amexROCCountPOA

        };

        try {
            CsvPreference redshiftPreprerence =
                    new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE)
                            .surroundingSpacesNeedQuotes(true)
                            .ignoreEmptyLines(true)
                            .useQuoteMode(new AlwaysQuoteMode())
                            .build();
            beanWriter = new CsvBeanWriter(new FileWriter(csvFileName),
                    redshiftPreprerence);
            String[] header = {"amexPayeeNumber", "amexSeNumber", "seUnitNumber", "paymentYear", "paymentNumber", "recordType", "detailRecordType",
                    "seBusinessDate", "amexProcessDate", "socInvoiceNumber", "socAmount", "discountAmount", "serviceFeeAmount", "netSOCAmount",
                    "discountRate", "serviceFeeRate", "amexGrossAmount", "amexROCCount", "trackingId", "cpcIndicator", "amexROCCountPOA"};
            beanWriter.writeHeader(header);

            for (SOCDetail record : records) {
                beanWriter.write(record, header, processors);
            }

        } catch (IOException ex) {
            System.err.println("Error writing the CSV file: " + ex);
        } finally {
            if (beanWriter != null) {
                try {
                    beanWriter.close();
                } catch (IOException ex) {
                    System.err.println("Error closing the writer: " + ex);
                }
            }
        }
    }
}

package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.money.MonetaryAmount;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dashirov on 5/12/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "dataFileHeaderRecordType",
        "amexApplication",
        "serviceAccessId",
        "dataType",
        "originalFileTransmissionDateTime"
})
public class Detail {
    public final static Pattern pattern = Pattern.compile("^(?<dataFileRecordType>D)(?<filler2>\\p{Space}{5})(?<serviceEstablishmentNumber>\\p{ASCII}{10})(?<filler4>\\p{Space}{10})(?<cardMemberAccountNumber>[\\p{Space}\\p{Alnum}]{19})(?<filler6>\\p{Space}{1})(?<currentCaseNumber>\\p{ASCII}{11})(?<currentActionNumber>\\p{ASCII}{2})(?<previousCaseNumber>\\p{ASCII}{11})(?<previousActionNumber>\\p{ASCII}{2})(?<resolution>[YN]{1})(?<fromSystem>[\\p{Space}FRSTXPG]{1})(?<rejectsToSystem>[\\p{Space}RSTXPG]{1})(?<disputesToSystem>[\\p{Space}RSTXPG]{1})(?<adjustmentDate>\\p{Digit}{8})(?<chargeDate>\\p{Digit}{8})(?<amexEmployeeId>\\p{ASCII}{7})(?<filler18>\\p{Space}{5})(?<caseType>\\p{ASCII}{6})(?<locationNumber>\\p{ASCII}{15})(?<chargebackReasonCode>\\p{Alnum}{3})(?<chargebackAmount>[\\{Space}-]\\p{Digit}{13}\\.\\p{Digit}{2})(?<chargebackAdjustmentNumber>\\p{ASCII}{6})(?<chargebackResolutionAdjustmentNumber>\\p{ASCII}{6})(?<chargebackReferenceCode>\\p{ASCII}{12})(?<filler26>\\p{Space}{13})(?<billedAmount>[\\p{Space}-]\\p{Digit}{13}\\.\\p{Digit}{2})(?<socAmount>[\\p{Space}-]\\p{Digit}{13}\\.\\p{Digit}{2})(?<socInvoiceNumber>\\p{ASCII}{6})(?<chargeInvoiceNumber>\\p{ASCII}{6})(?<foreignAmount>\\p{ASCII}{15})(?<currency>\\p{Upper}{3})(?<supportToFollow>[YIRN]{1})(?<cardMemberName1>\\p{ASCII}{30})(?<cardMemberName2>\\p{ASCII}{30})(?<cardMemberAddress1>\\p{ASCII}{30})(?<cardMemberAddress2>\\p{ASCII}{30})(?<cardMemberCityState>\\p{ASCII}{30})(?<cardMemberZip>\\p{ASCII}{9})(?<cardMemberFirstName1>\\p{ASCII}{12})(?<cardMemberMiddleName1>\\p{ASCII}{12})(?<cardMemberLastName1>\\p{ASCII}{20})(?<cardMemberOriginalAccountNumber>\\p{ASCII}{15})(?<cardMemberOriginalName>\\p{ASCII}{30})(?<cardMemberOriginalFirstName>\\p{ASCII}{12})(?<cardMemberOriginalMiddleName>\\p{ASCII}{12})(?<cardMemberOriginalLastName>\\p{ASCII}{20})(?<note1>\\p{ASCII}{66})(?<note2>\\p{ASCII}{78})(?<note3>\\p{ASCII}{60})(?<note4>\\p{ASCII}{60})(?<note5>\\p{ASCII}{60})(?<note6>\\p{ASCII}{60})(?<note7>\\p{ASCII}{60})(?<triumphSequenceNumber>\\p{ASCII}{2})(?<filler56>\\p{Space}{20})(?<filler57>\\p{Space}{15})(?<filler58>\\p{Space}{10})(?<industrySpecificPayload>\\p{ASCII}{1172})");
    private final static DateFormat headerDateTime = new SimpleDateFormat("yyyyDDDHHmmss");
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final DateFormat julianDate = new SimpleDateFormat("yyyyDDD");
    /**
     * dataFileRecordType
     * This field contains the constant literal “D”, a record type code that indicates that this is a
     * Chargeback Notifications (CBNOT) File Detail Record. This field must appear as the first item in the record.
     */
    @JsonProperty
    private String dataFileRecordType;

    /**
     * serviceEstablishmentNumber
     * This field contains the Service Establishment (SE) Number that STARS* searches for and routes data to, based
     * on the setup for the corresponding CBNOT data type (for outbound data).
     */
    @JsonProperty
    private String serviceEstablishmentNumber;

    @JsonProperty
    private String cardMemberAccountNumber;

    /**
     * FINCAP transactions — This field contains the FINCAP Tracking ID. See FINCAP_TRACKING_ID on page 54.
     *  Non-FINCAP transactions — This field contains the unique, American Express-assigned, current case
     * (identification) number for this transaction, if this is a chargeback notification or final resolution.
     * Notes:
     * 1. For Customer Service Systems, this field is composed of:
     * Subfield Name      Length     Position
     * CSS_CASE_NUMBER    7 bytes     47-53
     * FILLER‡            4 bytes     54-57
     * 2. For SIREN/SOFA, this field is composed of:
     * Subfield Name     Length     Position
     * SS_CASE_NUMBER     9 bytes     47-55
     * FILLER‡            2 bytes     56-57
     * <p>
     * * As defined for Customer Service Systems.
     * † As defined for SIREN/SOFA System. SIREN = SE Information Retrieval Entry Network. SOFA = SE Online Financial
     * Adjustment. A case processing system that contains unconverted Service Establishment dispute
     * case information and the interfacing financial adjustment system.
     * ‡ FILLER subfields are character space filled.
     * <p>
     * TODO: Additional processing may be required to determine field value class
     */
    @JsonProperty
    private String currentCaseNumber;

    /**
     * This field contains a case number from various sources, or is blank (character space filled),
     * depending on the specific details of this record.
     */
    @JsonProperty
    private String previousCaseNumber;

    /**
     * This field contains a code that indicates if this record is a Resolution Letter:
     * Y = Yes N = No
     * Note: If this value is “Y”, then Field 24 CB_RESOLUTION_ ADJ_NUMBER should be populated.
     */
    @JsonProperty
    private Resolution resolution; // Y=YES, N=NO

    /**
     * This field contains the date of the adjustment.
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/New_York")
    private Date adjustmentDate;

    /**
     * This field contains the date of the charge.
     */
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/New_York")
    private Date chargeDate;

    /**
     * AIRDS = Airline Credit Requested
     * AIRLT = Airline Lost/Stolen Ticket
     * AIRRT = Airline Returned Ticket
     * AIRTB = Airline Support of Charge
     * AREXS = Reservation/Cancellation
     * CARRD = CarRental
     * GSDIS = Goods/Services
     * NAXMG = Merchandise Not Received
     * NAXMR = Merchandise Returned
     * SEDIS = GeneralDispute
     * FRAUD = Fraud Dispute
     * CRCDW = Collision Damage Waiver Liability
     */
    @JsonProperty
    private CaseType caseType;

    /**
     * This field may contain the store or location number where the charge occurred.
     * Also, refer to Field 99, LOC_REF_NUMBER.
     */
    @JsonProperty
    private String locationNumber;


    /**
     * This field contains a three-character, chargeback reason code.
     */
    @JsonProperty
    private ChargebackReasonCode chargebackReasonCode;


    /**
     * This field contains the adjustment or chargeback amount, which can be a debit or credit. The format for this
     * field is a one-digit “sign,” followed by a 13-digit “dollar amount” (right justified and zero filled),
     * one-digit “decimal point,” and two-digit “cents.”
     */
    @JsonProperty
    private MonetaryAmount chargebackAmount;

    @JsonProperty
    private String chargebackAdjustmentNumber;


    @JsonProperty
    String chargebackResolutionAdjustmentNumber;

    @JsonProperty
    private String chargebackReferenceCode;

    @JsonProperty
    private MonetaryAmount billedAmount;

    @JsonProperty
    private MonetaryAmount socAmount;

    @JsonProperty
    private String socInvoiceNumber;

    @JsonProperty
    private String rocInvoiceNumber;

    @JsonProperty
    private MonetaryAmount foreignAmount;

    @JsonProperty
    private String currency;

    @JsonProperty
    private SupportToFollow supportToFollow;

    @JsonProperty
    private String cardMemberName1;

    @JsonProperty
    private String cardMemberName2;

    @JsonProperty
    private String cardMemberAddress1;

    @JsonProperty
    private String cardMemberAddress2;

    @JsonProperty
    private String cardMemberCityState;

    @JsonProperty
    private String cardMemberZip;

    @JsonProperty
    private String cardMemberFirstName1;

    @JsonProperty
    private String cardMemberMiddleName1;

    @JsonProperty
    private String cardMemberLastName1;

    @JsonProperty
    private String cardMemberOriginalAccountNumber;


    public Detail(String dataFileRecordType, String serviceEstablishmentNumber, String cardMemberAccountNumber, String currentCaseNumber, String previousCaseNumber, Resolution resolution, Date adjustmentDate, Date chargeDate, CaseType caseType, String locationNumber, ChargebackReasonCode chargebackReasonCode, MonetaryAmount chargebackAmount, String chargebackAdjustmentNumber, String chargebackResolutionAdjustmentNumber, String chargebackReferenceCode, MonetaryAmount billedAmount, MonetaryAmount socAmount, String socInvoiceNumber, String rocInvoiceNumber, MonetaryAmount foreignAmount, String currency, SupportToFollow supportToFollow, String cardMemberName1, String cardMemberName2, String cardMemberAddress1, String cardMemberAddress2, String cardMemberCityState, String cardMemberZip, String cardMemberFirstName1, String cardMemberMiddleName1, String cardMemberLastName1, String cardMemberOriginalAccountNumber) {
        this.dataFileRecordType = dataFileRecordType;
        this.serviceEstablishmentNumber = serviceEstablishmentNumber;
        this.cardMemberAccountNumber = cardMemberAccountNumber;
        this.currentCaseNumber = currentCaseNumber;
        this.previousCaseNumber = previousCaseNumber;
        this.resolution = resolution;
        this.adjustmentDate = adjustmentDate;
        this.chargeDate = chargeDate;
        this.caseType = caseType;
        this.locationNumber = locationNumber;
        this.chargebackReasonCode = chargebackReasonCode;
        this.chargebackAmount = chargebackAmount;
        this.chargebackAdjustmentNumber = chargebackAdjustmentNumber;
        this.chargebackResolutionAdjustmentNumber = chargebackResolutionAdjustmentNumber;
        this.chargebackReferenceCode = chargebackReferenceCode;
        this.billedAmount = billedAmount;
        this.socAmount = socAmount;
        this.socInvoiceNumber = socInvoiceNumber;
        this.rocInvoiceNumber = rocInvoiceNumber;
        this.foreignAmount = foreignAmount;
        this.currency = currency;
        this.supportToFollow = supportToFollow;
        this.cardMemberName1 = cardMemberName1;
        this.cardMemberName2 = cardMemberName2;
        this.cardMemberAddress1 = cardMemberAddress1;
        this.cardMemberAddress2 = cardMemberAddress2;
        this.cardMemberCityState = cardMemberCityState;
        this.cardMemberZip = cardMemberZip;
        this.cardMemberFirstName1 = cardMemberFirstName1;
        this.cardMemberMiddleName1 = cardMemberMiddleName1;
        this.cardMemberLastName1 = cardMemberLastName1;
        this.cardMemberOriginalAccountNumber = cardMemberOriginalAccountNumber;
    }


    public String getDataFileRecordType() {
        return dataFileRecordType;
    }

    public void setDataFileRecordType(String dataFileRecordType) {
        this.dataFileRecordType = dataFileRecordType;
    }

    public String getServiceEstablishmentNumber() {
        return serviceEstablishmentNumber;
    }

    public void setServiceEstablishmentNumber(String serviceEstablishmentNumber) {
        this.serviceEstablishmentNumber = serviceEstablishmentNumber;
    }

    public String getCardMemberAccountNumber() {
        return cardMemberAccountNumber;
    }

    public void setCardMemberAccountNumber(String cardMemberAccountNumber) {
        this.cardMemberAccountNumber = cardMemberAccountNumber;
    }

    public String getCurrentCaseNumber() {
        return currentCaseNumber;
    }

    public void setCurrentCaseNumber(String currentCaseNumber) {
        this.currentCaseNumber = currentCaseNumber;
    }

    public String getPreviousCaseNumber() {
        return previousCaseNumber;
    }

    public void setPreviousCaseNumber(String previousCaseNumber) {
        this.previousCaseNumber = previousCaseNumber;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public Date getAdjustmentDate() {
        return adjustmentDate;
    }

    public void setAdjustmentDate(Date adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public Date getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(Date chargeDate) {
        this.chargeDate = chargeDate;
    }

    public CaseType getCaseType() {
        return caseType;
    }

    public void setCaseType(CaseType caseType) {
        this.caseType = caseType;
    }

    public String getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    public ChargebackReasonCode getChargebackReasonCode() {
        return chargebackReasonCode;
    }

    public void setChargebackReasonCode(ChargebackReasonCode chargebackReasonCode) {
        this.chargebackReasonCode = chargebackReasonCode;
    }

    public MonetaryAmount getChargebackAmount() {
        return chargebackAmount;
    }

    public void setChargebackAmount(MonetaryAmount chargebackAmount) {
        this.chargebackAmount = chargebackAmount;
    }

    public String getChargebackAdjustmentNumber() {
        return chargebackAdjustmentNumber;
    }

    public void setChargebackAdjustmentNumber(String chargebackAdjustmentNumber) {
        this.chargebackAdjustmentNumber = chargebackAdjustmentNumber;
    }

    public String getChargebackResolutionAdjustmentNumber() {
        return chargebackResolutionAdjustmentNumber;
    }

    public void setChargebackResolutionAdjustmentNumber(String chargebackResolutionAdjustmentNumber) {
        this.chargebackResolutionAdjustmentNumber = chargebackResolutionAdjustmentNumber;
    }

    public String getChargebackReferenceCode() {
        return chargebackReferenceCode;
    }

    public void setChargebackReferenceCode(String chargebackReferenceCode) {
        this.chargebackReferenceCode = chargebackReferenceCode;
    }

    public MonetaryAmount getBilledAmount() {
        return billedAmount;
    }

    public void setBilledAmount(MonetaryAmount billedAmount) {
        this.billedAmount = billedAmount;
    }

    public MonetaryAmount getSocAmount() {
        return socAmount;
    }

    public void setSocAmount(MonetaryAmount socAmount) {
        this.socAmount = socAmount;
    }

    public String getSocInvoiceNumber() {
        return socInvoiceNumber;
    }

    public void setSocInvoiceNumber(String socInvoiceNumber) {
        this.socInvoiceNumber = socInvoiceNumber;
    }

    public String getRocInvoiceNumber() {
        return rocInvoiceNumber;
    }

    public void setRocInvoiceNumber(String rocInvoiceNumber) {
        this.rocInvoiceNumber = rocInvoiceNumber;
    }

    public MonetaryAmount getForeignAmount() {
        return foreignAmount;
    }

    public void setForeignAmount(MonetaryAmount foreignAmount) {
        this.foreignAmount = foreignAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public SupportToFollow getSupportToFollow() {
        return supportToFollow;
    }

    public void setSupportToFollow(SupportToFollow supportToFollow) {
        this.supportToFollow = supportToFollow;
    }

    public String getCardMemberName1() {
        return cardMemberName1;
    }

    public void setCardMemberName1(String cardMemberName1) {
        this.cardMemberName1 = cardMemberName1;
    }

    public String getCardMemberName2() {
        return cardMemberName2;
    }

    public void setCardMemberName2(String cardMemberName2) {
        this.cardMemberName2 = cardMemberName2;
    }

    public String getCardMemberAddress1() {
        return cardMemberAddress1;
    }

    public void setCardMemberAddress1(String cardMemberAddress1) {
        this.cardMemberAddress1 = cardMemberAddress1;
    }

    public String getCardMemberAddress2() {
        return cardMemberAddress2;
    }

    public void setCardMemberAddress2(String cardMemberAddress2) {
        this.cardMemberAddress2 = cardMemberAddress2;
    }

    public String getCardMemberCityState() {
        return cardMemberCityState;
    }

    public void setCardMemberCityState(String cardMemberCityState) {
        this.cardMemberCityState = cardMemberCityState;
    }

    public String getCardMemberZip() {
        return cardMemberZip;
    }

    public void setCardMemberZip(String cardMemberZip) {
        this.cardMemberZip = cardMemberZip;
    }

    public String getCardMemberFirstName1() {
        return cardMemberFirstName1;
    }

    public void setCardMemberFirstName1(String cardMemberFirstName1) {
        this.cardMemberFirstName1 = cardMemberFirstName1;
    }

    public String getCardMemberMiddleName1() {
        return cardMemberMiddleName1;
    }

    public void setCardMemberMiddleName1(String cardMemberMiddleName1) {
        this.cardMemberMiddleName1 = cardMemberMiddleName1;
    }

    public String getCardMemberLastName1() {
        return cardMemberLastName1;
    }

    public void setCardMemberLastName1(String cardMemberLastName1) {
        this.cardMemberLastName1 = cardMemberLastName1;
    }

    public String getCardMemberOriginalAccountNumber() {
        return cardMemberOriginalAccountNumber;
    }

    public void setCardMemberOriginalAccountNumber(String cardMemberOriginalAccountNumber) {
        this.cardMemberOriginalAccountNumber = cardMemberOriginalAccountNumber;
    }

    public enum SupportToFollow {
        Y("Support is coming via mail or fax"),
        I("A scanned image provides support"),
        R("Both forms of support to follow: Mail or fax, AND Scanned image"),
        N("No support is being forwarded");
        private String description;

        SupportToFollow(String description) {
            this.description = description;
        }
    }

    public enum Resolution {
        Y(true, "Yes"),
        N(false, "No");
        private boolean aBoolean;
        private String aString;

        Resolution(boolean aBoolean, String aString) {
            this.aBoolean = aBoolean;
            this.aString = aString;
        }
    }

    public enum CaseType {
        AIRDS("Airline Credit Requested"),
        AIRLT("Airline Lost/Stolen Ticket"),
        AIRRT("Airline Returned Ticket"),
        AIRTB("Airline Support of Charge"),
        AREXS("Reservation/Cancellation"),
        CARRD("CarRental"),
        GSDIS("Goods/Services"),
        NAXMG("Merchandise Not Received"),
        NAXMR("Merchandise Returned"),
        SEDIS("GeneralDispute"),
        FRAUD("Fraud Dispute"),
        CRCDW("Collision Damage Waiver Liability");
        private String type;

        CaseType(String type) {
            this.type = type;
        }
    }

    public enum ChargebackReasonCode {
        A01("The amount of the Authorization Approval was less than the amount of the Charge you submitted."),
        A02("The Charge you submitted did not receive a valid Authorization Approval; it was declined or the Card was expired."),
        A08("The Charge was submitted after the Authorization Approval expired."),
        C02("We have not received the Credit (or partial Credit) you were to apply to the Card."),
        C04("The goods or services were returned or refused but the Cardmember did not receive Credit."),
        C05("The Cardmember claims that the goods/services ordered were cancelled."),
        C08("The Cardmember claims to have not received (or only partially received) the goods/services."),
        C14("The Cardmember has provided us with proof of payment by another method."),
        C18("The Cardmember claims to have cancelled a lodging reservation or a Credit for a CARDeposit Charge was not received by the Cardmember."),
        C28("Cardmember claims to have cancelled or attempted to cancel Recurring Billing Charges for goods or services. Please discontinue all future billing for this Recurring Billing Charge."),
        C31("The Cardmember claims to have received goods/services that are different than the written description provided at the time of the Charge."),
        C32("The Cardmember claims to have received damaged or defective goods/services."),
        F10("The Cardmember claims they did not participate in this Charge and you have not provided a copy of an imprint of the Card."),
        F14("The Cardmember claims they did not participate in this Charge and you have not provided a copy of the Cardmember's signature to support the Charge."),
        F22("The Cardmember denies participation in the Charge you submitted and the Card was expired or was not yet valid when you processed the Charge."),
        F24("The Cardmember denies participation in the Charge you submitted and you have failed to provide proof that the Cardmember participated in the Charge."),
        F29("The Cardmember denies participation in a mail order, telephone order, or internet Charge."),
        F30("A counterfeit Chip Card was used at a terminal that was not capable of processing a Chip Card Transaction."),
        F31("A lost/stolen/non-received Chip Card was used at a terminal that was not capable of processing a Chip Card Transaction with PIN validation."),
        FR2("The Cardmember denies authorizing the Charge and your Establishment has been placed in the Fraud Full Recourse Program."),
        FR4("The Cardmember has disputed the Charge and you have been placed in the Immediate Chargeback Program."),
        FR5("Your account is on the Immediate Chargeback program. Under these circumstances, disputed charges are debited from your account with no further recourse. These chargebacks cannot be reversed unless you issue a credit to the account, or the Cardholder advises the charge(s) are valid."),
        FR6("The Cardmember has disputed the Charge and you have been placed in the Partial Immediate Chargeback Program."),
        M01("We have received your authorization to process Chargeback for the Charge."),
        M10("The Cardmember claims to have been incorrectly billed for Capital Damages."),
        M49("The Cardmember claims to have been incorrectly Charged for theft, loss of use, or other fees related to theft or loss of use of a rental vehicle."),
        P01("You have submitted a Charge using an invalid or otherwise incorrect Card Number."),
        P03("The Cardmember claims the Charge you submitted should have been submitted as a Credit."),
        P04("The Cardmember claims the Credit you submitted should have been submitted as a Charge."),
        P05("The Charge amount you submitted differs from the amount the Cardmember agreed to pay."),
        P07("The Charge was not submitted within the required timeframe."),
        P08("The individual Charge was submitted more than once."),
        P22("The Card Number in the Submission does not match the Card Number in the original Charge."),
        P23("The Charge was incurred in an invalid currency."),
        R03("Complete support and/or documentation were not provided as requested."),
        R13("We did not receive your response to our Inquiry within the specified timeframe."),
        M11("We recently debited your account for the adjustment amount indicated. We have now received your credit for this charge and we are reversing the debit and crediting your account."),
        M38("We recently debited your account for the adjustment amount indicated. We are now reversing the debit and crediting your account"),
        M19("According to our records, your credit was inadvertently deducted from another merchant's account. This has now been corrected, and a debit for this amount will be issued. We apologize for any inconvenience this may have caused."),
        M21("Our records indicate that your charge or summary was inadvertently paid to another merchant. This has been corrected and a credit has been issued."),
        M22("Our records indicate that your service establishment was inadvertently paid for a submission sent to us by another service establishment. To correct this erroneous payment, an adjustment debiting your account has been processed."),
        M23("A review of our records indicates that your service establishment inadvertently cashed a check that belonged to another service establishment. To correct this erroneous payment, an adjustment debiting your account for the amount of this check has been processed. Please submit your charges to cover this amount, or send us a check as soon as possible."),
        M24("We have processed an adjustment transferring a debit balance from your previous account to the account listed. This balance owed us was aging on an account that is no longer active."),
        M25("Our records indicate that your affiliated account has an outstanding debit balance. This debit has not cleared because charges are no longer being submitted by this account. Consequently, we have processed an adjustment to transfer this debit to your account."),
        M27("Your cheque was returned to us by your bank. Since your account was previously credited for this cheque, we are debiting your account for the amount involved. Please send us a replacement cheque immediately."),
        M28("According to our records, an incorrect discount rate was applied to your summaries. Since the rate should have been lower, credit has been issued."),
        M29("The invoice and report provided includes details regarding discount fees for American Express charges processed for the month indicated. This invoice amount will be debited to your bank account."),
        M32("We have processed an adjustment to your account. This adjustment represents your share of the media costs incurred in your participation in our cooperative advertisement program."),
        M33("In accordance with your request, an adjustment to your account has been processed."),
        M39("We have issued an adjustment to your account to correct a transaction that was previously processed in error."),
        M43("We have processed an adjustment to your account. This adjustment represents your participation in American Express Marketing Programs. Our records indicate a recent change in payment options for your participation. Therefore, future adjustments will be invoiced on a monthly frequency"),
        M44("We have processed an adjustment to your account. This adjustment represents your participation in American Express Marketing Programs. Our records indicate a recent change in payment options for your participation. Therefore, future adjustments will be deducted on a monthly frequency from payments for charges submitted."),
        M45("A review of our records indicates that a cheque was applied to your account in error. Therefore an adjustment has been processed to debit your account."),
        M46("We have determined that your establishment was inadvertently debited for item(s) which were submitted to us by another establishment. To correct this erroneous debit, an adjustment crediting your account has been processed, and will be included in a future statement."),
        P09("We have processed duplicate payments to your account for the same transaction."),
        M02("The Cardmember no longer disputes the charge(s). Please discontinue further investigation."),
        M04("We previously received your authorization to debit your account. Please deal directly with the Cardholder for resolution on this matter."),
        M36("Please see the additional notes related to this dispute."),
        M42("Due to the length of time between the chargeback to your account and receiving your dispute, we are unable to review this for reversal."),
        S01("Your request for a chargeback reversal has been reviewed. The chargeback will remain, and your account will not be credited."),
        S03("Support received."),
        S04("We have received your request for a chargeback reversal. Please allow 2 to 3 weeks for research.");
        private String type;
        private String description;

        ChargebackReasonCode(String description) {
            this.description = description;
            if (this.name().startsWith("A")) {
                this.type = "Authorization";
            } else if (this.name().startsWith("C")) {
                this.type = "Cardmember Dispute";
            } else if (this.name().startsWith("FR")) {
                this.type = "Full Recourse";
            } else if (this.name().startsWith("F")) {
                this.type = "Fraud";
            } else if (this.name().startsWith("M")) {
                this.type = "Miscellaneous Adjustments and Resolutions";
            } else if (this.name().startsWith("P")) {
                this.type = "Processing Error";
            } else if (this.name().startsWith("R")) {
                this.type = "Retrieval/Support";
            } else if (this.name().startsWith("S")) {
                this.type = "Status Updates";
            }

        }

    }


    public Detail withDataFileRecordType(String dataFileRecordType) {
        this.dataFileRecordType = dataFileRecordType;
        return this;
    }

    public Detail withServiceEstablishmentNumber(String serviceEstablishmentNumber) {
        this.serviceEstablishmentNumber = serviceEstablishmentNumber;
        return this;
    }

    public Detail withCardMemberAccountNumber(String cardMemberAccountNumber) {
        this.cardMemberAccountNumber = cardMemberAccountNumber;
        return this;
    }

    public Detail withCurrentCaseNumber(String currentCaseNumber) {
        this.currentCaseNumber = currentCaseNumber;
        return this;
    }

    public Detail withPreviousCaseNumber(String previousCaseNumber) {
        this.previousCaseNumber = previousCaseNumber;
        return this;
    }

    public Detail withResolution(Detail.Resolution resolution) {
        this.resolution = resolution;
        return this;
    }

    public Detail withAdjustmentDate(Date adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
        return this;
    }

    public Detail withChargeDate(Date chargeDate) {
        this.chargeDate = chargeDate;
        return this;
    }

    public Detail withCaseType(Detail.CaseType caseType) {
        this.caseType = caseType;
        return this;
    }

    public Detail withLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
        return this;
    }

    public Detail withChargebackReasonCode(Detail.ChargebackReasonCode chargebackReasonCode) {
        this.chargebackReasonCode = chargebackReasonCode;
        return this;
    }

    public Detail withChargebackAmount(MonetaryAmount chargebackAmount) {
        this.chargebackAmount = chargebackAmount;
        return this;
    }

    public Detail withChargebackAdjustmentNumber(String chargebackAdjustmentNumber) {
        this.chargebackAdjustmentNumber = chargebackAdjustmentNumber;
        return this;
    }

    public Detail withChargebackResolutionAdjustmentNumber(String chargebackResolutionAdjustmentNumber) {
        this.chargebackResolutionAdjustmentNumber = chargebackResolutionAdjustmentNumber;
        return this;
    }

    public Detail withChargebackReferenceCode(String chargebackReferenceCode) {
        this.chargebackReferenceCode = chargebackReferenceCode;
        return this;
    }

    public Detail withBilledAmount(MonetaryAmount billedAmount) {
        this.billedAmount = billedAmount;
        return this;
    }

    public Detail withSocAmount(MonetaryAmount socAmount) {
        this.socAmount = socAmount;
        return this;
    }

    public Detail withSocInvoiceNumber(String socInvoiceNumber) {
        this.socInvoiceNumber = socInvoiceNumber;
        return this;
    }

    public Detail withRocInvoiceNumber(String rocInvoiceNumber) {
        this.rocInvoiceNumber = rocInvoiceNumber;
        return this;
    }

    public Detail withForeignAmount(MonetaryAmount foreignAmount) {
        this.foreignAmount = foreignAmount;
        return this;
    }

    public Detail withCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public Detail withSupportToFollow(Detail.SupportToFollow supportToFollow) {
        this.supportToFollow = supportToFollow;
        return this;
    }

    public Detail withCardMemberName1(String cardMemberName1) {
        this.cardMemberName1 = cardMemberName1;
        return this;
    }

    public Detail withCardMemberName2(String cardMemberName2) {
        this.cardMemberName2 = cardMemberName2;
        return this;
    }

    public Detail withCardMemberAddress1(String cardMemberAddress1) {
        this.cardMemberAddress1 = cardMemberAddress1;
        return this;
    }

    public Detail withCardMemberAddress2(String cardMemberAddress2) {
        this.cardMemberAddress2 = cardMemberAddress2;
        return this;
    }

    public Detail withCardMemberCityState(String cardMemberCityState) {
        this.cardMemberCityState = cardMemberCityState;
        return this;
    }

    public Detail withCardMemberZip(String cardMemberZip) {
        this.cardMemberZip = cardMemberZip;
        return this;
    }

    public Detail withCardMemberFirstName1(String cardMemberFirstName1) {
        this.cardMemberFirstName1 = cardMemberFirstName1;
        return this;
    }

    public Detail withCardMemberMiddleName1(String cardMemberMiddleName1) {
        this.cardMemberMiddleName1 = cardMemberMiddleName1;
        return this;
    }

    public Detail withCardMemberLastName1(String cardMemberLastName1) {
        this.cardMemberLastName1 = cardMemberLastName1;
        return this;
    }

    public Detail withCardMemberOriginalAccountNumber(String cardMemberOriginalAccountNumber) {
        this.cardMemberOriginalAccountNumber = cardMemberOriginalAccountNumber;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Detail detail = (Detail) o;

        return new EqualsBuilder()
                .append(getDataFileRecordType(), detail.getDataFileRecordType())
                .append(getServiceEstablishmentNumber(), detail.getServiceEstablishmentNumber())
                .append(getCardMemberAccountNumber(), detail.getCardMemberAccountNumber())
                .append(getCurrentCaseNumber(), detail.getCurrentCaseNumber())
                .append(getPreviousCaseNumber(), detail.getPreviousCaseNumber())
                .append(getResolution(), detail.getResolution())
                .append(getAdjustmentDate(), detail.getAdjustmentDate())
                .append(getChargeDate(), detail.getChargeDate())
                .append(getCaseType(), detail.getCaseType())
                .append(getLocationNumber(), detail.getLocationNumber())
                .append(getChargebackReasonCode(), detail.getChargebackReasonCode())
                .append(getChargebackAmount(), detail.getChargebackAmount())
                .append(getChargebackAdjustmentNumber(), detail.getChargebackAdjustmentNumber())
                .append(getChargebackResolutionAdjustmentNumber(), detail.getChargebackResolutionAdjustmentNumber())
                .append(getChargebackReferenceCode(), detail.getChargebackReferenceCode())
                .append(getBilledAmount(), detail.getBilledAmount())
                .append(getSocAmount(), detail.getSocAmount())
                .append(getSocInvoiceNumber(), detail.getSocInvoiceNumber())
                .append(getRocInvoiceNumber(), detail.getRocInvoiceNumber())
                .append(getForeignAmount(), detail.getForeignAmount())
                .append(getCurrency(), detail.getCurrency())
                .append(getSupportToFollow(), detail.getSupportToFollow())
                .append(getCardMemberName1(), detail.getCardMemberName1())
                .append(getCardMemberName2(), detail.getCardMemberName2())
                .append(getCardMemberAddress1(), detail.getCardMemberAddress1())
                .append(getCardMemberAddress2(), detail.getCardMemberAddress2())
                .append(getCardMemberCityState(), detail.getCardMemberCityState())
                .append(getCardMemberZip(), detail.getCardMemberZip())
                .append(getCardMemberFirstName1(), detail.getCardMemberFirstName1())
                .append(getCardMemberMiddleName1(), detail.getCardMemberMiddleName1())
                .append(getCardMemberLastName1(), detail.getCardMemberLastName1())
                .append(getCardMemberOriginalAccountNumber(), detail.getCardMemberOriginalAccountNumber())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getDataFileRecordType())
                .append(getServiceEstablishmentNumber())
                .append(getCardMemberAccountNumber())
                .append(getCurrentCaseNumber())
                .append(getPreviousCaseNumber())
                .append(getResolution())
                .append(getAdjustmentDate())
                .append(getChargeDate())
                .append(getCaseType())
                .append(getLocationNumber())
                .append(getChargebackReasonCode())
                .append(getChargebackAmount())
                .append(getChargebackAdjustmentNumber())
                .append(getChargebackResolutionAdjustmentNumber())
                .append(getChargebackReferenceCode())
                .append(getBilledAmount())
                .append(getSocAmount())
                .append(getSocInvoiceNumber())
                .append(getRocInvoiceNumber())
                .append(getForeignAmount())
                .append(getCurrency())
                .append(getSupportToFollow())
                .append(getCardMemberName1())
                .append(getCardMemberName2())
                .append(getCardMemberAddress1())
                .append(getCardMemberAddress2())
                .append(getCardMemberCityState())
                .append(getCardMemberZip())
                .append(getCardMemberFirstName1())
                .append(getCardMemberMiddleName1())
                .append(getCardMemberLastName1())
                .append(getCardMemberOriginalAccountNumber())
                .toHashCode();
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return super.toString();
    }

    // TODO: add parser
}

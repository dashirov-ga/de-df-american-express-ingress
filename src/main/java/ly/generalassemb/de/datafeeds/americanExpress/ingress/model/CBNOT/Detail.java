package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT;

import com.ancientprogramming.fixedformat4j.annotation.*;
import com.ancientprogramming.fixedformat4j.format.AbstractFixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;
import com.ancientprogramming.fixedformat4j.format.impl.StringFormatter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Record(length = 2202)
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.ALWAYS )
@JsonPropertyOrder({
        "REC_TYPE",
        "SE_NUMB",
        "CM_ACCT_NUMB",
        "CURRENT_CASE_NUMBER",
        "CURRENT_ACTION_NUMBER",
        "PREVIOUS_CASE_NUMBER",
        "PREVIOUS_ACTION_NUMBER",
        "RESOLUTION",
        "FROM_SYSTEM",
        "REJECTS_TO_SYSTEM",
        "DISPUTES_TO_SYSTEM",
        "DATE_OF_ADJUSTMENT",
        "DATE_OF_CHARGE",
        "AMEX_ID",
        "CASE_TYPE",
        "LOC_NUMB",
        "CB_REAS_CODE",
        "CB_AMOUNT",
        "CB_ADJUSTMENT_NUMBER",
        "CB_RESOLUTION_ADJ_NUMBER",
        "CB_REFERENCE_CODE",
        "BILLED_AMOUNT",
        "SOC_AMOUNT",
        "SOC_INVOICE_NUMBER",
        "ROC_INVOICE_NUMBER",
        "FOREIGN_AMT",
        "CURRENCY",
        "SUPP_TO_FOLLOW",
        "CM_NAME1",
        "CM_NAME2",
        "CM_ADDR1",
        "CM_ADDR2",
        "CM_CITY_STATE",
        "CM_ZIP",
        "CM_FIRST_NAME_1",
        "CM_MIDDLE_NAME_1",
        "CM_LAST_NAME_1",
        "CM_ORIG_ACCT_NUM",
        "CM_ORIG_NAME",
        "CM_ORIG_FIRST_NAME",
        "CM_ORIG_MIDDLE_NAME",
        "CM_ORIG_LAST_NAME"
})
public class Detail {
    @JsonProperty("REC_TYPE")
    @NotNull
    private String recordType;
    @Field(offset = 1, length = 1)
    public String getRecordType() {
        return recordType;
    }
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @JsonProperty("SE_NUMB")
    @NotNull
    private BigDecimal serviceEstablishmentNumber;
    @Field(offset = 7, length = 10)
    @FixedFormatDecimal(decimals = 0)
    public BigDecimal getServiceEstablishmentNumber() {
        return serviceEstablishmentNumber;
    }
    public void setServiceEstablishmentNumber(BigDecimal serviceEstablishmentNumber) {
        this.serviceEstablishmentNumber = serviceEstablishmentNumber;
    }
    @JsonProperty("CM_ACCT_NUMB")
    @NotNull
    private String cardmemberAccountNumber;
    @Field(offset = 27, length = 19)
    public String getCardmemberAccountNumber() {
        return cardmemberAccountNumber;
    }
    public void setCardmemberAccountNumber(String cardmemberAccountNumber) {
        this.cardmemberAccountNumber = cardmemberAccountNumber;
    }


    @JsonProperty("CURRENT_CASE_NUMBER")
    @NotNull
    private String currentCaseNumber;
    @Field(offset = 47, length = 11, align = Align.LEFT, paddingChar = ' ')
    public String getCurrentCaseNumber() {
        return currentCaseNumber;
    }
    public void setCurrentCaseNumber(String currentCaseNumber) {
        this.currentCaseNumber = currentCaseNumber;
    }

    @JsonProperty("CURRENT_ACTION_NUMBER")
    private String currentActionNumber;
    @Field(offset = 58, length=2)
    public String getCurrentActionNumber() {
        return currentActionNumber;
    }
    public void setCurrentActionNumber(String currentActionNumber) {
        this.currentActionNumber = currentActionNumber;
    }
    @JsonProperty("PREVIOUS_CASE_NUMBER")
    private String previousCaseNumber;
    @Field(offset = 60, length=11)
    public String getPreviousCaseNumber() {
        return previousCaseNumber;
    }
    public void setPreviousCaseNumber(String previousCaseNumber) {
        this.previousCaseNumber = previousCaseNumber;
    }
    @JsonProperty("PREVIOUS_ACTION_NUMBER")
    private String previousActionNumber;

    @Field(offset = 71, length = 2)
    public String getPreviousActionNumber() {
        return previousActionNumber;
    }

    public void setPreviousActionNumber(String previousActionNumber) {
        this.previousActionNumber = previousActionNumber;
    }

    @JsonProperty("RESOLUTION")
    private String resolution;
    @Field(offset = 73, length = 1)
    public String getResolution() {
        return resolution;
    }
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @JsonProperty("FROM_SYSTEM")
    private String fromSystem;
    @Field(offset = 74, length = 1)
    public String getFromSystem() {
        return fromSystem;
    }
    public void setFromSystem(String fromSystem) {
        this.fromSystem = fromSystem;
    }

    @JsonProperty("REJECTS_TO_SYSTEM")
    private String rejectsToSystem;
    @Field(offset = 71, length=1)
    public String getRejectsToSystem() {
        return rejectsToSystem;
    }
    public void setRejectsToSystem(String rejectsToSystem) {
        this.rejectsToSystem = rejectsToSystem;
    }

    @JsonProperty("DISPUTES_TO_SYSTEM")
    private String disputesToSystem;

    @Field(offset = 76, length = 1)
    public String getDisputesToSystem() {
        return disputesToSystem;
    }
    public void setDisputesToSystem(String disputesToSystem) {
        this.disputesToSystem = disputesToSystem;
    }


    @JsonProperty("DATE_OF_ADJUSTMENT")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/New_York") // keep timezone to avoid shifts
    private Date dateOfAdjustment;

    @Field(offset=77, length = 8)
    @FixedFormatPattern("yyyyMMdd")
    public Date getDateOfAdjustment() {
        return dateOfAdjustment;
    }
    public void setDateOfAdjustment(Date dateOfAdjustment) {
        this.dateOfAdjustment = dateOfAdjustment;
    }

    @JsonProperty("DATE_OF_CHARGE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/New_York"  ) // keep timezone to avoid shifts
    private Date dateOfCharge;
    @Field(offset=85, length = 8)
    @FixedFormatPattern("yyyyMMdd")
    public Date getDateOfCharge() {
        return dateOfCharge;
    }
    public void setDateOfCharge(Date dateOfCharge) {
        this.dateOfCharge = dateOfCharge;
    }

    @JsonProperty("AMEX_ID")
    private String amexId;
    @Field(offset = 93, length = 7)
    public String getAmexId() {
        return amexId;
    }

    public void setAmexId(String amexId) {
        this.amexId = amexId;
    }

    @JsonProperty("CASE_TYPE")
    private CaseType caseType;
    @Field(offset = 105, length = 6, align = Align.LEFT, paddingChar = ' ', formatter = CaseTypeFormatter.class)
    public CaseType getCaseType() {
        return caseType;
    }

    public void setCaseType(CaseType caseType) {
        this.caseType = caseType;
    }

    @JsonProperty("LOC_NUMB")
    private String locationNumber;
    @Field(offset = 111, length = 15)
    public String getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    @JsonProperty("CB_REAS_CODE")
    private ChargebackReasonCode chargebackReasonCode;
    @Field(offset = 126,length = 3,formatter = ChargebackReasonCodeFormatter.class)
    public ChargebackReasonCode getChargebackReasonCode() {
        return chargebackReasonCode;
    }

    public void setChargebackReasonCode(ChargebackReasonCode chargebackReasonCode) {
        this.chargebackReasonCode = chargebackReasonCode;
    }

    @JsonProperty("CB_AMOUNT")
    private BigDecimal chargebackAmount;
    @FixedFormatDecimal(decimals = 2, useDecimalDelimiter = true, decimalDelimiter = '.')
    @FixedFormatNumber(sign = Sign.PREPEND, positiveSign = ' ', negativeSign = '-')
    @Field(offset = 129, length = 17)
    public BigDecimal getChargebackAmount() {
        return chargebackAmount;
    }
    public void setChargebackAmount(BigDecimal chargebackAmount) {
        this.chargebackAmount = chargebackAmount;
    }

    @JsonProperty("CB_ADJUSTMENT_NUMBER")
    private String chargebackAdjustmentNumber;
    @Field(offset = 146, length = 6)
    public String getChargebackAdjustmentNumber() {
        return chargebackAdjustmentNumber;
    }
    public void setChargebackAdjustmentNumber(String chargebackAdjustmentNumber) {
        this.chargebackAdjustmentNumber = chargebackAdjustmentNumber;
    }

    @JsonProperty("CB_RESOLUTION_ADJ_NUMBER")
    private String chargebackResolutionAdjustmentNumber;
    @Field(offset = 152, length = 6)
    public String getChargebackResolutionAdjustmentNumber() {
        return chargebackResolutionAdjustmentNumber;
    }
    public void setChargebackResolutionAdjustmentNumber(String chargebackResolutionAdjustmentNumber) {
        this.chargebackResolutionAdjustmentNumber = chargebackResolutionAdjustmentNumber;
    }

    @JsonProperty("CB_REFERENCE_CODE")
    private String chargebackReferenceCode;
    @Field(offset = 158, length = 12)
    public String getChargebackReferenceCode() {
        return chargebackReferenceCode;
    }
    public void setChargebackReferenceCode(String chargebackReferenceCode) {
        this.chargebackReferenceCode = chargebackReferenceCode;
    }

    @JsonProperty("BILLED_AMOUNT")
    private BigDecimal billedAmount;
    @Field(offset = 183, length = 17, align = Align.RIGHT, paddingChar = '0')
    @FixedFormatDecimal(decimals = 2, useDecimalDelimiter = true, decimalDelimiter = '.')
    @FixedFormatNumber(sign = Sign.PREPEND, positiveSign = ' ', negativeSign = '-')
    public BigDecimal getBilledAmount() {
        return billedAmount;
    }
    public void setBilledAmount(BigDecimal billedAmount) {
        this.billedAmount = billedAmount;
    }

    @JsonProperty("SOC_AMOUNT")
    private BigDecimal socAmount;
    @Field(offset = 200, length = 17, align = Align.RIGHT, paddingChar = '0')
    @FixedFormatDecimal(decimals = 2, useDecimalDelimiter = true, decimalDelimiter = '.')
    @FixedFormatNumber(sign = Sign.PREPEND, positiveSign = ' ', negativeSign = '-')
    public BigDecimal getSocAmount() {
        return socAmount;
    }
    public void setSocAmount(BigDecimal socAmount) {
        this.socAmount = socAmount;
    }

    @JsonProperty("SOC_INVOICE_NUMBER")
    private String socInvoiceNumner;
    @Field(offset = 217, length = 6)
    public String getSocInvoiceNumner() {
        return socInvoiceNumner;
    }
    public void setSocInvoiceNumner(String socInvoiceNumner) {
        this.socInvoiceNumner = socInvoiceNumner;
    }

    @JsonProperty("ROC_INVOICE_NUMBER")
    private String rocInvoiceNumner;
    @Field(offset = 223, length = 6)
    public String getRocInvoiceNumner() {
        return rocInvoiceNumner;
    }
    public void setRocInvoiceNumner(String rocInvoiceNumner) {
        this.rocInvoiceNumner = rocInvoiceNumner;
    }

    @JsonProperty("FOREIGN_AMT")
    private BigDecimal foreignAmount;
    @Field(offset = 229, length = 15)
    @FixedFormatDecimal(decimals = 2, useDecimalDelimiter = true, decimalDelimiter = '.')
    @FixedFormatNumber(sign = Sign.PREPEND, positiveSign = ' ', negativeSign = '-')
    public BigDecimal getForeignAmount() {
        return foreignAmount;
    }
    public void setForeignAmount(BigDecimal foreignAmount) {
        this.foreignAmount = foreignAmount;
    }

    @JsonProperty("CURRENCY")
    private String currency;
    @Field(offset = 244, length = 3)
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("SUPP_TO_FOLLOW")
    private SupportToFollow supportToFollow;
    @Field(offset = 247, length = 1, formatter = SupportToFollowFormatter.class)
    public SupportToFollow getSupportToFollow() {
        return supportToFollow;
    }
    public void setSupportToFollow(SupportToFollow supportToFollow) {
        this.supportToFollow = supportToFollow;
    }

    @JsonProperty("CM_NAME1")
    private String cardmemberName1;
    @Field(offset = 248, length = 30)
    public String getCardmemberName1() {
        return cardmemberName1;
    }
    public void setCardmemberName1(String cardmemberName1) {
        this.cardmemberName1 = cardmemberName1;
    }

    @JsonProperty("CM_NAME2")
    private String cardmemberName2;
    @Field(offset = 278, length = 30)
    public String getCardmemberName2() {
        return cardmemberName2;
    }
    public void setCardmemberName2(String cardmemberName2) {
        this.cardmemberName2 = cardmemberName2;
    }

    @JsonProperty("CM_ADDR1")
    private String cardmemberAddress1;
    @Field(offset = 308, length = 30)
    public String getCardmemberAddress1() {
        return cardmemberAddress1;
    }

    public void setCardmemberAddress1(String cardmemberAddress1) {
        this.cardmemberAddress1 = cardmemberAddress1;
    }

    @JsonProperty("CM_ADDR2")
    private String cardmemberAddress2;
    @Field(offset = 338, length = 30)
    public String getCardmemberAddress2() {
        return cardmemberAddress2;
    }

    public void setCardmemberAddress2(String cardmemberAddress2) {
        this.cardmemberAddress2 = cardmemberAddress2;
    }

    @JsonProperty("CM_CITY_STATE")
    private String cardmemberCityState;
    @Field(offset = 368, length = 30)
    public String getCardmemberCityState() {
        return cardmemberCityState;
    }
    public void setCardmemberCityState(String cardmemberCityState) {
        this.cardmemberCityState = cardmemberCityState;
    }

    @JsonProperty("CM_ZIP")
    private String cardmemberZip;
    @Field(offset = 398, length = 9)
    public String getCardmemberZip() {
        return cardmemberZip;
    }
    public void setCardmemberZip(String cardmemberZip) {
        this.cardmemberZip = cardmemberZip;
    }

    @JsonProperty("CM_FIRST_NAME_1")
    private String cardmemberFirstName1;
    @Field(offset = 407, length = 12)
    public String getCardmemberFirstName1() {
        return cardmemberFirstName1;
    }
    public void setCardmemberFirstName1(String cardmemberFirstName1) {
        this.cardmemberFirstName1 = cardmemberFirstName1;
    }

    @JsonProperty("CM_MIDDLE_NAME_1")
    private String cardmemberMiddleName1;
    @Field(offset = 419, length = 12)
    public String getCardmemberMiddleName1() {
        return cardmemberMiddleName1;
    }
    public void setCardmemberMiddleName1(String cardmemberMiddleName1) {
        this.cardmemberMiddleName1 = cardmemberMiddleName1;
    }

    @JsonProperty("CM_LAST_NAME_1")
    private String cardmemberLastName1;
    @Field(offset = 431, length = 20)
    public String getCardmemberLastName1() {
        return cardmemberLastName1;
    }
    public void setCardmemberLastName1(String cardmemberLastName1) {
        this.cardmemberLastName1 = cardmemberLastName1;
    }

    @JsonProperty("CM_ORIG_ACCT_NUM")
    private String cardmemberOriginalAccountNumber;
    @Field(offset = 451, length = 15)
    public String getCardmemberOriginalAccountNumber() {
        return cardmemberOriginalAccountNumber;
    }
    public void setCardmemberOriginalAccountNumber(String cardmemberOriginalAccountNumber) {
        this.cardmemberOriginalAccountNumber = cardmemberOriginalAccountNumber;
    }

    @JsonProperty("CM_ORIG_NAME")
    public String cardmemberOriginalName;
    @Field(offset = 466, length = 30 )
    public String getCardmemberOriginalName() {
        return cardmemberOriginalName;
    }
    public void setCardmemberOriginalName(String cardmemberOriginalName) {
        this.cardmemberOriginalName = cardmemberOriginalName;
    }

    @JsonProperty("CM_ORIG_FIRST_NAME")
    private String cardmemberOriginalFirstName;
    @Field(offset = 496, length = 12)
    public String getcardmemberOriginalFirstName() {
        return cardmemberOriginalFirstName;
    }
    public void setcardmemberOriginalFirstName(String cardmemberOriginalFirstName) {
        this.cardmemberOriginalFirstName = cardmemberOriginalFirstName;
    }

    @JsonProperty("CM_ORIG_MIDDLE_NAME")
    private String cardmemberOriginalMiddleName;
    @Field(offset = 508, length = 12)
    public String getcardmemberOriginalMiddleName() {
        return cardmemberOriginalMiddleName;
    }
    public void setcardmemberOriginalMiddleName(String cardmemberOriginalMiddleName) {
        this.cardmemberOriginalMiddleName = cardmemberOriginalMiddleName;
    }

    @JsonProperty("CM_ORIG_LAST_NAME")
    private String cardmemberOriginalLastName;
    @Field(offset = 520, length = 20)
    public String getcardmemberOriginalLastName() {
        return cardmemberOriginalLastName;
    }
    public void setcardmemberOriginalLastName(String cardmemberOriginalLastName) {
        this.cardmemberOriginalLastName = cardmemberOriginalLastName;
    }

    /**
            TODO: ADD FIELDS BELOW
            
            45 CM_ORIG_FIRST_NAME 12 bytes Alphanumeric 496-507 43
            46 CM_ORIG_MIDDLE_NAME 12 bytes Alphanumeric 508-519 43
            47 CM_ORIG_LAST_NAME 20 bytes Alphanumeric 520-539 44
            48 NOTE1 66 bytes Alphanumeric 540-605 44
            49 NOTE2 78 bytes Alphanumeric 606-683 45
            50 NOTE3 60 bytes Alphanumeric 684-743 45
            51 NOTE4 60 bytes Alphanumeric 744-803 46
            52 NOTE5 60 bytes Alphanumeric 804-863 46
            53 NOTE6 60 bytes Alphanumeric 864-923 47
            54 NOTE7 60 bytes Alphanumeric 924-983 47
            55 TRIUMPH_SEQ_NO 2 bytes Alphanumeric 984-985 48
            59 AIRLINE_TKT_NUM 14 bytes Alphanumeric 1031-1044 50
            60 AL_SEQUENCE_NUMBER 2 bytes Alphanumeric 1045-1046 50
            61 FOLIO_REF 18 bytes Alphanumeric 1047-1064 51
            62 MERCH_ORDER_NUM 10 bytes Alphanumeric 1065-1074 51
            63 MERCH_ORDER_DATE 8 bytes Alphanumeric 1075-1082 52
            64 CANC_NUM 20 bytes Alphanumeric 1083-1102 52
            65 CANC_DATE 8 bytes Alphanumeric 1103-1110 53
            66 FINCAP_TRACKING_ID 11 bytes Alphanumeric 1111-1121 54
            67 FINCAP_FILE_SEQ_NUM 6 bytes Alphanumeric 1122-1127 55
            68 FINCAP_BATCH_NUMBER 4 bytes Alphanumeric 1128-1131 55
            69 FINCAP_BATCH_INVOICE_DT 8 bytes Alphanumeric 1132-1139 56
            70 LABEL1 25 bytes Alphanumeric 1140-1164 57
            71 DATA1 25 bytes Alphanumeric 1165-1189 57
            72 LABEL2 25 bytes Alphanumeric 1190-1214 58
            73 DATA2 25 bytes Alphanumeric 1215-1239 58
            74 LABEL3 25 bytes Alphanumeric 1240-1264 59
            75 DATA3 25 bytes Alphanumeric 1265-1289 59
            76 LABEL4 25 bytes Alphanumeric 1290-1314 60
            77 DATA4 25 bytes Alphanumeric 1315-1339 60
            78 LABEL5 25 bytes Alphanumeric 1340-1364 61
            79 DATA5 25 bytes Alphanumeric 1365-1389 61
            80 LABEL6 25 bytes Alphanumeric 1390-1414 62
            81 DATA6 25 bytes Alphanumeric 1415-1439 62
            82 LABEL7 25 bytes Alphanumeric 1440-1464 63
            83 DATA7 25 bytes Alphanumeric 1465-1489 63
            84 LABEL8 25 bytes Alphanumeric 1490-1514 64
            85 DATA8 25 bytes Alphanumeric 1515-1539 64
            86 LABEL9 25 bytes Alphanumeric 1540-1564 65
            87 DATA9 25 bytes Alphanumeric 1565-1589 65
            88 LABEL10 25 bytes Alphanumeric 1590-1614 66
            89 DATA10 25 bytes Alphanumeric 1615-1639 66
            90 LABEL11 25 bytes Alphanumeric 1640-1664 67
            91 DATA11 25 bytes Alphanumeric 1665-1689 67
            92 CM_ACCNT_NUMB_EXD 19 bytes Alphanumeric 1690-1708 68
            94 CASE_NUMBER_EXD 16 bytes Alphanumeric 1715-1730 69
            96 IND_FORM_CODE 2 bytes Alphanumeric 1766-1767 70
            97 IND_REF_NUMBER 30 bytes Alphanumeric 1768-1797 71
            99 LOC_REF_NUMBER 15 bytes Alphanumeric 1801-1815 72
            100 PASSENGER_NAME 20 bytes Alphanumeric 1816-1835 72
            101 PASSENGER_FIRST_NAME 12 bytes Alphanumeric 1836-1847 72
            102 PASSENGER_MIDDLE_NAME 12 bytes Alphanumeric 1848-1859 72
            103 PASSENGER_LAST_NAME 20 bytes Alphanumeric 1860-1879 74
            104 SE_PROCESS_DATE 3 bytes Alphanumeric 1880-1882 74
            105 RETURN_DATE 6 bytes Alphanumeric 1883-1888 75
            106 CREDIT_RECEIPT_NUMBER 15 bytes Alphanumeric 1889-1903 75
            107 RETURN_TO_NAME 24 bytes Alphanumeric 1904-1927 76
            108 RETURN_TO_STREET 17 bytes Alphanumeric 1928-1944 76
            109 CARD_DEPOSIT 1 byte Alphanumeric 1945 77
            110 ASSURED_RESERVATION 1 byte Alphanumeric 1946 77
            111 RES_CANCELLED 1 byte Alphanumeric 1947 78
            112 RES_CANCELLED_DATE 6 bytes Alphanumeric 1948-1953 78
            113 CANCEL_ZONE 1 byte Alphanumeric 1954 79
            114 RESERVATION_MADE_FOR 6 bytes Alphanumeric 1955-1960 80
            115 RESERVATION_LOCATION 20 bytes Alphanumeric 1961-1980 80
            116 RESERVATION_MADE_ON 6 bytes Alphanumeric 1981-1986 81
            117 RENTAL_AGREEMENT_NUMBER 18 bytes Alphanumeric 1987-2004 81
            118 MERCHANDISE_TYPE 20 bytes Alphanumeric 2005-2024 82
            119 MERCHANDISE_RETURNED 1 byte Alphanumeric 2025 82
            120 RETURNED_NAME 24 bytes Alphanumeric 2026-2049 83
            121 RETURNED_DATE 6 bytes Alphanumeric 2050-2055 83
            122 RETURNED_HOW 8 bytes Alphanumeric 2056-2063 84
            123 RETURNED_REASON 50 byes Alphanumeric 2064-2113 84
            124 STORE_CREDIT_RECEIVED 1 byte Alphanumeric 2114 85


     */
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

    public static class SupportToFollowFormatter extends AbstractFixedFormatter<SupportToFollow> {
        public SupportToFollow asObject(String string, FormatInstructions instructions) {
            return SupportToFollow.valueOf(string);
        }
        public String asString(SupportToFollow enumeration, FormatInstructions instructions) {
            String result = null;
            if (enumeration != null) {
                result = new StringFormatter().asObject(enumeration.name(), instructions);
            }
            return result;
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

    public static class ResolutionFormatter extends AbstractFixedFormatter<Resolution> {
        public Resolution asObject(String string, FormatInstructions instructions) {
            return Resolution.valueOf(string);

        }
        public String asString(Resolution enumeration, FormatInstructions instructions) {
            String result = null;
            if (enumeration != null) {
                result = new StringFormatter().asObject(enumeration.name(), instructions);
            }
            return result;
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

    public static class CaseTypeFormatter extends AbstractFixedFormatter<CaseType> {
        public CaseType asObject(String string, FormatInstructions instructions) {
            return CaseType.valueOf(string);

        }
        public String asString(CaseType enumeration, FormatInstructions instructions) {
            String result = null;
            if (enumeration != null) {
                result = new StringFormatter().asObject(enumeration.name(), instructions);
            }
            return result;
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
        P23("The Charge was incurred in an invalid foreignCurrency."),
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
    public static class ChargebackReasonCodeFormatter extends AbstractFixedFormatter<ChargebackReasonCode> {
        public ChargebackReasonCode asObject(String string, FormatInstructions instructions) {
            return ChargebackReasonCode.valueOf(string);

        }
        public String asString(ChargebackReasonCode enumeration, FormatInstructions instructions) {
            String result = null;
            if (enumeration != null) {
                result = new StringFormatter().asObject(enumeration.name(), instructions);
            }
            return result;
        }
    }

    public Detail parse(FixedFormatManager manager, String line){
        return manager.load(Detail.class,line);
    }



}

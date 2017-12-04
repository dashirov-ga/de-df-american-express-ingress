package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.AmexSignedNumericFixedFormatter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by davidashirov on 12/2/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "SETTLEMENT_SE_ACCOUNT_NUMBER",
        "SETTLEMENT_ACCOUNT_NAME_CODE",


        "SUBMISSION_SE_ACCOUNT_NUMBER",
        "RECORD_CODE",
        "RECORD_SUB_CODE",

        "CHARGE_AMOUNT",
        "CHARGE_DATE",
        "ROC_REFERENCE_NUMBER",
        "ROC_REFERENCE_NUMBER_CPC_ONLY",
        "THREE_DIGIT_CHARGE_AUTHORIZATION_CODE",
        "CARD_MEMBER_ACCOUNT_NUMBER",
        "AIRLINE_TICKET_NUMBER",
        "SIX_DIGIT_CHARGE_AUTHORIZATION_CODE"

})
@Record(length = 441)
public class ROCRecord {
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final CsvMapper csvMapper = new CsvMapper();


    @JsonProperty("SETTLEMENT_SE_ACCOUNT_NUMBER")
    @Size(max = 10)
    @NotNull
    private String settlementSeAccountNumber;

    @JsonProperty("SETTLEMENT_ACCOUNT_NAME_CODE")
    @Size(max = 3)
    @NotNull
    private String settlementAccountNameCode;


    @JsonProperty("SUBMISSION_SE_ACCOUNT_NUMBER")
    @Size(max = 10)
    @NotNull
    private String submissionSeAccountNumber;

    @JsonProperty("RECORD_CODE")
    @Size(max = 1)
    @NotNull
    private Integer recordCode;

    @JsonProperty("RECORD_SUB_CODE")
    @Size(max = 2)
    @NotNull
    private String recordSubCode;

    @JsonProperty("CHARGE_AMOUNT")
    @Size(max = 11)
    @NotNull
    private BigDecimal chargeAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("CHARGE_DATE")
    @Size(max = 8)
    @NotNull
    private Date chargeDate;

    @JsonProperty("ROC_REFERENCE_NUMBER")
    @Size(max = 12)
    @NotNull
    private String rocReferenceNumber;

    @JsonProperty("ROC_REFERENCE_NUMBER_CPC_ONLY")
    @Size(max = 15)
    @NotNull
    private String rocReferenceNumberCpcOnly;

    @JsonProperty("THREE_DIGIT_CHARGE_AUTHORIZATION_CODE")
    @Size(max = 3)
    @NotNull
    private String threeDigitChargeAuthorizationCode;

    @JsonProperty("CARD_MEMBER_ACCOUNT_NUMBER")
    @Size(max = 15)
    @NotNull
    private String cardMemberAccountNumber;

    @JsonProperty("AIRLINE_TICKET_NUMBER")
    @Size(max = 14)
    @NotNull
    private String airlineTicketNumber;

    @JsonProperty("SIX_DIGIT_CHARGE_AUTHORIZATION_CODE")
    @Size(max = 6)
    @NotNull
    private String sixDigitChargeAuthorizationCode;

    @Field(offset = 1, length = 10, align = Align.LEFT, paddingChar = ' ')        //  getSettlementSeAccountNumber

    public String getSettlementSeAccountNumber() {
        return settlementSeAccountNumber;
    }

    public void setSettlementSeAccountNumber(String settlementSeAccountNumber) {
        this.settlementSeAccountNumber = settlementSeAccountNumber;
    }

    @Field(offset = 11, length = 3, align = Align.LEFT, paddingChar = ' ')        //  getSettlementAccountNameCode

    public String getSettlementAccountNameCode() {
        return settlementAccountNameCode;
    }

    public void setSettlementAccountNameCode(String settlementAccountNameCode) {
        this.settlementAccountNameCode = settlementAccountNameCode;
    }

    @Field(offset = 23, length = 10, align = Align.LEFT, paddingChar = ' ')        //  getSubmissionSeAccountNumber

    public String getSubmissionSeAccountNumber() {
        return submissionSeAccountNumber;
    }

    public void setSubmissionSeAccountNumber(String submissionSeAccountNumber) {
        this.submissionSeAccountNumber = submissionSeAccountNumber;
    }

    @Field(offset = 33, length = 1, align = Align.RIGHT, paddingChar = '0')        //  getRecordCode
    public Integer getRecordCode() {
        return recordCode;
    }


    public void setRecordCode(Integer recordCode) {
        this.recordCode = recordCode;
    }

    @Field(offset = 34, length = 2, align = Align.LEFT, paddingChar = ' ')        //  getRecordSubCode


    public String getRecordSubCode() {
        return recordSubCode;
    }

    public void setRecordSubCode(String recordSubCode) {
        this.recordSubCode = recordSubCode;
    }

    @Field(offset = 41, length = 11, align = Align.RIGHT, paddingChar = '0', formatter = AmexSignedNumericFixedFormatter.class)
    //  getChargeAmount
    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    @FixedFormatPattern("yyyyMMdd")
    @Field(offset = 52, length = 8, align = Align.RIGHT, paddingChar = '0')        //  getChargeDate
    public Date getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(Date chargeDate) {
        this.chargeDate = chargeDate;
    }

    @Field(offset = 60, length = 12, align = Align.LEFT, paddingChar = ' ')        //  getRocReferenceNumber

    public String getRocReferenceNumber() {
        return rocReferenceNumber;
    }

    public void setRocReferenceNumber(String rocReferenceNumber) {
        this.rocReferenceNumber = rocReferenceNumber;
    }

    @Field(offset = 72, length = 15, align = Align.LEFT, paddingChar = ' ')        //  getRocReferenceNumberCpcOnly
    public String getRocReferenceNumberCpcOnly() {
        return rocReferenceNumberCpcOnly;
    }

    public void setRocReferenceNumberCpcOnly(String rocReferenceNumberCpcOnly) {
        this.rocReferenceNumberCpcOnly = rocReferenceNumberCpcOnly;
    }

    @Field(offset = 87, length = 3, align = Align.LEFT, paddingChar = ' ')
    //  getThreeDigitChargeAuthorizationCode
    public String getThreeDigitChargeAuthorizationCode() {
        return threeDigitChargeAuthorizationCode;
    }

    public void setThreeDigitChargeAuthorizationCode(String threeDigitChargeAuthorizationCode) {
        this.threeDigitChargeAuthorizationCode = threeDigitChargeAuthorizationCode;
    }

    @Field(offset = 90, length = 15, align = Align.LEFT, paddingChar = ' ')        //  getCardMemberAccountNumber
    public String getCardMemberAccountNumber() {
        return cardMemberAccountNumber;
    }

    public void setCardMemberAccountNumber(String cardMemberAccountNumber) {
        this.cardMemberAccountNumber = cardMemberAccountNumber;
    }

    @Field(offset = 105, length = 14, align = Align.LEFT, paddingChar = ' ')        //  getAirlineTicketNumber
    public String getAirlineTicketNumber() {
        return airlineTicketNumber;
    }

    public void setAirlineTicketNumber(String airlineTicketNumber) {
        this.airlineTicketNumber = airlineTicketNumber;
    }

    @Field(offset = 119, length = 6, align = Align.LEFT, paddingChar = ' ')
    //  getSixDigitChargeAuthorizationCode
    public String getSixDigitChargeAuthorizationCode() {
        return sixDigitChargeAuthorizationCode;
    }

    public void setSixDigitChargeAuthorizationCode(String sixDigitChargeAuthorizationCode) {
        this.sixDigitChargeAuthorizationCode = sixDigitChargeAuthorizationCode;
    }


    @Override
    public String toString() {
        try {
            return jsonMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ((Object) this).toString();
        }
    }

    public String toCsv() {
        CsvSchema schema = csvMapper.schemaFor(SOCRecord.class).withColumnSeparator(',').withHeader();
        ObjectWriter myObjectWriter = csvMapper.writer(schema);
        try {
            return myObjectWriter.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toCsv(List<ROCRecord> list) {
        CsvSchema schema = csvMapper.schemaFor(ROCRecord.class).withColumnSeparator(',').withHeader();
        ObjectWriter myObjectWriter = csvMapper.writer(schema);
        try {
            return myObjectWriter.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}

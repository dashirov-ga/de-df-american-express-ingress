package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EMCBK;

import com.ancientprogramming.fixedformat4j.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by davidashirov on 12/10/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "DETAIL_INDICATOR",
        "RECORD_TYPE",
        "SERVICE_ESTABLISHMENT_NUMBER",
        "CARD_NUMBER",
        "CASE_NUMBER",
        "CHARGEBACK_DATE",
        "CHARGEBACK_AMOUNT",
        "CHARGEBACK_CURRENCY_CODE",
        "CHARGEBACK_REASON_CODE",
        "ADJUSTMENT_REFERENCE_NUMBER",
        "SOC_REFERENCE_NUMBER",
        "ROC_REFERENCE_NUMBER",
        "TRANSACTION_DATE",
        "TRANSACTION_AMOUNT",
        "TRANSACTION_CURRENCY_CODE",
        "SOC_DATE",
        "SOC_AMOUNT",
        "AIRLINE_TICKET_NUMBER",
        "MICRO_SEQUENCE_NUMBER",
        "SETTLEMENT_SERVICE_ESTABLISHMENT_NUMBER",
        "SETTLEMENT_AMOUNT",
        "SETTLEMENT_CURRENCY_CODE",
        "SETTLEMENT_DATE",
        "CASE_UPDATE_FLAG",
        "CASE_UPDATE_TYPE",
        "CASE_UPDATE_DATE",
        "SERVICE_ESTABLISHMENT_REPLY_BY_DATE",
        "MERCHANT_INDUSTRY_TYPE_CODE",
        "TERMINAL_ID",
        "SUBMISSION_CURRENCY_CODE",
        "FIRST_PRESENTMENT_AMOUNT",
        "FIRST_PRESENTMENT_CURRENCY_CODE",
        "TRANSACTION_ID",
        "ACQUIRER_REFERENCE_NUMBER",
        "ADDITIONAL_INFORMATION_TEXT_1",
        "ROC_REFERENCE_NUMBER_EXTENDED",
        "CARD_MEMBER_NAME",
        "ADDITIONAL_INFORMATION_TEXT_2",
        "SELLER_ID"
})
@Record
public class ChargebackDetailRecord {
    @JsonProperty("DETAIL_INDICATOR")
    @Size(max = 1)
    @NotNull
    private String detailIndicator;
    @Field(offset=1,length=1,align= Align.LEFT,paddingChar = ' ')        //  getDetailIndicator
    public String getDetailIndicator() {
        return detailIndicator;
    }
    public void setDetailIndicator(String detailIndicator) {
        this.detailIndicator = detailIndicator;
    }

    @JsonProperty("RECORD_TYPE")
    @Size(max = 5)
    @NotNull
    private String recordType;
    @Field(offset=2,length=5,align=Align.LEFT,paddingChar = ' ')        //  getRecordType
    public String getRecordType() {
        return recordType;
    }
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @JsonProperty("SERVICE_ESTABLISHMENT_NUMBER")
    @Size(max = 10)
    @NotNull
    private String serviceEstablishmentNumber;
    @Field(offset=7,length=10,align=Align.LEFT,paddingChar = ' ')        //  getServiceEstablishmentNumber
    public String getServiceEstablishmentNumber() {
        return serviceEstablishmentNumber;
    }
    public void setServiceEstablishmentNumber(String serviceEstablishmentNumber) {
        this.serviceEstablishmentNumber = serviceEstablishmentNumber;
    }

    @JsonProperty("CARD_NUMBER")
    @Size(max = 19)
    @NotNull
    private String cardNumber;
    @Field(offset=17,length=19,align=Align.LEFT,paddingChar = ' ')        //  getCardNumber
    public String getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @JsonProperty("CASE_NUMBER")
    @Size(max = 16)
    @NotNull
    private String caseNumber;
    @Field(offset=36,length=16,align=Align.LEFT,paddingChar = ' ')        //  getCaseNumber
    public String getCaseNumber() {
        return caseNumber;
    }
    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    @JsonProperty("CHARGEBACK_DATE")
    @Size(max = 10)
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date chargebackDate;
    @Field(offset=52,length=10,align=Align.LEFT,paddingChar = ' ')        //  getChargebackDate
    @FixedFormatPattern("yyyyMMdd")
    public Date getChargebackDate() {
        return chargebackDate;
    }
    public void setChargebackDate(Date chargebackDate) {
        this.chargebackDate = chargebackDate;
    }

    @JsonProperty("CHARGEBACK_AMOUNT")
    @Size(max = 20)
    @NotNull
    private BigDecimal chargebackAmount;
    @Field(offset=62,length=20,align=Align.LEFT,paddingChar = ' ')        //  getChargebackAmount
    @FixedFormatDecimal(decimals=2, useDecimalDelimiter=false)
    public BigDecimal getChargebackAmount() {
        return chargebackAmount;
    }
    public void setChargebackAmount(BigDecimal chargebackAmount) {
        this.chargebackAmount = chargebackAmount;
    }

    @JsonProperty("CHARGEBACK_CURRENCY_CODE")
    @Size(max = 3)
    @NotNull
    private String chargebackCurrencyCode;
    @Field(offset=82,length=3,align=Align.LEFT,paddingChar = ' ')        //  getChargebackCurrencyCode
    public String getChargebackCurrencyCode() {
        return chargebackCurrencyCode;
    }
    public void setChargebackCurrencyCode(String chargebackCurrencyCode) {
        this.chargebackCurrencyCode = chargebackCurrencyCode;
    }

    @JsonProperty("CHARGEBACK_REASON_CODE")
    @Size(max = 4)
    @NotNull
    private String chargebackReasonCode;
    @Field(offset=85,length=4,align=Align.LEFT,paddingChar = ' ')        //  getChargebackReasonCode
    public String getChargebackReasonCode() {
        return chargebackReasonCode;
    }
    public void setChargebackReasonCode(String chargebackReasonCode) {
        this.chargebackReasonCode = chargebackReasonCode;
    }

    @JsonProperty("ADJUSTMENT_REFERENCE_NUMBER")
    @Size(max = 15)
    @NotNull
    private String adjustmentReferenceNumber;
    @Field(offset=89,length=15,align=Align.LEFT,paddingChar = ' ')        //  getAdjustmentReferenceNumber
    public String getAdjustmentReferenceNumber() {
        return adjustmentReferenceNumber;
    }
    public void setAdjustmentReferenceNumber(String adjustmentReferenceNumber) {
        this.adjustmentReferenceNumber = adjustmentReferenceNumber;
    }

    @JsonProperty("SOC_REFERENCE_NUMBER")
    @Size(max = 15)
    @NotNull
    private String socReferenceNumber;
    @Field(offset=104,length=15,align=Align.LEFT,paddingChar = ' ')        //  getSocReferenceNumber
    public String getSocReferenceNumber() {
        return socReferenceNumber;
    }
    public void setSocReferenceNumber(String socReferenceNumber) {
        this.socReferenceNumber = socReferenceNumber;
    }

    @JsonProperty("ROC_REFERENCE_NUMBER")
    @Size(max = 15)
    @NotNull
    private String rocReferenceNumber;
    @Field(offset=119,length=15,align=Align.LEFT,paddingChar = ' ')        //  getRocReferenceNumber
    public String getRocReferenceNumber() {
        return rocReferenceNumber;
    }
    public void setRocReferenceNumber(String rocReferenceNumber) {
        this.rocReferenceNumber = rocReferenceNumber;
    }

    @JsonProperty("TRANSACTION_DATE")
    @Size(max = 10)
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date transactionDate;
    @Field(offset=134,length=10,align=Align.RIGHT,paddingChar = '0')        //  getTransactionDate
    @FixedFormatPattern("yyyyMMdd")
    public Date getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @JsonProperty("TRANSACTION_AMOUNT")
    @Size(max = 20)
    @NotNull
    private BigDecimal transactionAmount;
    @Field(offset=144,length=20,align=Align.LEFT,paddingChar = ' ')        //  getTransactionAmount
    @FixedFormatDecimal(decimals=2, useDecimalDelimiter=false)
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }
    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @JsonProperty("TRANSACTION_CURRENCY_CODE")
    @Size(max = 3)
    @NotNull
    private String transactionCurrencyCode;
    @Field(offset=164,length=3,align=Align.LEFT,paddingChar = ' ')        //  getTransactionCurrencyCode
    public String getTransactionCurrencyCode() {
        return transactionCurrencyCode;
    }
    public void setTransactionCurrencyCode(String transactionCurrencyCode) {
        this.transactionCurrencyCode = transactionCurrencyCode;
    }

    @JsonProperty("SOC_DATE")
    @Size(max = 10)
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date socDate;
    @Field(offset=167,length=10,align=Align.LEFT,paddingChar = ' ')        //  getSocDate
    @FixedFormatPattern("yyyyMMdd")
    public Date getSocDate() {
        return socDate;
    }
    public void setSocDate(Date socDate) {
        this.socDate = socDate;
    }

    @JsonProperty("SOC_AMOUNT")
    @Size(max = 20)
    @NotNull
    private BigDecimal socAmount;
    @Field(offset=177,length=20,align=Align.LEFT,paddingChar = ' ')        //  getSocAmount
    @FixedFormatDecimal(decimals=2, useDecimalDelimiter=false)
    public BigDecimal getSocAmount() {
        return socAmount;
    }
    public void setSocAmount(BigDecimal socAmount) {
        this.socAmount = socAmount;
    }

    @JsonProperty("AIRLINE_TICKET_NUMBER")
    @Size(max = 24)
    @NotNull
    private String airlineTicketNumber;
    @Field(offset=197,length=24,align=Align.LEFT,paddingChar = ' ')        //  getAirlineTicketNumber
    public String getAirlineTicketNumber() {
        return airlineTicketNumber;
    }
    public void setAirlineTicketNumber(String airlineTicketNumber) {
        this.airlineTicketNumber = airlineTicketNumber;
    }

    @JsonProperty("MICRO_SEQUENCE_NUMBER")
    @Size(max = 10)
    @NotNull
    private String microSequenceNumber;
    @Field(offset=221,length=10,align=Align.LEFT,paddingChar = ' ')        //  getMicroSequenceNumber
    public String getMicroSequenceNumber() {
        return microSequenceNumber;
    }
    public void setMicroSequenceNumber(String microSequenceNumber) {
        this.microSequenceNumber = microSequenceNumber;
    }

    @JsonProperty("SETTLEMENT_SERVICE_ESTABLISHMENT_NUMBER")
    @Size(max = 10)
    @NotNull
    private String settlementServiceEstablishmentNumber;
    @Field(offset=231,length=10,align=Align.LEFT,paddingChar = ' ')        //  getSettlementServiceEstablishmentNumber
    public String getSettlementServiceEstablishmentNumber() {
        return settlementServiceEstablishmentNumber;
    }
    public void setSettlementServiceEstablishmentNumber(String settlementServiceEstablishmentNumber) {
        this.settlementServiceEstablishmentNumber = settlementServiceEstablishmentNumber;
    }

    @JsonProperty("SETTLEMENT_AMOUNT")
    @Size(max = 20)
    @NotNull
    private BigDecimal settlementAmount;
    @Field(offset=241,length=20,align=Align.LEFT,paddingChar = ' ')        //  getSettlementAmount
    @FixedFormatDecimal(decimals=2, useDecimalDelimiter=false)
    public BigDecimal getSettlementAmount() {
        return settlementAmount;
    }
    public void setSettlementAmount(BigDecimal settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    @JsonProperty("SETTLEMENT_CURRENCY_CODE")
    @Size(max = 3)
    @NotNull
    private String settlementCurrencyCode;
    @Field(offset=261,length=3,align=Align.LEFT,paddingChar = ' ')        //  getSettlementCurrencyCode
    public String getSettlementCurrencyCode() {
        return settlementCurrencyCode;
    }
    public void setSettlementCurrencyCode(String settlementCurrencyCode) {
        this.settlementCurrencyCode = settlementCurrencyCode;
    }

    @JsonProperty("SETTLEMENT_DATE")
    @Size(max = 10)
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date settlementDate;
    @Field(offset=264,length=10,align=Align.RIGHT,paddingChar = '0')        //  getSettlementDate
    @FixedFormatPattern("yyyyMMdd")
    public Date getSettlementDate() {
        return settlementDate;
    }
    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    @JsonProperty("CASE_UPDATE_FLAG")
    @Size(max = 1)
    @NotNull
    private String caseUpdateFlag;
    @Field(offset=274,length=1,align=Align.LEFT,paddingChar = ' ')        //  getCaseUpdateFlag
    public String getCaseUpdateFlag() {
        return caseUpdateFlag;
    }
    public void setCaseUpdateFlag(String caseUpdateFlag) {
        this.caseUpdateFlag = caseUpdateFlag;
    }

    @JsonProperty("CASE_UPDATE_TYPE")
    @Size(max = 2)
    @NotNull
    private String caseUpdateType;
    @Field(offset=275,length=2,align=Align.LEFT,paddingChar = ' ')        //  getCaseUpdateType
    public String getCaseUpdateType() {
        return caseUpdateType;
    }
    public void setCaseUpdateType(String caseUpdateType) {
        this.caseUpdateType = caseUpdateType;
    }

    @JsonProperty("CASE_UPDATE_DATE")
    @Size(max = 10)
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date caseUpdateDate;
    @Field(offset=277,length=10,align=Align.LEFT,paddingChar = ' ')        //  getCaseUpdateDate
    @FixedFormatPattern("yyyyMMdd")
    public Date getCaseUpdateDate() {
        return caseUpdateDate;
    }
    public void setCaseUpdateDate(Date caseUpdateDate) {
        this.caseUpdateDate = caseUpdateDate;
    }

    @JsonProperty("SERVICE_ESTABLISHMENT_REPLY_BY_DATE")
    @Size(max = 10)
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date serviceEstablishmentReplyByDate;
    @Field(offset=287,length=10,align=Align.LEFT,paddingChar = ' ')        //  getServiceEstablishmentReplyByDate
    @FixedFormatPattern("yyyyMMdd")
    public Date getServiceEstablishmentReplyByDate() {
        return serviceEstablishmentReplyByDate;
    }
    public void setServiceEstablishmentReplyByDate(Date serviceEstablishmentReplyByDate) {
        this.serviceEstablishmentReplyByDate = serviceEstablishmentReplyByDate;
    }

    @JsonProperty("MERCHANT_INDUSTRY_TYPE_CODE")
    @Size(max = 10)
    @NotNull
    private String merchantIndustryTypeCode;
    @Field(offset=297,length=10,align=Align.LEFT,paddingChar = ' ')        //  getMerchantIndustryTypeCode
    public String getMerchantIndustryTypeCode() {
        return merchantIndustryTypeCode;
    }
    public void setMerchantIndustryTypeCode(String merchantIndustryTypeCode) {
        this.merchantIndustryTypeCode = merchantIndustryTypeCode;
    }

    @JsonProperty("TERMINAL_ID")
    @Size(max = 10)
    @NotNull
    private String terminalId;
    @Field(offset=307,length=10,align=Align.LEFT,paddingChar = ' ')        //  getTerminalId
    public String getTerminalId() {
        return terminalId;
    }
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @JsonProperty("SUBMISSION_CURRENCY_CODE")
    @Size(max = 3)
    @NotNull
    private String submissionCurrencyCode;
    @Field(offset=317,length=3,align=Align.LEFT,paddingChar = ' ')        //  getSubmissionCurrencyCode
    public String getSubmissionCurrencyCode() {
        return submissionCurrencyCode;
    }
    public void setSubmissionCurrencyCode(String submissionCurrencyCode) {
        this.submissionCurrencyCode = submissionCurrencyCode;
    }

    @JsonProperty("FIRST_PRESENTMENT_AMOUNT")
    @Size(max = 20)
    @NotNull
    private BigDecimal firstPresentmentAmount;
    @Field(offset=320,length=20,align=Align.LEFT,paddingChar = ' ')        //  getFirstPresentmentAmount
    @FixedFormatDecimal(decimals=2, useDecimalDelimiter=false)
    public BigDecimal getFirstPresentmentAmount() {
        return firstPresentmentAmount;
    }
    public void setFirstPresentmentAmount(BigDecimal firstPresentmentAmount) {
        this.firstPresentmentAmount = firstPresentmentAmount;
    }

    @JsonProperty("FIRST_PRESENTMENT_CURRENCY_CODE")
    @Size(max = 3)
    @NotNull
    private String firstPresentmentCurrencyCode;
    @Field(offset=340,length=3,align=Align.LEFT,paddingChar = ' ')        //  getFirstPresentmentCurrencyCode
    public String getFirstPresentmentCurrencyCode() {
        return firstPresentmentCurrencyCode;
    }
    public void setFirstPresentmentCurrencyCode(String firstPresentmentCurrencyCode) {
        this.firstPresentmentCurrencyCode = firstPresentmentCurrencyCode;
    }

    @JsonProperty("TRANSACTION_ID")
    @Size(max = 50)
    @NotNull
    private String transactionId;
    @Field(offset=3434,length=50,align=Align.LEFT,paddingChar = ' ')        //  getTransactionId
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @JsonProperty("ACQUIRER_REFERENCE_NUMBER")
    @Size(max = 23)
    @NotNull
    private String acquirerReferenceNumber;
    @Field(offset=393,length=23,align=Align.LEFT,paddingChar = ' ')        //  getAcquirerReferenceNumber
    public String getAcquirerReferenceNumber() {
        return acquirerReferenceNumber;
    }
    public void setAcquirerReferenceNumber(String acquirerReferenceNumber) {
        this.acquirerReferenceNumber = acquirerReferenceNumber;
    }

    @JsonProperty("ADDITIONAL_INFORMATION_TEXT_1")
    @Size(max = 1130)
    @NotNull
    private String additionalInformationText1;
    @Field(offset=416,length=1130,align=Align.LEFT,paddingChar = ' ')        //  getAdditionalInformationText1
    public String getAdditionalInformationText1() {
        return additionalInformationText1;
    }

    public void setAdditionalInformationText1(String additionalInformationText1) {
        this.additionalInformationText1 = additionalInformationText1;
    }

    @JsonProperty("ROC_REFERENCE_NUMBER_EXTENDED")
    @Size(max = 30)
    @NotNull
    private String rocReferenceNumberExtended;
    @Field(offset=1546,length=30,align=Align.LEFT,paddingChar = ' ')        //  getRocReferenceNumberExtended
    public String getRocReferenceNumberExtended() {
        return rocReferenceNumberExtended;
    }
    public void setRocReferenceNumberExtended(String rocReferenceNumberExtended) {
        this.rocReferenceNumberExtended = rocReferenceNumberExtended;
    }

    @JsonProperty("CARD_MEMBER_NAME")
    @Size(max = 30)
    @NotNull
    private String cardMemberName;
    @Field(offset=1576,length=30,align=Align.LEFT,paddingChar = ' ')        //  getCardMemberName
    public String getCardMemberName() {
        return cardMemberName;
    }
    public void setCardMemberName(String cardMemberName) {
        this.cardMemberName = cardMemberName;
    }

    @JsonProperty("ADDITIONAL_INFORMATION_TEXT_2")
    @Size(max = 438)
    @NotNull
    private String additionalInformationText2;
    @Field(offset=1607,length=438,align=Align.LEFT,paddingChar = ' ')        //  getAdditionalInformationText2
    public String getAdditionalInformationText2() {
        return additionalInformationText2;
    }
    public void setAdditionalInformationText2(String additionalInformationText2) {
        this.additionalInformationText2 = additionalInformationText2;
    }

    @JsonProperty("SELLER_ID")
    @Size(max = 20)
    @NotNull
    private String sellerId;
    @Field(offset=2046,length=20,align=Align.LEFT,paddingChar = ' ')        //  getSellerId
    public String getSellerId() {
        return sellerId;
    }
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}

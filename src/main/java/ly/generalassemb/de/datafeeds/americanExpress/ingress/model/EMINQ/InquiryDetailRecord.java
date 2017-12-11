package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EMINQ;

import com.ancientprogramming.fixedformat4j.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by davidashirov on 12/10/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "RECORD_TYPE",
        "DATA_TYPE",
        "CARD_NUMBER",
        "CASE_NUMBER",
        "AIRLINE_TICKET_NUMBER",
        "TERMINAL_ID",
        "CASE_UPDATE_TYPE",
        "CASE_UPDATE_DATE",
        "SERVICE_ESTABLISHMENT_REPLY_BY_DATE",
        "TRANSACTION_DATE",
        "SERVICE_ESTABLISHMENT_NUMBER",
        "TRANSACTION_CURRENCY_CODE",
        "TRANSACTION_AMOUNT",
        "FIRST_PRESENTMENT_CURRENCY_CODE",
        "FIRST_PRESENTMENT_AMOUNT",
        "SOC_INVOICE_NUMBER",
        "SOC_DATE",
        "SOC_AMOUNT",
        "ROC_REFERENCE_NUMBER",
        "SETTLEMENT_SERVICE_ESTABLISHMENT_NUMBER",
        "SETTLEMENT_CURRENCY_CODE",
        "SETTLEMENT_AMOUNT",
        "SETTLEMENT_DATE",
        "MICRO_SEQUENCE_NUMBER",
        "DISPUTE_REASON_CODE",
        "MERCHANT_INDUSTRY_TYPE_CODE",
        "CHARGEBACK_CODE",
        "SOC_CURRENCY_CODE",
        "INQUIRY_CASE_COUNT",
        "SOURCE_SYSTEM_CODE",
        "TRANSACTION_ID",
        "ACQUIRER_REFERENCE_NUMBER",
        "ADDITIONAL_INFORMATION_TEXT_1",
        "ROC_REFERENCE_NUMBER_EXTENDED",
        "ADDITIONAL_INFORMATION_TEXT_2",
        "INQUIRY_MARKET_CODE",
        "SELLER_ID"
})
@Record
public class InquiryDetailRecord {
    @JsonProperty("RECORD_TYPE")
    @Size(max = 1)
    @NotNull
    private String recordType;
    @Field(offset=1,length=1,align= Align.LEFT,paddingChar = ' ')        //  getRecordType
    public String getRecordType() {
        return recordType;
    }
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @JsonProperty("DATA_TYPE")
    @Size(max = 5)
    @NotNull
    private String dataType;
    @Field(offset=2,length=5,align=Align.LEFT,paddingChar = ' ')        //  getDataType
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @JsonProperty("CARD_NUMBER")
    @Size(max = 19)
    @NotNull
    private String cardNumber;
    @Field(offset=7,length=19,align=Align.LEFT,paddingChar = ' ')        //  getCardNumber
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
    @Field(offset=26,length=16,align=Align.LEFT,paddingChar = ' ')        //  getCaseNumber
    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    @JsonProperty("AIRLINE_TICKET_NUMBER")
    @Size(max = 24)
    @NotNull
    private String airlineTicketNumber;    
    @Field(offset=42,length=24,align=Align.LEFT,paddingChar = ' ')        //  getAirlineTicketNumber
    public String getAirlineTicketNumber() {
        return airlineTicketNumber;
    }
    public void setAirlineTicketNumber(String airlineTicketNumber) {
        this.airlineTicketNumber = airlineTicketNumber;
    }
    

    @JsonProperty("TERMINAL_ID")
    @Size(max = 10)
    @NotNull
    private String terminalId;    
    @Field(offset=66,length=10,align=Align.LEFT,paddingChar = ' ')        //  getTerminalId
    public String getTerminalId() {
        return terminalId;
    }
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @JsonProperty("CASE_UPDATE_TYPE")
    @Size(max = 2)
    @NotNull
    private String caseUpdateType;   
    @Field(offset=76,length=2,align=Align.LEFT,paddingChar = ' ')        //  getCaseUpdateType
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
    @FixedFormatPattern("yyyyMMdd")
    @Field(offset=78,length=10,align=Align.RIGHT,paddingChar = '0')        //  getCaseUpdateDate
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
    @Field(offset=88,length=10,align=Align.RIGHT,paddingChar = '0')        //  getServiceEstablishmentReplyByDate
    @FixedFormatPattern("yyyyMMdd")
    public Date getServiceEstablishmentReplyByDate() {
        return serviceEstablishmentReplyByDate;
    }
    public void setServiceEstablishmentReplyByDate(Date serviceEstablishmentReplyByDate) {
        this.serviceEstablishmentReplyByDate = serviceEstablishmentReplyByDate;
    }

    
    @JsonProperty("TRANSACTION_DATE")
    @Size(max = 10)
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date transactionDate;
    @Field(offset=98,length=10,align=Align.RIGHT,paddingChar = '0')        //  getTransactionDate
    @FixedFormatPattern("yyyyMMdd")
    public Date getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    
    @JsonProperty("SERVICE_ESTABLISHMENT_NUMBER")
    @Size(max = 10)
    @NotNull
    private String serviceEstablishmentNumber;    
    @Field(offset=108,length=10,align=Align.LEFT,paddingChar = ' ')        //  getServiceEstablishmentNumber
    public String getServiceEstablishmentNumber() {
        return serviceEstablishmentNumber;
    }

    public void setServiceEstablishmentNumber(String serviceEstablishmentNumber) {
        this.serviceEstablishmentNumber = serviceEstablishmentNumber;
    }

    @JsonProperty("TRANSACTION_CURRENCY_CODE")
    @Size(max = 3)
    @NotNull
    private String transactionCurrencyCode;    
    @Field(offset=118,length=3,align=Align.LEFT,paddingChar = ' ')        //  getTransactionCurrencyCode
    public String getTransactionCurrencyCode() {
        return transactionCurrencyCode;
    }
    public void setTransactionCurrencyCode(String transactionCurrencyCode) {
        this.transactionCurrencyCode = transactionCurrencyCode;
    }

    @JsonProperty("TRANSACTION_AMOUNT")
    @Size(max = 20)
    @NotNull
    private BigDecimal transactionAmount;
    @Field(offset=121,length=20,align=Align.RIGHT,paddingChar = ' ')        //  getTransactionAmount
    @FixedFormatDecimal(decimals=2, useDecimalDelimiter=true)
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }
    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @JsonProperty("FIRST_PRESENTMENT_CURRENCY_CODE")
    @Size(max = 3)
    @NotNull
    private String firstPresentmentCurrencyCode;
    @Field(offset=141,length=3,align=Align.LEFT,paddingChar = ' ')        //  getFirstPresentmentCurrencyCode
    public String getFirstPresentmentCurrencyCode() {
        return firstPresentmentCurrencyCode;
    }
    public void setFirstPresentmentCurrencyCode(String firstPresentmentCurrencyCode) {
        this.firstPresentmentCurrencyCode = firstPresentmentCurrencyCode;
    }

    @JsonProperty("FIRST_PRESENTMENT_AMOUNT")
    @Size(max = 20)
    @NotNull
    private BigDecimal firstPresentmentAmount;
    @Field(offset=144,length=20,align=Align.RIGHT,paddingChar = ' ')        //  getFirstPresentmentAmount
    @FixedFormatDecimal(decimals=2, useDecimalDelimiter=true)
    public BigDecimal getFirstPresentmentAmount() {
        return firstPresentmentAmount;
    }
    public void setFirstPresentmentAmount(BigDecimal firstPresentmentAmount) {
        this.firstPresentmentAmount = firstPresentmentAmount;
    }

    @JsonProperty("SOC_INVOICE_NUMBER")
    @Size(max = 15)
    @NotNull
    private String socInvoiceNumber;
    @Field(offset=164,length=15,align=Align.LEFT,paddingChar = ' ')        //  getSocInvoiceNumber
    public String getSocInvoiceNumber() {
        return socInvoiceNumber;
    }
    public void setSocInvoiceNumber(String socInvoiceNumber) {
        this.socInvoiceNumber = socInvoiceNumber;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("SOC_DATE")
    @Size(max = 10)
    @NotNull
    private Date socDate;
    @Field(offset=179,length=10,align=Align.RIGHT,paddingChar = '0')        //  getSocDate
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
    @Field(offset=189,length=20,align=Align.RIGHT,paddingChar = ' ')        //  getSocAmount
    @FixedFormatDecimal(decimals=2, useDecimalDelimiter=true)
    public BigDecimal getSocAmount() {
        return socAmount;
    }
    public void setSocAmount(BigDecimal socAmount) {
        this.socAmount = socAmount;
    }

    @JsonProperty("ROC_REFERENCE_NUMBER")
    @Size(max = 15)
    @NotNull
    private String rocReferenceNumber;
    @Field(offset=209,length=15,align=Align.LEFT,paddingChar = ' ')        //  getRocReferenceNumber
    public String getRocReferenceNumber() {
        return rocReferenceNumber;
    }
    public void setRocReferenceNumber(String rocReferenceNumber) {
        this.rocReferenceNumber = rocReferenceNumber;
    }

    @JsonProperty("SETTLEMENT_SERVICE_ESTABLISHMENT_NUMBER")
    @Size(max = 10)
    @NotNull
    private String settlementServiceEstablishmentNumber;
    @Field(offset=224,length=10,align=Align.LEFT,paddingChar = ' ')        //  getSettlementServiceEstablishmentNumber
    public String getSettlementServiceEstablishmentNumber() {
        return settlementServiceEstablishmentNumber;
    }
    public void setSettlementServiceEstablishmentNumber(String settlementServiceEstablishmentNumber) {
        this.settlementServiceEstablishmentNumber = settlementServiceEstablishmentNumber;
    }

    @JsonProperty("SETTLEMENT_CURRENCY_CODE")
    @Size(max = 3)
    @NotNull
    private String settlementCurrencyCode;
    @Field(offset=234,length=3,align=Align.LEFT,paddingChar = ' ')        //  getSettlementCurrencyCode
    public String getSettlementCurrencyCode() {
        return settlementCurrencyCode;
    }
    public void setSettlementCurrencyCode(String settlementCurrencyCode) {
        this.settlementCurrencyCode = settlementCurrencyCode;
    }

    @JsonProperty("SETTLEMENT_AMOUNT")
    @Size(max = 20)
    @NotNull
    private BigDecimal settlementAmount;
    @Field(offset=237,length=20,align=Align.RIGHT,paddingChar = ' ')        //  getSettlementAmount
    @FixedFormatDecimal(decimals=2, useDecimalDelimiter=true)
    public BigDecimal getSettlementAmount() {
        return settlementAmount;
    }
    public void setSettlementAmount(BigDecimal settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    @JsonProperty("SETTLEMENT_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Size(max = 10)
    @NotNull
    private Date settlementDate;
    @Field(offset=257,length=10,align=Align.RIGHT,paddingChar = '0')        //  getSettlementDate
    @FixedFormatPattern("yyyyMMdd")
    public Date getSettlementDate() {
        return settlementDate;
    }
    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }



    @JsonProperty("MICRO_SEQUENCE_NUMBER")
    @Size(max = 10)
    @NotNull
    private String microSequenceNumber;
    @Field(offset=267,length=10,align=Align.LEFT,paddingChar = ' ')        //  getMicroSequenceNumber
    public String getMicroSequenceNumber() {
        return microSequenceNumber;
    }
    public void setMicroSequenceNumber(String microSequenceNumber) {
        this.microSequenceNumber = microSequenceNumber;
    }

    @JsonProperty("DISPUTE_REASON_CODE")
    @Size(max = 4)
    @NotNull
    private String disputeReasonCode;
    @Field(offset=277,length=4,align=Align.LEFT,paddingChar = ' ')        //  getDisputeReasonCode
    public String getDisputeReasonCode() {
        return disputeReasonCode;
    }
    public void setDisputeReasonCode(String disputeReasonCode) {
        this.disputeReasonCode = disputeReasonCode;
    }

    @JsonProperty("MERCHANT_INDUSTRY_TYPE_CODE")
    @Size(max = 10)
    @NotNull
    private String merchantIndustryTypeCode;
    @Field(offset=281,length=10,align=Align.LEFT,paddingChar = ' ')        //  getMerchantIndustryTypeCode
    public String getMerchantIndustryTypeCode() {
        return merchantIndustryTypeCode;
    }
    public void setMerchantIndustryTypeCode(String merchantIndustryTypeCode) {
        this.merchantIndustryTypeCode = merchantIndustryTypeCode;
    }

    @JsonProperty("CHARGEBACK_CODE")
    @Size(max = 4)
    @NotNull
    private String chargebackCode;
    @Field(offset=291,length=4,align=Align.LEFT,paddingChar = ' ')        //  getChargebackCode
    public String getChargebackCode() {
        return chargebackCode;
    }
    public void setChargebackCode(String chargebackCode) {
        this.chargebackCode = chargebackCode;
    }

    @JsonProperty("SOC_CURRENCY_CODE")
    @Size(max = 3)
    @NotNull
    private String socCurrencyCode;
    @Field(offset=295,length=3,align=Align.LEFT,paddingChar = ' ')        //  getSocCurrencyCode
    public String getSocCurrencyCode() {
        return socCurrencyCode;
    }
    public void setSocCurrencyCode(String socCurrencyCode) {
        this.socCurrencyCode = socCurrencyCode;
    }

    @JsonProperty("INQUIRY_CASE_COUNT")
    @Size(max = 4)
    @NotNull
    private Integer inquiryCaseCount;
    @Field(offset=298,length=4,align=Align.LEFT,paddingChar = ' ')        //  getInquiryCaseCount
    public Integer getInquiryCaseCount() {
        return inquiryCaseCount;
    }
    public void setInquiryCaseCount(Integer inquiryCaseCount) {
        this.inquiryCaseCount = inquiryCaseCount;
    }

    @JsonProperty("SOURCE_SYSTEM_CODE")
    @Size(max = 3)
    @NotNull
    private String sourceSystemCode;
    @Field(offset=302,length=3,align=Align.LEFT,paddingChar = ' ')        //  getSourceSystemCode
    public String getSourceSystemCode() {
        return sourceSystemCode;
    }
    public void setSourceSystemCode(String sourceSystemCode) {
        this.sourceSystemCode = sourceSystemCode;
    }

    @JsonProperty("TRANSACTION_ID")
    @Size(max = 50)
    @NotNull
    private String transactionId;
    @Field(offset=305,length=50,align=Align.LEFT,paddingChar = ' ')        //  getTransactionId
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
    @Field(offset=355,length=23,align=Align.LEFT,paddingChar = ' ')        //  getAcquirerReferenceNumber
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
    @Field(offset=378,length=1130,align=Align.LEFT,paddingChar = ' ')        //  getAdditionalInformationText1
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
    @Field(offset=1508,length=30,align=Align.LEFT,paddingChar = ' ')        //  getRocReferenceNumberExtended
    public String getRocReferenceNumberExtended() {
        return rocReferenceNumberExtended;
    }
    public void setRocReferenceNumberExtended(String rocReferenceNumberExtended) {
        this.rocReferenceNumberExtended = rocReferenceNumberExtended;
    }

    @JsonProperty("ADDITIONAL_INFORMATION_TEXT_2")
    @Size(max = 470)
    @NotNull
    private String additionalInformationText2;
    @Field(offset=1538,length=470,align=Align.LEFT,paddingChar = ' ')        //  getAdditionalInformationText2
    public String getAdditionalInformationText2() {
        return additionalInformationText2;
    }
    public void setAdditionalInformationText2(String additionalInformationText2) {
        this.additionalInformationText2 = additionalInformationText2;
    }

    @JsonProperty("INQUIRY_MARKET_CODE")
    @Size(max = 3)
    @NotNull
    private String inquiryMarketCode;
    @Field(offset=2008,length=3,align=Align.LEFT,paddingChar = ' ')        //  getInquiryMarketCode
    public String getInquiryMarketCode() {
        return inquiryMarketCode;
    }
    public void setInquiryMarketCode(String inquiryMarketCode) {
        this.inquiryMarketCode = inquiryMarketCode;
    }

    @JsonProperty("SELLER_ID")
    @Size(max = 20)
    @NotNull
    private String sellerId;
    @Field(offset=2011,length=20,align=Align.LEFT,paddingChar = ' ')        //  getSellerId
    public String getSellerId() {
        return sellerId;
    }
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}

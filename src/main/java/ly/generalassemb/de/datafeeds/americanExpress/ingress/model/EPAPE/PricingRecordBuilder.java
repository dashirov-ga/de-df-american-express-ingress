package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import java.math.BigDecimal;

/**
 * Created by davidashirov on 12/3/17.
 */
public final class PricingRecordBuilder {
    private String settlementSeAccountNumber;
    private String settlementCurrencyCode;
    private Integer recordCode;
    private String recordSubCode;
    private String pricingDescription;
    private Integer discountRate;
    private BigDecimal feePerCharge;
    private BigDecimal numberOfCharges;
    private BigDecimal grossAmount;
    private BigDecimal grossDebitAmount;
    private BigDecimal grossCredeitAmount;
    private BigDecimal discountFee;
    private BigDecimal serviceFee;
    private BigDecimal netAmount;

    private PricingRecordBuilder() {
    }

    public static PricingRecordBuilder aPricingRecord() {
        return new PricingRecordBuilder();
    }

    public PricingRecordBuilder withSettlementSeAccountNumber(String settlementSeAccountNumber) {
        this.settlementSeAccountNumber = settlementSeAccountNumber;
        return this;
    }

    public PricingRecordBuilder withSettlementCurrencyCode(String settlementCurrencyCode) {
        this.settlementCurrencyCode = settlementCurrencyCode;
        return this;
    }

    public PricingRecordBuilder withRecordCode(Integer recordCode) {
        this.recordCode = recordCode;
        return this;
    }

    public PricingRecordBuilder withRecordSubCode(String recordSubCode) {
        this.recordSubCode = recordSubCode;
        return this;
    }

    public PricingRecordBuilder withPricingDescription(String pricingDescription) {
        this.pricingDescription = pricingDescription;
        return this;
    }

    public PricingRecordBuilder withDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public PricingRecordBuilder withFeePerCharge(BigDecimal feePerCharge) {
        this.feePerCharge = feePerCharge;
        return this;
    }

    public PricingRecordBuilder withNumberOfCharges(BigDecimal numberOfCharges) {
        this.numberOfCharges = numberOfCharges;
        return this;
    }

    public PricingRecordBuilder withGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
        return this;
    }

    public PricingRecordBuilder withGrossDebitAmount(BigDecimal grossDebitAmount) {
        this.grossDebitAmount = grossDebitAmount;
        return this;
    }

    public PricingRecordBuilder withGrossCredeitAmount(BigDecimal grossCredeitAmount) {
        this.grossCredeitAmount = grossCredeitAmount;
        return this;
    }

    public PricingRecordBuilder withDiscountFee(BigDecimal discountFee) {
        this.discountFee = discountFee;
        return this;
    }

    public PricingRecordBuilder withServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
        return this;
    }

    public PricingRecordBuilder withNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
        return this;
    }

    public PricingRecordBuilder but() {
        return aPricingRecord().withSettlementSeAccountNumber(settlementSeAccountNumber).withSettlementCurrencyCode(settlementCurrencyCode).withRecordCode(recordCode).withRecordSubCode(recordSubCode).withPricingDescription(pricingDescription).withDiscountRate(discountRate).withFeePerCharge(feePerCharge).withNumberOfCharges(numberOfCharges).withGrossAmount(grossAmount).withGrossDebitAmount(grossDebitAmount).withGrossCredeitAmount(grossCredeitAmount).withDiscountFee(discountFee).withServiceFee(serviceFee).withNetAmount(netAmount);
    }

    public PricingRecord build() {
        PricingRecord pricingRecord = new PricingRecord();
        pricingRecord.setSettlementSeAccountNumber(settlementSeAccountNumber);
        pricingRecord.setSettlementCurrencyCode(settlementCurrencyCode);
        pricingRecord.setRecordCode(recordCode);
        pricingRecord.setRecordSubCode(recordSubCode);
        pricingRecord.setPricingDescription(pricingDescription);
        pricingRecord.setDiscountRate(discountRate);
        pricingRecord.setFeePerCharge(feePerCharge);
        pricingRecord.setNumberOfCharges(numberOfCharges);
        pricingRecord.setGrossAmount(grossAmount);
        pricingRecord.setGrossDebitAmount(grossDebitAmount);
        pricingRecord.setGrossCredeitAmount(grossCredeitAmount);
        pricingRecord.setDiscountFee(discountFee);
        pricingRecord.setServiceFee(serviceFee);
        pricingRecord.setNetAmount(netAmount);
        return pricingRecord;
    }
}

package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE.FileHeaderRecord;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE.FileTrailerRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidashirov on 12/7/17.
 */
public class ReconciledPayment {
    private PaymentRecord payment;
    private PricingRecord pricingRecord;
    private List<AdjustmentRecord> adjustments;
    private List<MerchantSubmission> merchantSubmissions;

    public List<AdjustmentRecord> getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(List<AdjustmentRecord> adjustments) {
        this.adjustments = adjustments;
    }

    public PaymentRecord getPayment() {
        return payment;
    }

    public void setPayment(PaymentRecord payment) {
        this.payment = payment;
    }

    public PricingRecord getPricingRecord() {
        return pricingRecord;
    }

    public void setPricingRecord(PricingRecord pricingRecord) {
        this.pricingRecord = pricingRecord;
    }

    public List<MerchantSubmission> getMerchantSubmissions() {
        return merchantSubmissions;
    }

    public void setMerchantSubmissions(List<MerchantSubmission> merchantSubmissions) {
        this.merchantSubmissions = merchantSubmissions;
    }

    public ReconciledPayment() {
        merchantSubmissions=new ArrayList<>();
        adjustments = new ArrayList<>();
    }

    public static final class ReconciledPaymentBuilder {
        private PaymentRecord payment;
        private PricingRecord pricingRecord;
        private List<MerchantSubmission> merchantSubmissions;
        private List<AdjustmentRecord> adjustments;

        private ReconciledPaymentBuilder() {
        }

        public static ReconciledPaymentBuilder aReconciledPayment() {
            return new ReconciledPaymentBuilder();
        }

        public ReconciledPaymentBuilder withPayment(PaymentRecord payment) {
            this.payment = payment;
            return this;
        }

        public ReconciledPaymentBuilder withPricingRecord(PricingRecord pricingRecord) {
            this.pricingRecord = pricingRecord;
            return this;
        }

        public ReconciledPaymentBuilder withMerchantSubmissions(List<MerchantSubmission> merchantSubmissions) {
            this.merchantSubmissions = merchantSubmissions;
            return this;
        }

        public ReconciledPaymentBuilder withAdjustments(List<AdjustmentRecord> adjustments) {
            this.adjustments = adjustments;
            return this;
        }

        public ReconciledPaymentBuilder but() {
            return aReconciledPayment().withPayment(payment).withPricingRecord(pricingRecord).withMerchantSubmissions(merchantSubmissions).withAdjustments(adjustments);
        }

        public ReconciledPayment build() {
            ReconciledPayment reconciledPayment = new ReconciledPayment();
            reconciledPayment.setPayment(payment);
            reconciledPayment.setPricingRecord(pricingRecord);
            if (this.merchantSubmissions == null)
                this.merchantSubmissions=new ArrayList<>();
            if (this.adjustments == null)
                this.adjustments = new ArrayList<>();
            reconciledPayment.setMerchantSubmissions(merchantSubmissions);
            return reconciledPayment;
        }
    }
}

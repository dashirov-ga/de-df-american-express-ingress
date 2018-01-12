package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidashirov on 12/7/17.
 */
public class ReconciledPayment {
    private Header header;
    private PaymentRecord paymentSummary;
    private PricingRecord pricingSummary;
    private List<MerchantSubmission> merchantSubmissions;
    private Trailer trailer;

    public PaymentRecord getPaymentSummary() {
        return paymentSummary;
    }

    public void setPaymentSummary(PaymentRecord paymentSummary) {
        this.paymentSummary = paymentSummary;
    }

    public PricingRecord getPricingSummary() {
        return pricingSummary;
    }

    public void setPricingSummary(PricingRecord pricingSummary) {
        this.pricingSummary = pricingSummary;
    }

    public List<MerchantSubmission> getMerchantSubmissions() {
        return merchantSubmissions;
    }

    public void setMerchantSubmissions(List<MerchantSubmission> merchantSubmissions) {
        this.merchantSubmissions = merchantSubmissions;
    }

    public ReconciledPayment() {
        merchantSubmissions=new ArrayList<>();

    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    public void put(MerchantSubmission submission){
        merchantSubmissions.add(submission);
    }

    public void put(PricingRecord pricingRecord){
        this.pricingSummary = pricingRecord;
    }

    public void put(PaymentRecord paymentSummary){
        this.paymentSummary = paymentSummary;
    }



    public static final class ReconciledPaymentBuilder {
        private Header header;
        private PaymentRecord paymentSummary;
        private PricingRecord pricingSummary;
        private List<MerchantSubmission> merchantSubmissions;
        private Trailer trailer;

        private ReconciledPaymentBuilder() {
        }

        public static ReconciledPaymentBuilder aReconciledPayment() {
            return new ReconciledPaymentBuilder();
        }

        public ReconciledPaymentBuilder withHeader(Header header) {
            this.header = header;
            return this;
        }

        public ReconciledPaymentBuilder withPaymentSummary(PaymentRecord paymentSummary) {
            this.paymentSummary = paymentSummary;
            return this;
        }

        public ReconciledPaymentBuilder withPricingSummary(PricingRecord pricingSummary) {
            this.pricingSummary = pricingSummary;
            return this;
        }

        public ReconciledPaymentBuilder withMerchantSubmissions(List<MerchantSubmission> merchantSubmissions) {
            this.merchantSubmissions = merchantSubmissions;
            return this;
        }

        public ReconciledPaymentBuilder withTrailer(Trailer trailer) {
            this.trailer = trailer;
            return this;
        }

        public ReconciledPayment build() {
            ReconciledPayment reconciledPayment = new ReconciledPayment();
            reconciledPayment.setPaymentSummary(paymentSummary);
            reconciledPayment.setPricingSummary(pricingSummary);
            reconciledPayment.setMerchantSubmissions(merchantSubmissions);
            reconciledPayment.header = this.header;
            reconciledPayment.trailer = this.trailer;
            return reconciledPayment;
        }
    }
}

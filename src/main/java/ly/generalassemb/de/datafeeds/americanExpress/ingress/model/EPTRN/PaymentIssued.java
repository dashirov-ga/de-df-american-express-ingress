package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN;

import java.util.ArrayList;
import java.util.List;

public class PaymentIssued {
    PaymentRecord paymentRecord;
    List<MerchantSubmission> submissions;
    List<ChargebackRecord> chargebacks;
    List<AdjustmentRecord> adjustments;
    List<OtherRecord> other;

    public PaymentIssued() {
        submissions=new ArrayList<>();
        chargebacks = new ArrayList<>();
        adjustments = new ArrayList<>();
        other = new ArrayList<>();
    }

    public PaymentRecord getPaymentRecord() {
        return paymentRecord;
    }

    public void setPaymentRecord(PaymentRecord paymentRecord) {
        this.paymentRecord = paymentRecord;
    }

    public List<MerchantSubmission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<MerchantSubmission> submissions) {
        this.submissions = submissions;
    }

    public List<ChargebackRecord> getChargebacks() {
        return chargebacks;
    }

    public void setChargebacks(List<ChargebackRecord> chargebacks) {
        this.chargebacks = chargebacks;
    }

    public List<AdjustmentRecord> getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(List<AdjustmentRecord> adjustments) {
        this.adjustments = adjustments;
    }

    public List<OtherRecord> getOther() {
        return other;
    }

    public void setOther(List<OtherRecord> other) {
        this.other = other;
    }
}

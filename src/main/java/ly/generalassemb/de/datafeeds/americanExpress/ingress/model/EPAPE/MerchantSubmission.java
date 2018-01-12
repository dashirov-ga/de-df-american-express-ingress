package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidashirov on 12/7/17.
 */
public class MerchantSubmission {
    private SOCRecord summaryOfCharges;
    private List<ROCRecord> recordsOfCharges;
    private List<AdjustmentRecord> adjustments;

    public MerchantSubmission() {
        recordsOfCharges=new ArrayList<>();
        adjustments = new ArrayList<>();

    }

    public SOCRecord getSummaryOfCharges() {
        return summaryOfCharges;
    }

    public List<ROCRecord> getRecordsOfCharges() {
        return recordsOfCharges;
    }

    public List<AdjustmentRecord> getAdjustments() {
        return adjustments;
    }

    public SOCRecord getSocRecord() {
        return summaryOfCharges;
    }

    public void setSocRecord(SOCRecord socRecord) {
        this.summaryOfCharges = socRecord;
    }

    public List<ROCRecord> getRocRecords() {
        return recordsOfCharges;
    }

    public void setRocRecords(List<ROCRecord> rocRecords) {
        this.recordsOfCharges = rocRecords;
    }

    public void put(SOCRecord summaryOfCharges){
        this.summaryOfCharges = summaryOfCharges;
    }
    public void put(ROCRecord recordOfCharges) {
        this.recordsOfCharges.add(recordOfCharges);
    }
    public void put(AdjustmentRecord adjustment){
        this.adjustments.add(adjustment);
    }

    public static final class MerchantSubmissionBuilder {
        private SOCRecord summaryOfCharges;
        private List<ROCRecord> recordsOfCharges;
        private List<AdjustmentRecord> adjustments;

        private MerchantSubmissionBuilder() {
            this.adjustments = new ArrayList<>();
            this.recordsOfCharges = new ArrayList<>();

        }

        public static MerchantSubmissionBuilder aMerchantSubmission() {
            return new MerchantSubmissionBuilder();
        }

        public MerchantSubmissionBuilder withSummaryOfCharges(SOCRecord summaryOfCharges) {
            this.summaryOfCharges = summaryOfCharges;
            return this;
        }

        public MerchantSubmissionBuilder withRecordsOfCharges(List<ROCRecord> recordsOfCharges) {
            this.recordsOfCharges = recordsOfCharges;
            return this;
        }

        public MerchantSubmissionBuilder withAdjustments(List<AdjustmentRecord> adjustments) {
            this.adjustments = adjustments;
            return this;
        }

        public MerchantSubmission build() {
            MerchantSubmission merchantSubmission = new MerchantSubmission();
            if (this.recordsOfCharges == null)
                this.recordsOfCharges=new ArrayList<>();
            merchantSubmission.recordsOfCharges = this.recordsOfCharges;

            merchantSubmission.summaryOfCharges = this.summaryOfCharges;
            if (this.adjustments == null)
                this.adjustments = new ArrayList<>();
            merchantSubmission.adjustments = this.adjustments;
            return merchantSubmission;
        }
    }
}

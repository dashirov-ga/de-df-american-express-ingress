package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidashirov on 12/7/17.
 */
public class MerchantSubmission {
    private SOCRecord socRecord;

    private List<ROCRecord> rocRecords;

    public MerchantSubmission() {
        rocRecords=new ArrayList<>();

    }

    public SOCRecord getSocRecord() {
        return socRecord;
    }

    public void setSocRecord(SOCRecord socRecord) {
        this.socRecord = socRecord;
    }

    public List<ROCRecord> getRocRecords() {
        return rocRecords;
    }

    public void setRocRecords(List<ROCRecord> rocRecords) {
        this.rocRecords = rocRecords;
    }


    public static final class Builder {
        private SOCRecord socRecord;
        private List<AdjustmentRecord> adjustmentRecords;
        private List<ROCRecord> rocRecords;

        private Builder() {
        }

        public static Builder aMerchantSubmission() {
            return new Builder();
        }

        public Builder withSocRecord(SOCRecord socRecord) {
            this.socRecord = socRecord;
            return this;
        }

        public Builder withAdjustmentRecords(List<AdjustmentRecord> adjustmentRecords) {
            this.adjustmentRecords = adjustmentRecords;
            return this;
        }

        public Builder withRocRecords(List<ROCRecord> rocRecords) {
            this.rocRecords = rocRecords;
            return this;
        }

        public Builder but() {
            return aMerchantSubmission().withSocRecord(socRecord).withAdjustmentRecords(adjustmentRecords).withRocRecords(rocRecords);
        }

        public MerchantSubmission build() {
            MerchantSubmission merchantSubmission = new MerchantSubmission();
            merchantSubmission.setSocRecord(socRecord);
            if (this.rocRecords==null)
                this.rocRecords=new ArrayList<>();
            merchantSubmission.setRocRecords(rocRecords);
            return merchantSubmission;
        }
    }
}

package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN;

import java.util.ArrayList;
import java.util.List;

public class MerchantSubmission {
    SOCRecord socRecord;
    List<ROCRecord> rocRecords;

    public MerchantSubmission() {
        rocRecords = new ArrayList<>();
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
}

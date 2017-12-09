package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidashirov on 12/7/17.
 */
public class EPAPEFile {
    private final ObjectMapper mapper = new ObjectMapper();

    FileHeaderRecord headerRecord;
    List<ReconciledPayment> paymentList;
    FileTrailerRecord trailerRecord;

    public EPAPEFile() {
        this.paymentList = new ArrayList<>();
    }

    public FileHeaderRecord getHeaderRecord() {
        return headerRecord;
    }

    public void setHeaderRecord(FileHeaderRecord headerRecord) {
        this.headerRecord = headerRecord;
    }

    public FileTrailerRecord getTrailerRecord() {
        return trailerRecord;
    }

    public void setTrailerRecord(FileTrailerRecord trailerRecord) {
        this.trailerRecord = trailerRecord;
    }

    public List<ReconciledPayment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<ReconciledPayment> paymentList) {
        this.paymentList = paymentList;
    }

    public static final class FileBuilder {
        FileHeaderRecord headerRecord;
        FileTrailerRecord trailerRecord;
        List<ReconciledPayment> paymentList;

        private FileBuilder() {
        }

        public static FileBuilder aFile() {
            return new FileBuilder();
        }

        public FileBuilder withHeaderRecord(FileHeaderRecord headerRecord) {
            this.headerRecord = headerRecord;
            return this;
        }

        public FileBuilder withTrailerRecord(FileTrailerRecord trailerRecord) {
            this.trailerRecord = trailerRecord;
            return this;
        }

        public FileBuilder withPaymentList(List<ReconciledPayment> paymentList) {
            this.paymentList = paymentList;
            return this;
        }

        public FileBuilder but() {
            return aFile().withHeaderRecord(headerRecord).withTrailerRecord(trailerRecord).withPaymentList(paymentList);
        }

        public EPAPEFile build() {
            EPAPEFile file = new EPAPEFile();
            file.setHeaderRecord(headerRecord);
            file.setTrailerRecord(trailerRecord);

            if (this.paymentList == null)
                this.paymentList = new ArrayList<>();

            file.setPaymentList(paymentList);
            return file;
        }
    }
    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ((Object)this).toString();
        }
    }

}

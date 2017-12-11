package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EMCBK;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidashirov on 12/11/17.
 */
public class EMCBKFile {
    private FileHeaderRecord header;
    private List<ChargebackDetailRecord> details;
    private FileTrailerRecord trailer;

    public EMCBKFile() {
        details = new ArrayList<>();
    }

    public FileHeaderRecord getHeader() {
        return header;
    }

    public void setHeader(FileHeaderRecord header) {
        this.header = header;
    }

    public List<ChargebackDetailRecord> getDetails() {
        return details;
    }

    public void setDetails(List<ChargebackDetailRecord> details) {
        this.details = details;
    }

    public FileTrailerRecord getTrailer() {
        return trailer;
    }

    public void setTrailer(FileTrailerRecord trailer) {
        this.trailer = trailer;
    }

    public static final class Builder {
        private FileHeaderRecord header;
        private List<ChargebackDetailRecord> details;
        private FileTrailerRecord trailer;

        private Builder() {
        }

        public static Builder anEMCBKFile() {
            return new Builder();
        }

        public Builder withHeader(FileHeaderRecord header) {
            this.header = header;
            return this;
        }

        public Builder withDetails(List<ChargebackDetailRecord> details) {
            this.details = details;
            return this;
        }

        public Builder withTrailer(FileTrailerRecord trailer) {
            this.trailer = trailer;
            return this;
        }

        public Builder but() {
            return anEMCBKFile().withHeader(header).withDetails(details).withTrailer(trailer);
        }

        public EMCBKFile build() {
            EMCBKFile eMCBKFile = new EMCBKFile();
            eMCBKFile.setHeader(header);
            if (details != null)
               eMCBKFile.setDetails(details);
            eMCBKFile.setTrailer(trailer);
            return eMCBKFile;
        }
    }
}

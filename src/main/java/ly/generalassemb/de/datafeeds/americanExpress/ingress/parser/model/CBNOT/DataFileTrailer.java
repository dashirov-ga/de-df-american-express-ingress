package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.model.CBNOT;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.AmexFeedLineParserOutput;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by dashirov on 5/12/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "dataFileTrailerRecordType", "amexApplication", "serviceAccessId", "dataType",
                     "originalFileTransmissionDateTime" })
public class DataFileTrailer implements AmexFeedLineParserOutput {

    private static final ObjectMapper mapper = new ObjectMapper();
    @JsonProperty
    private AmexApplication amexApplication;

    @JsonProperty
    private String serviceAccessId;

    @JsonProperty
    private String dataType;

    @JsonProperty

    private String dataFileTrailerRecordType;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSZ", timezone = "America/New_York")
    private Date originalFileTransmissionDateTime;

    public DataFileTrailer withDataFileRecordType(String dataFileTrailerRecordType) {
        this.dataFileTrailerRecordType = dataFileTrailerRecordType;
        return this;
    }

    public DataFileTrailer withAmexApplication(AmexApplication amexApplication) {
        this.amexApplication = amexApplication;
        return this;
    }

    public DataFileTrailer withServiceAccessId(String serviceAccessId) {
        this.serviceAccessId = serviceAccessId;
        return this;
    }

    public DataFileTrailer withDataType(String dataType) {
        this.dataType = dataType;
        return this;

    }

    public DataFileTrailer withOriginalFileTransmissionDateTime(Date originalFileTransmissionDateTime) {
        this.originalFileTransmissionDateTime = originalFileTransmissionDateTime;
        return this;
    }

    public String getDataFileTrailerRecordType() {
        return dataFileTrailerRecordType;
    }

    public void setDataFileTrailerRecordType(String dataFileTrailerRecordType) {
        this.dataFileTrailerRecordType = dataFileTrailerRecordType;
    }

    public AmexApplication getAmexApplication() {
        return amexApplication;
    }

    public void setAmexApplication(AmexApplication amexApplication) {
        this.amexApplication = amexApplication;
    }

    public String getServiceAccessId() {
        return serviceAccessId;
    }

    public void setServiceAccessId(String serviceAccessId) {
        this.serviceAccessId = serviceAccessId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Date getOriginalFileTransmissionDateTime() {
        return originalFileTransmissionDateTime;
    }

    public void setOriginalFileTransmissionDateTime(Date originalFileTransmissionDateTime) {
        this.originalFileTransmissionDateTime = originalFileTransmissionDateTime;
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DataFileTrailer that = (DataFileTrailer) o;

        return new EqualsBuilder().append(getDataFileTrailerRecordType(), that.getDataFileTrailerRecordType())
                .append(getAmexApplication(), that.getAmexApplication())
                .append(getServiceAccessId(), that.getServiceAccessId())
                .append(getDataType(), that.getDataType())
                .append(getOriginalFileTransmissionDateTime(), that.getOriginalFileTransmissionDateTime())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getDataFileTrailerRecordType()).append(getAmexApplication())
                .append(getServiceAccessId()).append(getDataType()).append(getOriginalFileTransmissionDateTime())
                .toHashCode();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({ "applicationSytemCode", "fileTypeCode", "fileCreationDate", "recordCount" })
    public static class AmexApplication {
        public static final DateFormat fileCreationDateFormat = new SimpleDateFormat("yyyyMMdd");
        @JsonProperty
        private String applicationSytemCode;
        @JsonProperty
        private String fileTypeCode;
        @JsonProperty
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/New_York")
        private Date fileCreationDate;
        @JsonProperty
        private Long recordCount;

        public Long getRecordCount() {
            return recordCount;
        }

        public void setRecordCount(Long recordCount) {
            this.recordCount = recordCount;
        }

        public AmexApplication withRecordCount(Long recordCount) {
            this.recordCount = recordCount;
            return this;
        }

        public AmexApplication withApplicationSytemCode(String applicationSytemCode) {
            this.applicationSytemCode = applicationSytemCode;
            return this;
        }

        public AmexApplication withFileTypeCode(String fileTypeCode) {
            this.fileTypeCode = fileTypeCode;
            return this;
        }

        public AmexApplication withFileCreationDate(Date fileCreationDate) {
            this.fileCreationDate = fileCreationDate;
            return this;
        }

        public AmexApplication() {
        }

        public String getApplicationSytemCode() {
            return applicationSytemCode;
        }

        public void setApplicationSytemCode(String applicationSytemCode) {
            this.applicationSytemCode = applicationSytemCode;
        }

        public String getFileTypeCode() {
            return fileTypeCode;
        }

        public void setFileTypeCode(String fileTypeCode) {
            this.fileTypeCode = fileTypeCode;
        }

        public Date getFileCreationDate() {
            return fileCreationDate;
        }

        public void setFileCreationDate(Date fileCreationDate) {
            this.fileCreationDate = fileCreationDate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            AmexApplication that = (AmexApplication) o;

            return new EqualsBuilder().append(getApplicationSytemCode(), that.getApplicationSytemCode())
                    .append(getFileTypeCode(), that.getFileTypeCode())
                    .append(getFileCreationDate(), that.getFileCreationDate()).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(getApplicationSytemCode()).append(getFileTypeCode())
                    .append(getFileCreationDate()).toHashCode();
        }

        @Override
        public String toString() {
            try {
                return mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return super.toString();
        }
    }
}

package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dashirov on 5/12/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "dataFileTrailerRecordType",
        "amexApplication",
        "serviceAccessId",
        "dataType",
        "originalFileTransmissionDateTime"
})
public class DataFileTrailer {
    public final static Pattern pattern = Pattern.compile("^(?<dataFileTrailerRecordType>T)(?<amexApplication>(?<applicationSytemCode>\\p{ASCII}{2})(?<fileTypeCode>\\p{ASCII}{2})(?<fileCreationDate>\\p{ASCII}{8})(?<filler2dot1>\\p{Blank}{6})(?<filler2dot2>0{2})(?<filler2dot3>0{5})(?<recordCount>\\p{Digit}{9})(?<recordCountConfirmation>\\p{Digit}{9})(?<filler2dot4>\\p{Space}{5})(?<filler2dot5>\\p{Space}{52}))(?<serviceAccessId>\\p{ASCII}{6})(?<dataType>CBNOT)(?<originalFileTransmissionDateTime>(?<originalFileTransmissionDate>\\p{Digit}{7})0(?<originalFileTransmissionTime>\\p{Digit}{6}))(?<fileSequenceNumber>\\p{Digit}{3})(?<filler7>\\p{Blank}{2073})$");
    private final static DateFormat headerDateTime = new SimpleDateFormat("yyyyDDDHHmmss");

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final DateFormat julianDate = new SimpleDateFormat("yyyyDDD");

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

    public static DataFileTrailer parse(String lineOfText) throws java.text.ParseException {
        Matcher m = pattern.matcher(lineOfText);
        if (m.matches()) {
            return new DataFileTrailer()
                    .withAmexApplication(
                            new AmexApplication()
                                    .withApplicationSytemCode(m.group("applicationSytemCode"))
                                    .withFileCreationDate(AmexApplication.fileCreationDateFormat.parse(m.group("fileCreationDate")))
                                    .withFileTypeCode(m.group("fileTypeCode"))
                                    .withRecordCount(Long.valueOf(m.group("recordCount")))
                    )
                    .withDataFileRecordType(m.group("dataFileTrailerRecordType"))
                    .withDataType(m.group("dataType"))
                    .withServiceAccessId(m.group("serviceAccessId"))
                    .withOriginalFileTransmissionDateTime(headerDateTime.parse(m.group("originalFileTransmissionDate") + m.group("originalFileTransmissionTime")));
        }
        return null;
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

        return new EqualsBuilder()
                .append(getDataFileTrailerRecordType(), that.getDataFileTrailerRecordType())
                .append(getAmexApplication(), that.getAmexApplication())
                .append(getServiceAccessId(), that.getServiceAccessId())
                .append(getDataType(), that.getDataType())
                .append(getOriginalFileTransmissionDateTime(), that.getOriginalFileTransmissionDateTime())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getDataFileTrailerRecordType())
                .append(getAmexApplication())
                .append(getServiceAccessId())
                .append(getDataType())
                .append(getOriginalFileTransmissionDateTime())
                .toHashCode();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "applicationSytemCode",
            "fileTypeCode",
            "fileCreationDate",
            "recordCount"
    })
    static class AmexApplication {
        private static final DateFormat fileCreationDateFormat = new SimpleDateFormat("yyyyMMdd");
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

            return new EqualsBuilder()
                    .append(getApplicationSytemCode(), that.getApplicationSytemCode())
                    .append(getFileTypeCode(), that.getFileTypeCode())
                    .append(getFileCreationDate(), that.getFileCreationDate())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(getApplicationSytemCode())
                    .append(getFileTypeCode())
                    .append(getFileCreationDate())
                    .toHashCode();
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

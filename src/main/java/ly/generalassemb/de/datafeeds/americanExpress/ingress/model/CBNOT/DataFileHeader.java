package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dashirov on 5/12/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "dataFileHeaderRecordType",
        "amexApplication",
        "serviceAccessId",
        "dataType",
        "originalFileTransmissionDateTime"
})
public class DataFileHeader {
    public final static Pattern pattern = Pattern.compile("^(?<dataFileHeaderRecordType>H)(?<amexApplication>(?<applicationSytemCode>\\p{ASCII}{2})(?<fileTypeCode>\\p{ASCII}{2})(?<fileCreationDate>\\p{ASCII}{8})(?<filler2>\\p{Blank}{88}))(?<serviceAccessId>\\p{ASCII}{6})(?<dataType>CBNOT)(?<originalFileTransmissionDateTime>(?<originalFileTransmissionDate>\\p{Digit}{7})0(?<originalFileTransmissionTime>\\p{Digit}{6}))(?<filler7>\\p{Blank}{2076})$");
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

    private String dataFileHeaderRecordType;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSZ", timezone = "America/New_York")
    private Date originalFileTransmissionDateTime;

    public DataFileHeader withDataFileRecordType(String dataFileHeaderRecordType) {
        this.dataFileHeaderRecordType = dataFileHeaderRecordType;
        return this;
    }

    public DataFileHeader withAmexApplication(AmexApplication amexApplication) {
        this.amexApplication = amexApplication;
        return this;
    }

    public DataFileHeader withServiceAccessId(String serviceAccessId) {
        this.serviceAccessId = serviceAccessId;
        return this;
    }

    public DataFileHeader withDataType(String dataType) {
        this.dataType = dataType;
        return this;

    }

    public DataFileHeader withOriginalFileTransmissionDateTime(Date originalFileTransmissionDateTime) {
        this.originalFileTransmissionDateTime = originalFileTransmissionDateTime;
        return this;
    }

    public String getDataFileHeaderRecordType() {
        return dataFileHeaderRecordType;
    }

    public void setDataFileHeaderRecordType(String dataFileHeaderRecordType) {
        this.dataFileHeaderRecordType = dataFileHeaderRecordType;
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

    public static DataFileHeader parse(String lineOfText) throws java.text.ParseException {
        Matcher m = pattern.matcher(lineOfText);
        if (m.matches()) {
            return new DataFileHeader()
                    .withAmexApplication(
                            new AmexApplication()
                                    .withApplicationSytemCode(m.group("applicationSytemCode"))
                                    .withFileCreationDate(AmexApplication.fileCreationDateFormat.parse(m.group("fileCreationDate")))
                                    .withFileTypeCode(m.group("fileTypeCode"))
                    )
                    .withDataFileRecordType(m.group("dataFileHeaderRecordType"))
                    .withDataType(m.group("dataType"))
                    .withServiceAccessId(m.group("serviceAccessId"))
                    .withOriginalFileTransmissionDateTime(headerDateTime.parse(m.group("originalFileTransmissionDate") + m.group("originalFileTransmissionTime")));
        } else {
            return null;
        }
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

        DataFileHeader that = (DataFileHeader) o;

        return new EqualsBuilder()
                .append(getDataFileHeaderRecordType(), that.getDataFileHeaderRecordType())
                .append(getAmexApplication(), that.getAmexApplication())
                .append(getServiceAccessId(), that.getServiceAccessId())
                .append(getDataType(), that.getDataType())
                .append(getOriginalFileTransmissionDateTime(), that.getOriginalFileTransmissionDateTime())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getDataFileHeaderRecordType())
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
            "fileCreationDate"
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

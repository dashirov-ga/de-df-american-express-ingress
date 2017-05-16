package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dashirov on 5/12/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "DATAFILE_HEADER_RECORD_TYPE",
        "DATAFILE_HEADER_DATETIME",
        "DATAFILE_HEADER_FILE_ID",
        "DATAFILE_HEADER_FILE_NAME"
})
public class DataFileHeader {
    public final static Pattern pattern = Pattern.compile("^(?<dataFileHeaderRecordType>[\\p{Upper}\\p{Digit}]{5})(?<dataFileHeaderDate>\\p{Digit}{8})(?<dataFileHeaderTime>\\p{Digit}{4})(?<dataFileHeaderFileID>\\p{Digit}{6})(?<dataFileHeaderFileName>[\\p{Alnum}\\p{Space}]{1,20})[\\p{Space}]{0,407}");
    private final static DateFormat headerDateTime = new SimpleDateFormat("MMddyyyyHHmm");
    @JsonProperty("DATAFILE_HEADER_RECORD_TYPE")
    @Size(max = 5)
    @NotNull
    private String dataFileHeaderRecordType;

    @JsonProperty("DATAFILE_HEADER_DATETIME")
    @NotNull
    private Date dataFileHeaderDateTime;

    @JsonProperty("DATAFILE_HEADER_FILE_ID")
    @Size(max = 6)
    @NotNull
    private Long dataFileHeaderFileID;

    @JsonProperty("DATAFILE_HEADER_FILE_NAME")
    @Size(max = 20)
    @NotNull
    private String  dataFileHeaderFileName;

    public DataFileHeader() {
    }

    public String getDataFileHeaderRecordType() {
        return dataFileHeaderRecordType;
    }

    public void setDataFileHeaderRecordType(String dataFileHeaderRecordType) {
        this.dataFileHeaderRecordType = dataFileHeaderRecordType;
    }

    public Date getDataFileHeaderDateTime() {
        return dataFileHeaderDateTime;
    }

    public void setDataFileHeaderDateTime(Date dataFileHeaderDateTime) {
        this.dataFileHeaderDateTime = dataFileHeaderDateTime;
    }

    public Long getDataFileHeaderFileID() {
        return dataFileHeaderFileID;
    }

    public void setDataFileHeaderFileID(Long dataFileHeaderFileID) {
        this.dataFileHeaderFileID = dataFileHeaderFileID;
    }

    public String getDataFileHeaderFileName() {
        return dataFileHeaderFileName;
    }

    public void setDataFileHeaderFileName(String dataFileHeaderFileName) {
        this.dataFileHeaderFileName = dataFileHeaderFileName;
    }

    public DataFileHeader withDataFileHeaderRecordType(String dataFileHeaderRecordType) {
        this.dataFileHeaderRecordType = dataFileHeaderRecordType;
        return this;
    }

    public DataFileHeader withDataFileHeaderDateTime(Date dataFileHeaderDateTime) {
        this.dataFileHeaderDateTime = dataFileHeaderDateTime;
        return this;
    }

    public DataFileHeader withDataFileHeaderFileID(Long dataFileHeaderFileID) {
        this.dataFileHeaderFileID = dataFileHeaderFileID;
        return this;
    }

    public DataFileHeader withDataFileHeaderFileName(String dataFileHeaderFileName) {
        this.dataFileHeaderFileName = dataFileHeaderFileName;
        return this;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
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
                .append(getDataFileHeaderDateTime(), that.getDataFileHeaderDateTime())
                .append(getDataFileHeaderFileID(), that.getDataFileHeaderFileID())
                .append(getDataFileHeaderFileName(), that.getDataFileHeaderFileName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getDataFileHeaderRecordType())
                .append(getDataFileHeaderDateTime())
                .append(getDataFileHeaderFileID())
                .append(getDataFileHeaderFileName())
                .toHashCode();
    }

    public static DataFileHeader parse(String lineOfText) throws java.text.ParseException {
        Matcher m = pattern.matcher(lineOfText);
        if (m.matches()) {
            return new DataFileHeader()
                    .withDataFileHeaderRecordType(m.group("dataFileHeaderRecordType"))
                    .withDataFileHeaderDateTime(headerDateTime.parse(
                            m.group("dataFileHeaderDate") +
                                    m.group("dataFileHeaderTime")))
                    .withDataFileHeaderFileID(Long.valueOf(m.group("dataFileHeaderFileID")))
                    .withDataFileHeaderFileName(m.group("dataFileHeaderFileName"));
        }
        return null;
    }
}

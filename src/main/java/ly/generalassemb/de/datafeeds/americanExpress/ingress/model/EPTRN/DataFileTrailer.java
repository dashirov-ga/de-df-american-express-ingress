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
        "DATAFILE_TRAILER_RECORD_TYPE",
        "DATAFILE_TRAILER_DATETIME",
        "DATAFILE_TRAILER_FILE_ID",
        "DATAFILE_TRAILER_FILE_NAME",
        "DATAFILE_TRAILER_RECIPIENT_KEY",
        "DATAFILE_TRAILER_RECORD_COUNT"
})
public class DataFileTrailer  {
    public final static Pattern pattern = Pattern.compile("^(?<dataFileTrailerRecordType>[\\p{Upper}\\p{Digit}]{5})(?<dataFileTrailerDate>\\p{Digit}{8})(?<dataFileTrailerTime>\\p{Digit}{4})(?<dataFileTrailerFileID>\\p{Digit}{6})(?<dataFileTrailerFileName>[\\p{Alnum}\\p{Space}]{20})(?<dataFileTrailerRecipientKey>[\\p{Digit}\\p{Space}]{40})(?<dataFileTrailerRecordCount>\\p{Digit}{7})\\p{Space}{0,360}");
    private final static DateFormat trailerDateTime = new SimpleDateFormat("MMddyyyyHHmm");
    /**
     * dataFileTrailerRecordType
     *   This field contains the constant literal “DFTRL”, a Record Type code that indicates that
     *   this is a Data File Trailer Record.
     */
    @JsonProperty("DATAFILE_TRAILER_RECORD_TYPE")
    @Size(max = 5)
    @NotNull
    private String dataFileTrailerRecordType;

    /**
     *  dataFileTrailerDateTime
     *     This model field is the result of combining underlying feed fields dataFileTrailerDate and dataFileTrailerTime
     *     into a single Date field with both date and time components, TimeZone has not yet been resolved
     *  dataFileTrailerDate
     *     This field contains the File Creation Date.
     *   dataFileTrailerTime
     *     This field contains the File Creation Time (24-hour format), when the file was created.
     */
    @JsonProperty("DATAFILE_TRAILER_DATETIME")
    @NotNull
    private Date dataFileTrailerDateTime;

    /**
     *  dataFileTrailerFileID
     *    This field contains an American Express, system-generated, File ID number that uniquely
     *   identifies this data file.
     */
    @JsonProperty("DATAFILE_TRAILER_FILE_ID")
    @Size(max = 6)
    @NotNull
    private Long dataFileTrailerFileID;

    /**
     *  dataFileTrailerFileName
     *    This field contains the File Name (as entered in the American Express data distribution
     *    database) that corresponds to Field 4, dataFileTrailerFileID.
     */
    @JsonProperty("DATAFILE_TRAILER_FILE_NAME")
    @Size(max = 20)
    @NotNull
    private String  dataFileTrailerFileName;

    /**
     *  dataFileTrailerRecipientKey
     *    This field contains the Recipient Key, a unique, AmericanExpress, system-generated number
     *    that identifies this data file.
     *    Note: This number is unique to each individual file.
     */
    @JsonProperty("DATAFILE_TRAILER_RECIPIENT_KEY")
    @Size(max = 40)
    @NotNull
    private String  dataFileTrailerRecipientKey;

    /**
     * dataFileTrailerRecordCount
     *   This field contains the Record Count for all items in this datafile, including the header
     *   and trailer records.
     */
    @JsonProperty("DATAFILE_TRAILER_RECORD_COUNT")
    @Size(max = 7)
    @NotNull
    private Long dataFileTrailerRecordCount;

    /**
     * No-arg constructor
     */
    public DataFileTrailer() {
    }

    public String getDataFileTrailerRecordType() {
        return dataFileTrailerRecordType;
    }

    public void setDataFileTrailerRecordType(String dataFileTrailerRecordType) {
        this.dataFileTrailerRecordType = dataFileTrailerRecordType;
    }

    public Date getDataFileTrailerDateTime() {
        return dataFileTrailerDateTime;
    }

    public void setDataFileTrailerDateTime(Date dataFileTrailerDateTime) {
        this.dataFileTrailerDateTime = dataFileTrailerDateTime;
    }

    public Long getDataFileTrailerFileID() {
        return dataFileTrailerFileID;
    }

    public void setDataFileTrailerFileID(Long dataFileTrailerFileID) {
        this.dataFileTrailerFileID = dataFileTrailerFileID;
    }

    public String getDataFileTrailerFileName() {
        return dataFileTrailerFileName;
    }

    public void setDataFileTrailerFileName(String dataFileTrailerFileName) {
        this.dataFileTrailerFileName = dataFileTrailerFileName;
    }

    public String getDataFileTrailerRecipientKey() {
        return dataFileTrailerRecipientKey;
    }

    public void setDataFileTrailerRecipientKey(String dataFileTrailerRecipientKey) {
        this.dataFileTrailerRecipientKey = dataFileTrailerRecipientKey;
    }

    public Long getDataFileTrailerRecordCount() {
        return dataFileTrailerRecordCount;
    }

    public void setDataFileTrailerRecordCount(Long dataFileTrailerRecordCount) {
        this.dataFileTrailerRecordCount = dataFileTrailerRecordCount;
    }

    public DataFileTrailer withDataFileTrailerRecordType(String dataFileTrailerRecordType) {
        this.dataFileTrailerRecordType = dataFileTrailerRecordType;
        return this;
    }

    public DataFileTrailer withDataFileTrailerDateTime(Date dataFileTrailerDateTime) {
        this.dataFileTrailerDateTime = dataFileTrailerDateTime;
        return this;
    }

    public DataFileTrailer withDataFileTrailerFileID(Long dataFileTrailerFileID) {
        this.dataFileTrailerFileID = dataFileTrailerFileID;
        return this;
    }

    public DataFileTrailer withDataFileTrailerFileName(String dataFileTrailerFileName) {
        this.dataFileTrailerFileName = dataFileTrailerFileName;
        return this;
    }

    public DataFileTrailer withDataFileTrailerRecordCount(Long dataFileTrailerRecordCount) {
        this.dataFileTrailerRecordCount = dataFileTrailerRecordCount;
        return this;
    }

    public DataFileTrailer withDataFileTrailerRecipientKey(String dataFileTrailerRecipientKey) {
        this.dataFileTrailerRecipientKey = dataFileTrailerRecipientKey;
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

        DataFileTrailer that = (DataFileTrailer) o;

        return new EqualsBuilder()
                .append(getDataFileTrailerRecordType(), that.getDataFileTrailerRecordType())
                .append(getDataFileTrailerDateTime(), that.getDataFileTrailerDateTime())
                .append(getDataFileTrailerFileID(), that.getDataFileTrailerFileID())
                .append(getDataFileTrailerFileName(), that.getDataFileTrailerFileName())
                .append(getDataFileTrailerRecipientKey(), that.getDataFileTrailerRecipientKey())
                .append(getDataFileTrailerRecordCount(),that.getDataFileTrailerRecordCount())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getDataFileTrailerRecordType())
                .append(getDataFileTrailerDateTime())
                .append(getDataFileTrailerFileID())
                .append(getDataFileTrailerFileName())
                .append(getDataFileTrailerRecipientKey())
                .append(getDataFileTrailerRecordCount())
                .toHashCode();
    }

    public static DataFileTrailer parse(String lineOfText) throws java.text.ParseException {
        Matcher m = pattern.matcher(lineOfText);
        if (m.matches()) {
            return new DataFileTrailer()
                    .withDataFileTrailerRecordType(m.group("dataFileTrailerRecordType"))
                    .withDataFileTrailerDateTime(trailerDateTime.parse(
                            m.group("dataFileTrailerDate") +
                                    m.group("dataFileTrailerTime")))
                    .withDataFileTrailerFileID(Long.valueOf(m.group("dataFileTrailerFileID")))
                    .withDataFileTrailerFileName(m.group("dataFileTrailerFileName"))
                    .withDataFileTrailerRecipientKey(m.group("dataFileTrailerRecipientKey"))
                    .withDataFileTrailerRecordCount(Long.valueOf(m.group("dataFileTrailerRecordCount")));
        }
        return null;
    }


}

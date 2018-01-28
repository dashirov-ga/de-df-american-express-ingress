package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.model.EPTRN;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.AmexRecorType;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.AmexFeedLineParserOutput;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by dashirov on 5/12/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "DATAFILE_HEADER_RECORD_TYPE", "DATAFILE_HEADER_DATETIME", "DATAFILE_HEADER_FILE_ID",
                     "DATAFILE_HEADER_FILE_NAME" })
public class DataFileHeader implements AmexFeedLineParserOutput {

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
    private String dataFileHeaderFileName;

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

        return new EqualsBuilder().append(getDataFileHeaderRecordType(), that.getDataFileHeaderRecordType())
                .append(getDataFileHeaderDateTime(), that.getDataFileHeaderDateTime())
                .append(getDataFileHeaderFileID(), that.getDataFileHeaderFileID())
                .append(getDataFileHeaderFileName(), that.getDataFileHeaderFileName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getDataFileHeaderRecordType())
                .append(getDataFileHeaderDateTime()).append(getDataFileHeaderFileID())
                .append(getDataFileHeaderFileName()).toHashCode();
    }

    @Override
    public AmexRecorType getAmexRecordType() {
        return AmexRecorType.EPTRN_HEADER;
    }
}

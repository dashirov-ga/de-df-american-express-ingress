package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;
import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Record(length = 2202)
@JsonPropertyOrder({
        "REC_TYPE",
        "SAID",
        "DATAT_YPE",
        "FILE_DATE"

})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Trailer {

    @JsonProperty("REC_TYPE")
    @NotNull
    private String recordType;
    @Field(offset = 1, length = 1)
    public String getRecordType() {
        return recordType;
    }
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }


    @JsonIgnore
    private String amexApplicationArea;
    @Field(offset = 2, length = 100)
    public String getAmexApplicationArea() {
        return amexApplicationArea;
    }
    public void setAmexApplicationArea(String amexApplicationArea) {
        this.amexApplicationArea = amexApplicationArea;
    }

    @JsonProperty("SAID")
    @NotNull
    private String serviceAccessId;
    @Field(offset = 102, length = 6)
    public String getServiceAccessId() {
        return serviceAccessId;
    }
    public void setServiceAccessId(String serviceAccessId) {
        this.serviceAccessId = serviceAccessId;
    }

    @JsonProperty("DATA_TYPE")
    @NotNull
    private String dataType;
    @Field(offset = 108, length=5)
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }


    @JsonProperty("FILE_DATE")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fileCreationDate;
    @Field(offset = 113, length = 14)
    @FixedFormatPattern("yyyyDDDHHHmmss")
    public Date getFileCreationDate() {
        return fileCreationDate;
    }
    public void setFileCreationDate(Date fileCreationDate) {
        this.fileCreationDate = fileCreationDate;
    }


    @JsonProperty("STARS_FILESEQ_NB")
    @NotNull
    private Integer fileSequenceNumber;
    @Field(offset = 127, length = 3, align = Align.RIGHT, paddingChar = '0')
    public Integer getFileSequenceNumber() {
        return fileSequenceNumber;
    }
    public void setFileSequenceNumber(Integer fileSequenceNumber) {
        this.fileSequenceNumber = fileSequenceNumber;
    }
    public Trailer parse(FixedFormatManager manager, String line){
        return manager.load(Trailer.class,line);
    }
}

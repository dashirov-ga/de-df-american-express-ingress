package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN;

import com.ancientprogramming.fixedformat4j.annotation.*;
import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.LocalDateTimeFormatter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Record(length = 450)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "DF_HDR_RECORD_TYPE",
        "DF_HDR_DATE",
        "DF_HDR_TIME",
        "DF_HDR_FILE_ID",
        "DF_HDR_FILE_NAME"
})
public class Header {
    @NotNull
    @JsonProperty("DF_HDR_RECORD_TYPE")
    private String recordType;
    @Field(offset=1,length=5,align= Align.LEFT,paddingChar = ' ')        //  getRecordType()
    public String getRecordType() {
        return recordType;
    }
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @NotNull
    @JsonProperty("DF_HDR_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/New_York")
    private LocalDateTime date;
    @FixedFormatPattern("MMddyyyyHHmm")
    @Field(offset=6,length=12,align=Align.LEFT,paddingChar = '0',formatter = LocalDateTimeFormatter.class)        //  getDate()
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @NotNull
    @JsonProperty("DF_HDR_FILE_ID")
    private BigDecimal fileId;
    @Field(offset=18,length=6,align=Align.RIGHT,paddingChar = '0')        //  getFileId()
    @FixedFormatDecimal(decimals = 0)
    public BigDecimal getFileId() {
        return fileId;
    }
    public void setFileId(BigDecimal fileId) {
        this.fileId = fileId;
    }

    @NotNull
    @JsonProperty("DF_HDR_FILE_NAME")
    private String fileName;
    @Field(offset=24,length=20,align=Align.LEFT,paddingChar = ' ')        //  getFileName()
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Header parse(FixedFormatManager manager, String line){
        return manager.load(Header.class,line);
    }
}

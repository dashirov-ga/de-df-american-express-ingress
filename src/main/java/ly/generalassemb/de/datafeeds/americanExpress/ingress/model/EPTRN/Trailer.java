package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN;

import com.ancientprogramming.fixedformat4j.annotation.*;
import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Record(length = 450)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "DF_TRL_RECORD_TYPE",
        "DF_TRL_DATE",
        "DF_TRL_TIME",
        "DF_TRL_FILE_ID",
        "DF_TRL_FILE_NAME",
        "DF_TRL_RECIPIENT_KEY",
        "DF_TRL_RECORD_COUNT"
})
public class Trailer {
    @JsonProperty("DF_TRL_RECORD_TYPE")
    @NotNull
    private String recordType;
    @Field(offset=1,length=5,align= Align.LEFT,paddingChar = ' ')        //  getRecordType()
    public String getRecordType() {
        return recordType;
    }
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @JsonProperty("DF_TRL_DATE")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/New_York")
    private Date date;
    @FixedFormatPattern("MMddyyyyHHmm")
    @Field(offset=6,length=12,align=Align.RIGHT,paddingChar = '0')        //  getDate()
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    @JsonProperty("DF_TRL_FILE_ID")
    @NotNull
    private BigDecimal fileId;
    @Field(offset=18,length=6,align=Align.RIGHT,paddingChar = '0')        //  getFileId()
    @FixedFormatNumber(sign = Sign.NOSIGN)
    @FixedFormatDecimal(decimals = 0)
    public BigDecimal getFileId() {
        return fileId;
    }
    public void setFileId(BigDecimal fileId) {
        this.fileId = fileId;
    }

    @JsonProperty("DF_TRL_FILE_NAME")
    @NotNull
    private String fileName;
    @Field(offset=24,length=20,align=Align.LEFT,paddingChar = ' ')        //  getFileName()
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonProperty("DF_TRL_RECIPIENT_KEY")
    @NotNull
    private String recipientKey;
    @Field(offset=44,length=40,align=Align.LEFT,paddingChar = ' ')        //  getRecipientKey()
    public String getRecipientKey() {
        return recipientKey;
    }

    public void setRecipientKey(String recipientKey) {
        this.recipientKey = recipientKey;
    }

    @JsonProperty("DF_TRL_RECORD_COUNT")
    @NotNull
    private Integer recordCount;
    @Field(offset=84,length=7,align=Align.RIGHT,paddingChar = '0')        //  getRecordCount()
    @FixedFormatNumber(sign = Sign.NOSIGN)
    @FixedFormatDecimal(decimals = 0, useDecimalDelimiter = false)
    public Integer getRecordCount() {
        return recordCount;
    }
    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    public Trailer parse(FixedFormatManager manager, String line){
        return manager.load(Trailer.class,line);
    }
}

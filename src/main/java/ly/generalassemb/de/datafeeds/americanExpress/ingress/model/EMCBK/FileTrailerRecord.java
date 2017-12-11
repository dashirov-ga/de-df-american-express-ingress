package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EMCBK;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by davidashirov on 12/10/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "RECORD_TYPE",
        "APPLICATION_SYSTEM_CODE",
        "FILE_TYPE_CODE",
        "FILE_CREATION_DATE",
        "DATABASE_SEQUENCE_NUMBER",
        "AMEX_TOTAL_RECORDS",
        "SERVICE_ESTABLISHMENT_TOTAL_RECORDS",
        "SERVICE_ACCESS_ID",
        "DATA_TYPE",
        "STARS_CREATION_DATE",
        "STARS_CREATION_TIME",
        "STARS_FILE_SEQUENCE_NUMBER"
})
@Record
public class FileTrailerRecord {
    @JsonProperty("RECORD_TYPE")
    @Size(max = 1)
    @NotNull
    private String recordType;
    @Field(offset=1,length=1,align= Align.LEFT,paddingChar = ' ')        //  getDataType
    public String getRecordType() {
        return recordType;
    }
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @JsonProperty("APPLICATION_SYSTEM_CODE")
    @Size(max = 2)
    @NotNull
    private String applicationSystemCode;
    @Field(offset=2,length=2,align=Align.LEFT,paddingChar = ' ')        //  getApplicationSystemCode
    public String getApplicationSystemCode() {
        return applicationSystemCode;
    }
    public void setApplicationSystemCode(String applicationSystemCode) {
        this.applicationSystemCode = applicationSystemCode;
    }

    @JsonProperty("FILE_TYPE_CODE")
    @Size(max = 2)
    @NotNull
    private String fileTypeCode;
    @Field(offset=4,length=2,align=Align.LEFT,paddingChar = ' ')        //  getFileTypeCode
    public String getFileTypeCode() {
        return fileTypeCode;
    }
    public void setFileTypeCode(String fileTypeCode) {
        this.fileTypeCode = fileTypeCode;
    }

    @JsonProperty("FILE_CREATION_DATE")
    @Size(max = 10)
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fileCreationDate;
    @Field(offset=6,length=8,align=Align.LEFT,paddingChar = ' ')        //  getFileCreationDate
    @FixedFormatPattern("yyyyMMdd")
    public Date getFileCreationDate() {
        return fileCreationDate;
    }
    public void setFileCreationDate(Date fileCreationDate) {
        this.fileCreationDate = fileCreationDate;
    }

    @JsonProperty("DATABASE_SEQUENCE_NUMBER")
    @Size(max = 2)
    @NotNull
    private Integer databaseSequenceNumber;
    @Field(offset=20,length=2,align=Align.RIGHT,paddingChar = '0')        //  getDatabaseSequenceNumber
    public Integer getDatabaseSequenceNumber() {
        return databaseSequenceNumber;
    }
    public void setDatabaseSequenceNumber(Integer databaseSequenceNumber) {
        this.databaseSequenceNumber = databaseSequenceNumber;
    }
    @JsonProperty("AMEX_TOTAL_RECORDS")
    @Size(max = 9)
    @NotNull
    private Integer amexTotalRecords;
    @Field(offset=27,length=9,align=Align.LEFT,paddingChar = ' ')        //  getAmexTotalRecords
    public Integer getAmexTotalRecords() {
        return amexTotalRecords;
    }
    public void setAmexTotalRecords(Integer amexTotalRecords) {
        this.amexTotalRecords = amexTotalRecords;
    }

    @JsonProperty("SERVICE_ESTABLISHMENT_TOTAL_RECORDS")
    @Size(max = 9)
    @NotNull
    private Integer serviceEstablishmentTotalRecords;
    @Field(offset=36,length=9,align=Align.LEFT,paddingChar = ' ')        //  getServiceEstablishmentTotalRecords
    public Integer getServiceEstablishmentTotalRecords() {
        return serviceEstablishmentTotalRecords;
    }
    public void setServiceEstablishmentTotalRecords(Integer serviceEstablishmentTotalRecords) {
        this.serviceEstablishmentTotalRecords = serviceEstablishmentTotalRecords;
    }

    @JsonProperty("SERVICE_ACCESS_ID")
    @Size(max = 6)
    @NotNull
    private String serviceAccessId;
    @Field(offset=102,length=6,align=Align.LEFT,paddingChar = ' ')        //  getServiceAccessId
    public String getServiceAccessId() {
        return serviceAccessId;
    }
    public void setServiceAccessId(String serviceAccessId) {
        this.serviceAccessId = serviceAccessId;
    }

    @JsonProperty("DATA_TYPE")
    @Size(max = 5)
    @NotNull
    private String dataType;
    @Field(offset=108,length=5,align=Align.LEFT,paddingChar = ' ')        //  getDataType
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @JsonProperty("STARS_CREATION_DATE")
    @Size(max = 10)
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date starsCreationDate;
    @Field(offset=113,length=7,align=Align.LEFT,paddingChar = ' ')        //  getStarsCreationDate
    @FixedFormatPattern("yyyyDDD") // Julian Date !
    public Date getStarsCreationDate() {
        return starsCreationDate;
    }
    public void setStarsCreationDate(Date starsCreationDate) {
        this.starsCreationDate = starsCreationDate;
    }

    @JsonProperty("STARS_CREATION_TIME")
    @Size(max = 6)
    @NotNull
    private String starsCreationTime;
    @Field(offset=121,length=6,align=Align.LEFT,paddingChar = ' ')        //  getStarsCreationTime
    public String getStarsCreationTime() {
        return starsCreationTime;
    }
    public void setStarsCreationTime(String starsCreationTime) {
        this.starsCreationTime = starsCreationTime;
    }

    @JsonProperty("STARS_FILE_SEQUENCE_NUMBER")
    @Size(max = 3)
    @NotNull
    private String starsFileSequenceNumber;
    @Field(offset=127,length=3,align=Align.LEFT,paddingChar = ' ')        //  getStarsFileSequenceNumber
    public String getStarsFileSequenceNumber() {
        return starsFileSequenceNumber;
    }
    public void setStarsFileSequenceNumber(String starsFileSequenceNumber) {
        this.starsFileSequenceNumber = starsFileSequenceNumber;
    }
}
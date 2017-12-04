package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Created by davidashirov on 12/4/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "RECORD_TYPE",
        "DATE",
        "TIME",
        "ID",
        "NAME",
        "RECIPIENT_KEY",
        "RECORD_COUNT"

})
@Record
public class FileTrailerRecord {
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final CsvMapper csvMapper = new CsvMapper();

    @JsonProperty("RECORD_TYPE")
    @Size(max = 6)
    @javax.validation.constraints.NotNull
    private String recordType;

    @Field(offset=1,length=6,align= Align.LEFT,paddingChar = ' ')        //  getRecordType
    public String getRecordType() {
        return recordType;
    }
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @JsonProperty("DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Size(max = 8)
    @javax.validation.constraints.NotNull
    private Date date;

    @FixedFormatPattern("yyyyMMdd")
    @Field(offset=7,length=8,align=Align.LEFT,paddingChar = ' ')        //  getDate
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @JsonProperty("TIME")
    @Size(max = 4)
    @javax.validation.constraints.NotNull
    private String time;

    @Field(offset=15,length=4,align=Align.LEFT,paddingChar = ' ')        //  getTime
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    @JsonProperty("ID")
    @Size(max = 6)
    @javax.validation.constraints.NotNull
    private String id;

    @Field(offset=19,length=6,align=Align.LEFT,paddingChar = ' ')        //  getId
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("NAME")
    @Size(max = 19)
    @javax.validation.constraints.NotNull
    private String name;

    @Field(offset=25,length=19,align=Align.LEFT,paddingChar = ' ')        //  getName
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("RECIPIENT_KEY")
    @Size(max = 40)
    @javax.validation.constraints.NotNull
    private String recipientKey;

    @Field(offset=45,length=40,align=Align.LEFT,paddingChar = ' ')        //  getRecipientKey
    public String getRecipientKey() {
        return recipientKey;
    }

    public void setRecipientKey(String recipientKey) {
        this.recipientKey = recipientKey;
    }

    @JsonProperty("RECORD_COUNT")
    @Size(max = 7)
    @javax.validation.constraints.NotNull
    private Integer recordCount;

    @Field(offset=85,length=7,align=Align.RIGHT,paddingChar = '0')        //  getRecordCount
    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    @Override
    public String toString() {
        try {
            return jsonMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ((Object)this).toString();
        }
    }

    public String toCsv() {
        CsvSchema schema = csvMapper.schemaFor(FileTrailerRecord.class).withColumnSeparator(',').withHeader();
        ObjectWriter myObjectWriter = csvMapper.writer(schema);
        try {
            return myObjectWriter.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toCsv(List<FileTrailerRecord> list) {
        CsvSchema schema = FileTrailerRecord.csvMapper.schemaFor(FileTrailerRecord.class).withColumnSeparator(',').withHeader();
        ObjectWriter myObjectWriter = csvMapper.writer(schema);
        try {
            return myObjectWriter.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


}

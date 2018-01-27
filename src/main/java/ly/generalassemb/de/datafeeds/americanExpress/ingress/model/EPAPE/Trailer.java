package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;
import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.LocalDateTimeFormatter;
import java.time.LocalDateTime;

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
@Record(length = 440)
public class Trailer {
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final CsvMapper csvMapper = new CsvMapper();

    @JsonProperty("RECORD_TYPE")
    @javax.validation.constraints.NotNull
    private String recordType;

    @Field(offset=1,length=6,align= Align.LEFT)        //  getRecordType
    public String getRecordType() {
        return recordType;
    }
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @JsonProperty("DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @javax.validation.constraints.NotNull
    private LocalDateTime date;
    @FixedFormatPattern("yyyyMMddHHmm")
    @Field(offset=7,length=12,align=Align.LEFT, formatter = LocalDateTimeFormatter.class)        //  getDate
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }


    @JsonProperty("ID")
    @javax.validation.constraints.NotNull
    private String id;

    @Field(offset=19,length=6,align=Align.LEFT)        //  getId
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("NAME")
    @javax.validation.constraints.NotNull
    private String name;

    @Field(offset=25,length=19,align=Align.LEFT)        //  getName
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("RECIPIENT_KEY")
    @javax.validation.constraints.NotNull
    private String recipientKey;

    @Field(offset=45,length=40,align=Align.LEFT)        //  getRecipientKey
    public String getRecipientKey() {
        return recipientKey;
    }

    public void setRecipientKey(String recipientKey) {
        this.recipientKey = recipientKey;
    }

    @JsonProperty("RECORD_COUNT")
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
        CsvSchema schema = csvMapper.schemaFor(Trailer.class).withColumnSeparator(',').withHeader();
        ObjectWriter myObjectWriter = csvMapper.writer(schema);
        try {
            return myObjectWriter.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Trailer parse(FixedFormatManager manager, String line){
        return manager.load(Trailer.class,line);
    }


    public static final class Builder {
        private String recordType;
        private LocalDateTime date;
        private String id;
        private String name;
        private String recipientKey;
        private Integer recordCount;

        private Builder() {
        }

        public static Builder aFileTrailerRecord() {
            return new Builder();
        }

        public Builder withRecordType(String recordType) {
            this.recordType = recordType;
            return this;
        }

        public Builder withDate(LocalDateTime date) {
            this.date = date;
            return this;
        }



        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withRecipientKey(String recipientKey) {
            this.recipientKey = recipientKey;
            return this;
        }

        public Builder withRecordCount(Integer recordCount) {
            this.recordCount = recordCount;
            return this;
        }

        public Builder but() {
            return aFileTrailerRecord().withRecordType(recordType).withDate(date).withId(id).withName(name).withRecipientKey(recipientKey).withRecordCount(recordCount);
        }

        public Trailer build() {
            Trailer fileTrailerRecord = new Trailer();
            fileTrailerRecord.setRecordType(recordType);
            fileTrailerRecord.setDate(date);
            fileTrailerRecord.setId(id);
            fileTrailerRecord.setName(name);
            fileTrailerRecord.setRecipientKey(recipientKey);
            fileTrailerRecord.setRecordCount(recordCount);
            return fileTrailerRecord;
        }
    }
}

package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import java.util.Date;

/**
 * Created by davidashirov on 12/4/17.
 */
public final class FileHeaderRecordBuilder {
    private String recordType;
    private Date date;
    private String time;
    private String id;
    private String name;
    private String versionControlNumber;

    private FileHeaderRecordBuilder() {
    }

    public static FileHeaderRecordBuilder aFileHeaderRecord() {
        return new FileHeaderRecordBuilder();
    }

    public FileHeaderRecordBuilder withRecordType(String recordType) {
        this.recordType = recordType;
        return this;
    }

    public FileHeaderRecordBuilder withDate(Date date) {
        this.date = date;
        return this;
    }

    public FileHeaderRecordBuilder withTime(String time) {
        this.time = time;
        return this;
    }

    public FileHeaderRecordBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public FileHeaderRecordBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public FileHeaderRecordBuilder withVersionControlNumber(String versionControlNumber) {
        this.versionControlNumber = versionControlNumber;
        return this;
    }

    public FileHeaderRecordBuilder but() {
        return aFileHeaderRecord().withRecordType(recordType).withDate(date).withTime(time).withId(id).withName(name).withVersionControlNumber(versionControlNumber);
    }

    public FileHeaderRecord build() {
        FileHeaderRecord fileHeaderRecord = new FileHeaderRecord();
        fileHeaderRecord.setRecordType(recordType);
        fileHeaderRecord.setDate(date);
        fileHeaderRecord.setTime(time);
        fileHeaderRecord.setId(id);
        fileHeaderRecord.setName(name);
        fileHeaderRecord.setVersionControlNumber(versionControlNumber);
        return fileHeaderRecord;
    }
}

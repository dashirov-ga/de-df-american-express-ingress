package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;

import java.util.Date;

/**
 * Created by davidashirov on 12/4/17.
 */
public final class FileTrailerRecordBuilder {
    private String recordType;
    private Date date;
    private String time;
    private String id;
    private String name;
    private String recipientKey;
    private Integer recordCount;

    private FileTrailerRecordBuilder() {
    }

    public static FileTrailerRecordBuilder aFileTrailerRecord() {
        return new FileTrailerRecordBuilder();
    }

    public FileTrailerRecordBuilder withRecordType(String recordType) {
        this.recordType = recordType;
        return this;
    }

    public FileTrailerRecordBuilder withDate(Date date) {
        this.date = date;
        return this;
    }

    public FileTrailerRecordBuilder withTime(String time) {
        this.time = time;
        return this;
    }

    public FileTrailerRecordBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public FileTrailerRecordBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public FileTrailerRecordBuilder withRecipientKey(String recipientKey) {
        this.recipientKey = recipientKey;
        return this;
    }

    public FileTrailerRecordBuilder withRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
        return this;
    }

    public FileTrailerRecordBuilder but() {
        return aFileTrailerRecord().withRecordType(recordType).withDate(date).withTime(time).withId(id).withName(name).withRecipientKey(recipientKey).withRecordCount(recordCount);
    }

    public FileTrailerRecord build() {
        FileTrailerRecord fileTrailerRecord = new FileTrailerRecord();
        fileTrailerRecord.setRecordType(recordType);
        fileTrailerRecord.setDate(date);
        fileTrailerRecord.setTime(time);
        fileTrailerRecord.setId(id);
        fileTrailerRecord.setName(name);
        fileTrailerRecord.setRecipientKey(recipientKey);
        fileTrailerRecord.setRecordCount(recordCount);
        return fileTrailerRecord;
    }
}

package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.model.CBNOT.DataFileHeader;

/**
 * Parser for CBNOT Header.
 */
public class CBNOTDataFileHeaderLineParser extends AbstractLineParser {

    public final static Pattern pattern = Pattern.compile(
            "^(?<dataFileHeaderRecordType>H)(?<amexApplication>(?<applicationSytemCode>\\p{ASCII}{2})(?<fileTypeCode>\\p{ASCII}{2})(?<fileCreationDate>\\p{ASCII}{8})(?<filler2>\\p{Blank}{88}))(?<serviceAccessId>\\p{ASCII}{6})(?<dataType>CBNOT)(?<originalFileTransmissionDateTime>(?<originalFileTransmissionDate>\\p{Digit}{7})0(?<originalFileTransmissionTime>\\p{Digit}{6}))(?<filler7>\\p{Blank}{2076})$");
    private final static DateFormat headerDateTime = new SimpleDateFormat("yyyyDDDHHmmss");
    private static final DateFormat julianDate = new SimpleDateFormat("yyyyDDD");

    @Override
    public AmexFeedLineParserOutput parse(String input) throws ParseException {

        Matcher m = pattern.matcher(input);
        if (m.matches()) {
            return new DataFileHeader()
                    .withAmexApplication(new DataFileHeader.AmexApplication()
                            .withApplicationSytemCode(m.group("applicationSytemCode"))
                            .withFileCreationDate(DataFileHeader.AmexApplication.fileCreationDateFormat
                                    .parse(m.group("fileCreationDate")))
                            .withFileTypeCode(m.group("fileTypeCode")))
                    .withDataFileRecordType(m.group("dataFileHeaderRecordType"))
                    .withDataType(m.group("dataType")).withServiceAccessId(m.group("serviceAccessId"))
                    .withOriginalFileTransmissionDateTime(headerDateTime.parse(
                            m.group("originalFileTransmissionDate") + m.group("originalFileTransmissionTime")));
        } else {
            return null;
        }
    }

}

package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.model.CBNOT.DataFileTrailer;

/**
 * Parser CBNOT Trailer.
 */
public class CBNOTDataFileTrailerLineParser extends AbstractLineParser {
    public final static Pattern pattern = Pattern.compile(
            "^(?<dataFileTrailerRecordType>T)(?<amexApplication>(?<applicationSytemCode>\\p{ASCII}{2})(?<fileTypeCode>\\p{ASCII}{2})(?<fileCreationDate>\\p{ASCII}{8})(?<filler2dot1>\\p{Blank}{6})(?<filler2dot2>0{2})(?<filler2dot3>0{5})(?<recordCount>\\p{Digit}{9})(?<recordCountConfirmation>\\p{Digit}{9})(?<filler2dot4>\\p{Space}{5})(?<filler2dot5>\\p{Space}{52}))(?<serviceAccessId>\\p{ASCII}{6})(?<dataType>CBNOT)(?<originalFileTransmissionDateTime>(?<originalFileTransmissionDate>\\p{Digit}{7})0(?<originalFileTransmissionTime>\\p{Digit}{6}))(?<fileSequenceNumber>\\p{Digit}{3})(?<filler7>\\p{Blank}{2073})$");
    private final static DateFormat headerDateTime = new SimpleDateFormat("yyyyDDDHHmmss");

    @Override
    public AmexFeedLineParserOutput parse(String input) throws ParseException {

        Matcher m = pattern.matcher(input);
        if (m.matches()) {
            return new DataFileTrailer()
                    .withAmexApplication(new DataFileTrailer.AmexApplication()
                            .withApplicationSytemCode(m.group("applicationSytemCode"))
                            .withFileCreationDate(DataFileTrailer.AmexApplication.fileCreationDateFormat
                                    .parse(m.group("fileCreationDate")))
                            .withFileTypeCode(m.group("fileTypeCode"))
                            .withRecordCount(Long.valueOf(m.group("recordCount"))))
                    .withDataFileRecordType(m.group("dataFileTrailerRecordType"))
                    .withDataType(m.group("dataType")).withServiceAccessId(m.group("serviceAccessId"))
                    .withOriginalFileTransmissionDateTime(headerDateTime.parse(
                            m.group("originalFileTransmissionDate") + m.group("originalFileTransmissionTime")));
        }
        return null;
    }

}

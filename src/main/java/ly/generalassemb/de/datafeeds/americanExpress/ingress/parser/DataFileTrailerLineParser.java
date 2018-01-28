package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.model.EPTRN.DataFileTrailer;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.parser is responsible for ..
 */
public class DataFileTrailerLineParser extends AbstractLineParser {

    private final static DateFormat trailerDateTime = new SimpleDateFormat("MMddyyyyHHmm");

    private final static Pattern pattern = Pattern.compile(
            "^(?<dataFileTrailerRecordType>[\\p{Upper}\\p{Digit}]{5})(?<dataFileTrailerDate>\\p{Digit}{8})(?<dataFileTrailerTime>\\p{Digit}{4})(?<dataFileTrailerFileID>\\p{Digit}{6})(?<dataFileTrailerFileName>[\\p{Alnum}\\p{Space}]{20})(?<dataFileTrailerRecipientKey>[\\p{Digit}\\p{Space}]{40})(?<dataFileTrailerRecordCount>\\p{Digit}{7})\\p{Space}{0,360}");

    @Override
    public AmexFeedLineParserOutput parse(String input) throws ParseException {

        Matcher m = pattern.matcher(input);
        if (m.matches()) {
            return new DataFileTrailer().withDataFileTrailerRecordType(m.group("dataFileTrailerRecordType"))
                    .withDataFileTrailerDateTime(trailerDateTime
                            .parse(m.group("dataFileTrailerDate") + m.group("dataFileTrailerTime")))
                    .withDataFileTrailerFileID(Long.valueOf(m.group("dataFileTrailerFileID")))
                    .withDataFileTrailerFileName(m.group("dataFileTrailerFileName"))
                    .withDataFileTrailerRecipientKey(m.group("dataFileTrailerRecipientKey"))
                    .withDataFileTrailerRecordCount(Long.valueOf(m.group("dataFileTrailerRecordCount")));
        } else {
            return null;
        }

    }
}

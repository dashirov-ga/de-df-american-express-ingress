package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.model.EPTRN.DataFileHeader;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.parser is responsible for ..
 */
public class DataFileHeaderLineParser extends AbstractLineParser {
    public final static Pattern pattern = Pattern.compile("^(?<dataFileHeaderRecordType>[\\p{Upper}\\p{Digit}]{5})(?<dataFileHeaderDate>\\p{Digit}{8})(?<dataFileHeaderTime>\\p{Digit}{4})(?<dataFileHeaderFileID>\\p{Digit}{6})(?<dataFileHeaderFileName>[\\p{Alnum}\\p{Space}]{1,20})[\\p{Space}]{0,407}");
    private final static DateFormat headerDateTime = new SimpleDateFormat("MMddyyyyHHmm");
    @Override
    public AmexFeedLineParserOutput parse(String input) throws ParseException {
        Matcher m = pattern.matcher(input);
        if (m.matches()) {
            return new DataFileHeader().withDataFileHeaderRecordType(m.group("dataFileHeaderRecordType"))
                    .withDataFileHeaderDateTime(
                            headerDateTime.parse(m.group("dataFileHeaderDate") + m.group("dataFileHeaderTime")))
                    .withDataFileHeaderFileID(Long.valueOf(m.group("dataFileHeaderFileID").trim()))
                    .withDataFileHeaderFileName(m.group("dataFileHeaderFileName").trim());
        } else {
            return null;
        }

    }
}

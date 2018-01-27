package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.parser is responsible for ..
 */
public abstract class AbstractLineParser implements AmexFeedLineParser {
    protected static final DateFormat julianDate = new SimpleDateFormat("yyyyDDD");
}

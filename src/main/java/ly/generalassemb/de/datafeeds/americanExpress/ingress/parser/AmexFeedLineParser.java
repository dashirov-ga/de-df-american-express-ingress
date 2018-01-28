package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

import java.text.ParseException;

/**
 *
 */
public interface AmexFeedLineParser {
    AmexFeedLineParserOutput parse(String input) throws ParseException;
}

package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

import java.io.File;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.parser is responsible for ..
 */
public interface AmexFeedFileParser {
    AmexFeedFileParserOutput parseFile(File input);
}

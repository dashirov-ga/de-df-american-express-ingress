package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

/**
 * The factory class.
 */
public class AmexFileParserFactory {

    public static AmexFeedFileParser getFeedFileParser(String type) {
        switch (type) {
            case "EPTRN" :
                return new EPTRNFileParser();
            case "CBNOT":
                return new CBNOTFileParser();
            default:
                throw new IllegalArgumentException("Are you mad !!");
        }
    }
}

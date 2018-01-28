package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.parser is responsible for ..
 */
public class CBNOTFileParser extends AbstractFileParser {

    public CBNOTFileParser() {
        parsers.add(new CBNOTDataFileHeaderLineParser());
        parsers.add(new CBNOTDataFileTrailerLineParser());
        parsers.add(new DetailLineParser());
    }

}

package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.parser is responsible for ..
 */
public class EPTRNFileParser extends AbstractFileParser {

    public EPTRNFileParser() {
        super();
        parsers.add(new DataFileHeaderLineParser());
        parsers.add(new DataFileTrailerLineParser());
        parsers.add(new SummaryLineParser());
        parsers.add(new ROCDetailLineParser());
        parsers.add(new SOCDetailLineParser());
        parsers.add(new AdjustmentDetailLineParser());
    }

}

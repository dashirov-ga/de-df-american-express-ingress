package ly.generalassemb.de.datafeeds.americanExpress.ingress.serializer;

import java.util.List;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.AmexFeedLineParserOutput;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.serializer is responsible for ..
 */
public interface AmexFeedFileSerializer {

    void writeCSVFile(String csvFileName, List<AmexFeedLineParserOutput> summaries);
}

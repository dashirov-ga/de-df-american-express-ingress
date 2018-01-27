package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.parser is responsible for ..
 */
public class AbstractFileParser implements AmexFeedFileParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileParser.class);
    protected final List<AmexFeedLineParser> parsers = new ArrayList<>();

    @Override
    public AmexFeedFileParserOutput parseFile(File inputFile) {
        AmexFeedFileParserOutput output = new AmexFeedFileParserOutput();

        String uniqueFileId = inputFile.getName().substring(0, inputFile.getName().indexOf('-')).replaceAll("[#]", "-");
        LOGGER.debug("Pricessing {}", uniqueFileId);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (AmexFeedLineParser parser : parsers) {
                    AmexFeedLineParserOutput lineParserOutput = parser.parse(line);
                    if (lineParserOutput != null) {
                        output.addParsedContent(parser.getClass(), lineParserOutput);
                        break;
                    }
                }
            }


        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return output;
    }
}

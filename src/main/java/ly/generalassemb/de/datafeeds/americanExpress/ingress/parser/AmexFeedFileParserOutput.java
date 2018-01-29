package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.AmexRecordType;

/**
 *
 */
public class AmexFeedFileParserOutput {
    private final Map<AmexRecordType, List<AmexFeedLineParserOutput>> fileContents = new HashMap<>();

    public Map<AmexRecordType, List<AmexFeedLineParserOutput>> getFileContents() {
        return ImmutableMap.copyOf(fileContents);
    }

    public void addParsedContent(AmexRecordType type, AmexFeedLineParserOutput parsedLine) {
        List<AmexFeedLineParserOutput> lines = fileContents.get(type);
        if (lines == null) {
            lines = new ArrayList<>();
            fileContents.put(type, lines);
        }
        lines.add(parsedLine);
    }
}

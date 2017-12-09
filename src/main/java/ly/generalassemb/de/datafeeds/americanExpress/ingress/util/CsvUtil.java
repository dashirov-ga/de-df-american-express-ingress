package ly.generalassemb.de.datafeeds.americanExpress.ingress.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.util.Arrays;
import java.util.List;

/**
 * Created by davidashirov on 12/8/17.
 */
public class CsvUtil {
    private static final CsvMapper mapper = new CsvMapper();

    public static <T> String toCSV(List<T> records, Class clazz) throws JsonProcessingException {
        CsvSchema schema = mapper.schemaFor(clazz).withColumnSeparator(',').withHeader();
        ObjectWriter myObjectWriter = mapper.writer(schema);
            return myObjectWriter.writeValueAsString(records);
    }
}

package ly.generalassemb.de.datafeeds.americanExpress.ingress.serializer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.AmexFeedLineParserOutput;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.quote.AlwaysQuoteMode;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.serializer is responsible for ..
 */
public class SummaryFileSerializer implements AmexFeedFileSerializer {

    @Override
    public void writeCSVFile(String csvFileName, List<AmexFeedLineParserOutput> summaries) {
        ICsvBeanWriter beanWriter = null;
        CellProcessor[] processors = new CellProcessor[] { new ParseLong(), // amexPayeeNumber
                                                           new ParseLong(), // paymentYear
                                                           new NotNull(), // paymentNumber
                                                           new NotNull(), // recordType
                                                           new NotNull(), // detailRecordType
                                                           new FmtDate("yyyy-MM-dd"), // paymentDate
                                                           new ParseLong(), // paymentAmount
                                                           new ParseLong(), // debitBalanceAmount
                                                           new NotNull(), // abaBankNumber
                                                           new NotNull(), // payeeDirectDepositAccountNumber
        };

        try {
            CsvPreference redshiftPreprerence =
                    new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE).surroundingSpacesNeedQuotes(true)
                            .ignoreEmptyLines(true).useQuoteMode(new AlwaysQuoteMode()).build();
            beanWriter = new CsvBeanWriter(new FileWriter(csvFileName), redshiftPreprerence);
            String[] header = { "amexPayeeNumber", "paymentYear", "paymentNumber", "recordType",
                                "detailRecordType", "paymentDate", "paymentAmount", "debitBalanceAmount",
                                "abaBankNumber", "payeeDirectDepositAccountNumber" };
            beanWriter.writeHeader(header);

            for (AmexFeedLineParserOutput summary : summaries) {
                beanWriter.write(summary, header, processors);
            }

        } catch (IOException ex) {
            System.err.println("Error writing the CSV file: " + ex);
        } finally {
            if (beanWriter != null) {
                try {
                    beanWriter.close();
                } catch (IOException ex) {
                    System.err.println("Error closing the writer: " + ex);
                }
            }
        }
    }
}

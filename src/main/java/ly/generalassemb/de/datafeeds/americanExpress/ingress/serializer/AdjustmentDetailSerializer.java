package ly.generalassemb.de.datafeeds.americanExpress.ingress.serializer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN.AdjustmentDetail;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.AmexFeedLineParserOutput;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.quote.AlwaysQuoteMode;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.serializer is responsible for ..
 */
public class AdjustmentDetailSerializer implements AmexFeedFileSerializer {

    @Override
    public void writeCSVFile(String csvFileName, List<AmexFeedLineParserOutput> records) {

        ICsvBeanWriter beanWriter = null;
        CellProcessor[] processors = new CellProcessor[]{
                new ParseLong(), // amexPayeeNumber
                new ParseLong(), // amexSeNumber
                new ParseLong(), // paymentYear
                new org.supercsv.cellprocessor.constraint.NotNull(), // paymentNumber
                new org.supercsv.cellprocessor.constraint.NotNull(), // recordType
                new Optional(new Trim()), // detailRecordType
                new FmtDate("yyyy-MM-dd"), // amexProcessDate
                new ParseLong(), //adjustmentNumner
                new ParseLong(), // adjustmentAmount
                new ParseLong(), // discountAmount
                new ParseLong(), // serviceFeeAmount
                new ParseLong(), // netAdjustmentAmount
                new ParseLong(), // discountRate
                new ParseLong(), // serviceFeeRate
                new org.supercsv.cellprocessor.constraint.NotNull(), // cardmemberNumber
                new org.supercsv.cellprocessor.constraint.NotNull() // adjustmentReason

        };

        try {
            CsvPreference redshiftPreprerence =
                    new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE)
                            .surroundingSpacesNeedQuotes(true)
                            .ignoreEmptyLines(true)
                            .useQuoteMode(new AlwaysQuoteMode())
                            .build();
            beanWriter = new CsvBeanWriter(new FileWriter(csvFileName),
                    redshiftPreprerence);
            String[] header = {"amexPayeeNumber", "amexSeNumber", "paymentYear", "paymentNumber", "recordType", "detailRecordType",
                               "amexProcessDate", "adjustmentNumber", "adjustmentAmount", "discountAmount", "serviceFeeAmount", "netAdjustmentAmount",
                               "discountRate", "serviceFeeRate", "cardMemberNumber", "adjustmentReason"};
            beanWriter.writeHeader(header);

            for (AmexFeedLineParserOutput record : records) {
                beanWriter.write(record, header, processors);
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

package ly.generalassemb.de.datafeeds.americanExpress.ingress.serializer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN.ROCDetail;
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
public class ROCDetailSerializer implements AmexFeedFileSerializer {
    @Override
    public void writeCSVFile(String csvFileName, List<AmexFeedLineParserOutput> records) {
        ICsvBeanWriter beanWriter = null;
        CellProcessor[] processors =
                new CellProcessor[] { new ParseLong(), // amexPayeeNumber
                                      new ParseLong(), // amexSeNumber
                                      new Trim(), // seUnitNumner
                                      new ParseLong(), // paymentYear
                                      new org.supercsv.cellprocessor.constraint.NotNull(), // paymentNumber
                                      new org.supercsv.cellprocessor.constraint.NotNull(), // recordType
                                      new Optional(new Trim()), // detailRecordType
                                      new FmtDate("yyyy-MM-dd"), // seBusinessDate
                                      new FmtDate("yyyy-MM-dd"), // amexProcessDate
                                      new ParseLong(), // socInvoiceNumber
                                      new ParseLong(), // socAmount
                                      new ParseLong(), // rocAmount
                                      new Trim(), // cardMemberNumber
                                      new Trim(), // cardMemberReferenceNumber
                                      new FmtDate("yyyy-MM-dd"), // transactionDate
                                      new Trim(), // invoiceReferenceNumber
                                      new Optional(new Trim()), new Optional(new Trim()),
                                      new Optional(new Trim()), new Optional(new Trim()),
                                      new Optional(new Trim()), new Optional(new Trim()), new Trim() // cardMemberNumberExtended

                };

        try {
            CsvPreference redshiftPreprerence =
                    new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE).surroundingSpacesNeedQuotes(true)
                            .ignoreEmptyLines(true).useQuoteMode(new AlwaysQuoteMode()).build();
            beanWriter = new CsvBeanWriter(new FileWriter(csvFileName), redshiftPreprerence);
            String[] header =
                    { "amexPayeeNumber", "amexSeNumber", "seUnitNumber", "paymentYear", "paymentNumber",
                      "recordType", "detailRecordType", "seBusinessDate", "amexProcessDate", "socInvoiceNumber",
                      "socAmount", "rocAmount", "cardMemberNumber", "cardMemberReferenceNumber",
                      "transactionDate", "invoiceReferenceNumber", "nonCompliantIndicator",
                      "nonCompliantErrorCode1", "nonCompliantErrorCode2", "nonCompliantErrorCode3",
                      "nonCompliantErrorCode4", "nonSwipedIndicator", "cardMEmberNumberExtended" };
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

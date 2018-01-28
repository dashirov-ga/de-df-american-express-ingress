package ly.generalassemb.de.datafeeds.americanExpress.ingress.serializer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.CBNOT.Detail;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.AmexFeedLineParserOutput;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBigDecimal;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.quote.AlwaysQuoteMode;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.serializer is responsible for ..
 */
public class ChargeBackDetailsSerializer implements AmexFeedFileSerializer {
    @Override
    public void writeCSVFile(String csvFileName, List<AmexFeedLineParserOutput> records) {
        ICsvBeanWriter beanWriter = null;
        CellProcessor[] processors = new CellProcessor[] { new org.supercsv.cellprocessor.constraint.NotNull(), // RecordType
                                                           new ParseLong(), // seNumber
                                                           new org.supercsv.cellprocessor.constraint.NotNull(), // cardmember account
                                                           new org.supercsv.cellprocessor.constraint.NotNull(), // current case
                                                           new org.supercsv.cellprocessor.constraint.NotNull(), // prev case
                                                           new org.supercsv.cellprocessor.constraint.NotNull(), // resolution
                                                           new FmtDate("yyyy-MM-dd"), // adjustment Date
                                                           new FmtDate("yyyy-MM-dd"), // charge  date
                                                           new org.supercsv.cellprocessor.constraint.NotNull(), // case type
                                                           new Optional(), // location numbner
                                                           new org.supercsv.cellprocessor.constraint.NotNull(), // chargeback reason
                                                           new ParseBigDecimal(), // chargeback Amount
                                                           new Trim(), // chargeback adj num
                                                           new Optional(), // chargeback ref
                                                           new ParseBigDecimal(), // billed Amount
                                                           new ParseBigDecimal(), // soc Amount
                                                           new ParseLong(), // socInvoice
                                                           new ParseLong(), // rocInvoice
                                                           new Optional(new ParseBigDecimal()), // Foreign Amount
                                                           new Optional(), //Foreign Currency
                                                           new NotNull(), // support to follow
                                                           new Optional(new Trim()), new Optional(new Trim()),
                                                           new Optional(new Trim()), new Optional(new Trim()),
                                                           new Optional(new Trim()), new Optional(new Trim()),
                                                           new Optional(new Trim()) };

        try {
            CsvPreference redshiftPreprerence =
                    new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE).surroundingSpacesNeedQuotes(true)
                            .ignoreEmptyLines(true).useQuoteMode(new AlwaysQuoteMode()).build();
            beanWriter = new CsvBeanWriter(new FileWriter(csvFileName), redshiftPreprerence);
            String[] header =
                    { "dataFileRecordType", "serviceEstablishmentNumber", "cardMemberAccountNumber",
                      "currentCaseNumber", "previousCaseNumber", "resolution", "adjustmentDate", "chargeDate",
                      "caseType", "locationNumber", "chargebackReasonCode", "chargebackAmount",
                      "chargebackAdjustmentNumber", "chargebackResolutionAdjustmentNumber",
                      "chargebackReferenceCode", "billedAmount", "socAmount", "socInvoiceNumber",
                      "rocInvoiceNumber", "foreignAmount", "foreignCurrency", "supportToFollow",
                      "cardMemberName1", "cardMemberName2", "cardMemberAddress1", "cardMemberAddress2",
                      "cardMemberCityState", "cardMemberZip", "cardMemberFirstName1", "cardMemberMiddleName1",
                      "cardMemberLastName1", "cardMemberOriginalAccountNumber" };
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

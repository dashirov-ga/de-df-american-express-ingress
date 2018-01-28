package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.model.EPTRN.Summary;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.AmountParser;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.parser is responsible for ..
 */
public class SummaryLineParser extends AbstractLineParser {
    public final static Pattern pattern = Pattern.compile("^(?<amexPayeeNumber>\\p{Digit}{10})(?<amexSortField1>[0]{10})(?<amexSortField2>[0]{10})(?<paymentYear>\\p{Digit}{4})(?<paymentNumber>(?<paymentNumberJulianDate>\\p{Digit}{3})(?<paymentNumberRecordTypeIndicator>\\p{Alnum}{1})(?<paymentNumberSequence>\\p{Digit}{4}))(?<recordType>1)(?<detailRecordType>00)(?<paymentDate>(?<paymentDateYear>\\p{Digit}{4})(?<paymentDateJulianDate>\\p{Digit}{3}))(?<paymentAmount>(?<paymentAmountPrefix>\\p{Digit}{10})(?<paymentAmountSuffix>[A-R}{]{1}))(?<debitBalanceAmount>(?<debitBalanceAmountPrefix>\\p{Digit}{8})(?<debitBalanceAmountSuffix>[A-R}{]{1}))(?<abaBankNumber>\\p{Digit}{9})(?<payeeDirectDepositAccountNumber>[\\p{Digit}\\p{Blank}]{1,17})(?<filler13>\\p{Blank}{352})$");

    @Override public AmexFeedLineParserOutput parse(String input) throws ParseException {
        Matcher m = pattern.matcher(input);
        if (m.matches()) {
            return new Summary()
                    .withAmexPayeeNumber(Long.valueOf(m.group("amexPayeeNumber")))
                    .withPaymentYear(Long.valueOf(m.group("paymentYear")))
                    .withPaymentNumber(m.group("paymentNumber"))
                    .withRecordType(Long.valueOf(m.group("recordType")))
                    .withDetailRecordType(Long.valueOf(m.group("detailRecordType")))
                    .withPaymentDate(julianDate.parse(m.group("paymentDate")))
                    .withPaymentAmount(AmountParser.toLong(m.group("paymentAmountPrefix"), m.group("paymentAmountSuffix")))
                    .withDebitBalanceAmount(AmountParser.toLong(m.group("debitBalanceAmountPrefix"), m.group("debitBalanceAmountSuffix")))
                    .withAbaBankNumber(Long.valueOf(m.group("abaBankNumber")))
                    .withPayeeDirectDepositAccountNumber(m.group("payeeDirectDepositAccountNumber").replace(" ", ""));
        } else {
            return null;
        }
    }
}

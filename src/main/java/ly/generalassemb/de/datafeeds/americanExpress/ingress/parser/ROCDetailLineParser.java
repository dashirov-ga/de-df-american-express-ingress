package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.model.EPTRN.ROCDetail;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.AmountParser;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.parser is responsible for ..
 */
public class ROCDetailLineParser extends AbstractLineParser {
    public final static Pattern pattern = Pattern.compile("^(?<amexPayeeNumber>\\p{Digit}{10})(?<amexSeNumber>[\\p{Alnum}]{10})(?<seUnitNumber>[\\p{Digit}\\p{Blank}]{10})(?<paymentYear>\\p{Digit}{4})(?<paymentNumber>(?<paymentNumberJulianDate>\\p{Digit}{3})(?<paymentNumberRecordTypeIndicator>\\p{Alnum})(?<paymentNumberSequence>\\p{Digit}{4}))(?<recordType>3)(?<detailRecordType>11)(?<seBusinessDate>(?<seBusinessDateYear>\\p{Digit}{4})(?<seBuisnessDateJulianDate>\\p{Digit}{3}))(?<amexProcessDate>(?<amexProcessDateYear>\\p{Digit}{4})(?<amexProcessDateJulianDate>\\p{Digit}{3}))(?<socInvoiceNumber>\\p{Digit}{6})(?<socAmount>(?<socAmountPrefix>\\p{Digit}{12})(?<socAmountSuffix>[A-R}{]{1}))(?<rocAmount>(?<rocAmountPrefix>\\p{Digit}{12})(?<rocAmountSuffix>[A-R}{]{1}))(?<cardMemberNumber>[\\p{Blank}\\p{Alnum}]{15})(?<cardMemberReferenceNumber>[\\p{Blank}\\p{Alnum}]{11}).{9}\\p{Blank}{10}(?<rocNumber>[\\p{Alnum}\\p{Blank}]{10})(?<transactionDate>(?<transactionDateYear>\\p{Digit}{4})(?<transactionJulianDate>\\p{Digit}{3}))(?<invoiceReferenceNumber>[\\p{Alnum}\\p{Blank}]{30})(?<nonCompliantIndicator>[ AN]{1})(?<nonCompliantErrorCode1>[\\p{Alnum}\\p{Blank}]{4})(?<nonCompliantErrorCode2>[\\p{Alnum}\\p{Blank}]{4})(?<nonCompliantErrorCode3>[\\p{Alnum}\\p{Blank}]{4})(?<nonCompliantErrorCode4>[\\p{Alnum}\\p{Blank}]{4})(?<nonSwipedIndicator>[ CH~Z]{1})[\\p{Blank}\\p{Alnum}]{1}.{4}.{22}(?<cardMemberNumberExtended>[\\p{Blank}\\p{Alnum}]{19})(?<filler30>\\p{Blank}{203})$");
    @Override
    public AmexFeedLineParserOutput parse(String input) throws ParseException {

        Matcher m = pattern.matcher(input);
        if (m.matches()) {
            return new ROCDetail().withAmexPayeeNumber(Long.valueOf(m.group("amexPayeeNumber")))
                    .withAmexSeNumber(Long.valueOf(m.group("amexSeNumber")))
                    .withSeUnitNumber(m.group("seUnitNumber").trim())
                    .withPaymentYear(Long.valueOf(m.group("paymentYear")))
                    .withPaymentNumber(m.group("paymentNumber"))
                    .withRecordType(Long.valueOf(m.group("recordType")))
                    .withDetailRecordType(Long.valueOf(m.group("detailRecordType")))
                    .withSeBusinessDate(julianDate.parse(m.group("seBusinessDate")))
                    .withAmexProcessDate(julianDate.parse(m.group("amexProcessDate")))
                    .withSocInvoiceNumber(Long.valueOf(m.group("socInvoiceNumber")))
                    .withSocAmount(AmountParser.toLong(m.group("socAmountPrefix"), m.group("socAmountSuffix")))
                    .withRocAmount(AmountParser.toLong(m.group("rocAmountPrefix"), m.group("rocAmountSuffix")))
                    .withCardMemberNumber(m.group("cardMemberNumber"))
                    .withCardMemberReferenceNumber(m.group("cardMemberReferenceNumber").trim())
                    .withTransactionDate(julianDate.parse(m.group("transactionDate")))
                    .withInvoiceReferenceNumber(m.group("invoiceReferenceNumber").trim())
                    .withNonCompliantIndicator(m.group("nonCompliantIndicator").trim())
                    .withNonCompliantErrorCode1(m.group("nonCompliantErrorCode1").trim())
                    .withNonCompliantErrorCode2(m.group("nonCompliantErrorCode2").trim())
                    .withNonCompliantErrorCode3(m.group("nonCompliantErrorCode3").trim())
                    .withNonCompliantErrorCode4(m.group("nonCompliantErrorCode4").trim())
                    .withNonSwipedIndicator(m.group("nonSwipedIndicator").trim())
                    .withCardMemberNumberExtended(m.group("cardMemberNumberExtended").trim());
        } else {
            return null;
        }

    }
}

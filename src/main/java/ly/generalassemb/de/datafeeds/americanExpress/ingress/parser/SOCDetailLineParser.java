package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.model.EPTRN.SOCDetail;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.AmountParser;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.parser is responsible for ..
 */
public class SOCDetailLineParser extends AbstractLineParser {
    private final static Pattern pattern = Pattern.compile("^(?<amexPayeeNumber>\\p{Digit}{10})(?<amexSeNumber>[\\p{Alnum}]{10})(?<seUnitNumber>[\\p{Alnum}\\p{Blank}]{10})(?<paymentYear>\\p{Digit}{4})(?<paymentNumber>(?<paymentNumberJulianDate>\\p{Digit}{3})(?<paymentNumberRecordTypeIndicator>\\p{Alnum})(?<paymentNumberSequence>\\p{Digit}{4}))(?<recordType>2)(?<detailRecordType>10)(?<seBusinessDate>(?<seBusinessDateYear>\\p{Digit}{4})(?<seBuisnessDateJulianDate>\\p{Digit}{3}))(?<amexProcessDate>(?<amexProcessDateYear>\\p{Digit}{4})(?<amexProcessDateJulianDate>\\p{Digit}{3}))(?<socInvoiceNumber>\\p{Digit}{6})(?<socAmount>(?<socAmountPrefix>\\p{Digit}{10})(?<socAmountSuffix>[A-R}{]{1}))(?<discountAmount>(?<discountAmountPrefix>\\p{Digit}{8})(?<discountAmountSuffix>[A-R}{]{1}))(?<serviceFeeAmount>(?<serviceFeeAmountPrefix>\\p{Digit}{6})(?<serviceFeeAmountSuffix>[A-R}{]{1}))[0{]{7}(?<netSOCAmount>(?<netSOCAmountPrefix>\\p{Digit}{10})(?<netSOCAmountSuffix>[A-R}{]{1}))(?<discountRate>\\p{Digit}{5})(?<serviceFeeRate>\\p{Digit}{5})0{5}[0{]{11}[0{]{5}(?<amexGrossAmount>(?<amexGrossAmountPrefix>\\p{Digit}{10})(?<amexGrossAmountSuffix>[A-R}{]{1}))(?<amexROCCount>(?<amexROCCountPrefix>\\p{Digit}{4})(?<amexROCCountSuffix>[A-I{]{1}))(?<trackingId>[\\p{Digit}\\p{Blank}]{9})(?<cpcIndicator>[\\p{Alnum}\\p{Blank}]{1})\\p{Blank}{7}\\p{Blank}{8}(?<amexROCCountPOA>(?<amexROCCountPOAPrefix>\\p{Digit}{6})(?<amexROCCountPOASuffix>[A-I{]{1})).{0,261}$");
    @Override
    public AmexFeedLineParserOutput parse(String input) throws ParseException {
        Matcher m = pattern.matcher(input);
        if (m.matches()) {
            return new SOCDetail().withAmexPayeeNumber(Long.valueOf(m.group("amexPayeeNumber")))
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
                    .withDiscountAmount(AmountParser.toLong(m.group("discountAmountPrefix"),
                            m.group("discountAmountSuffix")))
                    .withServiceFeeAmount(AmountParser.toLong(m.group("serviceFeeAmountPrefix"),
                            m.group("serviceFeeAmountSuffix")))
                    .withNetSOCAmount(
                            AmountParser.toLong(m.group("netSOCAmountPrefix"), m.group("netSOCAmountSuffix")))
                    .withDiscountRate(Long.valueOf(m.group("discountRate")))
                    .withServiceFeeRate(Long.valueOf(m.group("serviceFeeRate")))
                    .withAmexGrossAmount(AmountParser.toLong(m.group("amexGrossAmountPrefix"),
                            m.group("amexGrossAmountSuffix")))
                    .withAmexROCCount(
                            AmountParser.toLong(m.group("amexROCCountPrefix"), m.group("amexROCCountSuffix")))
                    .withTrackingId(Long.valueOf(m.group("trackingId")))
                    .withCpcIndicator(m.group("cpcIndicator").equals("P")).withAmexROCCountPOA(AmountParser
                            .toLong(m.group("amexROCCountPOAPrefix"), m.group("amexROCCountPOASuffix")));
        } else {
            return null;
        }

    }
}

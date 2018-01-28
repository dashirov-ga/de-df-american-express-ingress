package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.model.EPTRN.AdjustmentDetail;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.AmountParser;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.parser is responsible for ..
 */
public class AdjustmentDetailLineParser extends AbstractLineParser {
    private final static Pattern pattern = Pattern.compile("^(?<amexPayeeNumber>\\p{Digit}{10})(?<amexSeNumber>[\\p{Alnum}]{10})(?<seUnitNumber>[\\p{Alnum}\\p{Blank}]{10})(?<paymentYear>\\p{Digit}{4})(?<paymentNumber>(?<paymentNumberJulianDate>\\p{Digit}{3})(?<paymentNumberRecordTypeIndicator>\\p{Alnum}{1})(?<paymentNumberSequence>\\p{Digit}{4}))(?<recordType>2)(?<detailRecordType>30)(?<amexProcessDate>(?<amexProcessDateYear>\\p{Digit}{4})(?<amexProcessJulianDate>\\p{Digit}{3}))(?<adjustmentNumber>\\p{Digit}{6})(?<adjustmentAmount>(?<adjustmentAmountPrefix>\\p{Digit}{8})(?<adjustmentAmountSuffix>[A-R}{]{1}))(?<discountAmount>(?<discountAmountPrefix>\\p{Digit}{8})(?<discountAmountSuffix>[A-R}{]{1}))(?<serviceFeeAmount>(?<serviceFeeAmountPrefix>\\p{Digit}{6})(?<serviceFeeAmountSuffix>[A-R}{]{1}))(?<filler13>000000\\{)(?<netAdjustmentAmount>(?<netAdjustmentAmountPrefix>\\p{Digit}{8})(?<netAdjustmentAmountSuffix>[A-R}{]{1}))(?<discountRate>\\p{Digit}{5})(?<serviceFeeRate>\\p{Digit}{5})(?<filler17>00000)(?<filler18>0000000000\\{)(?<cardmemberNumber>\\p{Alnum}{17})(?<adjustmentReason>[\\p{ASCII}]{280})(?<filler21>\\p{ASCII}{3})(?<filler22>\\p{ASCII}{3})(?<filler23>\\p{Blank}{15})(?<filler24>\\p{ASCII}{1})(?<filler25>\\p{ASCII}{6})$");
    private static final DateFormat julianDate = new SimpleDateFormat("yyyyDDD");

    @Override
    public AmexFeedLineParserOutput parse(String input) throws ParseException {

        Matcher m = pattern.matcher(input);
        if (m.matches()) {
            return new AdjustmentDetail().withAmexPayeeNumber(Long.valueOf(m.group("amexPayeeNumber")))
                    .withAmexSeNumber(Long.valueOf(m.group("amexSeNumber")))
                    .withSeUnitNumber(m.group("seUnitNumber").trim())
                    .withPaymentYear(Long.parseLong(m.group("paymentYear")))
                    .withPaymentNumber(m.group("paymentNumber"))
                    .withRecordType(Long.parseLong(m.group("recordType")))
                    .withDetailRecordType(Long.parseLong(m.group("detailRecordType")))
                    .withAmexProcessDate(julianDate.parse(m.group("amexProcessDate")))
                    .withAdjustmentNumber(Long.valueOf(m.group("adjustmentNumber")))
                    .withAdjustmentAmount(AmountParser.toLong(m.group("adjustmentAmountPrefix"),
                            m.group("adjustmentAmountSuffix")))
                    .withDiscountAmount(AmountParser.toLong(m.group("discountAmountPrefix"),
                            m.group("discountAmountSuffix")))
                    .withServiceFeeAmount(AmountParser.toLong(m.group("serviceFeeAmountPrefix"),
                            m.group("serviceFeeAmountSuffix")))
                    .withNetAdjustmentAmount(AmountParser.toLong(m.group("netAdjustmentAmountPrefix"),
                            m.group("netAdjustmentAmountSuffix")))
                    .withDiscountRate(Long.valueOf(m.group("discountRate")))
                    .withServiceFeeRate(Long.valueOf(m.group("serviceFeeRate")))
                    .withCardMemberNumber(m.group("cardmemberNumber").trim())
                    .withAdjustmentReason(m.group("adjustmentReason").trim());
        } else {
            return null;
        }
    }

}

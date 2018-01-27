package ly.generalassemb.de.datafeeds.americanExpress.ingress.parser;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.money.Monetary;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.model.CBNOT.Detail;

/**
 * This ly.generalassemb.de.datafeeds.americanExpress.ingress.parser is responsible for ..
 */
public class DetailLineParser extends AbstractLineParser {

    private final static Pattern pattern = Pattern.compile("^(?<dataFileRecordType>D)(?<filler2>\\p{Space}{5})(?<serviceEstablishmentNumber>\\p{ASCII}{10})(?<filler4>\\p{Space}{10})(?<cardMemberAccountNumber>[\\p{Space}\\p{Alnum}]{19})(?<filler6>\\p{Space}{1})(?<currentCaseNumber>\\p{ASCII}{11})(?<currentActionNumber>\\p{ASCII}{2})(?<previousCaseNumber>\\p{ASCII}{11})(?<previousActionNumber>\\p{ASCII}{2})(?<resolution>[YN]{1})(?<fromSystem>[\\p{Space}FRSTXPG]{1})(?<rejectsToSystem>[\\p{Space}RSTXPG]{1})(?<disputesToSystem>[\\p{Space}RSTXPG]{1})(?<adjustmentDate>\\p{Digit}{8})(?<chargeDate>\\p{Digit}{8})(?<amexEmployeeId>\\p{ASCII}{7})(?<filler18>\\p{Space}{5})(?<caseType>\\p{ASCII}{6})(?<locationNumber>\\p{ASCII}{15})(?<chargebackReasonCode>\\p{Alnum}{3})(?<chargebackAmount>[\\{Space}-]\\p{Digit}{13}\\.\\p{Digit}{2})(?<chargebackAdjustmentNumber>\\p{ASCII}{6})(?<chargebackResolutionAdjustmentNumber>\\p{ASCII}{6})(?<chargebackReferenceCode>\\p{ASCII}{12})(?<filler26>\\p{Space}{13})(?<billedAmount>[\\p{Space}-]\\p{Digit}{13}\\.\\p{Digit}{2})(?<socAmount>[\\p{Space}-]\\p{Digit}{13}\\.\\p{Digit}{2})(?<socInvoiceNumber>\\p{ASCII}{6})(?<rocInvoiceNumber>\\p{ASCII}{6})(?<foreignAmount>\\p{ASCII}{15})(?<foreignCurrency>\\p{Upper}{3})(?<supportToFollow>[YIRN]{1})(?<cardMemberName1>\\p{ASCII}{30})(?<cardMemberName2>\\p{ASCII}{30})(?<cardMemberAddress1>\\p{ASCII}{30})(?<cardMemberAddress2>\\p{ASCII}{30})(?<cardMemberCityState>\\p{ASCII}{30})(?<cardMemberZip>\\p{ASCII}{9})(?<cardMemberFirstName1>\\p{ASCII}{12})(?<cardMemberMiddleName1>\\p{ASCII}{12})(?<cardMemberLastName1>\\p{ASCII}{20})(?<cardMemberOriginalAccountNumber>\\p{ASCII}{15})(?<cardMemberOriginalName>\\p{ASCII}{30})(?<cardMemberOriginalFirstName>\\p{ASCII}{12})(?<cardMemberOriginalMiddleName>\\p{ASCII}{12})(?<cardMemberOriginalLastName>\\p{ASCII}{20})(?<note1>\\p{ASCII}{66})(?<note2>\\p{ASCII}{78})(?<note3>\\p{ASCII}{60})(?<note4>\\p{ASCII}{60})(?<note5>\\p{ASCII}{60})(?<note6>\\p{ASCII}{60})(?<note7>\\p{ASCII}{60})(?<triumphSequenceNumber>\\p{ASCII}{2})(?<filler56>\\p{Space}{20})(?<filler57>\\p{Space}{15})(?<filler58>\\p{Space}{10})(?<industrySpecificPayload>\\p{ASCII}{1172})");
    private final static DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private static final DecimalFormat decimalFormat = new DecimalFormat();

    @Override
    public AmexFeedLineParserOutput parse(String input) throws ParseException {
        decimalFormat.setParseBigDecimal(true);
        Matcher m = pattern.matcher(input);
        if (m.matches()) {
            return new Detail().withDataFileRecordType(m.group("dataFileRecordType").trim())
                    .withServiceEstablishmentNumber(m.group("serviceEstablishmentNumber").trim())
                    .withCardMemberAccountNumber(m.group("cardMemberAccountNumber").trim())
                    .withCurrentCaseNumber(m.group("currentCaseNumber").trim())
                    .withPreviousCaseNumber(m.group("previousCaseNumber").trim())
                    .withResolution(Detail.Resolution.valueOf(m.group("resolution").trim()))
                    .withAdjustmentDate(dateFormat.parse(m.group("adjustmentDate").trim()))
                    .withChargeDate(dateFormat.parse(m.group("chargeDate").trim()))
                    .withCaseType(Detail.CaseType.valueOf(m.group("caseType").trim()))
                    .withLocationNumber(m.group("locationNumber").trim())
                    .withChargebackReasonCode(
                            Detail.ChargebackReasonCode.valueOf(m.group("chargebackReasonCode").trim()))
                    .withChargebackAmount(Monetary.getDefaultAmountFactory().setCurrency("USD")
                            .setNumber(decimalFormat.parse(m.group("chargebackAmount"))).create())
                    .withChargebackAdjustmentNumber(m.group("chargebackAdjustmentNumber").trim())
                    .withChargebackResolutionAdjustmentNumber(
                            m.group("chargebackResolutionAdjustmentNumber").trim())
                    .withChargebackReferenceCode(m.group("chargebackReferenceCode").trim())
                    .withBilledAmount(Monetary.getDefaultAmountFactory().setCurrency("USD")
                            .setNumber(decimalFormat.parse(m.group("billedAmount"))).create())
                    .withSocAmount(Monetary.getDefaultAmountFactory().setCurrency("USD")
                            .setNumber(decimalFormat.parse(m.group("socAmount"))).create())
                    .withSocInvoiceNumber(m.group("socInvoiceNumber").trim())
                    .withRocInvoiceNumber(m.group("rocInvoiceNumber").trim())
                    .withForeignAmount(
                            Monetary.getDefaultAmountFactory().setCurrency(m.group("foreignCurrency").trim())
                                    .setNumber(decimalFormat.parse(m.group("foreignAmount").trim())).create())
                    .withForeignCurrency(Currency.getInstance(m.group("foreignCurrency").trim()))
                    .withSupportToFollow(Detail.SupportToFollow.valueOf(m.group("supportToFollow").trim()))
                    .withCardMemberName1(m.group("cardMemberName1").trim())
                    .withCardMemberName2(m.group("cardMemberName2").trim())
                    .withCardMemberAddress1(m.group("cardMemberAddress1").trim())
                    .withCardMemberAddress2(m.group("cardMemberAddress2").trim())
                    .withCardMemberCityState(m.group("cardMemberCityState").trim())
                    .withCardMemberZip(m.group("cardMemberZip").trim())
                    .withCardMemberFirstName1(m.group("cardMemberFirstName1").trim())
                    .withCardMemberMiddleName1(m.group("cardMemberMiddleName1").trim())
                    .withCardMemberLastName1(m.group("cardMemberLastName1").trim())
                    .withCardMemberOriginalAccountNumber(m.group("cardMemberOriginalAccountNumber").trim());

        }
        return null;

    }
}

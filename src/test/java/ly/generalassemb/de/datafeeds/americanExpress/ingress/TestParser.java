package ly.generalassemb.de.datafeeds.americanExpress.ingress;

import com.google.common.io.ByteStreams;
import com.jcraft.jsch.*;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN.*;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.AmountParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dashirov on 5/9/17.
 */
public class TestParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestParser.class);
    BufferedReader reader;

    private InputStream getFile(String fileName) {
        InputStream result;
        ClassLoader classLoader = getClass().getClassLoader();
        result = classLoader.getResourceAsStream(fileName);
        return result;

    }

    @Test
    public void secureFileTransfer(){
        JSch ssh = new JSch();
        String host = "localhost";
        int port = 22;
        String user= System.getProperty("user.name");
        try {
            byte[] private_key = ByteStreams.toByteArray(getFile("ssh_private_key"));
            byte[] public_key  = ByteStreams.toByteArray(getFile("ssh_public_key"));
            ssh.addIdentity("GeneralAssembly", private_key,public_key,null);
            ssh.setKnownHosts(getFile("known_hosts"));

            Session session = ssh.getSession(user,host,port);
            System.out.println("session created.");

            session.connect();
            System.out.println("session connected.");

            Channel channel = session.openChannel("sftp");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect();
            System.out.println("sftp shell channel connected.");

            ChannelSftp c = (ChannelSftp) channel;

            String fileName = "test.txt";
            c.put(fileName,"./in/");
            c.exit();
            System.out.println("Done.");


        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testExample()  {
        ClassLoader classLoader = getClass().getClassLoader();
        BufferedReader reader = new BufferedReader(new InputStreamReader(getFile("EPTRN.dat")));
        try {

            SimpleDateFormat postgresTsWithTz = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
            SimpleDateFormat julianDate = new SimpleDateFormat("yyyyDDD");

            UUID runId = UUID.fromString("00000000-0000-0000-0000-000000000000");
            String line;
            int lineNo = 0;
            while ((line = reader.readLine()) != null) {
                Matcher m;

                m = Summary.pattern.matcher(line);
                if (m.matches()) {
                    LOGGER.debug("{} Got: SUMMARY_100", runId);
                    // Interface 1
                    Summary record = new Summary();
                    record.setAmexPayeeNumber(Long.valueOf(m.group("amexPayeeNumber")));
                    record.setPaymentYear(Long.valueOf(m.group("paymentYear")));
                    record.setPaymentNumber(m.group("paymentNumber"));
                    record.setRecordType(Long.valueOf(m.group("recordType")));
                    record.setDetailRecordType(Long.valueOf(m.group("detailRecordType")));
                    record.setPaymentDate(julianDate.parse(m.group("paymentDate")));
                    record.setPaymentAmount(AmountParser.toLong(m.group("paymentAmountPrefix"), m.group("paymentAmountSuffix")));
                    record.setDebitBalanceAmount(AmountParser.toLong(m.group("debitBalanceAmountPrefix"), m.group("debitBalanceAmountSuffix")));
                    record.setAbaBankNumber(Long.valueOf(m.group("abaBankNumber")));
                    record.setPayeeDirectDepositAccountNumber(m.group("payeeDirectDepositAccountNumber").replace(" ", ""));

                    // Interface 2
                    Summary summary = new Summary()
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
                    LOGGER.debug(summary.toString());

                    continue;
                }

                m = SOCDetail.pattern.matcher(line);
                if (m.matches()) {

                    // Interface 1
                    SOCDetail record = new SOCDetail();
                    record.setAmexPayeeNumber(Long.valueOf(m.group("amexPayeeNumber")));
                    record.setAmexSeNumber(Long.valueOf(m.group("amexSeNumber")));
                    record.setSeUnitNumber(m.group("seUnitNumber").replaceAll("(?:^\\p{Blank}|\\p{Blank}$)", ""));
                    record.setPaymentYear(Long.valueOf(m.group("paymentYear")));
                    record.setPaymentNumber(m.group("paymentNumber"));
                    record.setRecordType(Long.valueOf(m.group("recordType")));
                    record.setDetailRecordType(Long.valueOf(m.group("detailRecordType")));
                    record.setSeBusinessDate(julianDate.parse(m.group("seBusinessDate")));
                    record.setAmexProcessDate(julianDate.parse(m.group("amexProcessDate")));
                    record.setSocInvoiceNumber(Long.valueOf(m.group("socInvoiceNumber")));
                    record.setSocAmount(AmountParser.toLong(m.group("socAmountPrefix"), m.group("socAmountSuffix")));
                    record.setDiscountAmount(AmountParser.toLong(m.group("discountAmountPrefix"), m.group("discountAmountSuffix")));
                    record.setServiceFeeAmount(AmountParser.toLong(m.group("serviceFeeAmountPrefix"), m.group("serviceFeeAmountSuffix")));
                    record.setNetSOCAmount(AmountParser.toLong(m.group("netSOCAmountPrefix"), m.group("netSOCAmountSuffix")));
                    record.setDiscountRate(Long.valueOf(m.group("discountRate")));
                    record.setServiceFeeRate(Long.valueOf(m.group("serviceFeeRate")));
                    record.setAmexGrossAmount(AmountParser.toLong(m.group("amexGrossAmountPrefix"), m.group("amexGrossAmountSuffix")));
                    record.setAmexROCCount(AmountParser.toLong(m.group("amexROCCountPrefix"), m.group("amexROCCountSuffix")));
                    record.setTrackingId(Long.valueOf(m.group("trackingId")));
                    record.setCpcIndicator(m.group("cpcIndicator").equals("P"));
                    record.setAmexROCCountPOA(AmountParser.toLong(m.group("amexROCCountPOAPrefix"), m.group("amexROCCountPOASuffix")));

                    // Interface 2
                    SOCDetail socDetail = new SOCDetail()
                            .withAmexPayeeNumber(Long.valueOf(m.group("amexPayeeNumber")))
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
                            .withDiscountAmount(AmountParser.toLong(m.group("discountAmountPrefix"), m.group("discountAmountSuffix")))
                            .withServiceFeeAmount(AmountParser.toLong(m.group("serviceFeeAmountPrefix"), m.group("serviceFeeAmountSuffix")))
                            .withNetSOCAmount(AmountParser.toLong(m.group("netSOCAmountPrefix"), m.group("netSOCAmountSuffix")))
                            .withDiscountRate(Long.valueOf(m.group("discountRate")))
                            .withServiceFeeRate(Long.valueOf(m.group("serviceFeeRate")))
                            .withAmexGrossAmount(AmountParser.toLong(m.group("amexGrossAmountPrefix"), m.group("amexGrossAmountSuffix")))
                            .withAmexROCCount(AmountParser.toLong(m.group("amexROCCountPrefix"), m.group("amexROCCountSuffix")))
                            .withTrackingId(Long.valueOf(m.group("trackingId")))
                            .withCpcIndicator(m.group("cpcIndicator").equals("P"))
                            .withAmexROCCountPOA(AmountParser.toLong(m.group("amexROCCountPOAPrefix"), m.group("amexROCCountPOASuffix")));
                    LOGGER.debug(socDetail.toString());
                    continue;
                }


                m = ROCDetail.pattern.matcher(line);
                if (m.matches()) {

                    // Interface 1
                    ROCDetail record = new ROCDetail();
                    record.setAmexPayeeNumber(Long.valueOf(m.group("amexPayeeNumber")));
                    record.setAmexSeNumber(Long.valueOf(m.group("amexSeNumber")));
                    record.setSeUnitNumber(m.group("seUnitNumber").trim());
                    record.setPaymentYear(Long.valueOf(m.group("paymentYear")));
                    record.setPaymentNumber(m.group("paymentNumber"));
                    record.setRecordType(Long.valueOf(m.group("recordType")));
                    record.setDetailRecordType(Long.valueOf(m.group("detailRecordType")));
                    record.setSeBusinessDate(julianDate.parse(m.group("seBusinessDate")));
                    record.setAmexProcessDate(julianDate.parse(m.group("amexProcessDate")));
                    record.setSocInvoiceNumber(Long.valueOf(m.group("socInvoiceNumber")));
                    record.setSocAmount(AmountParser.toLong(m.group("socAmountPrefix"), m.group("socAmountSuffix")));
                    record.setRocAmount(AmountParser.toLong(m.group("rocAmountPrefix"), m.group("rocAmountSuffix")));
                    record.setCardMemberNumber(m.group("cardMemberNumber"));
                    record.setCardMemberReferenceNumber(m.group("cardMemberReferenceNumber").trim());
                    record.setTransactionDate(julianDate.parse(m.group("transactionDate")));
                    record.setInvoiceReferenceNumber(m.group("invoiceReferenceNumber").trim());
                    record.setNonCompliantIndicator(m.group("nonCompliantIndicator").trim());
                    record.setNonCompliantErrorCode1(m.group("nonCompliantErrorCode1").trim());
                    record.setNonCompliantErrorCode2(m.group("nonCompliantErrorCode2").trim());
                    record.setNonCompliantErrorCode3(m.group("nonCompliantErrorCode3").trim());
                    record.setNonCompliantErrorCode4(m.group("nonCompliantErrorCode4").trim());
                    record.setNonSwipedIndicator(m.group("nonSwipedIndicator").trim());
                    record.setCardMemberNumberExtended(m.group("cardMemberNumberExtended").trim());


                    ROCDetail rocDetail = new ROCDetail()
                            .withAmexPayeeNumber(Long.valueOf(m.group("amexPayeeNumber")))
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

                    LOGGER.debug(rocDetail.toString());
                    continue;
                }

                m = AdjustmentDetail.pattern.matcher(line);
                if (m.matches()) {
                    AdjustmentDetail record = new AdjustmentDetail();
                    record.setAmexPayeeNumber(Long.valueOf(m.group("amexPayeeNumber")));
                    record.setAmexSeNumber(Long.valueOf(m.group("amexSeNumber")));
                    record.setSeUnitNumber(m.group("seUnitNumber").trim());
                    record.setPaymentYear(Long.parseLong(m.group("paymentYear")));
                    record.setPaymentNumber(m.group("paymentNumber"));
                    record.setRecordType(Long.parseLong(m.group("recordType")));
                    record.setAmexProcessDate(julianDate.parse(m.group("amexProcessDate")));
                    record.setAdjustmentNumber(Long.valueOf(m.group("adjustmentNumber")));
                    record.setAdjustmentAmount(AmountParser.toLong(m.group("adjustmentAmountPrefix"), m.group("adjustmentAmountSuffix")));
                    record.setDiscountAmount(AmountParser.toLong(m.group("discountAmountPrefix"), m.group("discountAmountSuffix")));
                    record.setServiceFeeAmount(AmountParser.toLong(m.group("serviceFeeAmountPrefix"), m.group("serviceFeeAmountSuffix")));
                    record.setNetAdjustmentAmount(AmountParser.toLong(m.group("netAdjustmentAmountPrefix"), m.group("netAdjustmentAmountSuffix")));
                    record.setDiscountRate(Long.valueOf(m.group("discountRate")));
                    record.setServiceFeeRate(Long.valueOf(m.group("serviceFeeRate")));
                    record.setCardMemberNumber(m.group("cardmemberNumber"));
                    record.setAdjustmentReason(m.group("adjustmentReason"));


                    AdjustmentDetail adjustmentDetail = new AdjustmentDetail()
                            .withAmexPayeeNumber(Long.valueOf(m.group("amexPayeeNumber")))
                            .withAmexSeNumber(Long.valueOf(m.group("amexSeNumber")))
                            .withSeUnitNumber(m.group("seUnitNumber").trim())
                            .withPaymentYear(Long.parseLong(m.group("paymentYear")))
                            .withPaymentNumber(m.group("paymentNumber"))
                            .withRecordType(Long.parseLong(m.group("recordType")))
                            .withAmexProcessDate(julianDate.parse(m.group("amexProcessDate")))
                            .withAdjustmentNumber(Long.valueOf(m.group("adjustmentNumber")))
                            .withAdjustmentAmount(AmountParser.toLong(m.group("adjustmentAmountPrefix"), m.group("adjustmentAmountSuffix")))
                            .withDiscountAmount(AmountParser.toLong(m.group("discountAmountPrefix"), m.group("discountAmountSuffix")))
                            .withServiceFeeAmount(AmountParser.toLong(m.group("serviceFeeAmountPrefix"), m.group("serviceFeeAmountSuffix")))
                            .withNetAdjustmentAmount(AmountParser.toLong(m.group("netAdjustmentAmountPrefix"), m.group("netAdjustmentAmountSuffix")))
                            .withDiscountRate(Long.valueOf(m.group("discountRate")))
                            .withServiceFeeRate(Long.valueOf(m.group("serviceFeeRate")))
                            .withCardMemberNumber(m.group("cardmemberNumber").trim())
                            .withAdjustmentReason(m.group("adjustmentReason").trim());

                    LOGGER.debug(adjustmentDetail.toString());
                    continue;
                }

                m = DataFileTrailer.pattern.matcher(line);
                if (m.matches()) {


                    DateFormat trailerDateTime = new SimpleDateFormat("MMddyyyyHHmm");

                    // Interface 1
                    DataFileTrailer record = new DataFileTrailer();
                    record.setDataFileTrailerRecordType(m.group("dataFileTrailerRecordType"));
                    record.setDataFileTrailerDateTime(trailerDateTime.parse(
                            m.group("dataFileTrailerDate") +
                                    m.group("dataFileTrailerTime")));
                    record.setDataFileTrailerFileID(Long.valueOf(m.group("dataFileTrailerFileID")));
                    record.setDataFileTrailerFileName(m.group("dataFileTrailerFileName"));
                    record.setDataFileTrailerRecipientKey(m.group("dataFileTrailerRecipientKey"));
                    record.setDataFileTrailerRecordCount(Long.valueOf(m.group("dataFileTrailerRecordCount")));

                    // Interface 2
                    DataFileTrailer trailer = new DataFileTrailer()
                            .withDataFileTrailerRecordType(m.group("dataFileTrailerRecordType"))
                            .withDataFileTrailerDateTime(trailerDateTime.parse(
                                    m.group("dataFileTrailerDate") +
                                            m.group("dataFileTrailerTime")))
                            .withDataFileTrailerFileID(Long.valueOf(m.group("dataFileTrailerFileID")))
                            .withDataFileTrailerFileName(m.group("dataFileTrailerFileName"))
                            .withDataFileTrailerRecipientKey(m.group("dataFileTrailerRecipientKey"))
                            .withDataFileTrailerRecordCount(Long.valueOf(m.group("dataFileTrailerRecordCount")));

                    LOGGER.debug(trailer.toString());
                    continue;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}

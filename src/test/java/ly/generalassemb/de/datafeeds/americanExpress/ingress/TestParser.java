package ly.generalassemb.de.datafeeds.americanExpress.ingress;

import com.google.common.io.ByteStreams;
import com.jcraft.jsch.*;
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(getFile("GeneralAssemblyEPTRNsample5-9-17.txt")));
        try {
            // PATTERNS WILL NEVER MISTAKE ONE RECORD TYPE FOR ANOTHER BECAUSE RECORD TYPES ARE HARDCODED AS DOCUMENTED
            Pattern DFHDR = Pattern.compile("^(?<dataFileHeaderRecordType>[\\p{Upper}\\p{Digit}]{5})(?<dataFileHeaderDate>\\p{Digit}{8})(?<dataFileHeaderTime>\\p{Digit}{4})(?<dataFileHeaderFileID>\\p{Digit}{6})(?<dataFileHeaderFileName>[\\p{Alnum}\\p{Space}]{1,20})[\\p{Space}]{0,407}");
            Pattern SUMMARY_100 = Pattern.compile("^(?<amexPayeeNumber>\\p{Digit}{10})(?<amexSortField1>[0]{10})(?<amexSortField2>[0]{10})(?<paymentYear>\\p{Digit}{4})(?<paymentNumber>(?<paymentNumberJulianDate>\\p{Digit}{3})(?<paymentNumberRecordTypeIndicator>\\p{Alnum}{1})(?<paymentNumberSequence>\\p{Digit}{4}))(?<recordType>1)(?<detailRecordType>00)(?<paymentDate>(?<paymentDateYear>\\p{Digit}{4})(?<paymentDateJulianDate>\\p{Digit}{3}))(?<paymentAmount>(?<paymentAmountPrefix>\\p{Digit}{10})(?<paymentAmountSuffix>[A-R}{]{1}))(?<debitBalanceAmount>(?<debitBalanceAmountPrefix>\\p{Digit}{8})(?<debitBalanceAmountSuffix>[A-R}{]{1}))(?<abaBankNumber>\\p{Digit}{9})(?<seDDANumber>\\p{Digit}{1,17})\\p{Blank}{362}$");
            Pattern SOC_DETAIL_210 = Pattern.compile("^(?<amexPayeeNumber>\\p{Digit}{10})(?<amexSeNumber>[\\p{Alnum}]{10})(?<seUnitNumber>[\\p{Alnum}\\p{Blank}]{10})(?<paymentYear>\\p{Digit}{4})(?<paymentNumber>(?<paymentNumberJulianDate>\\p{Digit}{3})(?<paymentNumberRecordTypeIndicator>\\p{Alnum})(?<paymentNumberSequence>\\p{Digit}{4}))(?<recordType>[2]{1})(?<detailRecordType>10)(?<seBusinessDate>(?<seBusinessDateYear>\\p{Digit}{4})(?<seBuisnessDateJulianDate>\\p{Digit}{3}))(?<amexProcessingDate>(?<amexProcessingDateYear>\\p{Digit}{4})(?<amexProcessingDateJulianDate>\\p{Digit}{3}))(?<socInvoiceNumber>\\p{Digit}{6})(?<socInvoiceAmount>(?<socInvoicePrefix>\\p{Digit}{10})(?<socInvoiceSuffix>[A-R}{]{1}))(?<discountAmount>(?<discountAmountPrefix>\\p{Digit}{8})(?<discountAmountSuffix>[A-R}{]{1}))(?<serviceFeeAmount>(?<serviceFeePrefix>\\p{Digit}{6})(?<serviceFeeSuffix>[A-R}{]{1}))[0{]{7}(?<netSOCAmount>(?<netSOCPrefix>\\p{Digit}{10})(?<netSOCSuffix>[A-R}{]{1}))(?<discountRate>\\p{Digit}{5})(?<serviceFeeRate>\\p{Digit}{5})0{5}[0{]{11}[0{]{5}(?<amexGrossAmount>(?<amexGrossPrefix>\\p{Digit}{10})(?<amexGrossSuffix>[A-R}{]{1}))(?<amexROCCount>(?<amexROCCountPrefix>\\p{Digit}{4})(?<amexROCCountSuffix>[A-I{]{1}))(?<trackingId>[\\p{Digit}\\p{Blank}]{9})(?<cpcIndicator>[\\p{Alnum}\\p{Blank}]{1})\\p{Blank}{7}\\p{Blank}{8}(?<amexROCCountPOA>(?<amexROCCountPOAPrefix>\\p{Digit}{6})(?<amexROCCountPOASuffix>[A-I{]{1})).{0,261}$");                                                             //
            Pattern ROC_DETAIL_311 = Pattern.compile("^(?<amexPayeeNumber>\\p{Digit}{10})(?<amexSeNumber>[\\p{Alnum}]{10})(?<seUnitNumber>[\\p{Alnum}\\p{Blank}]{10})(?<paymentYear>\\p{Digit}{4})(?<paymentNumber>(?<paymentNumberJulianDate>\\p{Digit}{3})(?<paymentNumberRecordTypeIndicator>\\p{Alnum})(?<paymentNumberSequence>\\p{Digit}{4}))(?<recordType>3)(?<detailRecordType>11)(?<seBusinessDate>(?<seBusinessDateYear>\\p{Digit}{4})(?<seBuisnessDateJulianDate>\\p{Digit}{3}))(?<amexProcessingDate>(?<amexProcessingDateYear>\\p{Digit}{4})(?<amexProcessingDateJulianDate>\\p{Digit}{3}))(?<socInvoiceNumber>\\p{Digit}{6})(?<socAmount>(?<socAmountPrefix>\\p{Digit}{12})(?<socAmountSuffix>[A-R}{]{1}))(?<rocAmount>(?<rocAmountPrefix>\\p{Digit}{12})(?<rocAmountSuffix>[A-R}{]{1}))(?<cardMemberNumber>[\\p{Blank}\\p{Alnum}]{15})(?<cardMemberReferenceNumber>[\\p{Blank}\\p{Alnum}]{11}).{9}\\p{Blank}{10}(?<rocNumber>[\\p{Alnum}\\p{Blank}]{10})(?<transactionDate>(?<transactionDateYear>\\p{Digit}{4})(?<transactionJulianDate>\\p{Digit}{3}))(?<invoiceReferenceNumber>[\\p{Alnum}\\p{Blank}]{30})(?<nonCompliantIndicator>[ AN~]{1})(?<nonCompliantErrorCode1>[\\p{Alnum}\\p{Blank}]{4})(?<nonCompliantErrorCode2>[\\p{Alnum}\\p{Blank}]{4})(?<nonCompliantErrorCode3>[\\p{Alnum}\\p{Blank}]{4})(?<nonCompliantErrorCode4>[\\p{Alnum}\\p{Blank}]{4})(?<nonSwipedIndicator>[ CH~Z]{1})[\\p{Blank}\\p{Alnum}]{1}.{4}.{22}(?<cardMemberNumberUsedInTransaction>[\\p{Blank}\\p{Alnum}]{15})(.*)$");
            Pattern ADJUSTMENT_DETAIL_230 = Pattern.compile("^(?<amexPayeeNumber>\\p{Digit}{10})(?<amexSeNumber>[\\p{Alnum}]{10})(?<seUnitNumber>[\\p{Alnum}\\p{Blank}]{10})(?<paymentYear>\\p{Digit}{4})(?<paymentNumber>(?<paymentNumberJulianDate>\\p{Digit}{3})(?<paymentNumberRecordTypeIndicator>\\p{Alnum}{1})(?<paymentNumberSequence>\\p{Digit}{4}))(?<recordType>2)(?<detailRecordType>30)(?<amexProcessDate>(?<amexProcessDateYear>\\p{Digit}{4})(?<amexProcessJulianDate>\\p{Digit}{3}))(?<adjustmentNumber>\\p{Digit}{6})(?<adjustmentAmount>(?<adjustmentAmountPrefix>\\p{Digit}{8})(?<adjustmentAmountSuffix>[A-R}{]{1}))(?<discountAmount>(?<discountAmountPrefix>\\p{Digit}{8})(?<discountAmountSuffix>[A-R}{]{1}))(?<serviceFeeAmount>(?<serviceFeeAmountPrefix>\\p{Digit}{6})(?<serviceFeeAmountSuffix>[A-R}{]{1}))(?<filler13>000000\\{)(?<netAdjustmentAmount>(?<netAdjustmentAmountPrefix>\\p{Digit}{8})(?<netAdjustmentAmountSuffix>[A-R}{]{1}))(?<discountRate>\\p{Digit}{5})(?<serviceFeeRate>\\p{Digit}{5})(?<filler17>00000)(?<filler18>0000000000\\{)(?<cardmemberNumber>\\p{Alnum}{17})(?<adjustmentReason>[\\p{ASCII}]{280})(?<filler21>\\p{ASCII}{3})(?<filler22>\\p{ASCII}{3})(?<filler23>\\p{Blank}{15})(?<filler24>\\p{ASCII}{1})(?<filler25>\\p{ASCII}{6})$");
            Pattern DFTRL = Pattern.compile("^(?<dataFileTrailerRecordType>[\\p{Upper}\\p{Digit}]{5})(?<dataFileTrailerDate>\\p{Digit}{8})(?<dataFileTrailerTime>\\p{Digit}{4})(?<dataFileTrailerFileID>\\p{Digit}{6})(?<dataFileTrailerFileName>[\\p{Alnum}\\p{Space}]{20})(?<dataFileTrailerRecipientKey>[\\p{Digit}\\p{Space}]{40})(?<dataFileTrailerRecordCount>\\p{Digit}{7})\\p{Space}{0,360}");

            SimpleDateFormat postgresTsWithTz = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
            SimpleDateFormat julianDate = new SimpleDateFormat("yyyyDDD");

            UUID runId = UUID.fromString("00000000-0000-0000-0000-000000000000");
            String line;
            int lineNo = 0;
            while ((line = reader.readLine()) != null) {
                Matcher m;

                m= DFHDR.matcher(line);
                if (m.matches()){
                    LOGGER.debug("{} Got: DFHDR", runId);
                    String dataFileHeaderRecordType = m.group("dataFileHeaderRecordType");
                    DateFormat headerDateTime = new SimpleDateFormat("MMddyyyyHHmm");
                    Date dataFileHeaderDateTime = headerDateTime.parse(
                            m.group("dataFileHeaderDate") +
                                    m.group("dataFileHeaderTime"));
                    Long dataFileHeaderFileID = Long.valueOf( m.group("dataFileHeaderFileID") );
                    String  dataFileHeaderFileName = m.group("dataFileHeaderFileName");

                    LOGGER.debug("'RunID': '{}', 'dataFileHeaderDateTime': '{}' , 'dataFileHeaderFileID': {}, 'dataFileHeaderFileName': '{}'",
                            runId, postgresTsWithTz.format(dataFileHeaderDateTime), dataFileHeaderFileID, dataFileHeaderFileName);
                    continue;
                }

                m= SUMMARY_100.matcher(line);
                if (m.matches()){
                    LOGGER.debug("{} Got: SUMMARY_100", runId);
                    Long amexPayeeNumber = Long.valueOf(m.group("amexPayeeNumber"));
                    String paymentNumber = m.group("paymentNumber");
                    Date paymentDate = julianDate.parse(m.group("paymentDate"));

                    Long paymentAmount = null;
                    AmexSignSuffix ps = AmexSignSuffix.fromCharacter(m.group("paymentAmountSuffix"));
                    if (ps != null )
                        paymentAmount = ps.toLong(m.group("paymentAmountPrefix"));

                    Long debitBalanceAmount = null;
                    ps = AmexSignSuffix.fromCharacter(m.group("debitBalanceAmountSuffix"));
                    if (ps != null)
                        debitBalanceAmount = ps.toLong(m.group("debitBalanceAmountPrefix"));

                    Long abaBankNumber = Long.valueOf(m.group("abaBankNumber"));

                    String payeeDirectDepositAccountNumber=m.group("seDDANumber");
                    payeeDirectDepositAccountNumber.replace(" ","");

                    LOGGER.debug("'RunID': '{}', 'amexPayeeNumber': {} , 'paymentNumber': {}, 'paymentDate': '{}', " +
                                    "'paymentAmount': {}, 'debitBalanceAmount': {}, 'abaBankNumber': '{}', " +
                                    "'payeeDirectDepositAccountNumber': '{}'",
                            runId, amexPayeeNumber, paymentNumber ,postgresTsWithTz.format(paymentDate),
                            paymentAmount, debitBalanceAmount, abaBankNumber, payeeDirectDepositAccountNumber);


                    continue;
                }

                m= SOC_DETAIL_210.matcher(line);
                if (m.matches()){
                    LOGGER.debug("{} Got: SOC DETAIL", runId);
                        /*
                           amexPayeeNumber
                            Numeric String 10 bytes long, required
                            This field contains the Service Establishment (SE) Number of the merchant that received the payment from American Express.
                            Note: SE Numbers are assigned by American Express.
                         */

                    Long amexPayeeNumber = Long.valueOf(m.group("amexPayeeNumber"));

                        /*
                           amexSeNumber
                             Numeric String 10 bytes long, required
                             This field contains the Service Establishment (SE) Number of the merchant being reconciled, which may not necessarily be the same SE receiving payment (see Field 1, AMEX_PAYEE_NUMBER).
                             This is the SE Number under which the transactions were sub-mitted, which usually corresponds to the physical location.
                         */
                    Long amexSeNumber    = Long.valueOf(m.group("amexSeNumber"));
                        /*
                           seUnitNumber
                             This field contains the merchant-assigned SE Unit Number (usually an internal, store identifier code) that corresponds to a specific store or location.
                             If no value is assigned, this field is character space filled.
                         */
                    String seUnitNumber = m.group("seUnitNumber").replaceAll("(?:^\\p{Blank}|\\p{Blank}$)","");

                        /*
                           paymentNumber
                             This field contains the Payment Number, a reference number used by the American Express Payee to reconcile the daily settlement to the daily payment.
                         */
                    String paymentNumber = m.group("paymentNumber");

                        /*
                           seBusinessDate
                              This field contains the SE Business Date assigned to this submission by the submitting merchant location.
                         */
                    Date seBusinessDate = julianDate.parse(m.group("seBusinessDate"));

                        /*
                           amexProcessingDate
                             This field contains the American Express Transaction Processing Date, which is used to determine the payment date.
                         */
                    Date amexProcessingDate = julianDate.parse(m.group("amexProcessingDate"));


                        /*

                        socInvoiceNumber
                            This field contains the Summary of Charge (SOC) Invoice Number.

                            For electronically submitted SOCs where the TBT_IDENTIFICATION_NUMBER (field 5 in the GFSG
                            Submission file) is populated with all zeros, this value is the concatenation of the Julian
                            Date (positions 60-62) and the last three digits of the PCID number under which this SOC was
                            submitted (positions 63-65).

                            For electronically submitted SOCs where the TBT_IDENTIFICATION_NUMBER (field 5 in the GFSG
                            Submission file) is populated with any numeric value other than zero, this value will be the
                            last six digits of the TBT_IDENTIFICATION_NUMBER under which this SOC was submitted
                            (positions 60-65).
                         */
                    Long socInvoiceNumber = Long.valueOf(m.group("socInvoiceNumber"));

                        /*
                          socInvoiceAmount
                             This field contains the Summary of Charge (SOC) Amount originally submitted for payment.
                             Note: For US Dollar (USD) and Canadian Dollar (CAD) trans-actions, two decimal places are implied.
                         */
                    Long socInvoiceAmount = 0L;
                    AmexSignSuffix ps = AmexSignSuffix.fromCharacter(m.group("socInvoiceSuffix"));
                    if (ps != null )
                        socInvoiceAmount = ps.toLong(m.group("socInvoicePrefix"));

                        /*
                          discountAmount
                            This field contains the total Discount Amount, based on socAmount and discountRate
                         */
                    Long discountAmount = 0L;
                    ps = AmexSignSuffix.fromCharacter(m.group("discountAmountSuffix"));
                    if (ps != null )
                        discountAmount = ps.toLong(m.group("discountAmountPrefix"));

                        /*
                          serviceFeeAmount
                            This field contains the total Service Fee Amount, based on socamount and serviceFeeRate
                         */
                    Long serviceFeeAmount = 0L;
                    ps = AmexSignSuffix.fromCharacter(m.group("serviceFeeSuffix"));
                    if (ps != null )
                        serviceFeeAmount = ps.toLong(m.group("serviceFeePrefix"));

                        /*
                          netSOCAmount
                            This field contains the Net SOC (Summary of Charge) Amount submitted to American Express for
                            payment, which is the sum total of  socAmount, less discountAmount and serviceFeeAmount
                         */
                    Long netSOCAmount = 0L;
                    ps = AmexSignSuffix.fromCharacter(m.group("netSOCSuffix"));
                    if (ps != null )
                        netSOCAmount = ps.toLong(m.group("netSOCPrefix"));

                        /*
                          discountRate
                            This field contains the Discount Rate (decimal place value) used to calculate the amount
                            American Express charges a merchant for services provided per the American Express Card
                            Acceptance Agreement.
                         */
                    Long discountRate = Long.valueOf( m.group("discountRate"));

                        /*
                           serviceFeeRate
                             This field contains the Service Fee Rate (decimal place value) used to calculate the amount
                             American Express charges a merchant as service fees.
                             Service fees are assessed only in certain situations and may not apply to all SEs.
                         */
                    Long serviceFeeRate = Long.valueOf(m.group("serviceFeeRate"));

                        /*
                           amexGrossAmount
                             This field contains the gross amount of American Express charges submitted in the original
                             SOC amount.
                         */
                    Long amexGrossAmount = 0L;
                    ps = AmexSignSuffix.fromCharacter(m.group("amexGrossSuffix"));
                    if (ps != null )
                        amexGrossAmount = ps.toLong(m.group("amexGrossPrefix"));
                        /*
                           amexROCCount
                              This field contains the quantity of American Express charges submitted in this Summary of
                              Charge (SOC). This entry is always positive, which is indicated by an upper-case alpha
                              code used in place of the last (least significant) digit.
                         */
                    Long amexROCCount = 0L;
                    ps = AmexSignSuffix.fromCharacter(m.group("amexROCCountSuffix"));
                    if (ps != null )
                        amexROCCount = ps.toLong(m.group("amexROCCountPrefix"));

                        /*
                           trackingId
                              This field contains the Tracking ID, which holds SOC processing information.
                         */
                    Long trackingId = Long.valueOf(m.group("trackingId"));
                        /*
                          cpcIndicator
                             This field contains the CPC Indicator, which indicates whether the batch that corresponds
                             to this SOC detail record contains CPC/Corporate Purchasing Card (a.k.a., CPS/Corporate
                             Purchasing Solutions Card) transactions. Valid entries include:
                                P = CPC/CPS Card transactions (special pricing applied)
                                ~ = Non-CPC/CPS Card transactions
                                Note: Tilde (~) represents a character space.
                         */
                    String cpcIndicator = m.group("cpcIndicator");

                        /*
                           amexROCCountPOA
                              This field contains the quantity of American Express charges submitted in this Summary of
                              Charge (SOC). This entry is always positive
                         */
                    Long amexROCCountPOA = 0L;
                    ps = AmexSignSuffix.fromCharacter(m.group("amexROCCountPOASuffix"));
                    if (ps != null )
                        amexROCCountPOA = ps.toLong(m.group("amexROCCountPOAPrefix"));

                    continue;
                }


                m = ROC_DETAIL_311.matcher(line);
                if ( m.matches()){
                    System.out.println("Got: ROC DETAIL: " + line);

                    continue;
                }

                m = ADJUSTMENT_DETAIL_230.matcher(line);
                if ( m.matches()){
                    System.out.println("Got: CHARGEBACK DETAIL");
                        /*
                           amexPayeeNumber
                            Numeric String 10 bytes long, required
                            This field contains the Service Establishment (SE) Number of the merchant that received the payment from American Express.
                            Note: SE Numbers are assigned by American Express.
                         */

                    Long amexPayeeNumber = Long.valueOf(m.group("amexPayeeNumber"));

                        /*
                           amexSeNumber
                             Numeric String 10 bytes long, required
                             This field contains the Service Establishment (SE) Number of the merchant being reconciled, which may not necessarily be the same SE receiving payment (see Field 1, AMEX_PAYEE_NUMBER).
                             This is the SE Number under which the transactions were sub-mitted, which usually corresponds to the physical location.
                         */
                    Long amexSeNumber    = Long.valueOf(m.group("amexSeNumber"));
                        /*
                           seUnitNumber
                             This field contains the merchant-assigned SE Unit Number (usually an internal, store identifier code) that corresponds to a specific store or location.
                             If no value is assigned, this field is character space filled.
                         */
                    String seUnitNumber = m.group("seUnitNumber").replaceAll("(?:^\\p{Blank}|\\p{Blank}$)","");

                        /*
                           paymentNumber
                             This field contains the Payment Number, a reference number used by the American Express Payee to reconcile the daily settlement to the daily payment.
                         */
                    String paymentNumber = m.group("paymentNumber");

                        /*
                           amexProcessDate
                             This field contains the American Express Transaction Processing Date, which is used to determine the payment date.
                         */
                    Date amexProcessDate = julianDate.parse(m.group("amexProcessDate"));


                        /*

                        adjustmentNumber
                            This field contains the Summary of Charge (SOC) Invoice Number.

                            For electronically submitted SOCs where the TBT_IDENTIFICATION_NUMBER (field 5 in the GFSG
                            Submission file) is populated with all zeros, this value is the concatenation of the Julian
                            Date (positions 60-62) and the last three digits of the PCID number under which this SOC was
                            submitted (positions 63-65).

                            For electronically submitted SOCs where the TBT_IDENTIFICATION_NUMBER (field 5 in the GFSG
                            Submission file) is populated with any numeric value other than zero, this value will be the
                            last six digits of the TBT_IDENTIFICATION_NUMBER under which this SOC was submitted
                            (positions 60-65).
                         */
                    Long adjustmentNumber = Long.valueOf(m.group("adjustmentNumber"));

                        /*
                          socAmount
                             This field contains the Summary of Charge (SOC) Amount originally submitted for payment.
                             Note: For US Dollar (USD) and Canadian Dollar (CAD) trans-actions, two decimal places are implied.
                         */
                    Long adjustmentAmount = 0L;
                    AmexSignSuffix ps = AmexSignSuffix.fromCharacter(m.group("adjustmentAmountSuffix"));
                    if (ps != null )
                        adjustmentAmount = ps.toLong(m.group("adjustmentAmountPrefix"));

                        /*
                          discountAmount
                            This field contains the total Discount Amount, based on socAmount and discountRate
                         */
                    Long discountAmount = 0L;
                    ps = AmexSignSuffix.fromCharacter(m.group("discountAmountSuffix"));
                    if (ps != null )
                        discountAmount = ps.toLong(m.group("discountAmountPrefix"));

                        /*
                          serviceFeeAmount
                            This field contains the total Service Fee Amount, based on socamount and serviceFeeRate
                         */
                    Long serviceFeeAmount = 0L;
                    ps = AmexSignSuffix.fromCharacter(m.group("serviceFeeAmountSuffix"));
                    if (ps != null )
                        serviceFeeAmount = ps.toLong(m.group("serviceFeeAmountPrefix"));

                        /*
                          netAdjustmentAmount
                            This field contains the Net SOC (Summary of Charge) Amount submitted to American Express for
                            payment, which is the sum total of  socAmount, less discountAmount and serviceFeeAmount
                         */
                    Long netAdjustmentAmount = 0L;
                    ps = AmexSignSuffix.fromCharacter(m.group("netAdjustmentAmountSuffix"));
                    if (ps != null )
                        netAdjustmentAmount = ps.toLong(m.group("netAdjustmentAmountPrefix"));

                        /*
                          discountRate
                            This field contains the Discount Rate (decimal place value) used to calculate the amount
                            American Express charges a merchant for services provided per the American Express Card
                            Acceptance Agreement.
                         */
                    Long discountRate = Long.valueOf( m.group("discountRate"));

                        /*
                           serviceFeeRate
                             This field contains the Service Fee Rate (decimal place value) used to calculate the amount
                             American Express charges a merchant as service fees.
                             Service fees are assessed only in certain situations and may not apply to all SEs.
                         */
                    Long serviceFeeRate = Long.valueOf(m.group("serviceFeeRate"));

                    String cardmemberNumber = m.group("cardmemberNumber");
                    String adjustmentReason = m.group("adjustmentReason");


                    continue;
                }

                m = DFTRL.matcher(line);
                if (m.matches()){

                    LOGGER.debug("{} Got: DFTRL", runId);
                        /*
                        dataFileTrailerRecordType
                           This field contains the constant literal “DFTRL”, a Record Type code that indicates that
                           this is a Data File Trailer Record.
                        */
                    String dataFileTrailerRecordType = m.group("dataFileTrailerRecordType");

                        /*
                            dataFileTrailerDate
                              This field contains the File Creation Date.
                            dataFileTrailerTime
                              This field contains the File Creation Time (24-hour format), when the file was created.
                        */
                    DateFormat trailerDateTime = new SimpleDateFormat("MMddyyyyHHmm");
                    Date dataFileTrailerDateTime = trailerDateTime.parse(
                            m.group("dataFileTrailerDate") +
                                    m.group("dataFileTrailerTime"));

                        /*
                            dataFileTrailerFileID
                               This field contains an American Express, system-generated, File ID number that uniquely
                               identifies this data file.
                         */
                    Long dataFileTrailerFileID = Long.valueOf( m.group("dataFileTrailerFileID") );
                        /*
                            dataFileTrailerFileName
                               This field contains the File Name (as entered in the American Express data distribution
                               database) that corresponds to Field 4, dataFileTrailerFileID.
                         */
                    String  dataFileTrailerFileName = m.group("dataFileTrailerFileName");
                        /*
                            dataFileTrailerRecipientKey
                               This field contains the Recipient Key, a unique, AmericanExpress, system-generated number
                               that identifies this data file.
                               Note: This number is unique to each individual file.
                         */
                    String dataFileTrailerRecipientKey = m.group("dataFileTrailerRecipientKey");

                        /*
                            dataFileTrailerRecordCount
                               This field contains the Record Count for all items in this datafile, including the header
                               and trailer records.
                         */

                    Long dataFileTrailerRecordCount = Long.valueOf(m.group("dataFileTrailerRecordCount"));

                    LOGGER.debug("'RunID': '{}', 'dataFileTrailerDateTime': '{}' , 'dataFileTrailerFileID': {}, 'dataFileTrailerFileName': '{}', " +
                                    "'dataFileTrailerRecipientKey': '{}', 'dataFileTrailerRecordCount'={} ",
                            runId, postgresTsWithTz.format(dataFileTrailerDateTime), dataFileTrailerFileID, dataFileTrailerFileName, dataFileTrailerRecipientKey, dataFileTrailerRecordCount);
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

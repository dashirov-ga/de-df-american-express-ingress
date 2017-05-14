package ly.generalassemb.de.datafeeds.americanExpress.ingress;

import batch.JobStarting;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Tag;
import com.jcraft.jsch.*;
import com.snowplowanalytics.snowplow.tracker.DevicePlatform;
import com.snowplowanalytics.snowplow.tracker.Tracker;
import com.snowplowanalytics.snowplow.tracker.emitter.BatchEmitter;
import com.snowplowanalytics.snowplow.tracker.events.Event;
import com.snowplowanalytics.snowplow.tracker.http.OkHttpClientAdapter;
import com.squareup.okhttp.OkHttpClient;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN.*;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.AmountParser;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.RedshiftManifest;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.RedshiftManifestEntry;
import org.apache.commons.cli.*;
import org.joda.time.DateTime;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dashirov on 5/10/17.
 */
public class FeedHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(FeedHandler.class);
    private static final Set<String> skipProcessingStepsSet = new TreeSet<>();
    private static final DateFormat df = new SimpleDateFormat("yyyyMMdd");
    private static final DateFormat postgresTsWithTz = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSX");
    private static final DateFormat julianDate = new SimpleDateFormat("yyyyDDD");


    private static final Pattern ADJUSTMENT_DETAIL_230 = Pattern.compile("^(?<amexPayeeNumber>\\p{Digit}{10})(?<amexSeNumber>[\\p{Alnum}]{10})(?<seUnitNumber>[\\p{Alnum}\\p{Blank}]{10})(?<paymentYear>\\p{Digit}{4})(?<paymentNumber>(?<paymentNumberJulianDate>\\p{Digit}{3})(?<paymentNumberRecordTypeIndicator>\\p{Alnum}{1})(?<paymentNumberSequence>\\p{Digit}{4}))(?<recordType>2)(?<detailRecordType>30)(?<amexProcessDate>(?<amexProcessDateYear>\\p{Digit}{4})(?<amexProcessJulianDate>\\p{Digit}{3}))(?<adjustmentNumber>\\p{Digit}{6})(?<adjustmentAmount>(?<adjustmentAmountPrefix>\\p{Digit}{8})(?<adjustmentAmountSuffix>[A-R}{]{1}))(?<discountAmount>(?<discountAmountPrefix>\\p{Digit}{8})(?<discountAmountSuffix>[A-R}{]{1}))(?<serviceFeeAmount>(?<serviceFeeAmountPrefix>\\p{Digit}{6})(?<serviceFeeAmountSuffix>[A-R}{]{1}))(?<filler13>000000\\{)(?<netAdjustmentAmount>(?<netAdjustmentAmountPrefix>\\p{Digit}{8})(?<netAdjustmentAmountSuffix>[A-R}{]{1}))(?<discountRate>\\p{Digit}{5})(?<serviceFeeRate>\\p{Digit}{5})(?<filler17>00000)(?<filler18>0000000000\\{)(?<cardmemberNumber>\\p{Alnum}{17})(?<adjustmentReason>[\\p{ASCII}]{280})(?<filler21>\\p{ASCII}{3})(?<filler22>\\p{ASCII}{3})(?<filler23>\\p{Blank}{15})(?<filler24>\\p{ASCII}{1})(?<filler25>\\p{ASCII}{6})$");


    private static final TransferQueue<String> loadableCSVDataFiles = new LinkedTransferQueue<>();
    private static final TransferQueue<Map<String, Object>> archivableCSVDataFiles = new LinkedTransferQueue<>();
    private static final TransferQueue<AmazonS3URI> s3Manifest = new LinkedTransferQueue<>();
    private static final TransferQueue<List<Calendar>> datesToProcess = new LinkedTransferQueue<>();

    private static final ExecutorService s3SinkExecutorService = Executors.newFixedThreadPool(5);
    private static final ExecutorService pgSinkExecutorService = Executors.newFixedThreadPool(2);

    private static Tracker tracker;
    private static Config configuration;
    private static final UUID runId = UUID.randomUUID();

    public static Tracker getTracker() {
        return tracker;
    }

    public static void setTracker(Tracker tracker) {
        FeedHandler.tracker = tracker;
    }

    private static void generateTodos(Calendar fromDate, Calendar uptoDate) throws InterruptedException {

        // TODO: Too convoluted - rewrite without use of old ass Calendar with either Joda Time or Java 8 Time, which are pretty much the same
        DateTime dateTime1 = new DateTime(fromDate);
        DateTime dateTime2 = new DateTime(uptoDate);

        while (!dateTime1.isAfter(dateTime2)) {
            Calendar item1 = Calendar.getInstance();
            item1.setTimeZone(TimeZone.getTimeZone("UTC"));
            item1.setTime(dateTime1.toDate());
            Calendar item2 = (Calendar) item1.clone();
            item2.add(Calendar.DATE, 1);
            item2.add(Calendar.MILLISECOND, -1);
            LOGGER.info("TODO: {} - {}", postgresTsWithTz.format(item1.getTime()), postgresTsWithTz.format(item2.getTime()));
            List<Calendar> todoItem = Collections.unmodifiableList(Arrays.asList(item1, item2));
            datesToProcess.add(todoItem);
            dateTime1 = dateTime1.plusDays(1);
        }

    }

    private static void init(String[] args) throws InterruptedException, ParseException, MalformedURLException {
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        postgresTsWithTz.setTimeZone(TimeZone.getTimeZone("UTC"));

        final Options options = new Options();
        /*
             Specify path to configuration file
             Command Line option -c <path to configuration file>
         */
        final Option configOpt = Option.builder("c").hasArg(true).desc("Path to alternative configuration file").required(false).type(String.class).build();
        options.addOption(configOpt);

        /*
            Skip processing step
            Command line option -x <clean|s3|redshift[,clean|s3|redshift]>
         */
        final Option skipProcessingStepOpt = Option.builder("x").hasArg(true).desc("Skip processing step(s): [s3, redshift, clean]").required(false).type(String.class).build();
        options.addOption(skipProcessingStepOpt);

        try {
            final CommandLine cmd = new DefaultParser().parse(options, args);
            // find config first
            // if -c <filepath> paramter passed - it will be used first

            URL configFileURL = null;
            String configurationFilePath = cmd.getOptionValue("c");
            if (configurationFilePath != null) {
                File configurationFile = new File(configurationFilePath);
                if (!configurationFile.exists())
                    throw new RuntimeException("Configuration file spacified with -c command line option does not exist or can not be found");
                if (!configurationFile.isFile())
                    throw new RuntimeException("Configuration file spacified with -c command line option exists, but is not a plain file");
                configFileURL = configurationFile.getAbsoluteFile().toURI().toURL();
            }
            configuration = new Config(configFileURL);
            if (configuration.get().isEmpty())
                throw new RuntimeException("Configuration not loaded!");

            // Snowplow Tracker
            setTracker(
                    new Tracker.TrackerBuilder(BatchEmitter.builder()
                            .httpClientAdapter(OkHttpClientAdapter.builder()
                                    .url(configuration.get().getString("monitoring.snowplow.url"))
                                    .httpClient(new OkHttpClient())
                                    .build())
                            .build(), configuration.get().getString("monitoring.snowplow.namespace"),
                            configuration.get().getString("monitoring.snowplow.application"))
                            .base64(true)
                            .platform(DevicePlatform.ServerSideApp)
                            .build()
            );

            String skipProcessingSteps = cmd.getOptionValue("x");
            if (skipProcessingSteps != null) {
                for (String skipProc : skipProcessingSteps.split("\\s*,\\s*")
                        ) {
                    skipProcessingStepsSet.add(skipProc);
                    LOGGER.warn("Skipping Processing Step {}", skipProc);
                }
            }

            // Put the redshift driver at the end so that it doesn't
            // conflict with postgres queries
            java.util.Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver d = drivers.nextElement();
                if (d.getClass().getName().equals("com.amazon.redshift.jdbc41.Driver") || d.getClass().getName().equals("com.amazon.redshift.jdbc42.Driver")) {
                    try {
                        DriverManager.deregisterDriver(d);
                        DriverManager.registerDriver(d);
                    } catch (SQLException e) {
                        throw new RuntimeException("Could not deregister redshift driver");
                    }
                    break;
                }
            }


        } catch (ParseException e) {
            LOGGER.warn("Failed to parse command line arguments", e);
            throw new RuntimeException(e);
        }
        LOGGER.info("Running with options: ");
        LOGGER.info("   Skip steps: " + skipProcessingStepsSet.toString());

    }

    public static void main(String[] args) throws InterruptedException, MalformedURLException, ParseException {
        /*
            Read command line arguments and load initial configuration
         */
        init(args);
        Event startEvent = new JobStarting().withRunId(runId.toString()).getEvent(null);
        tracker.track(startEvent);
        LOGGER.debug("{} Starting.", runId);

        /*
           A. Establish an SFTP Session
           B. List new data files
           C. For each new file found:
              1. Download the file
              2. Parse Records
              3. Validate detail records against summary records
              4. Validate data file footer ( record count ) against contents
              5. Extract detail records into detail recordset
              6. Extract summary records into summary recordset
              7. Establish set referential integrity
              8. Upload resulting data sets to S3
              9. Load data into Redshift
              10. Can't delete source file - it is automatically moved from incoming to sent directory where it
                  will remain for 24 hours prior to being deleted

           Steps skippable:
              a. clean-local  - do not delete temporary data files holding contents downloaded from Amex
              b. s3           - do not upload data to s3 data lake ( also prevents redshift data load and deletion of remote files )
              c. redshift     - do not load data into redshift ( prevents deletion of remote files )
              d. clean-remote - do not delete files on Amex servers that were successfully processed and loaded into GA systems
         */


        JSch ssh = new JSch();
        ssh.setLogger(new SFTPLogger());
        String user = configuration.get().getString("source.amex.sftp.user");
        String host = configuration.get().getString("source.amex.sftp.host");
        int port = configuration.get().getInt("source.amex.sftp.port");
        String private_key = configuration.get().getString("source.amex.sftp.private_key");
        String public_key = configuration.get().getString("source.amex.sftp.public_key");
        String inDirectory = configuration.get().getString("source.amex.sftp.directory");
        String fileNamePattern = configuration.get().getString("source.amex.sftp.filenamepattern");
        Pattern FilenamePattern = Pattern.compile(fileNamePattern);


        try {
            LOGGER.debug("Private Key:{}", private_key);

            ssh.setKnownHosts("/Users/dashirov/Source/GA/de-df-american-express-ingress/src/test/resources/known_hosts");
            ssh.addIdentity(user, private_key.getBytes("US-ASCII"), public_key.getBytes("US-ASCII"), null);
            Session session = ssh.getSession(user, host, port);
            LOGGER.debug("{} SSH session created for {} to host {} on port {}.", runId, user, host, port);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "yes");
            session.setConfig(config);

            session.connect();
            LOGGER.debug("{} SSH session connected to host {} on {} as user {}", runId, session.getHost(), session.getPort(), session.getUserName());

            Channel channel = session.openChannel("sftp");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect();
            LOGGER.debug("{} SFTP shell channel connected.", runId);

            ChannelSftp c = (ChannelSftp) channel;
            c.cd(inDirectory);

            final ArrayList<String> toBeDownloaded = new ArrayList<>();
            ChannelSftp.LsEntrySelector selector = new ChannelSftp.LsEntrySelector() {
                @Override
                public int select(ChannelSftp.LsEntry entry) {
                    Matcher m = FilenamePattern.matcher(entry.getFilename());
                    SftpATTRS attr = entry.getAttrs();
                    if (m.find() && !attr.isDir() && !attr.isLink()) {
                        LOGGER.debug("{} Found file {}. Will download.", runId, entry.getFilename());
                        toBeDownloaded.add(entry.getFilename());
                    }
                    return CONTINUE;
                }
            };
            try {
                c.ls(inDirectory, selector);
            } catch (SftpException e) {
                LOGGER.error("{} Error listing directory {}", runId, inDirectory, e);
            }
            final ArrayList<File> filesDownloaded = new ArrayList<>();
            for (String fileName : toBeDownloaded) {
                File remoteFile = new File(inDirectory, fileName);
                File localFile = File.createTempFile(fileName, ".tmp");
                if (!skipProcessingStepsSet.contains("clean-local"))
                    localFile.deleteOnExit();
                LOGGER.debug("{} Downloading {} to {}", runId, remoteFile.getPath(), localFile.getAbsolutePath());
                c.get(remoteFile.getPath(), localFile.getAbsolutePath());
                filesDownloaded.add(localFile);
            }

            for (File inputFile : filesDownloaded) {
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                List<Summary> summaries = new ArrayList<>();
                List<SOCDetail> socDetails = new ArrayList<>();
                List<ROCDetail> rocDetails = new ArrayList<>();
                List<AdjustmentDetail> adjustmentDetails = new ArrayList<>();
                String line;
                int lineNumber;
                while ((line = reader.readLine()) != null) {
                    Matcher m;
                    System.out.println(line);
                    m = Summary.pattern.matcher(line);
                    if (m.matches()) {

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
                        summaries.add(summary);
                        LOGGER.debug(summary.toString());
                        continue;
                    }

                    m = SOCDetail.pattern.matcher(line);
                    if (m.matches()) {
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
                        socDetails.add(socDetail);
                        LOGGER.debug(socDetail.toString());
                        continue;
                    }


                    m = ROCDetail.pattern.matcher(line);
                    if (m.matches()) {
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
                        rocDetails.add(rocDetail);

                        LOGGER.debug(rocDetail.toString());
                        continue;
                    }

                    m = AdjustmentDetail.pattern.matcher(line);
                    if (m.matches()) {
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
                        adjustmentDetails.add(adjustmentDetail);

                        LOGGER.debug(adjustmentDetail.toString());
                        continue;
                    }

                    m = DataFileTrailer.pattern.matcher(line);
                    if (m.matches()) {
                        DateFormat trailerDateTime = new SimpleDateFormat("MMddyyyyHHmm");

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
                if (summaries.size() > 0) {
                    File summaryFile = File.createTempFile("summary-" + runId + "-", ".csv");
                    if (!skipProcessingStepsSet.contains("clean-local"))
                        summaryFile.deleteOnExit();
                    else
                        LOGGER.debug("Summary CSV File: {}", summaryFile.getPath());
                    Summary.writeCSVFile(summaryFile.getPath(), summaries);
                }
                if (adjustmentDetails.size() > 0) {
                    File adjustmentDetailsFile = File.createTempFile("adjustments-" + runId + "-", ".csv");
                    if (!skipProcessingStepsSet.contains("clean-local"))
                        adjustmentDetailsFile.deleteOnExit();
                    else
                        LOGGER.debug("Adjustment Details CSV File: {}", adjustmentDetailsFile.getPath());
                    AdjustmentDetail.writeCSVFile(adjustmentDetailsFile.getPath(), adjustmentDetails);
                }
            }
            c.exit();
            LOGGER.debug("{} Done.", runId);


        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        // When all parallel tasks are complete, combine the output into a single redshift upload
        if (!skipProcessingStepsSet.contains("redshift"))
            uploadToRedshiftTask();
        else
            LOGGER.info("Skipping Redshift upload.");
        System.out.println("Finished all threads");
        System.exit(0);

    }

    // This should take a manifest, not sql statement
    private static void uploadToRedshiftTask() {
        // If s3 files were uploaded
        try {
            if (s3Manifest.size() > 0 && !skipProcessingStepsSet.contains("redshift")) {
                String key = "manifest/" + runId + ".json";
                String bucket = configuration.get().getString("sink.s3.bucket.name");
                AmazonS3URI manifestURI = new AmazonS3URI("s3://" + bucket + "/" + key); // validate!

                RedshiftManifest manifest = new RedshiftManifest();
                while (!s3Manifest.isEmpty()) {
                    manifest.addEntry(new RedshiftManifestEntry(true, s3Manifest.take()));
                }

                LOGGER.info("Loading {}", manifest.toString());
                AWSCredentialsProvider credentialsProvider;
                if (configuration.get().getString("sink.s3.credentials.accessKey") == null ||
                        configuration.get().getString("sink.s3.credentials.secretKey") == null) {
                    credentialsProvider = new DefaultAWSCredentialsProviderChain();
                } else {
                    credentialsProvider = new AWSStaticCredentialsProvider(
                            new BasicAWSCredentials(
                                    configuration.get().getString("sink.s3.credentials.accessKey"),
                                    configuration.get().getString("sink.s3.credentials.secretKey")
                            )
                    );
                }
                AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                        .withRegion(Regions.US_EAST_1)
                        .withCredentials(credentialsProvider)
                        .withAccelerateModeEnabled(false)
                        .build();

                ObjectMetadata manifestMetadata = new ObjectMetadata();
                manifestMetadata.setContentType("application/json");
                manifestMetadata.setContentEncoding("UTF-8");

                PutObjectRequest req = new PutObjectRequest(
                        bucket,
                        key, new ByteArrayInputStream(manifest.toString().getBytes(StandardCharsets.UTF_8)), manifestMetadata);
                s3.putObject(req);

                // Class.forName(configuration.get().getString("sink.redshift.jdbc.driver"));
                // DriverManager.registerDriver (new configuration.get().getString("sink.redshift.jdbc.driver") );

                Connection c = DriverManager.getConnection(
                        configuration.get().getString("sink.redshift.jdbc.url"),
                        configuration.get().getString("sink.redshift.jdbc.usr"),
                        configuration.get().getString("sink.redshift.jdbc.pwd")
                );

                c.setSchema(configuration.get().getString("sink.redshift.jdbc.schema"));

                PreparedStatement session_setup = c.prepareStatement("SET SEARCH_PATH TO " + configuration.get().getString("sink.redshift.jdbc.schema") + ",public;");
                session_setup.execute();

                // THERE ARE DUPLICATE RECORDS IN TRANSACTioN SEARCH REPORTS THAT SPAWN DIFFERENT TIME FRAMES
                // BASED ON OBSERVED SAMPLES THE RECORDS APPEAR IDENTICAL. INSTEAD OF A STRAIGHT LOAD, TRY LOADING
                // INTO TEMPORARY TABLE, DELETE CONFLICTING RECORDS FROM PRODUCTION, THEN RE-INSERT. DO ALL IN TRANSACTION.
                c.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                c.setAutoCommit(false);

                String creteTemp = "CREATE TEMPORARY TABLE \n" +
                        "temp_paypal_braintree_revenue_activity \n" +
                        "( LIKE paypal_braintree_revenue_activity  );";
                LOGGER.info(creteTemp);
                PreparedStatement s = c.prepareStatement(creteTemp);
                s.execute();

                /**
                 copy customer
                 from 's3://mybucket/cust.manifest'
                 iam_role 'arn:aws:iam::0123456789012:role/MyRedshiftRole'
                 manifest;

                 */
                String copyCommand =
                        "COPY  temp_paypal_braintree_revenue_activity " +
                                " (id, type, status, created_at, order_id, amount, tax_amount, service_fee_amount, " +
                                " merchant_account_id, channel, currency_iso_code, customer_email, customer_company, " +
                                " customer_first_name, customer_last_name, customer_id, settlement_batch_id, updated_at, " +
                                " processor_response_code, processor_response_text, subscription_id,  " +
                                " credit_card_type, credit_card_last4, credit_card_unique_id,  is_disbursed, disbursement_date, settlement_currency_exchange_rate, " +
                                " settlement_amount,settlement_currency_iso_code) " +
                                " FROM '" + manifestURI.getURI() + "'\n " +
                                " CREDENTIALS '" + configuration.get().getString("sink.redshift.jdbc.credentials") + "' " +
                                " MANIFEST CSV IGNOREHEADER 1;";

                LOGGER.info(copyCommand);
                s = c.prepareStatement(copyCommand);
                s.execute();

                String deleteOld = "DELETE FROM paypal_braintree_revenue_activity \n" +
                        "WHERE EXISTS ( SELECT 1 FROM temp_paypal_braintree_revenue_activity  " +
                        "WHERE temp_paypal_braintree_revenue_activity.id = paypal_braintree_revenue_activity.id )";
                LOGGER.info(deleteOld);
                s = c.prepareStatement(deleteOld);
                s.execute();

                String insertNew = "INSERT INTO paypal_braintree_revenue_activity\n" +
                        "select t.* \n" +
                        "from temp_paypal_braintree_revenue_activity t\n" +
                        "left outer join  paypal_braintree_revenue_activity p using (id)\n" +
                        "where p.id is null";
                LOGGER.info(insertNew);
                s = c.prepareStatement(insertNew);
                s.execute();

                c.commit();
                c.setAutoCommit(true);


            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            // }  catch (ClassNotFoundException e) {
            //  e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static AmazonS3URI uploadToDataLakeTask(Map<String, Object> fileMetadata) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String from = df.format((Date) fileMetadata.get("from"));
        String upto = df.format((Date) fileMetadata.get("upto"));
        String key = "data/" + (from.equals(upto) ? from : from + "-" + upto) + ".csv";
        String file = (String) fileMetadata.get("file");
        String bucket = configuration.get().getString("sink.s3.bucket.name");

        AWSCredentialsProvider credentialsProvider;
        if (configuration.get().getString("sink.s3.credentials.accessKey") == null ||
                configuration.get().getString("sink.s3.credentials.secretKey") == null) {
            credentialsProvider = new DefaultAWSCredentialsProviderChain();
        } else {
            credentialsProvider = new AWSStaticCredentialsProvider(
                    new BasicAWSCredentials(
                            configuration.get().getString("sink.s3.credentials.accessKey"),
                            configuration.get().getString("sink.s3.credentials.secretKey")
                    )
            );
        }
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(credentialsProvider)
                .withAccelerateModeEnabled(false)
                .build();


        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("Contents", "Braintree Credit Card Transactions"));
        tags.add(new Tag("PII", "TRUE"));
        tags.add(new Tag("Snapshot Date", df.format(new Date())));
        tags.add(new Tag("Format", "CSV"));
        tags.add(new Tag("CSV:Header", "TRUE"));
        tags.add(new Tag("RunId", runId.toString()));

        // tags.add(new Tag("CSV:Line-Separator", "\\n"));
        // tags.add(new Tag("CSV:Null-As", "\\N"));
        // tags.add(new Tag("Uploader:Name", FeedHandler.class.getClass().getCanonicalName()));
        // tags.add(new Tag("Uploader:S3", s3.getClass().getPackage().getImplementationVersion()));
        // tags.add(new Tag("Uploader:Version", FeedHandler.class.getClass().getPackage().getImplementationVersion()));

        File f = new File(file);
        LOGGER.info("Uploading to s3: {} bytes {}", f.length(), file);
        PutObjectRequest req = new PutObjectRequest(
                bucket,
                key,
                f); // takes File not String fileName...
        req.setTagging(new ObjectTagging(tags));
        s3.putObject(req);
        /**

         Proposed srtucture in the data lake:
         s3://ga-paypal-braintree/data/TX/2001-01-01.csv
         /manifest/runId=9af7e48431a39221666074247154197.json

         Resulting MANIFEST STRUCTURE:
         {
         "entries": [
         {"url":"s3://mybucket-alpha/2013-10-04-custdata", "mandatory":true},
         {"url":"s3://mybucket-alpha/2013-10-05-custdata", "mandatory":true},
         {"url":"s3://mybucket-beta/2013-10-04-custdata", "mandatory":true},
         {"url":"s3://mybucket-beta/2013-10-05-custdata", "mandatory":true}
         ]
         }

         THEN REDSHIFT LOAD WILL LOOK LIKE:
         copy customer
         from 's3://mybucket/cust.manifest'
         iam_role 'arn:aws:iam::0123456789012:role/MyRedshiftRole'
         manifest;

         */


        return new AmazonS3URI("s3://" + configuration.get().getString("sink.s3.bucket.name") +
                "/" + key);
    }

    class S3UploadTaskRunner implements Runnable {
        @Override
        public void run() {
            if (!skipProcessingStepsSet.contains("s3")) {
                LOGGER.info("Starting S3UploadTaskRunner...");
                // execute each thread until all downloaders are finished and there are no files to take from the queue

                try {
                    Map<String, Object> fileMeta = archivableCSVDataFiles.take();
                    LOGGER.info("S3UploadTaskRunner - got things to do");
                    AmazonS3URI s3url = uploadToDataLakeTask(fileMeta);
                    s3Manifest.add(s3url);
                    LOGGER.info("S3UploadTaskRunner - finished with {}", s3url.toString());


                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                LOGGER.info("S3UploadTaskRunner - Done, nothing else to do");
            } else {
                LOGGER.info("Skipping S3 upload.");
            }

        }
    }

    /**
     * Takes a file name from loadableCSVDataFiles queue
     * executes a function that actually loads the file into the pg db
     * repeats the cycle until indication is present that no more loadable files are to come for processing
     * then exits
     */
    class PostgresUploadTaskRunner implements Runnable {
        @Override
        public void run() {
            if (!skipProcessingStepsSet.contains("postgres")) {
                LOGGER.info("PostgresUploadTaskRunner - Starting");
                // execute each thread until all downloaders are finished and there are no files to take from the queue
                try {
                    LOGGER.info("PostgresUploadTaskRunner - Blocking for files to load");
                    String file = loadableCSVDataFiles.take();
                    LOGGER.info("PostgresUploadTaskRunner - Got things to to");
                    uploadToPostgresTask(file);
                    LOGGER.info("PostgresUploadTaskRunner - Finished {}", file);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.info("PostgresUploadTaskRunner - Exiting");
            } else {
                LOGGER.info("Skipping PostgreSQL load.");
            }
        }
    }

    /**
     * Separate function to be able to test business logic without complicated concurrency setup
     * Given a CSV file - load into the database.
     * 1. Creates a new database connection (postgres)
     * 2. Creates a temporary table modeled after production
     * 3. Copies CSV file into the temp table
     * 4. Deletes from production table those transactions that are found in the temp table
     * 5. Copies transactions from temp table to production table
     * 6. Commits the transaction and closes database connection
     * -- Returns
     *
     * @param file
     */
    private static void uploadToPostgresTask(String file) {
        try {
            LOGGER.info("Loading CSV File {} into PostgreSQL Database", file);
            FileReader reader = new FileReader(file);

            // Class.forName(configuration.get().getString("sink.postgres.jdbc.driver"));
            Connection c = DriverManager.getConnection(
                    configuration.get().getString("sink.postgres.jdbc.url"),
                    configuration.get().getString("sink.postgres.jdbc.usr"),
                    configuration.get().getString("sink.postgres.jdbc.pwd")
            );

            c.setSchema(configuration.get().getString("sink.postgres.jdbc.schema"));


            PreparedStatement session_setup = c.prepareStatement("SET SEARCH_PATH TO " + configuration.get().getString("sink.postgres.jdbc.schema") + ",public;");
            session_setup.execute();
            // THERE ARE DUPLICATE RECORDS IN TRANSACTioN SEARCH REPORTS THAT SPAWN DIFFERENT TIME FRAMES
            // BASED ON OBSERVED SAMPLES THE RECORDS APPEAR IDENTICAL. INSTEAD OF A STRAIGHT LOAD, TRY LOADING
            // INTO TEMPORARY TABLE, DELETE CONFLICTING RECORDS FROM PRODUCTION, THEN RE-INSERT. DO ALL IN TRANSACTION.
            c.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            c.setAutoCommit(false);


            PreparedStatement s = c.prepareStatement("CREATE TEMPORARY TABLE \n" +
                    "temp_paypal_braintree_revenue_activity_v4 \n" +
                    "( LIKE public.paypal_braintree_revenue_activity_v4 INCLUDING ALL );");
            s.execute();

            CopyManager copyManager = ((PGConnection) c).getCopyAPI();
            String copyCommand =
                    "COPY  temp_paypal_braintree_revenue_activity_v4 " +
                            "(id, type, status, created_at, order_id, amount, tax_amount, service_fee_amount, " +
                            "merchant_account_id, channel, currency_iso_code, customer_email, customer_company, " +
                            "customer_first_name, customer_last_name, customer_id, settlement_batch_id, updated_at, " +
                            "processor_response_code, processor_response_text, subscription_id, " +
                            "credit_card_type, credit_card_last4, credit_card_unique_id, is_disbursed, disbursement_date, settlement_currency_exchange_rate, " +
                            "settlement_amount,settlement_currency_iso_code) " +
                            "FROM STDIN\n" +
                            "WITH DELIMITER ',' CSV HEADER NULL '\\N';";
            copyManager.copyIn(copyCommand, reader);
            s = c.prepareStatement("DELETE FROM paypal_braintree_revenue_activity_v4 p\n" +
                    "WHERE EXISTS ( SELECT 1 FROM temp_paypal_braintree_revenue_activity_v4 t WHERE t.id = p.id )");
            s.execute();

            String insertCommand = "INSERT INTO public.paypal_braintree_revenue_activity_v4 \n" +
                    "select t.* \n" +
                    "from temp_paypal_braintree_revenue_activity_v4 t\n" +
                    "left outer join  public.paypal_braintree_revenue_activity_v4 p using (id)\n" +
                    "where p.id is null";
            LOGGER.info(insertCommand);
            s = c.prepareStatement(insertCommand);
            s.execute();
            c.commit();
            c.close();
            reader.close();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static class SFTPLogger implements com.jcraft.jsch.Logger {
        public boolean isEnabled(int level) {
            return true;
        }

        public void log(int level, String message) {
            switch (level) {
                case DEBUG:
                    LOGGER.debug(message);
                    break;
                case INFO:
                    LOGGER.info(message);
                    break;
                case WARN:
                    LOGGER.warn(message);
                    break;
                case ERROR:
                    LOGGER.error(message);
                    break;
                case FATAL:
                    LOGGER.error(message);
                    break;
                default:
                    break;

            }

        }
    }
}
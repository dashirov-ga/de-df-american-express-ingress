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
import com.amazonaws.services.s3.model.*;
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
import java.nio.file.Paths;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dashirov on 5/10/17.
 */
public class FeedHandler {
    private enum S3Prefix {
        EPTRN("EPTRN", "EPTRN", ".dat"),
        EPTRN_HEADER("EPTRN-HDR", "CSV", ".csv"),
        EPTRN_TRAILER("EPTRN-TRL", "CSV", ".csv"),
        EPTRN_SUMMARY("EPTRN-SUMMARY", "CSV", ".csv"),
        EPTRN_SOC_DETAIL("EPTRN-SOC-DETAIL", "CSV", ".csv"),
        EPTRN_ROC_DETAIL("EPTRN-ROC-DETAIL", "CSV", ".csv"),
        EPTRN_ADJUSTMENT_DETAIL("EPTRN-ADJ-DETAIL", "CSV", ".csv");
        private final String prefix;
        private final String format;
        private final String suffix;

        S3Prefix(String prefix, String format, String suffix) {
            this.prefix = prefix;
            this.format = format;
            this.suffix = suffix;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedHandler.class);
    private static final Set<String> skipProcessingStepsSet = new TreeSet<>();
    private static final DateFormat df = new SimpleDateFormat("yyyyMMdd");
    private static final DateFormat postgresTsWithTz = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSX");
    private static final DateFormat julianDate = new SimpleDateFormat("yyyyDDD");


    private static Tracker tracker;
    private static Config configuration;
    private static final UUID runId = UUID.randomUUID();

    public static Tracker getTracker() {
        return tracker;
    }

    public static void setTracker(Tracker tracker) {
        FeedHandler.tracker = tracker;
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
            Map<S3Prefix, List<AmazonS3URI>> redshiftLoadable = new HashMap<>();
            for (File inputFile : filesDownloaded) {
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                List<Summary> summaries = new ArrayList<>();
                List<SOCDetail> socDetails = new ArrayList<>();
                List<ROCDetail> rocDetails = new ArrayList<>();
                List<AdjustmentDetail> adjustmentDetails = new ArrayList<>();
                DataFileTrailer trailer = null;
                DataFileHeader header = null;
                String line;
                while ((line = reader.readLine()) != null) {
                    Object record;
                    if ((record = DataFileTrailer.parse(line)) != null) {
                        trailer = (DataFileTrailer) record;
                        LOGGER.debug(record.toString());
                    } else if ((record = DataFileHeader.parse(line)) != null) {
                        header = (DataFileHeader) record;
                        LOGGER.debug(record.toString());
                    } else if ((record = Summary.parse(line)) != null) {
                        LOGGER.debug(record.toString());
                        summaries.add((Summary) record);
                    } else if ((record = ROCDetail.parse(line)) != null) {
                        LOGGER.debug(record.toString());
                        rocDetails.add((ROCDetail) record);
                    } else if ((record = SOCDetail.parse(line)) != null) {
                        LOGGER.debug(record.toString());
                        socDetails.add((SOCDetail) record);
                        System.out.println(((SOCDetail) record).toString());
                    } else if ((record = AdjustmentDetail.parse(line)) != null) {
                        LOGGER.debug(record.toString());
                        adjustmentDetails.add((AdjustmentDetail) record);
                    }
                }
                // Every line in the file downloaded has been parsed, you have json and csv data available now

                String uniqueFileId = trailer.getDataFileTrailerRecipientKey().replaceAll("\\p{Blank}{2,}+", "-");
                LOGGER.debug("Uploading to s3://{}", Paths.get(configuration.get().getString("sink.s3.bucket.name"), uniqueFileId));
                if (summaries.size() > 0) {
                    File summaryFile = File.createTempFile("summary-" + runId + "-", ".csv");
                    if (!skipProcessingStepsSet.contains("clean-local"))
                        summaryFile.deleteOnExit();
                    else
                        LOGGER.debug("Summary CSV File: {}", summaryFile.getPath());
                    Summary.writeCSVFile(summaryFile.getPath(), summaries);
                    List<AmazonS3URI> entries = redshiftLoadable.computeIfAbsent(S3Prefix.EPTRN_SUMMARY, k -> new ArrayList<>());
                    entries.add(uploadToDataLakeTask(packS3UploadParameters(summaryFile.getAbsolutePath(), S3Prefix.EPTRN_SUMMARY, uniqueFileId)));

                }

                if (adjustmentDetails.size() > 0) {
                    File adjustmentDetailsFile = File.createTempFile("adjustments-" + runId + "-", ".csv");
                    if (!skipProcessingStepsSet.contains("clean-local"))
                        adjustmentDetailsFile.deleteOnExit();
                    else
                        LOGGER.debug("Adjustment Details CSV File: {}", adjustmentDetailsFile.getPath());
                    AdjustmentDetail.writeCSVFile(adjustmentDetailsFile.getPath(), adjustmentDetails);
                    List<AmazonS3URI> entries = redshiftLoadable.computeIfAbsent(S3Prefix.EPTRN_ADJUSTMENT_DETAIL, k -> new ArrayList<>());
                    entries.add(uploadToDataLakeTask(packS3UploadParameters(adjustmentDetailsFile.getAbsolutePath(), S3Prefix.EPTRN_ADJUSTMENT_DETAIL, uniqueFileId)));
                }

                if (socDetails.size() > 0) {
                    File socDetailsFile = File.createTempFile("socdetails-" + runId + "-", ".csv");
                    if (!skipProcessingStepsSet.contains("clean-local"))
                        socDetailsFile.deleteOnExit();
                    else
                        LOGGER.debug("SOC Details Details CSV File: {}", socDetailsFile.getPath());
                    SOCDetail.writeCSVFile(socDetailsFile.getPath(), socDetails);
                    List<AmazonS3URI> entries = redshiftLoadable.computeIfAbsent(S3Prefix.EPTRN_SOC_DETAIL, k -> new ArrayList<>());
                    entries.add(uploadToDataLakeTask(packS3UploadParameters(socDetailsFile.getAbsolutePath(), S3Prefix.EPTRN_SOC_DETAIL, uniqueFileId)));
                }

                if (rocDetails.size() > 0) {
                    File rocDetailsFile = File.createTempFile("rocdetails-" + runId + "-", ".csv");
                    if (!skipProcessingStepsSet.contains("clean-local"))
                        rocDetailsFile.deleteOnExit();
                    else
                        LOGGER.debug("ROC Details Details CSV File: {}", rocDetailsFile.getPath());
                    ROCDetail.writeCSVFile(rocDetailsFile.getPath(), rocDetails);
                    List<AmazonS3URI> entries = redshiftLoadable.computeIfAbsent(S3Prefix.EPTRN_ROC_DETAIL, k -> new ArrayList<>());
                    entries.add(uploadToDataLakeTask(packS3UploadParameters(rocDetailsFile.getAbsolutePath(), S3Prefix.EPTRN_ROC_DETAIL, uniqueFileId)));

                }

                uploadToDataLakeTask(packS3UploadParameters(inputFile.getAbsolutePath(), S3Prefix.EPTRN, uniqueFileId));


            }

            c.exit();

            if (!redshiftLoadable.isEmpty()) {
                // All files have been seen, parsed, split into record types and loaded to s3
                // Time to make them show up in the redshift data warehouse and or postgresql database
                for (S3Prefix type : redshiftLoadable.keySet()) {
                    if (redshiftLoadable.get(type).size() > 0) {
                        RedshiftManifest manifest = new RedshiftManifest();
                        for (AmazonS3URI file : redshiftLoadable.get(type)) {
                            manifest.addEntry(new RedshiftManifestEntry(true, file));
                        }
                        LOGGER.debug("{}: {}", type.name(), manifest.toString());
                        uploadToRedshiftTask(manifest, type);
                    }
                }
            }

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
        System.out.println("Finished all threads");
        System.exit(0);

    }

    private static Map<String, Object> packS3UploadParameters(String localFilePath, S3Prefix type, String uniqueFileId) {
        Map<String, Object> uploadTask = new HashMap<>();
        uploadTask.put("file", localFilePath);
        uploadTask.put("id", uniqueFileId);
        uploadTask.put("type", type);
        return uploadTask;
    }

    // This should take a manifest, not sql statement
    private static void uploadToRedshiftTask(RedshiftManifest manifest, S3Prefix type) {
        // If s3 files were uploaded
        String key = Paths.get("manifest", runId.toString(), type.name() + ".json").toString();
        String bucket = configuration.get().getString("sink.s3.bucket.name");
        AmazonS3URI manifestURI = new AmazonS3URI("s3://" + bucket + "/" + key); // validate!

        try {
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
                    configuration.get().getString("sink.s3.bucket.name"),
                    key,
                    new ByteArrayInputStream(manifest.toString().getBytes(StandardCharsets.UTF_8)),
                    manifestMetadata
            );
            s3.putObject(req);

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

            String targetTable = null;
            String deleteKey = null;
            switch (type) {
                case EPTRN_SUMMARY:
                    targetTable = "american_express_revenue_activity_summary";
                    deleteKey = "payment_number";
                    break;
                case EPTRN_ROC_DETAIL:
                    targetTable = "american_express_revenue_activity_record_of_charge_detail";
                    deleteKey = "payment_number";
                    break;
                case EPTRN_SOC_DETAIL:
                    targetTable = "american_express_revenue_activity_summary_of_charge_detail";
                    deleteKey = "payment_number";
                    break;
                case EPTRN_ADJUSTMENT_DETAIL:
                    targetTable = "american_express_revenue_activity_adjustment_detail";
                    deleteKey = "payment_number";
                    break;
                default:
                    break;
            }
            String creteTemp = "CREATE TEMPORARY TABLE temp_" + targetTable + "( LIKE " + targetTable + "  );";
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
                    "COPY  temp_" + targetTable +
                            " FROM '" + manifestURI.getURI() + "'\n " +
                            " CREDENTIALS '" + configuration.get().getString("sink.redshift.jdbc.credentials") + "' " +
                            " MANIFEST CSV IGNOREHEADER 1;";

            LOGGER.info(copyCommand);
            s = c.prepareStatement(copyCommand);
            s.execute();

            String deleteOld = "DELETE FROM " + targetTable +
                    " WHERE EXISTS ( SELECT 1 FROM temp_" + targetTable +
                    " WHERE temp_" + targetTable + "." + deleteKey + " = " + targetTable + "." + deleteKey + ");";
            LOGGER.info(deleteOld);
            s = c.prepareStatement(deleteOld);
            s.execute();

            String insertNew = "INSERT INTO " + targetTable +
                    " select t.* " +
                    " from temp_" + targetTable + " t" +
                    " left outer join  " + targetTable + " p using (" + deleteKey + ")" +
                    " where p." + deleteKey + " is null";
            LOGGER.info(insertNew);
            s = c.prepareStatement(insertNew);
            s.execute();

            c.commit();
            c.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static AmazonS3URI uploadToDataLakeTask(Map<String, Object> fileMetadata) {
        // This should not be triggered if s3 step is skipped
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        String file = (String) fileMetadata.get("file");
        S3Prefix constants = (S3Prefix) fileMetadata.get("type");
        String fileId = (String) fileMetadata.get("id");

        String format = constants.format;
        String key = Paths.get(fileId, constants.prefix + constants.suffix).toString();

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
        tags.add(new Tag("Contents", "American Express Credit Card Transactions"));
        tags.add(new Tag("PII", "FALSE"));
        tags.add(new Tag("PCI", "TRUE"));
        tags.add(new Tag("Snapshot Date", df.format(new Date())));
        tags.add(new Tag("Format", format));
        if (format.equals("CSV"))
            tags.add(new Tag("CSV:Header", "TRUE"));
        tags.add(new Tag("RunId", runId.toString()));

        File f = new File(file);
        LOGGER.info("Uploading to s3: {} bytes {}", f.length(), file);
        PutObjectRequest req = new PutObjectRequest(
                bucket,
                key,
                f); // takes File not String fileName...
        req.setTagging(new ObjectTagging(tags));
        s3.putObject(req);
        return new AmazonS3URI("s3://" + configuration.get().getString("sink.s3.bucket.name") +
                "/" + key);
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
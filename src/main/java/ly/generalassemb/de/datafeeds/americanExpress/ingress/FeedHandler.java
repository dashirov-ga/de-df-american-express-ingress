package ly.generalassemb.de.datafeeds.americanExpress.ingress;

import co.ga.batch.JobFailed;
import co.ga.batch.JobStarting;
import co.ga.batch.JobSucceeded;
import co.ga.batch.StepStatus;
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
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.snowplowanalytics.snowplow.tracker.DevicePlatform;
import com.snowplowanalytics.snowplow.tracker.Tracker;
import com.snowplowanalytics.snowplow.tracker.emitter.BatchEmitter;
import com.snowplowanalytics.snowplow.tracker.emitter.RequestCallback;
import com.snowplowanalytics.snowplow.tracker.events.Event;
import com.snowplowanalytics.snowplow.tracker.events.Unstructured;
import com.snowplowanalytics.snowplow.tracker.http.OkHttpClientAdapter;
import com.snowplowanalytics.snowplow.tracker.payload.TrackerPayload;
import com.squareup.okhttp.OkHttpClient;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.AmexRecordType;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.AmexFeedFileParser;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.AmexFeedFileParserOutput;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.parser.AmexFileParserFactory;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.serializer.AmexFeedFileSerializerFactory;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.RedshiftManifest;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.RedshiftManifestEntry;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.RunID;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.digest.DigestUtils;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dashirov on 5/10/17.
 */
public class FeedHandler {
    private static final AtomicInteger eventCounter = new AtomicInteger(0);

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedHandler.class);
    private static final Set<String> skipProcessingStepsSet = new TreeSet<>();
    private static final DateFormat df = new SimpleDateFormat("yyyyMMdd");
    private static final DateFormat postgresTsWithTz = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSX");
    private static final DateFormat julianDate = new SimpleDateFormat("yyyyDDD");


    private static Tracker tracker;
    private static Config configuration;
    private static final String runId = RunID.unique();

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
                            .requestCallback(
                                    new RequestCallback() {
                                        @Override
                                        public void onSuccess(int i) {
                                            int events_left = eventCounter.addAndGet(-1 * i);
                                            LOGGER.info("Snowplow: OK {} NOK 0; left to process {}", i, events_left);
                                        }

                                        @Override
                                        public void onFailure(int i, List<TrackerPayload> list) {
                                            int events_left = eventCounter.addAndGet(-1 * (i + list.size()));
                                            LOGGER.info("Snowplow: OK {} NOK {}; left to process {}", i, list.size(), events_left);
                                            LOGGER.error(list.toString());
                                        }
                                    }
                            )
                            .httpClientAdapter(OkHttpClientAdapter.builder()
                                    .url(configuration.get().getString("monitoring.snowplow.url"))
                                    .httpClient(new OkHttpClient())
                                    .build())
                            .build(), configuration.get().getString("monitoring.snowplow.namespace"),
                            configuration.get().getString("monitoring.snowplow.application"))
                            .base64(false)
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

    private static void track(Event event) {
        if (tracker != null) {
            tracker.track(event);
            eventCounter.getAndIncrement();
        }
    }

    private static void terminate(int status) {
        // TODO: This is done better with :
        //  Runtime.getRuntime().addShutdownHook(Thread)
        tracker.getEmitter().flushBuffer();
        while (eventCounter.get() > 0) {
            LOGGER.warn("Waiting for events to be delivered or error out...");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("Shutdown completed");
        System.exit(status);
    }

    public static void main(String[] args) throws InterruptedException, MalformedURLException, ParseException {
        /*
            Read command line arguments and load initial configuration
         */
        init(args);
        Map<String, Date> runTimers = new HashMap<>();
        track(Unstructured.builder().eventData(
                new JobStarting().withRunId(runId).getSelfDescribingJson()
        ).build());
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


        JSch.setLogger(new SFTPLogger());
        JSch ssh = new JSch();
        String user = configuration.get().getString("source.amex.sftp.user");
        String host = configuration.get().getString("source.amex.sftp.host");
        int port = configuration.get().getInt("source.amex.sftp.port");
        String private_key = configuration.get().getString("source.amex.sftp.private_key");
        String public_key = configuration.get().getString("source.amex.sftp.public_key");
        String inDirectory = configuration.get().getString("source.amex.sftp.directory");
        String fileNamePattern = configuration.get().getString("source.amex.sftp.filenamepattern");
        Pattern FilenamePattern = Pattern.compile(fileNamePattern);

        Date stepStart;

        // SECURE FILE TRANSFER STEP
        stepStart = new Date();
        runTimers.putIfAbsent("sft", stepStart);
        final ArrayList<Map<String, Object>> filesDownloaded = new ArrayList<>();
        try {
            track(Unstructured.builder().eventData(new StepStatus().withName("sft").withState(StepStatus.State.PENDING).withRunId(runId).withStartedAt(stepStart).getSelfDescribingJson()).build());

            LOGGER.debug("Private Key:{}", private_key);
            ssh.setKnownHosts(configuration.get().getString("source.amex.sftp.known_hosts"));
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
            ChannelSftp.LsEntrySelector selector = entry -> {
                Matcher m = FilenamePattern.matcher(entry.getFilename());
                SftpATTRS attr = entry.getAttrs();
                if (m.find() && !attr.isDir() && !attr.isLink()) {
                    LOGGER.debug("{} Found file {}. Will download.", runId, entry.getFilename());
                    toBeDownloaded.add(entry.getFilename());
                }
                return ChannelSftp.LsEntrySelector.CONTINUE;
            };
            c.ls(inDirectory, selector);
            track(Unstructured.builder().eventData(new StepStatus().withName("sft").withState(StepStatus.State.RUNNING).withRunId(runId).withStartedAt(stepStart).getSelfDescribingJson()).build());
            for (String fileName : toBeDownloaded) {
                LOGGER.debug("prototyping {}", fileName);
                Matcher m = FilenamePattern.matcher(fileName);
                if (m.matches()) {
                    String type = m.group(1);
                    File remoteFile = new File(inDirectory, fileName);
                    File localFile = File.createTempFile(fileName + "-", ".dat");
                    if (!skipProcessingStepsSet.contains("clean-local"))
                        localFile.deleteOnExit();
                    LOGGER.debug("{} Downloading {} to {}", runId, remoteFile.getPath(), localFile.getAbsolutePath());
                    c.get(remoteFile.getPath(), localFile.getAbsolutePath());

                    Map<String, Object> entry = new HashMap<>();
                    entry.put("type", type);
                    entry.put("file", localFile);
                    filesDownloaded.add(entry);
                } else
                    LOGGER.error("Whoa! second time around, no match!");
            }

            try {
                c.exit();
            } catch (Exception e) {
                LOGGER.warn("Could not close ssh/sftp communication channels cleanly. Will not fail the job, but this was the error:", e);
            }

            track(Unstructured.builder().eventData(new StepStatus().withName("sft").withState(StepStatus.State.COMPLETED).withRunId(runId).withStartedAt(stepStart).withEndedAt(new Date()).getSelfDescribingJson()).build());
        } catch (SftpException | JSchException | IOException e) {
            track(Unstructured.builder().eventData(new StepStatus().withName("sft").withState(StepStatus.State.FAILED).withRunId(runId).withStartedAt(stepStart).withEndedAt(new Date()).getSelfDescribingJson()).build());
            LOGGER.error("Step sft failed.", e);
            track(Unstructured.builder().eventData(new JobFailed().withRunId(runId).getSelfDescribingJson()).build());
            LOGGER.error("Job failed.");
            terminate(1);
        }


        // FILE PARSING STEP: All files are here, on a local file system. No SSH/SFTP communications.
        stepStart = new Date();
        runTimers.putIfAbsent("file-parse", stepStart);
        Map<AmexRecordType, List<AmazonS3URI>> redshiftLoadable = new HashMap<>();
        try {
            track(
                    Unstructured.builder().eventData(
                            new StepStatus()
                                    .withRunId(runId)
                                    .withName("file-parse")
                                    .withState(StepStatus.State.RUNNING)
                                    .withStartedAt(stepStart)
                                    .getSelfDescribingJson()
                    ).build()
            );
            for (Map<String, Object> input : filesDownloaded) {
                File inputFile = (File) input.get("file");
                String type = (String) input.get("type");
                String uniqueFileId = inputFile.getName().substring(0, inputFile.getName().indexOf('-')).replaceAll("[#]", "-");
                LOGGER.debug("Pricessing {}", uniqueFileId);

                AmexFeedFileParser feedFileParser = AmexFileParserFactory.getFeedFileParser(type);
                AmexFeedFileParserOutput parserOutput = feedFileParser.parseFile(inputFile);

                parserOutput.getFileContents().entrySet().forEach(entry -> {
                    try {
                        AmexRecordType recorType = entry.getKey();
                        if (!"".equals(recorType.getFileNamePrefix())) {
                            File serializedFile =
                                    File.createTempFile(recorType.getFileNamePrefix() + runId + "-", ".csv");
                            if (!skipProcessingStepsSet.contains("clean-local"))
                                serializedFile.deleteOnExit();
                            else
                                LOGGER.debug("{} Type, CSV File: {}", recorType.name(),
                                        serializedFile.getPath());

                            AmexFeedFileSerializerFactory.getAmexFeedFileSerializerFor(recorType)
                                    .writeCSVFile(serializedFile.getPath(), entry.getValue());
                            List<AmazonS3URI> entries =
                                    redshiftLoadable.computeIfAbsent(recorType, k -> new ArrayList<>());
                            entries.add(uploadToDataLakeTask(packS3UploadParameters(
                                    serializedFile.getAbsolutePath(), recorType, uniqueFileId)));

                        }
                    } catch (Exception ex) {
                        LOGGER.error(ex.getMessage(), ex);
                        throw new RuntimeException(ex);
                    }
                });

                uploadToDataLakeTask(packS3UploadParameters(inputFile.getAbsolutePath(), AmexRecordType.getAmexRecordTypeForFileType(type) , uniqueFileId));

                track(
                        Unstructured.builder().eventData(
                                new StepStatus()
                                        .withRunId(runId)
                                        .withName("s3-upload")
                                        .withState(StepStatus.State.COMPLETED)
                                        .withStartedAt(stepStart)
                                        .withEndedAt(new Date())
                                        .getSelfDescribingJson()
                        ).build()
                );
            }
        } catch (Exception e) {
            // had trouble reading input or creating temp files for the output. Abort the job.
            track(
                    Unstructured.builder().eventData(
                            new StepStatus()
                                    .withRunId(runId)
                                    .withName("file-parse")
                                    .withState(StepStatus.State.FAILED)
                                    .withStartedAt(stepStart)
                                    .withEndedAt(new Date())
                                    .getSelfDescribingJson()
                    ).build()
            );

            LOGGER.error("Step file-parse failed.", e);

            track(
                    Unstructured.builder().eventData(
                            new JobFailed()
                                    .withRunId(runId)
                                    .getSelfDescribingJson()
                    ).build()
            );

            LOGGER.error("Job failed.");
            terminate(1);
        }
        stepStart = new Date();
        runTimers.putIfAbsent("dw-upload", stepStart);
        try {
            if (!redshiftLoadable.isEmpty()) {
                // All files have been seen, parsed, split into record types and loaded to s3
                // Time to make them show up in the redshift data warehouse and or postgresql database
                stepStart = new Date();
                track(
                        Unstructured.builder().eventData(
                                new StepStatus()
                                        .withRunId(runId)
                                        .withName("dw-upload")
                                        .withState(StepStatus.State.RUNNING)
                                        .withStartedAt(stepStart)
                                        .getSelfDescribingJson()
                        ).build()
                );

                for (AmexRecordType type : redshiftLoadable.keySet()) {
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
            track(
                    Unstructured.builder().eventData(
                            new StepStatus()
                                    .withRunId(runId)
                                    .withName("dw-upload")
                                    .withState(StepStatus.State.COMPLETED)
                                    .withStartedAt(stepStart)
                                    .withEndedAt(new Date())
                                    .getSelfDescribingJson()
                    ).build()
            );

            LOGGER.debug("{} Done.", runId);
        } catch (SQLException e) {
            track(
                    Unstructured.builder().eventData(
                            new StepStatus()
                                    .withRunId(runId)
                                    .withName("dw-upload")
                                    .withState(StepStatus.State.FAILED)
                                    .withStartedAt(stepStart)
                                    .withEndedAt(new Date())
                                    .getSelfDescribingJson()
                    ).build()
            );

            LOGGER.error("Step dw-upload failed.", e);
            track(
                    Unstructured.builder().eventData(
                            new JobFailed()
                                    .withRunId(runId)
                                    .getSelfDescribingJson()
                    ).build()
            );

            LOGGER.error("Job failed.");
            terminate(1);
        }

        System.out.println("Finished all threads");
        track(
                Unstructured.builder().eventData(
                        new JobSucceeded()
                                .withRunId(runId)
                                .getSelfDescribingJson()
                ).build()
        );
        terminate(0);

    }

    private static Map<String, Object> packS3UploadParameters(
            String localFilePath,
            AmexRecordType type,
            String uniqueFileId) {
        Map<String, Object> uploadTask = new HashMap<>();
        uploadTask.put("file", localFilePath);
        uploadTask.put("id", uniqueFileId);
        uploadTask.put("type", type);
        return uploadTask;
    }

    // This should take a manifest, not sql statement
    private static void uploadToRedshiftTask(RedshiftManifest manifest, AmexRecordType type) throws SQLException {
        // If s3 files were uploaded
        String key = Paths.get("manifest", runId, type.name() + ".json").toString();
        String bucket = configuration.get().getString("sink.s3.bucket.name");
        AmazonS3URI manifestURI = new AmazonS3URI("s3://" + bucket + "/" + key); // validate!


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


        byte[] manifestContent = manifest.toString().getBytes(StandardCharsets.UTF_8);
        ObjectMetadata manifestMetadata = new ObjectMetadata();
        manifestMetadata.setContentType("application/json");
        manifestMetadata.setContentEncoding("UTF-8");
        manifestMetadata.setContentLength(manifestContent.length);
        manifestMetadata.setContentMD5(new String(com.amazonaws.util.Base64.encode(DigestUtils.md5(manifestContent))));

        PutObjectRequest req = new PutObjectRequest(
                configuration.get().getString("sink.s3.bucket.name"),
                key,
                new ByteArrayInputStream(manifestContent),
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
            case CBNOT_DETAIL:
                targetTable = "american_express_revenue_activity_chargeback_detail";
                deleteKey = "chargeback_adjustment_number";
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

    }


    private static AmazonS3URI uploadToDataLakeTask(Map<String, Object> fileMetadata) {
        // This should not be triggered if s3 step is skipped
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        String file = (String) fileMetadata.get("file");
        AmexRecordType constants = (AmexRecordType) fileMetadata.get("type");
        String fileId = (String) fileMetadata.get("id");

        String format = constants.getFormat();
        String key = Paths.get(fileId, constants.getPrefix() + constants.getSuffix()).toString();

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
        tags.add(new Tag("RunId", runId));

        File f = new File(file);
        LOGGER.info("Uploading to s3: {} bytes {}", f.length(), file);
        PutObjectRequest req = new PutObjectRequest(
                bucket,
                key,
                f); // takes File not String fileName...
        req.setTagging(new ObjectTagging(tags));
        ObjectMetadata meta = new ObjectMetadata();
        if (f.getName().endsWith(".csv")) {
            meta.setContentType("text/csv");
        } else if (f.getName().endsWith(".dat")) {
            meta.setContentType("text/plain");
        }
        req.setMetadata(meta);
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
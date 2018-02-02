package ly.generalassemb.de.datafeeds.americanExpress.ingress;

import co.ga.batch.JobStarting;
import co.ga.batch.StepStatus;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import com.jcraft.jsch.*;
import com.snowplowanalytics.snowplow.tracker.DevicePlatform;
import com.snowplowanalytics.snowplow.tracker.Tracker;
import com.snowplowanalytics.snowplow.tracker.emitter.BatchEmitter;
import com.snowplowanalytics.snowplow.tracker.emitter.RequestCallback;
import com.snowplowanalytics.snowplow.tracker.events.Event;
import com.snowplowanalytics.snowplow.tracker.events.Unstructured;
import com.snowplowanalytics.snowplow.tracker.http.OkHttpClientAdapter;
import com.snowplowanalytics.snowplow.tracker.payload.TrackerPayload;
import com.squareup.okhttp.OkHttpClient;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.FixedWidthDataFile;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.FixedWidthDataFileComponent;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.model.FixedWidthDataFileFactory;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.EnumUtil;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.RedshiftManifest;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.RunID;
import org.apache.commons.cli.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ly.generalassemb.de.datafeeds.americanExpress.ingress.SkipOption.CLEAN;


/**
 * Created by dashirov on 5/10/17.
 */
enum SkipOption {
    CLEAN,
    S3,
    REDSHIFT
}

public class FeedHandler {
    private static final AtomicInteger eventCounter = new AtomicInteger(0);
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedHandler.class);
    private static final Set<SkipOption> skipProcessingStepsSet = new TreeSet<SkipOption>();
    private static final DateFormat df = new SimpleDateFormat("yyyyMMdd");
    private static final DateFormat postgresTsWithTz = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSX");
    private static final DateFormat julianDate = new SimpleDateFormat("yyyyDDD");
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final CsvMapper csvMapper = new CsvMapper();

    private static Tracker tracker;
    private static Config configuration;
    private static AmazonS3 s3;
    private static Bucket  s3Bucket;
    private static String runId;

    public static Tracker getTracker() {
        return tracker;
    }

    private static void setTracker(Tracker tracker) {
        FeedHandler.tracker = tracker;
    }

    private static void init(String[] args)  {
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
        final Option skipProcessingStepOpt =
                Option.builder("x")
                        .hasArgs()
                        .desc("Skip processing step(s): " + Arrays.asList(SkipOption.values()))
                        .type(String.class)
                        .required(false)
                        .optionalArg(true)
                        .build();
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
                    throw new ConfigurationException("Configuration file spacified with -c command line option does not exist or can not be found");
                if (!configurationFile.isFile())
                    throw new ConfigurationException("Configuration file spacified with -c command line option exists, but is not a plain file");
                configFileURL = configurationFile.getAbsoluteFile().toURI().toURL();
            }
            configuration = new Config(configFileURL);
            if (configuration.get().isEmpty())
                throw new ConfigurationException("Configuration not loaded!");

            String[] skipProcessingSteps = cmd.getOptionValues("x");
            if (skipProcessingSteps!=null && skipProcessingSteps.length != 0) {
                for (String skipProc : skipProcessingSteps) {
                    try {
                        SkipOption skipOption = SkipOption.valueOf(skipProc);
                        skipProcessingStepsSet.add(skipOption);
                        LOGGER.warn("Skipping Processing Step {}", skipOption);
                    } catch (IllegalArgumentException e) {
                        throw new ConfigurationException("Invalid command line option: -x " + skipProc + ". Must be one of " + Arrays.asList(SkipOption.values()));
                    }
                }
            }


            initDateFormats();
            initSnowplowTracker();
            initObjectMappers();
            initJDBC();
            initS3Client();
            initS3Bucket();


        } catch (SQLException|ConfigurationException|ParseException|MalformedURLException  e) {
            LOGGER.error("Failed to parse command line arguments and initialize", e);
            e.printStackTrace();
            terminate(1);
        }
        LOGGER.info("Running with options: ");
        LOGGER.info("   Skip steps: " + skipProcessingStepsSet.toString());

    }
    private static void initDateFormats(){
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        postgresTsWithTz.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    private static void initSnowplowTracker(){

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
                                        LOGGER.info(String.format("Snowplow: OK {} NOK {}; left to process {}", i, list.size(), events_left));
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
    }
    private static void initObjectMappers(){
        // initialize custom JSON serializer
        jsonMapper.registerModule(new JavaTimeModule());
        jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // initialize custom CSV serializer
        csvMapper.registerModule(new JavaTimeModule());
        csvMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    private static void initJDBC() throws SQLException {
        // Put the redshift driver at the end so that it doesn't
        // conflict with postgres queries
        java.util.Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver d = drivers.nextElement();
            if (d.getClass().getName().equals("com.amazon.redshift.jdbc41.Driver") || d.getClass().getName().equals("com.amazon.redshift.jdbc42.Driver")) {
                    DriverManager.deregisterDriver(d);
                    DriverManager.registerDriver(d);
                break;
            }
        }
    }
    private static void initS3Client(){
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
        s3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(credentialsProvider)
                .withAccelerateModeEnabled(false)
                .build();
    }
    private static void initS3Bucket(){
        String bucket = configuration.get().getString("sink.s3.bucket.name");
        s3Bucket = new Bucket(bucket);
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
    private static final Map<String, Date> runTimers = new HashMap<>();
    public static void main(String[] args) throws Exception {
        /*
            Read command line arguments and load initial configuration
         */

        init(args);

        while (true) {
            runId=RunID.unique();
            LOGGER.debug("{} Starting.", runId);
            track(Unstructured.builder().eventData(new JobStarting().withRunId(runId).getSelfDescribingJson()).build());

            Date stepStart = new Date();
            LOGGER.debug("{} Downloading files from Amex SFTP site.", runId);
            runTimers.putIfAbsent("file-download", stepStart);
            track(Unstructured.builder().eventData(new StepStatus().withName("sft").withState(StepStatus.State.RUNNING).withRunId(runId).withStartedAt(stepStart).withEndedAt(new Date()).getSelfDescribingJson()).build());
            List<File> filesDownloaded = downloadPublishedDataFiles();
            LOGGER.debug("{} Downloaded files from Amex SFTP site.", runId);
            track(Unstructured.builder().eventData(new StepStatus().withName("sft").withState(StepStatus.State.COMPLETED).withRunId(runId).withStartedAt(stepStart).withEndedAt(new Date()).getSelfDescribingJson()).build());

            stepStart = new Date();
            LOGGER.debug("{} Parsing files.", runId);
            runTimers.putIfAbsent("file-parse", stepStart);
            track(Unstructured.builder().eventData(new StepStatus().withRunId(runId).withName("file-parse").withState(StepStatus.State.RUNNING).withStartedAt(stepStart).getSelfDescribingJson()).build());
            List<FixedWidthDataFile> parsedFiles = new ArrayList<>();
            for (File file : filesDownloaded) {
                FixedWidthDataFile f = FixedWidthDataFileFactory.getDataFile(Paths.get(file.getAbsolutePath())).parse(file);
                parsedFiles.add(f);
            }
            LOGGER.debug("{} Parsed files.", runId);
            track(Unstructured.builder().eventData(new StepStatus().withName("file-parse").withState(StepStatus.State.COMPLETED).withRunId(runId).withStartedAt(stepStart).withEndedAt(new Date()).getSelfDescribingJson()).build());


            stepStart = new Date();
            LOGGER.debug("{} Uploading files to S3.", runId);
            runTimers.putIfAbsent("file-upload", stepStart);
            track(Unstructured.builder().eventData(new StepStatus().withRunId(runId).withName("s3-upload").withState(StepStatus.State.RUNNING).withStartedAt(stepStart).getSelfDescribingJson()).build());
            List<Map<FixedWidthDataFileComponent, AmazonS3URI>> s3UploadedFiles = uploadToS3(parsedFiles);
            LOGGER.debug("{} Uploaded files to S3.", runId);
            track(Unstructured.builder().eventData(new StepStatus().withName("s3-upload").withState(StepStatus.State.COMPLETED).withRunId(runId).withStartedAt(stepStart).withEndedAt(new Date()).getSelfDescribingJson()).build());

            stepStart = new Date();
            LOGGER.debug("{} Uploading files to Redshift.", runId);
            runTimers.putIfAbsent("file-upload", stepStart);
            track(Unstructured.builder().eventData(new StepStatus().withRunId(runId).withName("dw-upload").withState(StepStatus.State.RUNNING).withStartedAt(stepStart).getSelfDescribingJson()).build());
            List<Map<FixedWidthDataFileComponent, String>> dwUploadedFiles = uploadToRedshift(s3UploadedFiles);
            LOGGER.debug("{} Uploaded files to Redshift.", runId);
            track(Unstructured.builder().eventData(new StepStatus().withName("dw-upload").withState(StepStatus.State.COMPLETED).withRunId(runId).withStartedAt(stepStart).withEndedAt(new Date()).getSelfDescribingJson()).build());

            Thread.sleep(1000 * 60); // sleep for an hour
        }
    }
    private static List<Map<FixedWidthDataFileComponent, String>> uploadToRedshift(List<Map<FixedWidthDataFileComponent, AmazonS3URI>> s3PersistedComponents) throws SQLException {
        List<Map<FixedWidthDataFileComponent,String>> uploadedComponents = new ArrayList<>();
        List<FixedWidthDataFileComponent> selector =
                EnumUtil.stringToEnums(FixedWidthDataFileComponent.class, Arrays.asList(configuration.get().getStringArray("sink.redshift.load.component")));
       Map<FixedWidthDataFileComponent,List<AmazonS3URI>> componentsToManifest = new HashMap<>();
        for ( Map<FixedWidthDataFileComponent, AmazonS3URI> fileComponents: s3PersistedComponents) {
            for (Map.Entry fileComponent: fileComponents.entrySet()) {
                if ( selector.contains(fileComponent.getKey())) {
                    componentsToManifest.putIfAbsent((FixedWidthDataFileComponent) fileComponent.getKey(), new ArrayList<AmazonS3URI>());
                    componentsToManifest.get(fileComponent.getKey()).add((AmazonS3URI)fileComponent.getValue());
                }
            }
        }
        Map<FixedWidthDataFileComponent, RedshiftManifest> manifests = new HashMap<>();
        for (Map.Entry pair : componentsToManifest.entrySet()) {
            RedshiftManifest manifest = new RedshiftManifest();
            manifest.addAll((List<AmazonS3URI>) pair.getValue(), true);
            manifests.put((FixedWidthDataFileComponent) pair.getKey(), manifest);
        }

        Map<FixedWidthDataFileComponent,AmazonS3URI> componentsToCopy = uploadtoS3(manifests);

        try {
            LOGGER.debug(jsonMapper.writer().writeValueAsString(manifests));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        try {
            // connect to the database
            Connection c = DriverManager.getConnection(
                    configuration.get().getString("sink.redshift.jdbc.url"),
                    configuration.get().getString("sink.redshift.jdbc.usr"),
                    configuration.get().getString("sink.redshift.jdbc.pwd")
            );

            String targetSchema = configuration.get().getString("sink.redshift.jdbc.schema");

            Statement setPath = c.createStatement();
            setPath.execute("SET SEARCH_PATH TO "+ targetSchema+",public; ");
            setPath.close();

            Statement checkPath = c.createStatement();
            ResultSet rs = checkPath.executeQuery("SHOW SEARCH_PATH;");
            while (rs.next()){
               LOGGER.debug("Current Search Path: " +rs.getString(1));
            }
            rs.close();
            checkPath.close();

            c.setSchema(targetSchema);
            List<String> statements = new ArrayList<>();

            statements.add("SET SEARCH_PATH TO " + configuration.get().getString("sink.redshift.jdbc.schema") + ",public;");
            for (Entry pair : componentsToCopy.entrySet()) {
                FixedWidthDataFileComponent component = (FixedWidthDataFileComponent) pair.getKey();
                AmazonS3URI uri = (AmazonS3URI) pair.getValue();
                String targetTable = component.getDefaultTableName();

                statements.add(
                        "CREATE TEMPORARY TABLE temp_" + targetTable +
                                "( LIKE " + targetTable + "  );"
                );
                statements.add(
                        "COPY  temp_" + targetTable +
                                " FROM '" + uri + "'\n " +
                                " CREDENTIALS '" + configuration.get().getString("sink.redshift.jdbc.credentials") + "' " +
                                " MANIFEST CSV IGNOREHEADER 1 DATEFORMAT 'YYYY-MM-DD' TIMEFORMAT 'YYYY-MM-DD HH24:MI:SS';"
                );
                statements.add(
                        "DELETE FROM " + targetTable +
                                " WHERE EXISTS ( SELECT 1 FROM temp_" + targetTable +
                                " WHERE " +
                                component.getPrimaryKey()
                                        .stream()
                                        .map(primaryKey -> String.format("temp_%s.%s=%s.%s", targetTable, primaryKey, targetTable, primaryKey))
                                        .collect(Collectors.joining(" AND "))
                                + ");"
                );
                statements.add(
                        "INSERT INTO " + targetTable +
                                " select t.* " +
                                " from temp_" + targetTable + " t;"
                );
                statements.add(
                        "DROP TABLE IF EXISTS  temp_" + targetTable + ";"
                );

            }
            List<Statement> statement = new ArrayList<>();
            try {
                c.setAutoCommit(false);
                for (String query: statements) {
                    Statement st = c.createStatement();
                    statement.add(st);
                    boolean is_ok = st.execute(query);
                    LOGGER.debug("QUERY: {}\nOK: {}\n UPDATED: {}", query, is_ok , st.getUpdateCount());
                }
                c.commit();
            } catch (SQLException sqlException){
                printSQLException(sqlException);
                try {
                    c.rollback();
                } catch ( SQLException rollbackException){
                    printSQLException(rollbackException);
                }
            } finally {
                for (Statement st: statement)
                   if (st!=null)
                        st.close();
                c.setAutoCommit(true);
            }
            c.close();
        } catch (SQLException e) {
            printSQLException(e);
            throw e;
        }
        return  uploadedComponents;
    }

    private static boolean ignoreSQLException(String sqlState) {

        if (sqlState == null) {
            System.out.println("The SQL state is not defined!");
            return false;
        }

        // X0Y32: Jar file already exists in schema
        if (sqlState.equalsIgnoreCase("X0Y32"))
            return true;

        // 42Y55: Table already exists in schema
        if (sqlState.equalsIgnoreCase("42Y55"))
            return true;

        return false;
    }
    private static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                if (!ignoreSQLException(
                        ((SQLException) e).
                                getSQLState())) {

                    e.printStackTrace(System.err);
                    LOGGER.error("SQLState: " +
                            ((SQLException)e).getSQLState());

                    LOGGER.error("Error Code: " +
                            ((SQLException)e).getErrorCode());

                    LOGGER.error("Message: " + e.getMessage());

                    Throwable t = ex.getCause();
                    while(t != null) {
                        LOGGER.error("Cause: " + t);
                        t = t.getCause();
                    }
                }
            }
        }
    }
    private static Map<FixedWidthDataFileComponent, AmazonS3URI> uploadtoS3(Map<FixedWidthDataFileComponent, RedshiftManifest> manifestList){
        Map<FixedWidthDataFileComponent, AmazonS3URI> out = new HashMap<>();
        manifestList.forEach((component,manifest)->{
            Path path= Paths.get("manifest", runId,  component.getDefaultTableName() + ".json" );
            byte[] content = manifest.toString().getBytes(Charset.forName("UTF-8"));
            String streamMD5 = new String(Base64.getEncoder().encode(DigestUtils.md5(content)));

            List<Tag> tags = new ArrayList<>();
            tags.add(new Tag("Contents", "Redshift Load Manifest for table " + component.getFileNameExtension() ));
            tags.add(new Tag("PII", "FALSE"));
            tags.add(new Tag("PCI", "FALSE"));
            tags.add(new Tag("Snapshot Date", df.format(new Date())));
            tags.add(new Tag("Content-Type",RedshiftManifest.getContentType()));

            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(content.length);
            meta.setContentMD5(streamMD5);
            meta.setContentType(RedshiftManifest.getContentType());

            PutObjectRequest req = new PutObjectRequest(
                    s3Bucket.getName(),
                    path.toString(),
                    new ByteArrayInputStream(content),
                    meta);
            req.setTagging(new ObjectTagging(tags));
            s3.putObject(req);
            AmazonS3URI manifestS3URI  = new AmazonS3URI("s3://" + req.getBucketName() + "/" + req.getKey());
            LOGGER.debug("Manifest uploaded: " + manifestS3URI );
            out.put(component,manifestS3URI);
        });
        return out;
    }
    private static List<Map<FixedWidthDataFileComponent,AmazonS3URI>> uploadToS3(List<FixedWidthDataFile> parsedFiles){
        List<Map<FixedWidthDataFileComponent,AmazonS3URI>> uploadedComponents = new ArrayList<>();
        List<FixedWidthDataFileComponent> selector =
                EnumUtil.stringToEnums(FixedWidthDataFileComponent.class, Arrays.asList(configuration.get().getStringArray("sink.s3.load.component")));
        for (FixedWidthDataFile file: parsedFiles) {
            try {
                // TODO: move to a single class?
                Map<FixedWidthDataFileComponent, String> loadableComponents = file.toLoadableComponents(jsonMapper,csvMapper);
                Map<FixedWidthDataFileComponent, AmazonS3URI> out = new HashMap<>();
                List<Entry<FixedWidthDataFileComponent, String>> wantedComponents = loadableComponents
                        .entrySet()
                        .stream()
                        .filter(
                                entry->selector.contains(entry.getKey())
                        )
                        .collect(
                                Collectors.toList()
                        );
                for (Entry<FixedWidthDataFileComponent, String> entry : wantedComponents) {
                    FixedWidthDataFileComponent k = entry.getKey();
                    String v = entry.getValue();
                    // create tags to be attached to S3 object in onbject store
                    List<Tag> tags = new ArrayList<>();
                    tags.add(new Tag("Contents", "American Express Credit Card Transactions"));
                    tags.add(new Tag("PII", "FALSE"));
                    tags.add(new Tag("PCI", "TRUE"));
                    tags.add(new Tag("Snapshot Date", df.format(new Date())));
                    tags.add(new Tag("Content-Type", k.getFormat()));
                    if (k.getFormat().equals("text/csv"))
                        tags.add(new Tag("CSV:Header", "TRUE"));


                    // calculate MD5 of the stream content
                    byte[] content = v.getBytes(Charset.forName("UTF-8"));
                    String streamMD5 = new String(Base64.getEncoder().encode(DigestUtils.md5(content)));

                    // Attach
                    ObjectMetadata meta = new ObjectMetadata();
                    meta.setContentLength(content.length);
                    meta.setContentMD5(streamMD5);
                    meta.setContentType(k.getFormat());

                    System.out.println(file.getFileId());

                    PutObjectRequest req = new PutObjectRequest(
                            s3Bucket.getName(),
                            file.getId() + "/" + file.getFileId() + "/" + k.getS3Prefix() + k.getFileNameExtension(),
                            new ByteArrayInputStream(content),
                            meta);

                    req.setTagging(new ObjectTagging(tags));
                    // req.setMetadata(meta);
                    s3.putObject(req);

                    // TODO: move to data lake, do not drop in the top level
                    out.put(k, new AmazonS3URI("s3://" + req.getBucketName() + "/" + req.getKey()));
                }
                uploadedComponents.add(out);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return uploadedComponents;
    }


    private static List<File> downloadPublishedDataFiles() throws JSchException, IOException, SftpException {
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
        JSch.setLogger(new SFTPLogger());
        JSch ssh = new JSch();

        final List<File> filesDownloaded = new ArrayList<>();

        track(Unstructured.builder().eventData(new StepStatus().withName("sft").withState(StepStatus.State.PENDING).withRunId(runId).withStartedAt(stepStart).getSelfDescribingJson()).build());

        LOGGER.debug("Private Key:{}", private_key);
        String known_hosts = configuration.get().getString("source.amex.sftp.known_hosts");
        ssh.setKnownHosts(known_hosts);
        ssh.addIdentity(user, private_key.getBytes("US-ASCII"), public_key.getBytes("US-ASCII"), null);
        Session session = ssh.getSession(user, host, port);
        LOGGER.debug(String.format("{} SSH session created for {} to host {} on port {}.", runId, user, host, port));
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "yes");
        session.setConfig(config);
        session.connect();
        LOGGER.debug(String.format("{} SSH session connected to host {} on {} as user {}",  runId, session.getHost(), session.getPort(), session.getUserName()));
        Channel channel = session.openChannel("sftp");
        channel.setInputStream(System.in);
        channel.setOutputStream(System.out);
        channel.connect();
        LOGGER.debug(String.format("{} SFTP shell channel connected.", runId));
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
                if (!skipProcessingStepsSet.contains(CLEAN))
                    localFile.deleteOnExit();
                LOGGER.debug(String.format("{} Downloading {} to {}", runId, remoteFile.getPath(), localFile.getAbsolutePath()));
                c.get(remoteFile.getPath(), localFile.getAbsolutePath());
                filesDownloaded.add(localFile);
            } else
                LOGGER.error("Whoa! second time around, no match!");
        }

        try {
            c.exit();
        } catch (Exception e) {
            LOGGER.warn("Could not close ssh/sftp communication channels cleanly. Will not fail the job, but this was the error:", e);
        }
        return filesDownloaded;
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
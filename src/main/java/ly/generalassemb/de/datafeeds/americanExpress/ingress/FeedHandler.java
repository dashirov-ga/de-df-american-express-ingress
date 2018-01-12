package ly.generalassemb.de.datafeeds.americanExpress.ingress;

import co.ga.batch.JobStarting;
import co.ga.batch.StepStatus;
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
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.RunID;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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

            String[] skipProcessingSteps = cmd.getOptionValues("x");
            if (skipProcessingSteps.length != 0) {
                for (String skipProc : skipProcessingSteps) {
                    try {
                        SkipOption skipOption = SkipOption.valueOf(skipProc);
                        skipProcessingStepsSet.add(skipOption);
                        LOGGER.warn("Skipping Processing Step {}", skipOption);
                    } catch (IllegalArgumentException e) {
                        throw new ParseException("Invalid command line option: -x " + skipProc + ". Must be one of " + Arrays.asList(SkipOption.values()));
                    }
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

    private static final Map<String, Date> runTimers = new HashMap<>();

    public static void main(String[] args) throws Exception {
        /*
            Read command line arguments and load initial configuration
         */
        init(args);

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

        List<FixedWidthDataFile> parsedFiles = filesDownloaded.parallelStream().map(f -> {
            try {
                return FixedWidthDataFileFactory.getDataFile(f.getAbsolutePath()).parse(f);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

        List<Map<FixedWidthDataFileComponent, String>> uploadedFiles = parsedFiles.parallelStream().map(parsedFile -> {
            try {
                return parsedFile.toRedshift(configuration);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }).collect(Collectors.toList());

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
                if (!skipProcessingStepsSet.contains(CLEAN))
                    localFile.deleteOnExit();
                LOGGER.debug("{} Downloading {} to {}", runId, remoteFile.getPath(), localFile.getAbsolutePath());
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
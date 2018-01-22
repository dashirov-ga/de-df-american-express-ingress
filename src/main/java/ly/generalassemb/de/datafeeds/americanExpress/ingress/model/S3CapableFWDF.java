package ly.generalassemb.de.datafeeds.americanExpress.ingress.model;

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
import com.fasterxml.jackson.annotation.JsonIgnore;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.Config;
import ly.generalassemb.de.datafeeds.americanExpress.ingress.util.EnumUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class S3CapableFWDF implements FixedWidthDataFile {
    @JsonIgnore
    private static final Pattern fileNamePattern = Pattern.compile("^(?<account>\\p{Alnum}+)\\.(?<type>EPAPE|EPTRN|CBNOT|EMINQ|EMCBK)\\#(?<fileId>\\p{Alnum}+)");

    @JsonIgnore
    @Override
    public String getId() {
        if (this.getFileName() != null ) {
            Matcher m = fileNamePattern.matcher(this.getFileName());
            LOGGER.debug("filename: {} , matches: {}", this.getFileName(), m.matches());
            return m.group("type");
        }
        return null;
    }

    @JsonIgnore
    @Override
    public String getFileId() {
        if (this.getFileName() != null ) {
            Matcher m = fileNamePattern.matcher(this.getFileName());
            LOGGER.debug("filename: {} , matches: {}", this.getFileName(), m.matches());
            return m.group("fileId");
        }
        return null;
    }

    @JsonIgnore
    public static  String getFileId(Path filePath) {
        if (filePath.getFileName() != null ) {
            Matcher m = fileNamePattern.matcher(filePath.getFileName().toString());
            LOGGER.debug("filename: {} , matches: {}", filePath.getFileName().toString(), m.matches());
            return m.group("type");
        }
        return null;
    }

    @JsonIgnore
    public static  String getFileType(Path filePath) {
        if (filePath.getFileName() != null ) {
            Matcher m = fileNamePattern.matcher(filePath.getFileName().toString());
            LOGGER.debug("filename: {} , matches: {}", filePath.getFileName().toString(), m.matches());
            return m.group("type");
        }
        return null;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(S3CapableFWDF.class);
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    /**
     *  Given a target directory, serialize components passed via the map to file system
     *  Using defaults exposed by FixedWidthDataFileComponent (target file names, extensions)
     * @param directory
     *   Final directory where files are to be created. RunID is not being injected here, make sure you take care of it some place else
     * @throws Exception
     */
    @Override
    public void toDirectory(File directory) throws Exception {
        Map<FixedWidthDataFileComponent, String> loadableComponents = this.toLoadableComponents();
        if ((!directory.exists()) && (!directory.mkdirs())) {
            throw new RuntimeException("Cannot create directory " + directory.getAbsolutePath());
        }
        loadableComponents.forEach((FixedWidthDataFileComponent objectType, String fileContentToWrite) -> {

                    File targetCsv = new File(directory, objectType.getDefaultTableName() + objectType.getFileNameExtension());
                    try {

                        BufferedWriter w = new BufferedWriter(new FileWriter(targetCsv));

                           w.write(fileContentToWrite);
                        w.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    /**
     *  Given configuration containing all elements necessary to connect to S3 bucket
     *  Transfer all components stored in a given directory to S3 data lake for subsequent
     *  load into Amazon Redshift or RDS database
     * @param configuration
     * @return
     */
    @Override
    public   Map<FixedWidthDataFileComponent,AmazonS3URI> toS3(Config configuration)  throws Exception{
        Map<FixedWidthDataFileComponent, String> loadableComponents = this.toLoadableComponents();
        Map <FixedWidthDataFileComponent,AmazonS3URI> out = new HashMap<>();
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

        loadableComponents.forEach((FixedWidthDataFileComponent k, String v) ->{
            try {

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



                System.out.println(this.getFileId());

                PutObjectRequest req = new PutObjectRequest(
                        bucket,
                        this.getId() + "/" + this.getFileId() + "/" + k.getS3Prefix() + k.getFileNameExtension(),
                        new ByteArrayInputStream(content),
                        meta);

                req.setTagging(new ObjectTagging(tags));
                // req.setMetadata(meta);
                s3.putObject(req);

                // TODO: move to data lake, do not drop in the top level
                out.put(k, new AmazonS3URI("s3://" + req.getBucketName() + "/" + req.getKey()));
            } catch (Exception e){
                throw new RuntimeException(e);
            }

        });
        return out;
    }



    /**
     * Given configuration with all elements necessary to connect to aconnect to S3 data lake as well as
     * connect to the redshift cluster, upload desired components to s3 and then load them into the database tables
     * @param configuration
     * @return A map of componets and the destination tables where those were loaded to
     * @throws Exception
     */
    @Override
    public Map<FixedWidthDataFileComponent,String> toRedshift(Config configuration) throws Exception{

        LOGGER.debug("Uploading to s3 and then to redshift db");

        List<FixedWidthDataFileComponent> componentsWanted =
                EnumUtil.stringToEnums(FixedWidthDataFileComponent.class, Arrays.asList(configuration.get().getStringArray("sink.redshift.load.component")));

        LOGGER.debug("Components wanted " + componentsWanted.stream().map(Enum::name).collect(Collectors.joining(", ")));

        // prepare return object
        Map<FixedWidthDataFileComponent,String> out = new HashMap<>();
        // connect to the database
        Connection c = DriverManager.getConnection(
                configuration.get().getString("sink.redshift.jdbc.url"),
                configuration.get().getString("sink.redshift.jdbc.usr"),
                configuration.get().getString("sink.redshift.jdbc.pwd")
        );
        c.setSchema(configuration.get().getString("sink.redshift.jdbc.schema"));
        PreparedStatement session_setup = c.prepareStatement("SET SEARCH_PATH TO " + configuration.get().getString("sink.redshift.jdbc.schema") + ",public;");
        session_setup.execute();

        LOGGER.debug("Connected to redshift cluster.");
        // upload loadable components to the s3 data lake prior to a redshift load attempt
        Map<FixedWidthDataFileComponent,AmazonS3URI> loadableComponents = this.toS3(configuration);

        LOGGER.debug("Upload to S3 complete: " + loadableComponents.keySet().stream().map(Enum::name).collect(Collectors.joining(", ")));
        // iterate through uploaded components to issue copy commands to the redshift cluster
        componentsWanted.forEach((FixedWidthDataFileComponent component) ->{
            // process CSV files and wanted components only
            // TODO: figure out how to load fixed width files and processed json objects
            if ("text/csv".equals(component.getFormat()) && loadableComponents.containsKey(component)) {
                LOGGER.debug("Located wanted component " + component.name() + ". Proceeding with db load.");
                try {
                    // in order to load redshift tables, data needs to be already on s3 - get the s3 url
                    AmazonS3URI amazonS3URI = loadableComponents.get(component);
                    // which table is the file going to? for now use defaults
                    // TODO: allow to override table names via configuration file
                    String targetTable = component.getDefaultTableName();
                    // First, always try to create a temporary table, similar in structure to the destination table
                    String creteTemp = "CREATE TEMPORARY TABLE temp_" + component.getDefaultTableName() + "( LIKE " + component.getDefaultTableName() + "  );";
                    LOGGER.debug(creteTemp);
                    PreparedStatement s = c.prepareStatement(creteTemp);
                    s.execute();

                    // Next,copy the data file into the temporary table created
                    String copyCommand =
                            "COPY  temp_" + targetTable +
                                    " FROM '" + amazonS3URI + "'\n " +
                                    " CREDENTIALS '" + configuration.get().getString("sink.redshift.jdbc.credentials") + "' " +
                                    " CSV IGNOREHEADER 1 DATEFORMAT 'YYYY-MM-DD' TIMEFORMAT 'YYYY-MM-DD HH24:MI:SS';";

                    LOGGER.debug(copyCommand);
                    s = c.prepareStatement(copyCommand);
                    s.execute();

                    // Then, delete conflicting data from the destination table, using the primary key

                    String deleteCommand = "DELETE FROM " + targetTable + " WHERE EXISTS ( SELECT 1 FROM temp_" + targetTable + " WHERE " +
                        component.getPrimaryKey().stream().map(k-> String.format("temp_%s.%s=%s.%s",targetTable,k,targetTable,k)).collect(Collectors.joining(" AND "))
                     + ");";

                    LOGGER.debug(deleteCommand);
                    s = c.prepareStatement(deleteCommand);
                    s.execute();

                    // Finally, insert all data from temporary table into the master storage
                    String insertNew = "INSERT INTO " + targetTable +
                            " select t.* " +
                            " from temp_" + targetTable +" t;";
                    LOGGER.debug(insertNew);
                    s = c.prepareStatement(insertNew);
                    s.execute();

                    out.put(component,targetTable);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        c.commit();
        c.setAutoCommit(true);
        return out;
    }
    public void toDirectory(String directoryPath) throws Exception {
        this.toDirectory(new File(directoryPath));
    }

}

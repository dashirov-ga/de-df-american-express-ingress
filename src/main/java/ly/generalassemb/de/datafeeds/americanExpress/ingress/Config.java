package ly.generalassemb.de.datafeeds.americanExpress.ingress;

/**
 * Created by davidashirov on 3/10/17.
 */

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.FileBasedBuilderParameters;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private Configuration config;

    public Configuration get() {
        return config;
    }

    public Config(URL url) {
        try {
            List<FileLocationStrategy> subs = Arrays.asList(
                    new ProvidedURLLocationStrategy(), // -c path -> URL
                    new BasePathLocationStrategy(),    // where the binary is
                    new FileSystemLocationStrategy(),
                    new ClasspathLocationStrategy());  // look inside jar ( find a resource same name )

            FileLocationStrategy strategy = new CombinedLocationStrategy(subs);

            FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class);
            Parameters params = new Parameters();

            final FileBasedBuilderParameters fileBasedBuilderParameters = params.fileBased();
            fileBasedBuilderParameters.setListDelimiterHandler(new DefaultListDelimiterHandler(','));
            if (url == null)
                fileBasedBuilderParameters.setLocationStrategy(strategy).setFileName("df-american-express-ingress.properties");
            else
                fileBasedBuilderParameters.setLocationStrategy(strategy).setLocationStrategy(strategy).setURL(url);

            builder.configure(fileBasedBuilderParameters);

            this.config = builder.getConfiguration();
            logger.debug("Configuration file successfully loaded");
        } catch (ConfigurationException e) {
            logger.error("Error while lading the configuration file; default config will be used", e);
            throw new RuntimeException(e);
        }
    }


}
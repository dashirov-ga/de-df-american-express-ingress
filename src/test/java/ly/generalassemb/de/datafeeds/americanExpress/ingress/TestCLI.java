package ly.generalassemb.de.datafeeds.americanExpress.ingress;

import org.apache.commons.cli.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;


public class TestCLI {
    private static final Set<SkipOption> skipProcessingStepsSet = new TreeSet<>();
    private static final Logger LOGGER=  LoggerFactory.getLogger(TestCLI.class);
    enum SkipOption {
        CLEAN,
        S3,
        REDSHIFT
    }
    @Test
    public  void  main() throws ParseException {
        String[] args = {"-x","S3","REDSHIFT","CLEAN"};
        final Options options = new Options();
        final Option skipProcessingStepOpt =
                Option.builder("x")
                        .hasArgs()
                        .desc("Skip processing step(s): [clean, s3, redshift]")
                        .type(String.class)
                        .required(false)
                        .optionalArg(true)

                        .build();
        options.addOption(skipProcessingStepOpt);
        final CommandLine cmd = new DefaultParser().parse(options,args);
        String[] skipProcessingSteps = cmd.getOptionValues("x");
        System.out.println(Arrays.toString(skipProcessingSteps));

        if (skipProcessingSteps.length != 0) {
            for (String skipProc : skipProcessingSteps ) {
                try {
                    SkipOption skipOption = SkipOption.valueOf(skipProc);
                    skipProcessingStepsSet.add(skipOption);
                    LOGGER.warn("Skipping Processing Step {}", skipOption);
                } catch (IllegalArgumentException e){
                    throw new ParseException("Invalid command line option: -x " + skipProc +". Must be one of "+ Arrays.asList(SkipOption.values()) );
                }
            }
        }

    }
}

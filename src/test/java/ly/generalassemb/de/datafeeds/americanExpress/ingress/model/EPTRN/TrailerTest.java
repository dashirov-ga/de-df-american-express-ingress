package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPTRN;


import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;

public class TrailerTest {
    private static final FixedFormatManager fixedFormatManager = new FixedFormatManagerImpl();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String testString = "DFTRL090220170457000000ACME CORPORATION INC00000000008918979531          00000000000000090                                                                                                                                                                                                                                                                                                                                                                        ";
    private static final Logger LOGGER = LoggerFactory.getLogger(TrailerTest.class);
    private static Trailer trailer;
    private static boolean setUpIsDone = false;

    @Before
    public void init() {
        if (setUpIsDone) {
            return;
        } else {
            LOGGER.debug("⟱⟱⟱⟱⟱⟱⟱⟱⟱⟱");
            setUpIsDone = true;
            trailer = fixedFormatManager.load(Trailer.class, testString);
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            try {
                LOGGER.debug(mapper.writer().writeValueAsString(trailer));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            LOGGER.debug("⟰⟰⟰⟰⟰⟰⟰⟰⟰⟰");
        }
    }

    @After
    public void finalyze() {
    }

    @Test
    public void testTrailerModelTrailerTimestamp() {
        ZoneId tz = ZoneId.of("UTC");
        LocalDateTime expectedDateTime = LocalDateTime.of(2017, Month.SEPTEMBER, 2, 4, 57);
        Instant expected = expectedDateTime.atZone(tz).toInstant();
        LocalDateTime actualDateTime = LocalDateTime.ofInstant(trailer.getDate().atZone(tz).toInstant(), tz);
        assertEquals(expected, actualDateTime.toInstant(tz.getRules().getOffset(Instant.now())));
        assertEquals(expectedDateTime, actualDateTime);
    }

    @Test
    public void testTrailerModelTrailerFileName() {
        assertEquals("ACME CORPORATION INC", trailer.getFileName());
    }

    @Test
    public void testTrailerModelTrailerFileId() {
        assertEquals(BigDecimal.ZERO, trailer.getFileId());
    }

    @Test
    public void testTrailerModelTrailerIndicator() {
        assertEquals("DFTRL", trailer.getRecordType());
    }

    @Test
    public void testTrailerModelTrailerRecordCount() {
        assertEquals(Integer.valueOf(90), trailer.getRecordCount());
    }

    @Test
    public void testTrailerModelTrailerRecipientKey() {
        // Error in documentation: Statement "This number is unique to each individual file." is factually incorrect.
        // Value is identical across all files generated for the same service establishment number '8918979531'
        assertEquals(40, trailer.getRecipientKey().length());
        assertEquals("00000000008918979531          0000000000", trailer.getRecipientKey());
    }

    // TODO: Check JSON/CSV serialization. What are the rules?
    // ➥ keys in documentation must match keys in JSON object?
    //    ➥ not going to work 100% of the time: documentation is convoluted and often misleading

}

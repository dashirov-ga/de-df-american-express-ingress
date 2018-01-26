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

public class HeaderTest {
    private static final FixedFormatManager fixedFormatManager = new FixedFormatManagerImpl();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String testString = "DFHDR062020170351000000ACME CORPORATION INC                                                                                                                                                                                                                                                                                                                                                                                                                       ";
    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderTest.class);
    private static Header header;
    private static boolean setUpIsDone = false;

    @Before
    public void init() {
        if (setUpIsDone) {
            return;
        } else {
            LOGGER.debug("⟱⟱⟱⟱⟱⟱⟱⟱⟱⟱");
            setUpIsDone = true;
            header = fixedFormatManager.load(Header.class, testString);
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            try {
                LOGGER.debug(mapper.writer().writeValueAsString(header));
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
    public void testHeaderModelHeaderTimestamp() {
        ZoneId tz = ZoneId.of("UTC");
        LocalDateTime expectedDateTime = LocalDateTime.of(2017, Month.JUNE, 20, 3, 51);
        Instant expected = expectedDateTime.atZone(tz).toInstant();
        LocalDateTime actualDateTime = LocalDateTime.ofInstant(header.getDate().atZone(tz).toInstant(), tz);
        assertEquals(expected, actualDateTime.toInstant(tz.getRules().getOffset(Instant.now())));
        assertEquals(expectedDateTime, actualDateTime);

    }

    @Test
    public void testHeaderModelHeaderFileName() {
        assertEquals("ACME CORPORATION INC", header.getFileName());
    }

    @Test
    public void testHeaderModelHeaderFileId() {
        assertEquals(BigDecimal.ZERO, header.getFileId());
    }

    @Test
    public void testHeaderModelHeaderIndicator() {
        assertEquals("DFHDR", header.getRecordType());
    }


    // TODO: Check JSON/CSV serialization. What are the rules?
    // ➥ keys in documentation must match keys in JSON object?
    //    ➥ not going to work 100% of the time: documentation is convoluted and often misleading

}

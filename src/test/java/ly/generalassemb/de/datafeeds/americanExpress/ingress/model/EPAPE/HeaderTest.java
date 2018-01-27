package ly.generalassemb.de.datafeeds.americanExpress.ingress.model.EPAPE;


import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;

public class HeaderTest {
    private static final FixedFormatManager fixedFormatManager = new FixedFormatManagerImpl();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String testString = "DFHDR 201801080534PANEURPAN-EUROPE EPA FILE                                                                                                                                                                                                                                                                                                                                                                                                             ";
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
        LocalDateTime expectedDateTime = LocalDateTime.of(2018, Month.JANUARY, 8, 5, 34);
        Instant expected = expectedDateTime.atZone(tz).toInstant();
        LocalDateTime actualDateTime = LocalDateTime.ofInstant(header.getDate().atZone(tz).toInstant(), tz);
        assertEquals(expected, actualDateTime.toInstant(tz.getRules().getOffset(Instant.now())));
        assertEquals(expectedDateTime, actualDateTime);
    }

    @Test
    public void testHeaderModelHeaderFileName() {
        assertEquals("PAN-EUROPE EPA FILE", header.getName());
    }

    @Test
    public void testHeaderModelHeaderFileId() {
        assertEquals("PANEUR", header.getId());
    }

    @Test
    public void testHeaderModelHeaderIndicator() {
        assertEquals("DFHDR", header.getRecordType());
    }

    @Test
    public void testHeaderModelHeaderVersion() {
        assertEquals("", header.getVersionControlNumber());
    }

    @Test
    public void testHEaderModelJsonSerialization() throws JSONException, JsonProcessingException {
        String exactMatchRequired = "{\"RECORD_TYPE\":\"DFHDR\",\"DATE\":\"2018-01-08 05:34:00\",\"ID\":\"PANEUR\",\"NAME\":\"PAN-EUROPE EPA FILE\",\"VERSION_CONTROL_NUMBER\":\"\"}";
        JSONObject test = new JSONObject(mapper.writeValueAsString(header));
        JSONAssert.assertEquals(exactMatchRequired, test, true);
    }

    // TODO: Check JSON/CSV serialization. What are the rules?
    // ➥ keys in documentation must match keys in JSON object?
    //    ➥ not going to work 100% of the time: documentation is convoluted and often misleading

}

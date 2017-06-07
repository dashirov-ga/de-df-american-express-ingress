package co.ga.batch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by davidashirov on 3/24/17.
 */
public class JsonSelfDescribingContext {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    protected final String iglu;
    protected JsonSelfDescribingContext(String iglu) {
        this.iglu = iglu;
    }
    @JsonIgnore
    public SelfDescribingJson getSelfDescribingJson(){
        return new SelfDescribingJson(iglu, this);
    }
}

package co.ga.batch;

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

    public JsonSelfDescribingContext(String iglu) {
        this.iglu = iglu;
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList((Field[])type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }

    public SelfDescribingJson getSelfDescribingJson() {
        LOGGER.info("Extracting json annotated fields in {}", this.getClass());
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter dateWriter = mapper.writer(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        Map<String, String> out = new HashMap<>();

        List<Field> fields = new ArrayList<Field>();
        getAllFields(fields,this.getClass());

        AccessibleObject.setAccessible(fields.toArray(new Field[fields.size()]), true);
        for (Field f : fields) {
            LOGGER.debug("Examining field {}", f);
            JsonProperty property = f.getAnnotation(JsonProperty.class);
            if (property != null) {
                try {
                    String key = property.value();
                    String fieldName = f.getName();
                    Object fieldType = f.getType();
                    if (f.get(this) != null) {
                        String val = (fieldType.equals(Date.class)) ? dateWriter.writeValueAsString(f.get(this)) : mapper.writeValueAsString(f.get(this));
                        out.put(key, val);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        LOGGER.debug("Completed SDJ Map");
        return new SelfDescribingJson(iglu, out);

    }
}

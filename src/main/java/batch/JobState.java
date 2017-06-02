package batch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.snowplowanalytics.snowplow.tracker.Subject;
import com.snowplowanalytics.snowplow.tracker.events.Event;
import com.snowplowanalytics.snowplow.tracker.events.Unstructured;
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Schema for an Google AdWords Ingress Data Feed job starting
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "run_id"
})
public class JobState implements Serializable
{
    public enum State {
        STARTING("iglu:co.ga.monitoring.batch/job_starting/jsonschema/1-0-0"),
        FAILED("iglu:co.ga.monitoring.batch/job_failed/jsonschema/1-0-0"),
        SUCCEEDED("iglu:co.ga.monitoring.batch/job_succeeded/jsonschema/1-0-0");
        private final String state;
        private State(final String state) {
            this.state = state;
        }
        @Override
        public String toString() {
            return state;
        }
    }

    @JsonProperty("run_id")
    @Size(max = 32)
    private String runId;
    private final static long serialVersionUID = 3366603880232992815L;


    protected static String iglu;
    public static final String version = "1.0.0";
    public SelfDescribingJson getSelfDescribingJson(){
        SelfDescribingJson selfDescribingJson = new SelfDescribingJson(iglu);
        Map<String,Object> data = new HashMap<>();
        data.put("run_id", runId);
        selfDescribingJson.setData(data);
        return selfDescribingJson;
    }

    public Event getEvent(Subject subject){
        if ( subject != null)
            return Unstructured.builder()
                .eventId(UUID.randomUUID().toString())
                .timestamp(new Date().getTime())
                .subject(subject)
                .eventData(getSelfDescribingJson())
                .build();
        else
            return Unstructured.builder()
                    .eventId(UUID.randomUUID().toString())
                    .timestamp(new Date().getTime())
                    .eventData(getSelfDescribingJson())
                    .build();
    }

    public static void setIglu(String iglu) {
        JobState.iglu = iglu;
    }
    public static void setState(State state) {
        JobState.iglu = state.name();
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public JobState() {
    }

    /**
     *
     * @param runId
     */
    public JobState(String runId) {
        super();
        this.runId = runId;
    }

    @JsonProperty("run_id")
    public String getRunId() {
        return runId;
    }

    @JsonProperty("run_id")
    public void setRunId(String runId) {
        this.runId = runId;
    }

    public JobState withRunId(String runId) {
        this.runId = runId;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(runId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof JobState)) {
            return false;
        }
        JobState rhs = ((JobState) other);
        return new EqualsBuilder().append(runId, rhs.runId).isEquals();
    }

}
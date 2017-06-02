
package co.ga.batch;

import com.fasterxml.jackson.annotation.*;
import com.snowplowanalytics.snowplow.tracker.Subject;
import com.snowplowanalytics.snowplow.tracker.events.Event;
import com.snowplowanalytics.snowplow.tracker.events.Unstructured;
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Schema for an Google AdWords Ingress Data Feed job starting
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "run_id",
        "name",
        "state",
        "started_at",
        "ended_at"
})
public class StepStatus implements Serializable {

    protected static String iglu = "iglu:co.ga.batch/step_status/jsonschema/2-0-1";
    public static final String version = "2.0.1";

    public SelfDescribingJson getSelfDescribingJson() {
        SelfDescribingJson selfDescribingJson = new SelfDescribingJson(iglu);
        Map<String, Object> data = new HashMap<>();
        //REQUIRED
        data.put("name", name);
        data.put("state", state);
        // OPTIONAL
        if (runId != null)
            data.put("run_id", runId);

        if (startedAt != null)
            data.put("started_at", startedAt);

        if (endedAt != null)
            data.put("ended_at", endedAt);
        selfDescribingJson.setData(data);
        return selfDescribingJson;
    }

    public Event getEvent(Subject subject) {
        if (subject != null)
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

    @JsonProperty("run_id")
    @Size(max = 32)
    private String runId;
    /**
     * (Required)
     */
    @JsonProperty("name")
    @Size(max = 255)
    @NotNull
    private String name;

    /**
     * (Required)
     */
    @JsonProperty("state")
    @NotNull
    private StepStatus.State state;
    @JsonProperty("started_at")
    private Date startedAt;
    @JsonProperty("ended_at")
    private Date endedAt;
    private final static long serialVersionUID = -510014190490845329L;

    /**
     * No args constructor for use in serialization
     */
    public StepStatus() {
    }

    /**
     * @param endedAt
     * @param name
     * @param startedAt
     * @param state
     * @param runId
     */
    public StepStatus(String runId, String name, StepStatus.State state, Date startedAt, Date endedAt) {
        super();
        this.runId = runId;
        this.name = name;
        this.state = state;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    @JsonProperty("run_id")
    public String getRunId() {
        return runId;
    }

    @JsonProperty("run_id")
    public void setRunId(String runId) {
        this.runId = runId;
    }

    public StepStatus withRunId(String runId) {
        this.runId = runId;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * (Required)
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public StepStatus withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * (Required)
     */
    @JsonProperty("state")
    public StepStatus.State getState() {
        return state;
    }

    /**
     * (Required)
     */
    @JsonProperty("state")
    public void setState(StepStatus.State state) {
        this.state = state;
    }

    public StepStatus withState(StepStatus.State state) {
        this.state = state;
        return this;
    }

    @JsonProperty("started_at")
    public Date getStartedAt() {
        return startedAt;
    }

    @JsonProperty("started_at")
    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public StepStatus withStartedAt(Date startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    @JsonProperty("ended_at")
    public Date getEndedAt() {
        return endedAt;
    }

    @JsonProperty("ended_at")
    public void setEndedAt(Date endedAt) {
        this.endedAt = endedAt;
    }

    public StepStatus withEndedAt(Date endedAt) {
        this.endedAt = endedAt;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(runId).append(name).append(state).append(startedAt).append(endedAt).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StepStatus) == false) {
            return false;
        }
        StepStatus rhs = ((StepStatus) other);
        return new EqualsBuilder().append(runId, rhs.runId).append(name, rhs.name).append(state, rhs.state).append(startedAt, rhs.startedAt).append(endedAt, rhs.endedAt).isEquals();
    }

    public enum State {

        PENDING("PENDING"),
        RUNNING("RUNNING"),
        COMPLETED("COMPLETED"),
        CANCELLED("CANCELLED"),
        FAILED("FAILED");
        private final String value;
        private final static Map<String, State> CONSTANTS = new HashMap<String, State>();

        static {
            for (StepStatus.State c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private State(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static StepStatus.State fromValue(String value) {
            StepStatus.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}

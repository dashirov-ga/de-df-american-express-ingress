package co.ga.batch;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Schema for an Batch Job Step Status Event
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "run_id",
        "name",
        "context",
        "state",
        "started_at",
        "ended_at"
})
public class StepStatus extends JsonSelfDescribingContext  implements Serializable , Cloneable {
    private static final String    iglu = "iglu:co.ga.batch/step_status/jsonschema/2-0-1";

    /**
     * The UUID identifying the particular job run, which will be used to tie events together later.
     * (Required)
     */
    @JsonProperty("run_id")
    @JsonPropertyDescription("The UUID identifying the particular job run, which will be used to tie events together later.")
    @Size(max = 36)
    @NotNull
    private String runId;
    /**
     * Name of the step of the job run.
     * (Required)
     *
     */
    @JsonProperty("name")
    @JsonPropertyDescription("Name of the step of the job run.")
    @Size(max = 255)
    @NotNull
    private String name;
    /**
     * Additional context for the step (e.g. Google AdWords Account ID).
     *
     */
    @JsonProperty("context")
    @JsonPropertyDescription("Additional context for the step (e.g. Google AdWords Account ID).")
    @Size(max = 255)
    private String context;
    /**
     * State of the step of the job run.
     * (Required)
     *
     */
    @JsonProperty("state")
    @JsonPropertyDescription("State of the step of the job run.")
    @NotNull
    private StepStatus.State state;
    /**
     * When did this step in the job run start (e.g. '2017-06-01T13:54:59Z')?
     *
     */
    @JsonProperty("started_at")
    @JsonPropertyDescription("When did this step in the job run start (e.g. '2017-06-01T13:54:59Z')?")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT")
    private Date startedAt;
    /**
     * When did this step in the job run end (e.g. '2017-06-01T13:55:05.114Z')?
     *
     */
    @JsonProperty("ended_at")
    @JsonPropertyDescription("When did this step in the job run end (e.g. '2017-06-01T13:55:05.114Z')?")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT")
    private Date endedAt;
    private final static long serialVersionUID = -8826362676335194585L;

    /**
     * No args constructor for use in serialization
     *
     */
    public StepStatus() {
        super(iglu);
    }

    /**
     * The UUID identifying the particular job run, which will be used to tie events together later.
     * (Required)
     *
     */
    @JsonProperty("run_id")
    public String getRunId() {
        return runId;
    }

    /**
     * The UUID identifying the particular job run, which will be used to tie events together later.
     * (Required)
     *
     */
    @JsonProperty("run_id")
    public void setRunId(String runId) {
        this.runId = runId;
    }

    public StepStatus withRunId(String runId) {
        this.runId = runId;
        return this;
    }

    /**
     * Name of the step of the job run.
     * (Required)
     *
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Name of the step of the job run.
     * (Required)
     *
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
     * Additional context for the step (e.g. Google AdWords Account ID).
     *
     */
    @JsonProperty("context")
    public String getContext() {
        return context;
    }

    /**
     * Additional context for the step (e.g. Google AdWords Account ID).
     */
    @JsonProperty("context")
    public void setContext(String context) {
        this.context = context;
    }

    public StepStatus withContext(String context) {
        this.context = context;
        return this;
    }

    /**
     * State of the step of the job run.
     * (Required)
     *
     */
    @JsonProperty("state")
    public StepStatus.State getState() {
        return state;
    }

    /**
     * State of the step of the job run.
     * (Required)
     *
     */
    @JsonProperty("state")
    public void setState(StepStatus.State state) {
        this.state = state;
    }

    public StepStatus withState(StepStatus.State state) {
        this.state = state;
        return this;
    }

    /**
     * When did this step in the job run start (e.g. '2017-06-01T13:54:59Z')?
     *
     */
    @JsonProperty("started_at")
    public Date getStartedAt() {
        return startedAt;
    }

    /**
     * When did this step in the job run start (e.g. '2017-06-01T13:54:59Z')?
     *
     */
    @JsonProperty("started_at")
    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public StepStatus withStartedAt(Date startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    /**
     * When did this step in the job run end (e.g. '2017-06-01T13:55:05.114Z')?
     *
     */
    @JsonProperty("ended_at")
    public Date getEndedAt() {
        return endedAt;
    }

    /**
     * When did this step in the job run end (e.g. '2017-06-01T13:55:05.114Z')?
     *
     */
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
        return new HashCodeBuilder().append(runId).append(name).append(context).append(state).append(startedAt).append(endedAt).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof StepStatus)) {
            return false;
        }
        StepStatus rhs = ((StepStatus) other);
        return new EqualsBuilder().append(runId, rhs.runId).append(name, rhs.name).append(context, rhs.context).append(state, rhs.state).append(startedAt, rhs.startedAt).append(endedAt, rhs.endedAt).isEquals();
    }

    public enum State {

        PENDING("PENDING"),
        RUNNING("RUNNING"),
        COMPLETED("COMPLETED"),
        CANCELLED("CANCELLED"),
        FAILED("FAILED");
        private final String value;
        private final static Map<String, StepStatus.State> CONSTANTS = new HashMap<String, StepStatus.State>();

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


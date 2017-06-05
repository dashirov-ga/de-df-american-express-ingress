package co.ga.batch;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Schema for a Batch Job Failed Event
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "run_id"
})
public class JobState extends JsonSelfDescribingContext implements Serializable {
    public enum State {
        STARTING("iglu:co.ga.batch/job_starting/jsonschema/2-0-1"),
        FAILED("iglu:co.ga.batch/job_failed/jsonschema/2-0-1"),
        SUCCEEDED("iglu:co.ga.batch/job_succeeded/jsonschema/2-0-1");
        private final String state;
        private State(final String state) {
            this.state = state;
        }
        @Override
        public String toString() {
            return state;
        }
    }


    /**
     * The UUID identifying the particular job run, which will be used to tie events together later.
     * (Required)
     */
    @JsonProperty("run_id")
    @JsonPropertyDescription("The UUID identifying the particular job run, which will be used to tie events together later.")
    @Size(max = 36)
    @NotNull
    private String runId;
    private final static long serialVersionUID = 5396097983235411492L;

    /**
     * No args constructor for use in serialization
     *
     */
    public JobState(String iglu) {
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
        if ((other instanceof JobState) == false) {
            return false;
        }
        JobState rhs = ((JobState) other);
        return new EqualsBuilder().append(runId, rhs.runId).isEquals();
    }

}


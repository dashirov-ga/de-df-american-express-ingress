package co.ga.batch;

/**
 * Created by dashirov on 1/12/17.
 */
public class JobSucceeded extends JobState {
    public JobSucceeded() {
        super(State.SUCCEEDED.iglu());

    }

    public JobSucceeded(String runId) {
        super(State.SUCCEEDED.iglu());
        this.setRunId(runId);
    }
}

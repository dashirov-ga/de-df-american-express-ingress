package co.ga.batch;

/**
 * Created by dashirov on 1/12/17.
 */
public class JobFailed extends JobState {
    public JobFailed() {
        super(State.FAILED.iglu());

    }

    public JobFailed(String runId) {
        super(State.FAILED.iglu());
        this.setRunId(runId);
    }
}

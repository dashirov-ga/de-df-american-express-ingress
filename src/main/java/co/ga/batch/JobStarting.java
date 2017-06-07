package co.ga.batch;

/**
 * Created by dashirov on 1/12/17.
 */
public class JobStarting extends JobState {
    public JobStarting() {
        super(State.STARTING.iglu());

    }

    public JobStarting(String runId) {
        super(State.STARTING.iglu());
        this.setRunId(runId);
        return;
    }
}

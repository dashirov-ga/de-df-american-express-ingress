package co.ga.batch;

/**
 * Created by dashirov on 1/12/17.
 */
public class JobFailed extends JobState {
    public JobFailed() {
        super();
        setIglu(State.FAILED.name());
    }

    public JobFailed(String runId) {
        super(runId);
        setIglu(State.FAILED.name());
    }
}

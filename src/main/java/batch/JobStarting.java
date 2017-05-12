package batch;

/**
 * Created by dashirov on 1/12/17.
 */
public class JobStarting extends JobState{
   public JobStarting() {
      super();
      setIglu(State.STARTING.name());
   }
   public JobStarting(String runId) {
      super(runId);
      setIglu(State.STARTING.name());
   }
}

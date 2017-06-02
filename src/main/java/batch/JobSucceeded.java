package batch;

/**
 * Created by dashirov on 1/12/17.
 */
public class JobSucceeded extends JobState{
   public JobSucceeded() {
      super();
     setIglu(State.SUCCEEDED.name());
   }
   public JobSucceeded(String runId) {
      super(runId);
      setIglu(State.SUCCEEDED.name());
   }
}

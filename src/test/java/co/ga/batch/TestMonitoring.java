package co.ga.batch;

import com.snowplowanalytics.snowplow.tracker.DevicePlatform;
import com.snowplowanalytics.snowplow.tracker.Tracker;
import com.snowplowanalytics.snowplow.tracker.emitter.BatchEmitter;
import com.snowplowanalytics.snowplow.tracker.emitter.Emitter;
import com.snowplowanalytics.snowplow.tracker.emitter.RequestCallback;
import com.snowplowanalytics.snowplow.tracker.events.Event;
import com.snowplowanalytics.snowplow.tracker.events.Unstructured;
import com.snowplowanalytics.snowplow.tracker.http.ApacheHttpClientAdapter;
import com.snowplowanalytics.snowplow.tracker.payload.TrackerPayload;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dashirov on 6/2/17.
 */
public class TestMonitoring {
    private Tracker tracker;
    private static final Logger LOGGER = LoggerFactory.getLogger(TestMonitoring.class);
    private static final String snowplowUrl = "http://sp.generalassemb.ly/";
    private static final AtomicInteger eventCounter = new AtomicInteger(0);


    @BeforeClass
    public static void setUpBeforeClass() {

    }

    @Before
    public void setUp() {
        Emitter emitter;
        emitter = BatchEmitter.builder()
                .bufferSize(5)
                .threadCount(5)
                .httpClientAdapter(
                        ApacheHttpClientAdapter.builder()

                                .url(snowplowUrl)
                                .httpClient(
                                        HttpClients.createDefault()
                                ).build()
                ).requestCallback(
                        new RequestCallback() {
                            @Override
                            public void onSuccess(int i) {
                                int events_left = eventCounter.getAndAdd(-1 * i);
                                LOGGER.info("Successful delivery: good {}; left to process {}", i, events_left);
                            }

                            @Override
                            public void onFailure(int i, List<TrackerPayload> list) {
                                int events_left = eventCounter.getAndAdd(-1 * (i + list.size()));
                                LOGGER.info("Unsuccessful delivery: good {} + bad {}; left to process {}", i, list.size(), events_left);
                                LOGGER.error(list.toString());
                            }
                        }
                ).build();

        // Before the test, setup a tracker to be used through this suite

        this.tracker = new Tracker.TrackerBuilder(emitter,
                "namespace",
                "df-american-express-ingress::test"
        )
                .base64(false)
                .platform(DevicePlatform.ServerSideApp)
                .build();
    }

    @After
    public void tearDown() {
        // nothing to carefully deconstruct prior to exit
        LOGGER.info("Buffer is empty: {} ({})", tracker.getEmitter().getBuffer().isEmpty(), tracker.getEmitter().getBuffer().size());
        this.tracker.getEmitter().flushBuffer();


        while (eventCounter.get() > 0) {
            LOGGER.warn("Waiting for events to be delivered or error out...");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("Shutdown completed");

    }

    @Test
    public void testJobStarting() {
        String runId=new UUID(0L, 0L).toString().replace("-", "");

        Event t ;
        t = Unstructured.builder().eventData(new JobStarting(runId).getSelfDescribingJson()).build();
        tracker.track(t);
        eventCounter.incrementAndGet();
        for (int i = 0; i < 1; i++) {
            t = Unstructured.builder()
                    .eventData(
                            new StepStatus()
                                    .withName("test-step-1")
                                    .withState(StepStatus.State.RUNNING)
                                    .withContext("Account #12345")
                                    .withStartedAt(new Date())
                                    .withRunId(runId
                                    ).getSelfDescribingJson()
                    )//  .customContext(
                    // Takes a list of custom contexts
                    //       Collections.singletonList( new GoogleAdWordsAccountContext().withAccount(Long.toString(12345678910L)).getSelfDescribingJson())
                    // )
                    .build();
            tracker.track(t);

            int events_to_deliver = eventCounter.getAndIncrement();
            LOGGER.info("Scheduled {}, left to deliver {}", i, events_to_deliver);

        }
        LOGGER.info("Done Generating");
        Thread.yield();
        // Wait for all events to make it into SP
/*
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        LOGGER.info("Done Waiting");
*/

    }
}

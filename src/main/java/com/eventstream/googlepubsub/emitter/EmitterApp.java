package com.eventstream.googlepubsub.emitter;

import com.eventstream.googlepubsub.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class EmitterApp {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Properties properties = Util.readProperties("/application.properties");

        var start = System.currentTimeMillis();

        final var config = new PublisherConfig(
                properties.getProperty("spring.cloud.gcp.project-id"),
                properties.getProperty("gcp.stream-name"),
                properties.getProperty("gcp.credentials.location")
        );

        final GooglePubsubEmitter googlePubsubEmitter = new GooglePubsubEmitter(config);

        final List<String> events = List.of(
                "{\"eventType\" : \"event-1\", \"desc\":\"ignoreme1\"}",
                "{\"eventType\" : \"event-2\", \"desc\":\"ignoreme2\"}",
                "{\"eventType\" : \"event-3\", \"desc\":\"ignoreme3\"}",
                "{\"eventType\" : \"event-4\", \"desc\":\"ignoreme4\"}",
                "{\"eventType\" : \"event-5\", \"desc\":\"ignoreme5\"}",
                "{\"eventType\" : \"event-6\", \"desc\":\"ignoreme6\"}",
                "{\"eventType\" : \"event-7\", \"desc\":\"ignoreme7\"}",
                "{\"eventType\" : \"event-8\", \"desc\":\"ignoreme8\"}",
                "{\"eventType\" : \"event-9\", \"desc\":\"ignoreme9\"}",
                "{\"eventType\" : \"event-10\", \"desc\":\"ignoreme10\"}",
                "{\"eventType\" : \"event-11\", \"desc\":\"ignoreme11\"}",
                "{\"eventType\" : \"event-12\", \"desc\":\"ignoreme12\"}",
                "{\"eventType\" : \"event-13\", \"desc\":\"ignoreme13\"}",
                "{\"eventType\" : \"event-14\", \"desc\":\"ignoreme14\"}",
                "{\"eventType\" : \"event-15\", \"desc\":\"ignoreme15\"}",
                "{\"eventType\" : \"event-16\", \"desc\":\"ignoreme16\"}",
                "{\"eventType\" : \"event-17\", \"desc\":\"ignoreme17\"}",
                "{\"eventType\" : \"event-18\", \"desc\":\"ignoreme18\"}",
                "{\"eventType\" : \"event-19\", \"desc\":\"ignoreme19\"}",
                "{\"eventType\" : \"event-20\", \"desc\":\"ignoreme20\"}",
                "{\"eventType\" : \"event-21\", \"desc\":\"ignoreme21\"}",
                "{\"eventType\" : \"event-22\", \"desc\":\"ignoreme22\"}",
                "{\"eventType\" : \"event-23\", \"desc\":\"ignoreme23\"}",
                "{\"eventType\" : \"event-24\", \"desc\":\"ignoreme24\"}",
                "{\"eventType\" : \"event-25\", \"desc\":\"ignoreme25\"}",
                "{\"eventType\" : \"event-26\", \"desc\":\"ignoreme26\"}",
                "{\"eventType\" : \"event-27\", \"desc\":\"ignoreme28\"}",
                "{\"eventType\" : \"event-28\", \"desc\":\"ignoreme28\"}",
                "{\"eventType\" : \"event-29\", \"desc\":\"ignoreme29\"}",
                "{\"eventType\" : \"event-30\", \"desc\":\"ignoreme30\"}",
                "{\"eventType\" : \"event-31\", \"desc\":\"ignoreme31\"}",
                "{\"eventType\" : \"event-32\", \"desc\":\"ignoreme32\"}"
        );

        int i = 1;
        for (String event : events) {
            googlePubsubEmitter.emitEvent(i, event);
            i++;
        }

        Util.writeToFile("performance/pubsub_perf_async_1.csv", googlePubsubEmitter.getTimeMap());

        googlePubsubEmitter.stop();
        System.out.println("===============================================");
        System.out.println("total: " + (System.currentTimeMillis() - start));
        System.out.println("===============================================");
    }

}

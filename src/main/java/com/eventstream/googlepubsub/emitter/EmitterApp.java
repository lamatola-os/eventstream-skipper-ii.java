package com.eventstream.googlepubsub.emitter;

import java.util.List;

public class EmitterApp {

    public static void main(String[] args) {

        final var config = new PublisherConfig(
                "customer-support-61013",
                "orders-streaming",
                "src/main/resources/credentials.json"
        );
        GooglePubsubEmitter googlePubsubEmitter = new GooglePubsubEmitter(config);

        final List<String> events = List.of(
                "{\"eventType\" : \"event-1\", \"desc\":\"ignoreme1\"}",
                "{\"eventType\" : \"event-2\", \"desc\":\"ignoreme2\"}"
        );

        googlePubsubEmitter.emitEvents(events);

        googlePubsubEmitter.stop();
    }
}

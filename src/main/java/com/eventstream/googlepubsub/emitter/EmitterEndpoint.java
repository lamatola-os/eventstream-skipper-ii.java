package com.eventstream.googlepubsub.emitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
public class EmitterEndpoint {

    private static final String EVENT_TEST = "{\"eventType\": \"order_placed\"}";

    @Autowired
    private PublisherGateway messagingGateway;

    @Autowired
    PublisherConfig config;

    @Autowired
    GooglePubsubEmitter googlePubsubEmitter;

    public EmitterEndpoint() throws IOException {
    }

    @PostMapping("/v1/event/publish")
    public CompletableFuture<Long> publish() throws IOException {
        var start = System.currentTimeMillis();
        var res = googlePubsubEmitter.emitEvent(1, EVENT_TEST)
                .thenApply($ -> (System.currentTimeMillis() - start));

        return res;
    }

    @PostMapping("/v2/event/publish")
    public Long publish2() throws IOException {
        var start = System.currentTimeMillis();
        messagingGateway.sendToPubsub(EVENT_TEST);

        return (System.currentTimeMillis() - start);
    }
}

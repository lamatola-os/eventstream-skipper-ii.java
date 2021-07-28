package com.eventstream.googlepubsub.emitter;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "pubsubOutputChannel")
public interface PublisherGateway {

    void sendToPubsub(String text);
}

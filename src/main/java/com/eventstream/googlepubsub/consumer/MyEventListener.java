package com.eventstream.googlepubsub.consumer;

import com.eventstream.googlepubsub.consumer.api.Event;

public class MyEventListener extends GcpPubSubEventListener {

    @Override
    public void onEvent(Event event) {
        System.out.println("received event: " + event.getId() + " ~> " + event.getPayload());
    }
}

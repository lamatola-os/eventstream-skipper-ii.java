package com.eventstream.googlepubsub.consumer;

import com.eventstream.googlepubsub.consumer.api.Event;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;

public abstract class GcpPubSubEventListener implements MessageReceiver {

    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
        onEvent(new Event(message.getMessageId(), message.getData().toStringUtf8()));
        consumer.ack();
    }

    abstract void onEvent(Event event);
}

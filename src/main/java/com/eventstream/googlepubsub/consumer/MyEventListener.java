package com.eventstream.googlepubsub.consumer;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;

public class MyEventListener implements MessageReceiver {

    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {

        System.out.println("received event: " +
                message.getMessageId() + " ~> " + message.getData().toStringUtf8());
        consumer.ack();

    }
}

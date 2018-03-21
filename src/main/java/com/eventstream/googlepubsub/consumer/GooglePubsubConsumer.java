package com.eventstream.googlepubsub.consumer;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

class Config {

    public String projectId;
    public String consumerName;
    public String credentials;

    public Config(String projectId, String consumerName, String credentials) {
        this.projectId = projectId;
        this.consumerName = consumerName;
        this.credentials = credentials;
    }
}

public class GooglePubsubConsumer {

    private static final Logger logger = LoggerFactory.getLogger(GooglePubsubConsumer.class);

    public static void main(String[] args) throws Exception {
        String projectId = "customer-support-610a3";
        String subscriberId = "orders-streaming-consumer";
        ProjectSubscriptionName subsciptionName = ProjectSubscriptionName.of(projectId, subscriberId);
        Subscriber.Builder builder = Subscriber.newBuilder(subsciptionName, new OurReceiver());

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/credentials.json"));
        builder.setCredentialsProvider(FixedCredentialsProvider.create(credentials));
        Subscriber subscriber = builder.build();

        logger.info("starting " + subscriberId);
        subscriber.startAsync().awaitRunning();

        subscriber.awaitTerminated(10000000, TimeUnit.MILLISECONDS);
    }

    private static class OurReceiver implements MessageReceiver {
        @Override
        public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
            System.out.println("received message: " + message.getMessageId());
            consumer.ack();
        }
    }
}

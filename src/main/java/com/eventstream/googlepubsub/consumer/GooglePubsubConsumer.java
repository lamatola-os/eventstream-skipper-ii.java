package com.eventstream.googlepubsub.consumer;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

class ConsumerConfig {

    public String projectId;
    public String consumerName;
    public String credentials;

    public ConsumerConfig(String projectId, String consumerName, String credentials) {
        this.projectId = projectId;
        this.consumerName = consumerName;
        this.credentials = credentials;
    }
}

public class GooglePubsubConsumer {

    private static final Logger logger = LoggerFactory.getLogger(GooglePubsubConsumer.class);

    public static void main(String[] args) throws Exception {
        final var config = new ConsumerConfig(
                "customer-support-610a3",
                "orders-streaming-consumer",
                "src/main/resources/credentials.json"
        );

        ProjectSubscriptionName subsciptionName = ProjectSubscriptionName.of(config.projectId, config.consumerName);
        Subscriber.Builder builderMut = Subscriber.newBuilder(subsciptionName, new MyEventListener());

        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new FileInputStream(config.credentials));

        builderMut.setCredentialsProvider(FixedCredentialsProvider.create(credentials));
        Subscriber subscriber = builderMut.build();

        logger.info("starting consumer " + config.consumerName);
        subscriber.startAsync().awaitRunning();

        subscriber.awaitTerminated(10000000, TimeUnit.MILLISECONDS);
    }
}

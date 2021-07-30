package com.eventstream.googlepubsub.consumer;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Data
class ConsumerConfig {

    private String projectId;
    private String consumerName;
    private String credentials;

    public ConsumerConfig(String projectId, String consumerName, String credentials) {
        this.projectId = projectId;
        this.consumerName = consumerName;
        this.credentials = credentials;
    }
}

public class GooglePubsubConsumer {

    private static final Logger logger = LoggerFactory.getLogger(GooglePubsubConsumer.class);
    private static final ConsumerConfig config = new ConsumerConfig(
            "customer-support-610a3",
            "orders-streaming-consumer",
            "src/main/resources/credentials.json"
    );

    public static void main(String[] args) throws Exception {

        Subscriber subscriber = createStreamSubscriber();

        logger.info("starting consumer " + config.getConsumerName());
        subscriber.startAsync().awaitRunning();

        subscriber.awaitTerminated(10000000, TimeUnit.MILLISECONDS);
    }

    private static Subscriber createStreamSubscriber() throws IOException {
        ProjectSubscriptionName subsciptionName = ProjectSubscriptionName.of(config.getProjectId(), config.getConsumerName());

        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new FileInputStream(config.getCredentials()));

        Subscriber.Builder builderMut = Subscriber.newBuilder(subsciptionName, new MyEventListener());
        builderMut.setCredentialsProvider(FixedCredentialsProvider.create(credentials));
        Subscriber subscriber = builderMut.build();
        return subscriber;
    }
}

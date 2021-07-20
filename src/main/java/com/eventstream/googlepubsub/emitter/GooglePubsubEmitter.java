package com.eventstream.googlepubsub.emitter;

import com.google.api.core.ApiFuture;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Data
class PublisherConfig {
    private String projectId;
    private String streamName;
    private String credentials;

    public PublisherConfig(String projectId, String streamName, String credentials) {
        this.projectId = projectId;
        this.streamName = streamName;
        this.credentials = credentials;
    }
}

public class GooglePubsubEmitter {

    private PublisherConfig config;
    private Publisher pubSubPublisher = null;

    public GooglePubsubEmitter(PublisherConfig config) {
        this.config = config;
    }

    public void emitEvents(List<String> events) {
        final var topicName =
                ProjectTopicName.of(config.getProjectId(), config.getStreamName());

        List<ApiFuture<String>> messageIdFutures = new ArrayList<>();

        try {

            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new FileInputStream(config.getCredentials()));

            pubSubPublisher = Publisher.newBuilder(topicName)
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();

            for (String message : events) {
                ByteString data = ByteString.copyFromUtf8(message);
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

                ApiFuture<String> messageIdFuture = pubSubPublisher.publish(pubsubMessage);
                messageIdFutures.add(messageIdFuture);
                var emittedMessageId = messageIdFuture.get();
                System.out.println("emittedMessageId: " + emittedMessageId);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void stop() {
        System.out.println("Shutting down publisher for " + pubSubPublisher.getTopicName());
        pubSubPublisher.shutdown();
    }
}

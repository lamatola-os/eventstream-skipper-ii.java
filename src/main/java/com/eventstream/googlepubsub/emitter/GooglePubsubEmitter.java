package com.eventstream.googlepubsub.emitter;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

class PublisherConfig {

    public String projectId;
    public String streamName;
    public String credentials;

    public PublisherConfig(String projectId, String streamName, String credentials) {
        this.projectId = projectId;
        this.streamName = streamName;
        this.credentials = credentials;
    }
}

public class GooglePubsubEmitter {

    public static void main(String[] args) {

        final var config = new PublisherConfig(
                "customer-support-61013",
                "orders-streaming",
                "src/main/resources/credentials.json"
        );

        final List<String> events = List.of("event-1", "event-2");

        final ProjectTopicName topicName =
                ProjectTopicName.of(config.projectId, config.streamName);

        Publisher publisher = null;
        List<ApiFuture<String>> messageIdFutures = new ArrayList<>();

        try {

            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new FileInputStream(config.credentials));

            publisher = Publisher.newBuilder(topicName)
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();

            for (String message : events) {
                ByteString data = ByteString.copyFromUtf8(message);
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

                ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
                messageIdFutures.add(messageIdFuture);
            }
        } catch (Throwable t) {
            t.printStackTrace();

        } finally {
            List<String> messageIds = null;
            try {
                System.out.println("Waiting for acks");
                messageIds = ApiFutures.allAsList(messageIdFutures).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            for (String messageId : messageIds) {
                System.out.println("published with message ID: " + messageId);
            }

            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                try {
                    System.out.println("Shutting down publisher for " + publisher.getTopicName());
                    publisher.shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

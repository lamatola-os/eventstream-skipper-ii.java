package com.eventstream.googlepubsub.emitter;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.batching.BatchingSettings;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import lombok.Data;
import org.threeten.bp.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    Duration delayThreshold = Duration.ofMillis(500);
    private HashMap<Integer, Long> timeMap = new HashMap<>();

    public GooglePubsubEmitter(PublisherConfig config) throws IOException {
        this.config = config;
        final var topicName =
                ProjectTopicName.of(config.getProjectId(), config.getStreamName());

        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new FileInputStream(config.getCredentials()));

        long maxNumberOfEvents = 8L;
        long maxRequestThreshold = 5000L;

        BatchingSettings batchingSettings =
                BatchingSettings.newBuilder()
                        .setElementCountThreshold(maxNumberOfEvents)
                        .setRequestByteThreshold(maxRequestThreshold)
                        .setDelayThreshold(delayThreshold)
                        .build();

        pubSubPublisher = Publisher.newBuilder(topicName)
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
//                .setBatchingSettings(batchingSettings)
                .build();
    }

    public void emitEvents(List<String> events) {

        try {
            for (String event : events) {
                var id = emitEvent(1, event).get();
                System.out.println("after processing: " + id);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public CompletableFuture<String> emitEvent(int id, String message) throws InterruptedException, java.util.concurrent.ExecutionException {
        ByteString data = ByteString.copyFromUtf8(message);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

        var start = System.currentTimeMillis();
        ApiFuture<String> messageIdFuture = pubSubPublisher.publish(pubsubMessage);

        CompletableFuture<String> f = new CompletableFuture<>();

        ApiFutures.addCallback(
                messageIdFuture,
                new ApiFutureCallback<>() {

                    @Override
                    public void onFailure(Throwable throwable) {
                        if (throwable instanceof ApiException) {
                            ApiException apiException = ((ApiException) throwable);
                            System.out.println(apiException.getStatusCode().getCode());
                            System.out.println(apiException.isRetryable());
                        }
                        //FIXME send alert
                        System.out.println("Error publishing message : " + message);
                        //retry
                        f.completeExceptionally(throwable);
                    }

                    @Override
                    public void onSuccess(String messageId) {
                        System.out.println("Published event ID: " + messageId);
                        f.complete(messageId);
                    }
                },
//                MoreExecutors.directExecutor());
                MoreExecutors.newDirectExecutorService());

        long timeTaken = System.currentTimeMillis() - start;
        timeMap.put(id, timeTaken);

//        System.out.println("emittedMessageId: " + emittedMessageId + ", timeTaken:" + timeTaken + " ms");
        return f;
    }

    public void stop() {
        System.out.println("Shutting down publisher for " + pubSubPublisher.getTopicName());
        pubSubPublisher.shutdown();
    }

    public HashMap<Integer, Long> getTimeMap() {
        return timeMap;
    }
}

package com.eventstream.googlepubsub.emitter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.IOException;
import java.time.LocalDateTime;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${gcp.stream-name}")
    private String eventStream;

    @Bean
    @ServiceActivator(inputChannel = "pubsubOutputChannel")
    public MessageHandler eventSender(PubSubTemplate pubsubTemplate) {
        PubSubMessageHandler pubSubMessageHandler = new PubSubMessageHandler(pubsubTemplate, eventStream);
        pubSubMessageHandler.setSync(true);

        pubSubMessageHandler.setPublishCallback(new ListenableFutureCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("failed at : " + System.currentTimeMillis());
            }

            @Override
            public void onSuccess(String s) {
                System.out.println("completed at : " + System.currentTimeMillis());
            }
        });
        return pubSubMessageHandler;
    }

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    @Value("${gcp.stream-name}")
    private String streamName;

    @Value("${gcp.credentials.location}")
    private String projectCreds;

    @Bean
    public PublisherConfig config(@Value("${spring.cloud.gcp.project-id}") String projectId,
                                  @Value("${gcp.stream-name}") String streamName,
                                  @Value("${gcp.credentials.location}") String projectCreds
                                  ) {
        return new PublisherConfig(
                projectId,
                streamName,
                projectCreds
        );
    }

    @Bean
    public GooglePubsubEmitter googlePubsubEmitter(PublisherConfig config) throws IOException {
        final GooglePubsubEmitter googlePubsubEmitter = new GooglePubsubEmitter(config);
        return googlePubsubEmitter;
    }
}

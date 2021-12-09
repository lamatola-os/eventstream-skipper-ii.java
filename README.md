eventstream-skipper (googlepubsub)
-------------------

- Java pipeline to stream events to Google Pub sub using Spring, PubSub SDK
- JMeter perf is included in 

eventstream-googlepubsub
-----------------------

```
              |----------------------------------
publisher ~>  | 1 | 2 | 3 | 4 | 5 | ........| n |    ~>  subscription ~> Subscriber 
              |----------------------------------     
                        stream (topic)
```

Publisher
---------

- create stream (`topic`)
- create IAM role with write access and use it in publisher
```bash
pubsub.schemas.attach
pubsub.schemas.create
pubsub.schemas.delete
pubsub.schemas.get
pubsub.schemas.getIamPolicy
pubsub.schemas.list
pubsub.schemas.setIamPolicy
pubsub.schemas.validate
pubsub.snapshots.create
pubsub.snapshots.delete
pubsub.snapshots.get
pubsub.snapshots.getIamPolicy
pubsub.snapshots.list
pubsub.snapshots.seek
pubsub.snapshots.setIamPolicy
pubsub.snapshots.update
pubsub.subscriptions.consume
pubsub.subscriptions.create
pubsub.subscriptions.delete
pubsub.subscriptions.get
pubsub.subscriptions.getIamPolicy
pubsub.subscriptions.list
pubsub.subscriptions.setIamPolicy
pubsub.subscriptions.update
pubsub.topics.attachSubscription
pubsub.topics.create
pubsub.topics.delete
pubsub.topics.detachSubscription
pubsub.topics.get
pubsub.topics.getIamPolicy
pubsub.topics.list
pubsub.topics.publish
pubsub.topics.setIamPolicy
pubsub.topics.update
pubsub.topics.updateTag
resourcemanager.projects.get
serviceusage.quotas.get
serviceusage.services.get
serviceusage.services.list
```  
- publisher - https://github.com/duwamish-os/eventstream-skipper-ii.java/blob/master/src/main/java/com/eventstream/googlepubsub/emitter/GooglePubsubEmitter.java

Subscriber
-----------

- create subscription
- use `subscriptionId` in consumer
- subscriber - https://github.com/duwamish-os/eventstream-skipper-ii.java/blob/master/src/main/java/com/eventstream/googlepubsub/consumer/GooglePubsubConsumer.java

HTTP Write Perf
----------

```bash
## google sdk
curl --request POST localhost:8080/v1/publish
## spring sdk

1540 ms
1071 ms
1040 ms
1040 ms
1040 ms
1040 ms
1040 ms
1040 ms
1040 ms
1040 ms
```

Connections
--

```bash
12:09:46.933 [grpc-nio-worker-ELG-1-2] DEBUG i.g.n.s.i.n.handler.ssl.SslHandler - [id: 0x438d90fe, L:/172.23.16.60:64014 - R:pubsub.googleapis.com/172.217.4.202:443] HANDSHAKEN: protocol:TLSv1.3 cipher suite:TLS_AES_128_GCM_SHA256
12:09:46.937 [grpc-nio-worker-ELG-1-2] DEBUG i.g.n.s.i.g.netty.NettyClientHandler - [id: 0x438d90fe, L:/172.23.16.60:64014 - R:pubsub.googleapis.com/172.217.4.202:443] OUTBOUND SETTINGS: ack=false settings={ENABLE_PUSH=0, MAX_CONCURRENT_STREAMS=0, INITIAL_WINDOW_SIZE=1048576, MAX_HEADER_LIST_SIZE=8192}
12:09:46.939 [grpc-nio-worker-ELG-1-2] DEBUG i.g.n.s.i.g.netty.NettyClientHandler - [id: 0x438d90fe, L:/172.23.16.60:64014 - R:pubsub.googleapis.com/172.217.4.202:443] OUTBOUND WINDOW_UPDATE: streamId=0 windowSizeIncrement=983041
12:09:47.015 [grpc-nio-worker-ELG-1-2] DEBUG i.g.n.s.i.g.netty.NettyClientHandler - [id: 0x438d90fe, L:/172.23.16.60:64014 - R:pubsub.googleapis.com/172.217.4.202:443] INBOUND SETTINGS: ack=false settings={MAX_CONCURRENT_STREAMS=100, INITIAL_WINDOW_SIZE=1048576, MAX_HEADER_LIST_SIZE=65536}
12:09:47.016 [grpc-nio-worker-ELG-1-2] DEBUG i.g.n.s.i.g.netty.NettyClientHandler - [id: 0x438d90fe, L:/172.23.16.60:64014 - R:pubsub.googleapis.com/172.217.4.202:443] OUTBOUND SETTINGS: ack=true
12:09:47.062 [grpc-nio-worker-ELG-1-2] DEBUG i.g.n.s.i.g.netty.NettyClientHandler - [id: 0x438d90fe, L:/172.23.16.60:64014 - R:pubsub.googleapis.com/172.217.4.202:443] INBOUND WINDOW_UPDATE: streamId=0 windowSizeIncrement=983041
12:09:47.063 [grpc-nio-worker-ELG-1-2] DEBUG i.g.n.s.i.g.netty.NettyClientHandler - [id: 0x438d90fe, L:/172.23.16.60:64014 - R:pubsub.googleapis.com/172.217.4.202:443] INBOUND SETTINGS: ack=true
12:09:47.066 [grpc-nio-worker-ELG-1-2] DEBUG i.g.n.s.i.g.netty.NettyClientHandler - [id: 0x438d90fe, L:/172.23.16.60:64014 - R:pubsub.googleapis.com/172.217.4.202:443] OUTBOUND HEADERS: streamId=3 headers=GrpcHttp2OutboundHeaders[:authority: pubsub.googleapis.com:443, :path: /google.pubsub.v1.Subscriber/StreamingPull, :method: POST, :scheme: https, content-type: application/grpc, te: trailers, user-agent: grpc-java-netty/1.39.0, x-goog-api-client: gl-java/12.0.2 gapic/1.113.5 gax/1.66.0 grpc/1.39.0, grpc-accept-encoding: gzip, authorization: Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjM2YTBhZjAxODI4NWUyNmMwOTRlODgxYWM2ZWM4YzE1Nzk5OGRiZTgiLCJ0eXAiOiJKV1QifQ.eyJhdWQiOiJodHRwczovL3B1YnN1Yi5nb29nbGVhcGlzLmNvbS8iLCJleHAiOjE2Mzc2MTUzODcsImlhdCI6MTYzNzYxMTc4NywiaXNzIjoiYXdzLWF3YWNzLWluZ2VzdG9yLTc0NDdAYmJ5dXMtY3BsYXQtY3NhLXAwMS5pYW0uZ3NlcnZpY2VhY2NvdW50LmNvbSIsInN1YiI6ImF3cy1hd2Fjcy1pbmdlc3Rvci03NDQ3QGJieXVzLWNwbGF0LWNzYS1wMDEuaWFtLmdzZXJ2aWNlYWNjb3VudC5jb20ifQ.ZJlRGULrMqQsykFnJlVGRN1JNkKsZwCOlUFolI6CaWaLM0uZ2zZNiXm8t0-gqISxBUpV5XsoTUr1ryw-JdPoIvB8PN4Os3rQuwcSmcQeVxUdsSM827ddvJp9UqUP1e9JOVk4EL51wbXWMJJG2KR4S3mDmsALU_RE16VYAYcvOHjghVkK4QwnUbaKkKsEbmzvN1zCDlO81_1Zq8VJ7Qbuzzb09vEtis74fZkxSkBErH2DDaNf2wQlzi9CzMZ7Cv2CH_UKOI62YV7_S7DaAK4ngJefB-AMqIotEeKIl0_iSdOY3tPo4bylP1uNzaDJpcsflGDa9-PkqR-puG_DOXAu8A] streamDependency=0 weight=16 exclusive=false padding=0 endStream=false
```

Threads
------

![](performance/GCP-Before_HttpCall-pub-sub-Threads.png)

![](performance/GCP-After_Http_pub-sub-Threads.png)

pricing
--------
https://cloud.google.com/pubsub/pricing

scala version
--
https://github.com/duwamish-os/eventstream-skipper-ii


known error
------

```bash
java.lang.IllegalAccessException: class io.grpc.netty.shaded.io.netty.util.internal.PlatformDependent0$6 cannot access class jdk.internal.misc.Unsafe (in module java.base) because module java.base does not export jdk.internal.misc to unnamed module @63376bed
	at java.base/jdk.internal.reflect.Reflection.newIllegalAccessException(Reflection.java:376)
	at java.base/java.lang.reflect.AccessibleObject.checkAccess(AccessibleObject.java:639)
	at java.base/java.lang.reflect.Method.invoke(Method.java:559)
	at io.grpc.netty.shaded.io.netty.util.internal.PlatformDependent0$6.run(PlatformDependent0.java:352)
	at java.base/java.security.AccessController.doPrivileged(AccessController.java:310)
	at io.grpc.netty.shaded.io.netty.util.internal.PlatformDependent0.<clinit>(PlatformDependent0.java:343)
	at io.grpc.netty.shaded.io.netty.util.internal.PlatformDependent.isAndroid(PlatformDependent.java:289)
	at io.grpc.netty.shaded.io.netty.util.internal.PlatformDependent.<clinit>(PlatformDependent.java:92)
	at io.grpc.netty.shaded.io.netty.util.AsciiString.<init>(AsciiString.java:223)
	at io.grpc.netty.shaded.io.netty.util.AsciiString.<init>(AsciiString.java:210)
	at io.grpc.netty.shaded.io.netty.util.AsciiString.cached(AsciiString.java:1401)
	at io.grpc.netty.shaded.io.netty.util.AsciiString.<clinit>(AsciiString.java:48)
	at io.grpc.netty.shaded.io.grpc.netty.Utils.<clinit>(Utils.java:74)
	at io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder.<clinit>(NettyChannelBuilder.java:82)
	at io.grpc.netty.shaded.io.grpc.netty.NettyChannelProvider.builderForAddress(NettyChannelProvider.java:38)
	at io.grpc.netty.shaded.io.grpc.netty.NettyChannelProvider.builderForAddress(NettyChannelProvider.java:24)
	at io.grpc.ManagedChannelBuilder.forAddress(ManagedChannelBuilder.java:39)
	at com.google.api.gax.grpc.InstantiatingGrpcChannelProvider.createSingleChannel(InstantiatingGrpcChannelProvider.java:325)
	at com.google.api.gax.grpc.InstantiatingGrpcChannelProvider.access$1800(InstantiatingGrpcChannelProvider.java:81)
	at com.google.api.gax.grpc.InstantiatingGrpcChannelProvider$1.createSingleChannel(InstantiatingGrpcChannelProvider.java:231)
	at com.google.api.gax.grpc.ChannelPool.create(ChannelPool.java:72)
	at com.google.api.gax.grpc.InstantiatingGrpcChannelProvider.createChannel(InstantiatingGrpcChannelProvider.java:241)
	at com.google.api.gax.grpc.InstantiatingGrpcChannelProvider.getTransportChannel(InstantiatingGrpcChannelProvider.java:219)
	at com.google.api.gax.rpc.ClientContext.create(ClientContext.java:199)
	at com.google.cloud.pubsub.v1.stub.GrpcPublisherStub.create(GrpcPublisherStub.java:195)
	at com.google.cloud.pubsub.v1.Publisher.<init>(Publisher.java:188)
	at com.google.cloud.pubsub.v1.Publisher.<init>(Publisher.java:88)
	at com.google.cloud.pubsub.v1.Publisher$Builder.build(Publisher.java:829)
	at com.eventstream.googlepubsub.emitter.GooglePubsubEmitter.<init>(GooglePubsubEmitter.java:48)
	at com.eventstream.googlepubsub.emitter.EmitterApp.main(EmitterApp.java:15)
```

- https://stackoverflow.com/a/58250507/432903
- https://github.com/googleapis/java-pubsub/issues/27

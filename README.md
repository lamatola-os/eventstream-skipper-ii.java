eventstream-skipper (googlepubsub)
-------------------

eventstream-googlepubsub
-----------------------

```
              |----------------------------------
publisher ~>  | 1 | 2 | 3 | 4 | 5 | ........| n |    ~>  subscription ~> Subscriber 
              |----------------------------------     
                        stream (topic)
```

Publisher
-----
- create stream (`topic`)
- create IAM role with write access and use it in publisher
- publisher - https://github.com/duwamish-os/eventstream-skipper-ii.java/blob/master/src/main/java/com/eventstream/googlepubsub/emitter/GooglePubsubEmitter.java

Subscriber
-----------
- create subscription
- use `subscriptionId` in consumer
- subscriber - https://github.com/duwamish-os/eventstream-skipper-ii.java/blob/master/src/main/java/com/eventstream/googlepubsub/consumer/GooglePubsubConsumer.java

pricing
--------
https://cloud.google.com/pubsub/pricing

scala version
--
https://github.com/duwamish-os/eventstream-skipper-ii

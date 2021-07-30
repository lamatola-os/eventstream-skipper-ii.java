package com.eventstream.googlepubsub.consumer.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Event {
    private String id;
    private String payload;
}

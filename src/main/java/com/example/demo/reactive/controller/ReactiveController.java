package com.example.demo.reactive.controller;

import com.example.demo.reactive.model.Foo;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class ReactiveController {

    /**
     * This method generates events in a duration of a second.
     * @return
     */
    @GetMapping(value="foo/get",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Foo> sentFooResource(){
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> Foo.builder().id(sequence).name("foo - " +sequence).build());

    }

    /**
     * Using ServerSentEvent Object ,with this approach we can handle the events metadata
     * and we can get rid of MediaType.TEXT_EVENT_STREAM_VALUE.
     * @return
     */
    @GetMapping("sse/foo/get")
    public Flux<ServerSentEvent<Foo>> sentFooResourceWithServerSentEvent() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.<Foo> builder()
                        .data(Foo.builder().id(sequence).name("foo - "+sequence).build())
                        .build());
    }
}

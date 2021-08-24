## Create an API endpoint with the new reactive support in Spring 5. 

The aim of this exercize is to build an endpoint using the Spring WebFlux - reactive programming support for Web applications in Spring Framework 5.

As we want our client to receive automatic updates from the server we use the server push technology **Server-Sent Events (SSE)**

The two endpoints implemented simply return/emit one resource every second.

1. ###### With the ServerSentEvent Object

```
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
```

2. ###### Without the ServerSentEvent Object

```
/**
     * This method generates events in a duration of a second.
     * @return
     */
    @GetMapping(value="foo/get",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Foo> sentFooResource(){
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> Foo.builder().id(sequence).name("foo - " +sequence).build());

    }
```

The integration test was done with **WebTestClient**

```
@Test
	public void testEndpointWithoutServerSentEvent(){
		List<Foo> foos = webTestClient.get()
				.uri("/foo/get")
				.accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
				.exchange()
				.expectStatus().isOk()
				.returnResult(Foo.class)
				.getResponseBody()
				.take(2)
				.collectList()
				.block();
		Assertions.assertNotNull(foos);
		Assertions.assertEquals(2,foos.size());
	}

	@Test
	public void testEndpointWithServerSentEvent(){
		List<Foo> foos = webTestClient.get()
				.uri("/sse/foo/get")
				.exchange()
				.expectStatus().isOk()
				.returnResult(Foo.class)
				.getResponseBody()
				.take(2)
				.collectList()
				.block();
		Assertions.assertNotNull(foos);
		Assertions.assertEquals(2,foos.size());
	}
```
The app has been deployed to Heroku and the two endpoints are available through these urls:
    
    . https://murmuring-sierra-82507.herokuapp.com/foo/get
    . https://murmuring-sierra-82507.herokuapp.com/see/foo/get

package com.example.demo.reactive;

import com.example.demo.reactive.model.Foo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

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

}

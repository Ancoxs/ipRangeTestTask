package com.example.demo.controller;

import com.example.demo.service.AWSService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(AWSService.class)
public class IPRangesControllerTest {

    @Autowired
    WebTestClient client;

    @Test
    public void findIPRangesShouldReturnPlainText(){
        client.get().uri("/v1/ipranges?validRegion=ALL")
                .exchange().expectBody(String.class);
    }

    @Test
    public void findIPRangesShouldReturn302ResponseStatusWithValidRegion(){
        client.get().uri("/v1/ipranges?validRegion=ALL")
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    public void findIPRangesforEURegionShouldNotContainUSIP(){
        client.get().uri("/v1/ipranges?validRegion=EU")
                .exchange()
                .expectStatus().isFound()
                .expectBodyList(String.class).doesNotContain("150.222.234.54/31");
    }

    @Test
    public void findIPRangesShouldReturn400ResponseStatusWithInValidRegion() {
        client.get().uri("/v1/ipranges?validRegion=ED")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void findIPRangesShouldReturn400ResponseStatusWithNullRegion() {
        client.get().uri("/v1/ipranges?validRegion=")
                .exchange()
                .expectStatus().isBadRequest();
    }
}

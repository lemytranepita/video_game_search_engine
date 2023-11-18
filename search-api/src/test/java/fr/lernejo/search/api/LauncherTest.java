package fr.lernejo.search.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LauncherTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertNotNull(restTemplate);
    }

    @Test
    void apiHelloEndpointTest() {
        String url = "http://localhost:" + port + "/api/hello";
        String result = restTemplate.getForObject(url, String.class);
        assertNotNull(result);
        assertTrue(result.contains("Hello random wanderer"));
    }
}

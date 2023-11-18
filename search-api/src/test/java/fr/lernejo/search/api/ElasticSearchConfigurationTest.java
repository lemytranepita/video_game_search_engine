package fr.lernejo.search.api;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

public class ElasticSearchConfigurationTest {

    @Test
    public void testClient() {
        RestHighLevelClient mockClient = mock(RestHighLevelClient.class);
        ElasticSearchConfiguration configuration = new ElasticSearchConfiguration("localhost", 9200, "elastic", "admin");
        RestHighLevelClient client = configuration.client();
        assertThat(client, notNullValue());
    }
}

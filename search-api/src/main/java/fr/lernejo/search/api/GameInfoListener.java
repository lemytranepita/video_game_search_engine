package fr.lernejo.search.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;

@Component
public class GameInfoListener {
    private final RestHighLevelClient client;

    public GameInfoListener(RestHighLevelClient client) {
        this.client = client;
    }
    @RabbitListener(queues = AmqpConfiguration.GAME_INFO_QUEUE)
    public void onMessage(Message message)  throws IOException {
        Map<String, Object> messageBody = deserializeMessage(message);
        String gameId = extractGameId(message);
        IndexRequest request = createIndexRequest(gameId, messageBody);
        indexDocument(request);
    }

    private Map<String, Object> deserializeMessage(Message message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(message.getBody(), new TypeReference<>() {});
    }

    private String extractGameId(Message message) {
        return (String) message.getMessageProperties().getHeaders().get("game_id");
    }

    private IndexRequest createIndexRequest(String gameId, Map<String, Object> messageBody) {
        return new IndexRequest("games").id(gameId).source(messageBody);
    }

    private void indexDocument(IndexRequest request) throws IOException {
        RequestOptions requestOptions = RequestOptions.DEFAULT;
        client.index(request, requestOptions);
    }
}

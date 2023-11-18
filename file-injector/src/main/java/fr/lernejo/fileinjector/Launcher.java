package fr.lernejo.fileinjector;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lernejo.search.api.AmqpConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class FileInjector {
    private final RabbitTemplate rabbitTemplate;

    public FileInjector(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void injectFile(String filename) throws Exception {
        byte[] jsonData = Files.readAllBytes(Paths.get(filename));
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> messages = objectMapper.readValue(jsonData, List.class);
        for (Map<String, Object> message : messages) {
            rabbitTemplate.convertAndSend(AmqpConfiguration.GAME_INFO_QUEUE, message);
        }
    }
}

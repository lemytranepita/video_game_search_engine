package fr.lernejo.fileinjector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Launcher {

    public static void main(String[] args) throws IOException {
        try (AnnotationConfigApplicationContext springApplicationContext = new AnnotationConfigApplicationContext(Launcher.class)) {

            RabbitTemplate rabbitTemplate = springApplicationContext.getBean(RabbitTemplate.class);
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class);

            List<Map<String, Object>> messages = objectMapper.readValue(new File(args[0]), listType);

            messages.forEach(message -> {
                MessageProperties messageProperties = new MessageProperties();
                messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
                messageProperties.setHeader("id", message.get("id"));
                rabbitTemplate.send("game_info", rabbitTemplate.getMessageConverter().toMessage(message, messageProperties));
            });
        }
    }
}

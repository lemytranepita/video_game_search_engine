package fr.lernejo.fileinjector;

import fr.lernejo.search.api.Launcher;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LauncherTest {
    @Test
    public void main() throws IOException {
        Path tempPath = Files.createTempFile("tempFile", ".json");
        Files.write(tempPath, "[{\"id\": \"test\"}]".getBytes());

        AnnotationConfigApplicationContext context = mock(AnnotationConfigApplicationContext.class);
        when(context.getBean(RabbitTemplate.class)).thenReturn(mock(RabbitTemplate.class));

        Launcher.main(new String[]{tempPath.toString()});

        Files.delete(tempPath);
    }
}

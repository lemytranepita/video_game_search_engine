package fr.lernejo.search.api;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AmqpConfigurationTest {

    @Test
    public void when_queue_is_called_then_correct_queue_is_returned() {
        AmqpConfiguration config = new AmqpConfiguration();
        Queue queue = config.queue();
        assertEquals(AmqpConfiguration.GAME_INFO_QUEUE, queue.getName());
        assertEquals(true, queue.isDurable());
    }
}

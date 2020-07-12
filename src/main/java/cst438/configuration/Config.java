package cst438.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
	
	@Value("${queueName}")
	private String queueName;

    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }
}
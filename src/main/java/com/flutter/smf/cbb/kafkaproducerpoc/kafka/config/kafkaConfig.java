package com.flutter.smf.cbb.kafkaproducerpoc.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Profile("local")
public class kafkaConfig {

    @Bean
    public NewTopic GameEvents(){
        return TopicBuilder.name("cbb-game-events")
                .partitions(1)
                .replicas(1)
                .build();
    }

}

package com.myfileingesterpart.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/*
* this is a configuration class, and we use this to create our topics for the kafka.
* we use the topic method and this method will return a NewTopic that is in the springboot kafka library.
* for creating a new topic, just need to call TopicBuilder and give a name for the topic to it.
* we have 3 topics because we assign one topic to each component and assumed that we have 3 components.
 */
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic myTopic() {
        return TopicBuilder.name("myTopic")
                .build();
    }

    @Bean
    public NewTopic yourTopic() {
        return TopicBuilder.name("yourTopic")
                .build();
    }

    @Bean
    public NewTopic hisTopic() {
        return TopicBuilder.name("hisTopic")
                .build();
    }

}
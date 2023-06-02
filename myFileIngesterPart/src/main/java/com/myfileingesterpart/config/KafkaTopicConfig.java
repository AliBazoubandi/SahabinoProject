package com.myfileingesterpart.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

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

    // this is for the final test
//    @Bean
//    public NewTopic firstComponentTopic() {
//        return TopicBuilder.name("firstComponentTopic")
//                .build();
//    }
//
//    @Bean
//    public NewTopic secondComponentTopic() {
//        return TopicBuilder.name("secondComponentTopic")
//                .build();
//    }
//
//    @Bean
//    public NewTopic thirdComponentTopic() {
//        return TopicBuilder.name("thirdComponentTopic")
//                .build();
//    }

}
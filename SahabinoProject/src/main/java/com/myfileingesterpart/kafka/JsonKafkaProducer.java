package com.myfileingesterpart.kafka;

import com.myfileingesterpart.api.model.MyLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class JsonKafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaProducer.class);

    private KafkaTemplate<String, MyLog> kafkaTemplate;

    public JsonKafkaProducer(KafkaTemplate<String, MyLog> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(MyLog log, String topic) {

        LOGGER.info(String.format("Message sent -> %s", log.toString()));

        Message<MyLog> message = MessageBuilder
                .withPayload(log)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
        kafkaTemplate.send(message);
    }
}
package com.myfileingesterpart.kafka;

import com.myfileingesterpart.api.model.MyLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/*
* this is out produce data for data class
* in this class we use KafkaTemplate with a String for key and a MyLog (our payload class) for the value.
* we have a sendMessage method in this class
* that will send new data using this KafkaTemplate to the topic that we want.
* this method gets two parameters, a MyLog object and a String for topic.
* in this method, we use Message and MessageBuilder class
* to create a message with the given MyLog object and String topic.
* at the end of the method we will send the new data (message)
* to the topic that we were given to using KafKaTemplate.send() method
 */
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
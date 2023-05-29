package com.myfileingesterpart.kafka;

import com.myfileingesterpart.model.MyLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JsonKafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaConsumer.class);

    @KafkaListener(topics = "myTopic", groupId = "myGroupId")
    public void consume(MyLog log) {
        LOGGER.info(String.format("Json message received -> %s", log.toString()));

    }
}

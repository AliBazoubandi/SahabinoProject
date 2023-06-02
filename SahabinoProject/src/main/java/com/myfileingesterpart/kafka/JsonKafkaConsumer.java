package com.myfileingesterpart.kafka;

import static com.myfileingesterpart.validator.RuleEvaluator.*;
import com.myfileingesterpart.api.model.MyLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JsonKafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaConsumer.class);

    @KafkaListener(topics = "myTopic", groupId = "myGroupId")
    public void consumeMyTopic(MyLog log) {
        firstComponentLogs.add(log);
//        LOGGER.info(String.format("this is firstComponentLogs now -> %s", firstComponentLogs.toString()));
//        LOGGER.info(String.format("size of saved loges1 -> %s", firstComponentLogs.size()));
        LOGGER.info(String.format("Json message received from myTopic -> %s", log.toString()));
        run(firstComponentLogs);
    }
    @KafkaListener(topics = "yourTopic", groupId = "myGroupId")
    public void consumeYourTopic(MyLog log) {
        secondComponentLogs.add(log);
//        LOGGER.info(String.format("this is secondComponentLogs now -> %s", secondComponentLogs.toString()));
//        LOGGER.info(String.format("size of saved loges2 -> %s", secondComponentLogs.size()));
        LOGGER.info(String.format("Json message received from yourTopic-> %s", log.toString()));
        run(secondComponentLogs);
    }
    @KafkaListener(topics = "hisTopic", groupId = "myGroupId")
    public void consumeHisTopic(MyLog log) {
        thirdComponentLogs.add(log);
//        LOGGER.info(String.format("this is thirdComponentLogs now -> %s", thirdComponentLogs.toString()));
//        LOGGER.info(String.format("size of saved loges3 -> %s", thirdComponentLogs.size()));
        LOGGER.info(String.format("Json message received from hisTopic-> %s", log.toString()));
        run(thirdComponentLogs);
    }
}
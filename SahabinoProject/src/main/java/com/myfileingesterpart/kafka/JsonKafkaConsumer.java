package com.myfileingesterpart.kafka;

import static com.myfileingesterpart.validator.RuleEvaluator.*;
import com.myfileingesterpart.api.model.MyLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/*
 * this is our consumed from kafka class
 * in this class we use @KafkaListener annotation on top of a consumed method, and with this combination we can consume data from a specific topic
 * for every @KafkaListener. we need two parameters, one is the name of the topic that we want to read from it, and another one is the groupId that we set in the properties
 * the method itself get a MyLog object from the listener and add this object into a List of logs.
 * we have considered that there are 3 components in this project, so we built one topic for each one. 3 in total.
 * we have one list for gathering each component's logs, so we have 3 static lists in total too.
 * after adding each data in this consumed class and showing the message that has been received, we call run method from RuleEvaluator class.
 * this method gets a list of component's logs and will call 3 rule validation methods in itself and will pass the list of component's logs into them
 */
@Service
public class JsonKafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaConsumer.class);

    @KafkaListener(topics = "myTopic", groupId = "myGroupId")
    public void consumeMyTopic(MyLog log) {
        firstComponentLogs.add(log);
        LOGGER.info(String.format("Json message received from myTopic -> %s", log.toString()));
        run(firstComponentLogs);
    }
    @KafkaListener(topics = "yourTopic", groupId = "myGroupId")
    public void consumeYourTopic(MyLog log) {
        secondComponentLogs.add(log);
        LOGGER.info(String.format("Json message received from yourTopic-> %s", log.toString()));
        run(secondComponentLogs);
    }
    @KafkaListener(topics = "hisTopic", groupId = "myGroupId")
    public void consumeHisTopic(MyLog log) {
        thirdComponentLogs.add(log);
        LOGGER.info(String.format("Json message received from hisTopic-> %s", log.toString()));
        run(thirdComponentLogs);
    }
}
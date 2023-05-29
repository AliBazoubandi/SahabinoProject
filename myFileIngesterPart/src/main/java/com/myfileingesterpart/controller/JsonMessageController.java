package com.myfileingesterpart.controller;

import com.myfileingesterpart.kafka.JsonKafkaProducer;
import com.myfileingesterpart.model.MyLog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class JsonMessageController {

    private JsonKafkaProducer jsonKafkaProducer;

    public JsonMessageController(JsonKafkaProducer jsonKafkaProducer) {
        this.jsonKafkaProducer = jsonKafkaProducer;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody MyLog log) {
        jsonKafkaProducer.sendMessage(log);
        return ResponseEntity.ok("Json message sent to topic");
    }
}

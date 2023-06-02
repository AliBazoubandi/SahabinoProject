package com.myfileingesterpart.kafka;

import com.myfileingesterpart.api.model.MyLog;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

class JsonKafkaProducerTest {

    @Test
    public void testSendMessage() {
        // Create a mock KafkaTemplate
        KafkaTemplate<String, MyLog> kafkaTemplate = mock(KafkaTemplate.class);

        // Create an instance of MyClass
        JsonKafkaProducer jsonKafkaProducer = new JsonKafkaProducer(kafkaTemplate);

        // Create a test MyLog object and topic
        MyLog testLog = new MyLog();
        String testTopic = "test-topic";

        // Invoke the sendMessage method
        jsonKafkaProducer.sendMessage(testLog, testTopic);

        // Verify that the KafkaTemplate's send method was called once
        verify(kafkaTemplate, times(1)).send(any(Message.class));

        // Capture the argument passed to the send method
        ArgumentCaptor<Message<MyLog>> captor = ArgumentCaptor.forClass(Message.class);
        verify(kafkaTemplate).send(captor.capture());
        // Get the captured Message object
        Message<MyLog> capturedMessage = captor.getValue();

        // Verify the payload and headers of the captured Message object
        assertSame(testLog, capturedMessage.getPayload());
        MessageHeaders headers = capturedMessage.getHeaders();
        assertEquals(testTopic, headers.get(KafkaHeaders.TOPIC));
    }
}
package com.booking.room.service;

import com.booking.room.model.Room;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class RoomMessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomMessageProducer.class);

    private NewTopic topic;

    private KafkaTemplate<String, Room> kafkaTemplate;

    public RoomMessageProducer(NewTopic topic, KafkaTemplate<String, Room> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Room event){
        LOGGER.info(String.format("Room event => %s", event.toString()));

        // create Message
        Message<Room> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();
        kafkaTemplate.send(message);
    }
}

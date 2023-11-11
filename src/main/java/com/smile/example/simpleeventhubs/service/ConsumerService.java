package com.smile.example.simpleeventhubs.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerService {
    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.group-id}")
    public void consumeMessage(String msg) {
    }
}

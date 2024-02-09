package com.smile.example.simpleeventhubs.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class ProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishMessage(String topic, String data) {
        CompletableFuture<SendResult<String, String>> future = this.kafkaTemplate.send(topic, data);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                String resultTopic = result.getRecordMetadata().topic();
                Integer partition = result.getRecordMetadata().partition();
                log.info("success for topic {} at partition {}", resultTopic, partition);
            } else {
                log.error("fail to push message --- {}", ex.getMessage());
            }
        });
    }

    public void publishMessageWithCorrelation(String topic, String data) {
        List<Header> kafkaHeaders = new ArrayList<>();
        kafkaHeaders.add(new RecordHeader("X-Correlation-Id", UUID.randomUUID().toString().getBytes()));
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, null, null, null, data, kafkaHeaders);
        CompletableFuture<SendResult<String, String>> future = this.kafkaTemplate.send(producerRecord);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                String resultTopic = result.getRecordMetadata().topic();
                Integer partition = result.getRecordMetadata().partition();
                log.info("success for topic {} at partition {}", resultTopic, partition);
            } else {
                log.error("fail to push message --- {}", ex.getMessage());
            }
        });
    }
}

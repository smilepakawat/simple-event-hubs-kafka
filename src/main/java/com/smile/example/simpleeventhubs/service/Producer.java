package com.smile.example.simpleeventhubs.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.Executor;

@Slf4j
@Service
public class Producer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Executor executor;

    public Producer(KafkaTemplate<String, String> kafkaTemplate, @Qualifier("applicationTaskExecutor") Executor executor) {
        this.kafkaTemplate = kafkaTemplate;
        this.executor = executor;
    }

    public void publishMessage(String topic, String data) {
        this.executor.execute(() -> {
            ListenableFuture<SendResult<String, String>> future = this.kafkaTemplate.send(topic, data);
            future.addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onSuccess(SendResult<String, String> result) {
                    String topic = result.getRecordMetadata().topic();
                    Integer partition = result.getRecordMetadata().partition();
                    log.info("success for topic {} at partition {}", topic, partition);
                }

                @Override
                public void onFailure(Throwable e) {
                    log.info("fail to push message --- {}", e.getMessage());
                }
            });
        });
    }
}

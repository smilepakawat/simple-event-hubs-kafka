package com.smile.example.simpleeventhubs.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.MDC;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class MyConsumerInterceptor implements ConsumerInterceptor<String, String> {
    @Override
    public void configure(Map<String, ?> configs) {
    }

    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {
        ConsumerRecord<String, String> record = records.iterator().next();
        try {
            String correlationId = StreamUtils.copyToString(new ByteArrayInputStream(record.headers().lastHeader("X-Correlation-Id").value()), StandardCharsets.UTF_8);
            MDC.put("correlationId", correlationId);
            log.info("this is record: {}", record);
            log.info("this is value: {}", record.value());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
    }

    @Override
    public void close() {
    }

}

package com.smile.example.simpleeventhubs.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyProducerInterceptor implements ProducerInterceptor<String, String> {
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord record) {
        List<Header> kafkaHeaders = new ArrayList<>();
        String correlationId = MDC.get("X-Correlation-Id");
        kafkaHeaders.add(new RecordHeader("X-Correlation-Id", correlationId.getBytes()));
        return new ProducerRecord<>(record.topic(), null, null, null, record.value().toString(), kafkaHeaders);
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}

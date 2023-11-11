package com.smile.example.simpleeventhubs.configuration;

import com.smile.example.simpleeventhubs.interceptor.MyConsumerInterceptor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@EnableAsync
@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.properties.client-id}")
    private String clientId;
    @Value("${spring.kafka.properties.security.protocol}")
    private String protocol;
    @Value("${spring.kafka.properties.sasl.login.callback.handler.class}")
    private String callbackHandler;
    @Value("${spring.kafka.properties.sasl.mechanism}")
    private String mechanism;
    @Value("${spring.kafka.properties.sasl.jaas.config}")
    private String jaas;
    @Value("${spring.kafka.properties.sasl.login.callback.handler.class}")
    private String callback;

    @Bean
    public ProducerFactory<String, String> kafkaProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, this.clientId);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, this.protocol);
        configProps.put(SaslConfigs.SASL_MECHANISM, this.mechanism);
        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, this.jaas);
        configProps.put(SaslConfigs.SASL_LOGIN_CALLBACK_HANDLER_CLASS, this.callback);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ConsumerFactory<?, ?> kafkaConsumerFactory() {
        Map<String, Object> consumerProperties = new HashMap<>();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, this.clientId);
        consumerProperties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, this.protocol);
        consumerProperties.put(SaslConfigs.SASL_MECHANISM, this.mechanism);
        consumerProperties.put(SaslConfigs.SASL_JAAS_CONFIG, this.jaas);
        consumerProperties.put(SaslConfigs.SASL_LOGIN_CALLBACK_HANDLER_CLASS, this.callback);
        consumerProperties.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, MyConsumerInterceptor.class.getName());
        return new DefaultKafkaConsumerFactory<>(consumerProperties);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }
}

package com.github.vkvish19.avro;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.github.vkvish19.Customer;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;

public class KafkaAvroConsumerV2
{
    private static final String BOOTSTRAP_SERVERS = "127.0.0.1:9092";
    private static final String SCHEMA_REGISTRY_URL = "http://127.0.0.1:8081";
    
    public static void main(String[] args)
    {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", BOOTSTRAP_SERVERS);
        properties.setProperty("group.id", "my-avro-consumer-v2");
        properties.setProperty("enable.auto.commit", "false");
        properties.setProperty("auto.offset.reset", "earliest");
        
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("schema.registry.url", SCHEMA_REGISTRY_URL);
        properties.setProperty("specific.avro.reader", "true");
        
        KafkaConsumer<String,Customer> kafkaConsumer = new KafkaConsumer<>(properties);
        String topic = "customer-avro";
        
        kafkaConsumer.subscribe(Collections.singleton(topic));
        
        System.out.println("Waiting for data...");
        
        while(true)
        {
            ConsumerRecords<String, Customer> records = kafkaConsumer.poll(Duration.ofMillis(500));
            for(ConsumerRecord<String, Customer> record: records)
            {
                Customer customer = record.value();
                System.out.println("customer = " + customer);
            }
            kafkaConsumer.commitSync();
        }
        
        // uncomment below if not in endless while-loop
        //        kafkaConsumer.close();
    }
}

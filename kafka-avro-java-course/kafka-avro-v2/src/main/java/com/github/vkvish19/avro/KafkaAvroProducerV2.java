package com.github.vkvish19.avro;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.github.vkvish19.Customer;

import io.confluent.kafka.serializers.KafkaAvroSerializer;

public class KafkaAvroProducerV2
{
    private static final String BOOTSTRAP_SERVERS = "127.0.0.1:9092";
    private static final String SCHEMA_REGISTRY_URL = "http://127.0.0.1:8081";

    public static void main(String[] args)
    {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", BOOTSTRAP_SERVERS);
        properties.setProperty("acks", "1");
        properties.setProperty("retries", "10");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url", SCHEMA_REGISTRY_URL);
    
        KafkaProducer<String,Customer> kafkaProducer = new KafkaProducer<>(properties);
        String topic = "customer-avro";
    
        Customer customer = Customer.newBuilder()
                .setFirstName("Michael")
                .setLastName("Bourne")
                .setHeight(171f)
                .setWeight(78.5f)
                .setAge(37)
                .setEmail("abc@xyz.com")
                .setPhoneNumber("123-4567-890")
                .build();
    
        ProducerRecord<String, Customer> producerRecord = new ProducerRecord<>(
                topic, customer
        );
    
        kafkaProducer.send(producerRecord, (recordMetadata, e) -> {
            if(e == null)
            {
                System.out.println("Success!");
                System.out.println(recordMetadata.toString());
            }
            else
            {
                e.printStackTrace();
            }
        });
    
        kafkaProducer.flush();
        kafkaProducer.close();    }
}

package se.liu.ida.tdp024.account.utils.logger;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaLoggerProducer implements AccountLogger {

  private final KafkaProducer<String, String> kafkaProducer;

  private final String bootstrapServers = System.getProperty("KAFKA_BOOTSTRAP_SERVERS", "kafka:9094");
  public KafkaLoggerProducer() {
    kafkaProducer = getProducer();
  }

  @Override
  public void logMessages(String TOPIC, String message) {
    ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, message);

    kafkaProducer.send(record);
    System.out.println("Messaged log to Kafka: " + message);
  }

  public KafkaProducer<String, String> getProducer() {
    System.out.println("Setting up kafka properties");
    Properties props = new Properties();
    props.put("bootstrap.servers", bootstrapServers);
    props.put("client.id", "KafkaProducer");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    return new KafkaProducer<String, String>(props);
  }
}

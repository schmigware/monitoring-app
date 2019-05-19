package org.cit.mcaleerj.thesis.monitorservice.task.impl;

import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.cit.mcaleerj.thesis.management.dto.ConfigurationPropertyDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.monitorservice.dao.repository.MessageRepository;
import org.cit.mcaleerj.thesis.monitorservice.domain.Message;
import org.cit.mcaleerj.thesis.monitorservice.job.MonitoringTask;
import org.cit.mcaleerj.thesis.monitorservice.job.exception.MonitoringTaskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/*
 * Monitors a Kafka environment, persisting messages.
 * @param <K> Kafka message key type
 * @param <V> Kafka message value type
 */
@SuppressWarnings("unused")
public class KafkaMonitoringTask implements MonitoringTask {

  /*
   * Consumer group identifier.
   */
  private static final String CONSUMER_GROUP_ID = "Monitoring-Service";

  /*
   * Bootstrap servers configuration property.
   */
  private static final String CONFIG_PROP_BOOTSTRAP_SERVERS = "bootstrap-servers";

  /*
   * Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(KafkaMonitoringTask.class);

  /*
   * Kafka header containing source application name.
   */
  private static final String HEADER_APPLICATION_NAME = "HEADER_APP_NAME";

  /*
   * Ignored topic names.
   */
  private static final String TOPIC_CONSUMER_OFFSETS = "__consumer_offsets";

  @Getter
  private final UUID uuid;

  @Getter
  private EnvironmentDto environment;

  /*
   * Message repository.
   */
  private MessageRepository messageRepository;

  /**
   * Flags running state.
   */
  private boolean running;

  /**
   * Task thread.
   */
  private Thread consumerThread;

  /**
   * Kafka consumer.
   */
  private KafkaConsumer<String, byte[]> kafkaConsumer;

  /**
   * Constructor.
   */
  public KafkaMonitoringTask() {
    this.uuid = UUID.randomUUID();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(EnvironmentDto environment, MessageRepository messageRepository) throws MonitoringTaskException {

    final Properties consumerProperties = this.buildConsumerProperties(environment);
    this.environment = environment;
    this.messageRepository = messageRepository;
    this.kafkaConsumer = buildKafkaConsumer(consumerProperties);
    this.consumerThread = this.createConsumerThread(environment);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start() {
    this.running = true;
    this.consumerThread.start();
    log.info(MessageFormat.format("Started Kafka monitoring task {0}", this.uuid));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop() {
    this.running = false;
  }

  /**
   * Builds and returns a Kafka consumer for the given consumer properties.
   *
   * @param consumerProperties Kafka consumer properties.
   * @return KafkaConsumer instance
   */
  private KafkaConsumer<String, byte[]> buildKafkaConsumer(@NonNull final Properties consumerProperties) {
    final KafkaConsumer<String, byte[]> kafkaConsumer = new KafkaConsumer<>(consumerProperties);

    final List<String> topicNames = new ArrayList<>(kafkaConsumer.listTopics().keySet());
    topicNames.remove(TOPIC_CONSUMER_OFFSETS);

    kafkaConsumer.subscribe(topicNames);
    return kafkaConsumer;
  }

  /**
   * Builds and returns a Kafka consumer thread for the given environment.
   *
   * @param environment {@link EnvironmentDto} instance
   * @return monitoring {@link Thread}
   */
  private Thread createConsumerThread(@NonNull final EnvironmentDto environment) {

    return new Thread(() -> {
      log.info(MessageFormat.format("Staring kafka consumer for environment {0}", environment.getUuid()));
      while (this.running) {

        ConsumerRecords<String, byte[]> records = this.kafkaConsumer.poll(100);
        for (ConsumerRecord<String, byte[]> record : records) {

          final Message message = new Message();

          final String topicName = record.topic();
          message.setEdgeLabel(topicName);

          //determine the source node name
          record.headers().forEach(header -> {
            if (HEADER_APPLICATION_NAME.equals(header.key())) {
              String sourceNodeName = new String(header.value());
              //Spring Kafka headers are double quoted as a result of JSON encoding
              if(sourceNodeName.startsWith("\"") && sourceNodeName.endsWith("\"")) {
                sourceNodeName = sourceNodeName.substring(1, sourceNodeName.length()-1);
              }
              message.setSourceNode(sourceNodeName);
            }
          });

          message.setMessageContent(new String(record.value()));

          //truncate milliseconds
          message.setTimestamp(new Timestamp(record.timestamp()));

          message.setEnvironmentUuid(environment.getUuid());
          this.messageRepository.save(message);

        }
      }
    });
  }


  private Properties buildConsumerProperties(final EnvironmentDto environment) throws MonitoringTaskException {

    String bootstrapServers = null;
    for (ConfigurationPropertyDto prop : environment.getConfigurationProperties()) {
      if (CONFIG_PROP_BOOTSTRAP_SERVERS.equals(prop.getName())) {
        bootstrapServers = prop.getValue();
      }
    }

    if (StringUtils.isBlank(bootstrapServers)) {
      throw new MonitoringTaskException("Configuration property " + CONFIG_PROP_BOOTSTRAP_SERVERS + " not set.");
    }


    final Properties properties = new Properties();

    properties.put("group.id", CONSUMER_GROUP_ID);
    properties.put("bootstrap.servers", bootstrapServers);
    properties.put("zookeeper.session.timeout.ms", "6000");
    properties.put("zookeeper.sync.time.ms", "2000");
    properties.put("auto.commit.enable", "false");
    properties.put("auto.commit.interval.ms", "1000");
    properties.put("consumer.timeout.ms", "-1");
    properties.put("max.poll.records", "1");
    properties.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
    properties.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");

    return properties;
  }


}

package org.cit.mcaleerj.thesis.monitorservice.purge;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.monitorservice.dao.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * Periodically purges old messages.
 */
@Component
public class MessageRepositoryPurgeTask {

  private static final Logger log = LoggerFactory.getLogger(MessageRepositoryPurgeTask.class);

  /*
   * Message retention period.
   */
  @Value("${monitoring.service.retention.period}")
  private int retentionPeriod;

  /*
   * Message repository.
   */
  private final MessageRepository messageRepository;

  /**
   * Constructor.
   *
   * @param messageRepository {@link MessageRepository} instance
   */
  public MessageRepositoryPurgeTask(@NonNull final MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  @Scheduled(fixedRateString = "${monitoring.service.purge.period}")
  public void synchronize() {
    if (log.isDebugEnabled()) {
      log.debug("Purging monitored messages...");
    }

    final Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MINUTE, 0 - this.retentionPeriod);
    final Date threshold = cal.getTime();

    this.messageRepository.removeOlderThan(new java.sql.Date(threshold.getTime()));

    if (log.isDebugEnabled()) {
      log.debug("Purge complete.");
    }
  }

}

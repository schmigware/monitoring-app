package org.cit.mcaleerj.thesis.aggregationservice.purge;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.aggregationservice.dao.repository.EnvironmentAggregationRepository;
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
public class AggregationRepositoryPurgeTask {

  private static final Logger log = LoggerFactory.getLogger(AggregationRepositoryPurgeTask.class);

  /*
   * Aggregation retention period.
   */
  @Value("${aggregation.service.retention.period}")
  private int retentionPeriod;

  /*
   * Aggregation repository.
   */
  private final EnvironmentAggregationRepository aggregationRepository;

  /**
   * Constructor.
   *
   * @param aggregationRepository {@link EnvironmentAggregationRepository} instance
   */
  public AggregationRepositoryPurgeTask(@NonNull final EnvironmentAggregationRepository aggregationRepository) {

    this.aggregationRepository = aggregationRepository;
  }

  @Scheduled(fixedRateString = "${aggregation.service.purge.period}")
  public void synchronize() {
    if (log.isDebugEnabled()) {
      log.debug("Purging aggregations...");
    }

    final Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MINUTE, 0 - this.retentionPeriod);
    final Date threshold = cal.getTime();

    this.aggregationRepository.removeOlderThan(new java.sql.Date(threshold.getTime()));

    if (log.isDebugEnabled()) {
      log.debug("Purge complete.");
    }
  }

}

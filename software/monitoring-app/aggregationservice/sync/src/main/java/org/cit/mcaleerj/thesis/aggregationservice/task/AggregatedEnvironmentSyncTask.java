package org.cit.mcaleerj.thesis.aggregationservice.task;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.aggregationservice.service.AggregationService;
import org.cit.mcaleerj.thesis.aggregationservice.service.exception.AggregationServiceException;
import org.cit.mcaleerj.thesis.management.client.ManagementServiceClient;
import org.cit.mcaleerj.thesis.management.client.exception.ManagementServiceClientException;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Periodically synchronizes set of monitored environments with the aggregation service.
 */
@Component
public class AggregatedEnvironmentSyncTask {

  private static final Logger log = LoggerFactory.getLogger(AggregatedEnvironmentSyncTask.class);

  private final ManagementServiceClient client;
  private final AggregationService aggregationService;

  /**
   * Constructor.
   * @param aggregationService {@link AggregationService} instance
   * @param mgmtServiceClient {@link ManagementServiceClient} instance.
   */
  public AggregatedEnvironmentSyncTask(@NonNull final AggregationService aggregationService,
                                      @NonNull final ManagementServiceClient mgmtServiceClient) {
    this.aggregationService = aggregationService;
    this.client = mgmtServiceClient;
  }

  @Scheduled(fixedRateString = "${aggregation.service.sync.period}")
  public void synchronize() {
    if(log.isDebugEnabled()) {
      log.debug("Synchronizing monitored evironments...");
    }
    try {

      final List<EnvironmentDto> aggregatedEnvs = this.client.getMonitoredEnvironments();
      this.aggregationService.synchronizeAggregatedEnvironments(aggregatedEnvs);

    } catch(ManagementServiceClientException | AggregationServiceException ex) {
      log.error("Aggregated environment synchronisation failed", ex);
    }
    if(log.isDebugEnabled()) {
      log.debug("Monitored evironment synchronization complete.");
    }
  }

}

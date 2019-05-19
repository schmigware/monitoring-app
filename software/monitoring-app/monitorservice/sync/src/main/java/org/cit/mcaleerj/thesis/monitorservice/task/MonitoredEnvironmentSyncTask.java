package org.cit.mcaleerj.thesis.monitorservice.task;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.management.client.ManagementServiceClient;
import org.cit.mcaleerj.thesis.management.client.exception.ManagementServiceClientException;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.monitorservice.service.MonitoringService;
import org.cit.mcaleerj.thesis.monitorservice.service.exception.MonitoringServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Periodically synchronizes set of monitored environments with the management service.
 */
@Component
public class MonitoredEnvironmentSyncTask {

  private static final Logger log = LoggerFactory.getLogger(MonitoredEnvironmentSyncTask.class);

  private final ManagementServiceClient client;
  private final MonitoringService monitoringService;

  /**
   * Constructor.
   * @param monitoringService {@link MonitoringService} instance
   * @param mgmtServiceClient {@link ManagementServiceClient} instance.
   */
  public MonitoredEnvironmentSyncTask(@NonNull  final MonitoringService monitoringService,
                                      @NonNull final ManagementServiceClient mgmtServiceClient) {
    this.monitoringService = monitoringService;
    this.client = mgmtServiceClient;
  }

  @Scheduled(fixedRateString = "${monitoring.service.sync.period}")
  public void synchronize() {
    if(log.isDebugEnabled()) {
      log.debug("Synchronizing monitored evironments...");
    }
    try {

      final List<EnvironmentDto> monitoredEnvs = this.client.getMonitoredEnvironments();
      this.monitoringService.synchronizeMonitoredEnvironments(monitoredEnvs);

    } catch(ManagementServiceClientException | MonitoringServiceException ex) {
      log.error("Monitored environment synchronisation failed", ex);
    }
    if(log.isDebugEnabled()) {
      log.debug("Monitored evironment synchronization complete.");
    }
  }

}

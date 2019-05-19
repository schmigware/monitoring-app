package org.cit.mcaleerj.thesis.discoveryservice.task;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.discoveryservice.service.DiscoveryService;
import org.cit.mcaleerj.thesis.discoveryservice.service.exception.DiscoveryServiceException;
import org.cit.mcaleerj.thesis.management.client.ManagementServiceClient;
import org.cit.mcaleerj.thesis.management.client.exception.ManagementServiceClientException;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Periodically discovers environment topologies.
 */
@Component
public class DiscoveryTask {

  /*
   * Logging.
   */
  private static final Logger log = LoggerFactory.getLogger(DiscoveryTask.class);

  /*
   * Discovery service..
   */
  private final DiscoveryService discoveryService;

  /*
   * Management service client.
   */
  private final ManagementServiceClient mgmtServiceClient;

  /**
   * Constructor.
   *
   * @param mgmtServiceClient The management service client
   * @param discoveryService  The Discovery service
   */
  public DiscoveryTask(@NonNull final ManagementServiceClient mgmtServiceClient,
                       @NonNull final DiscoveryService discoveryService) {
    this.mgmtServiceClient = mgmtServiceClient;
    this.discoveryService = discoveryService;
  }

  /**
   * Scheduled discovery operation.
   */
  @Scheduled(fixedRateString = "${discovery.service.discovery.period}")
  public void discover() {
    try {
      final List<EnvironmentDto> monitoredEnvs = this.mgmtServiceClient.getMonitoredEnvironments();

      if (monitoredEnvs.isEmpty()) {
        log.info("No monitored environments.");
        return;
      }
      try {

        this.discoveryService.discover(monitoredEnvs);
      } catch (DiscoveryServiceException ex) {
        log.error("Discovery failed", ex);
      }

    } catch (ManagementServiceClientException ex) {
      log.error("Monitored environment lookup failed", ex);
    }

  }

}

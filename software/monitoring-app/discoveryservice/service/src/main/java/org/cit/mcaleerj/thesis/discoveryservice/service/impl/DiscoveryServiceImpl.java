package org.cit.mcaleerj.thesis.discoveryservice.service.impl;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.discovery.agent.DiscoveryAgentFactory;
import org.cit.mcaleerj.thesis.discovery.agent.api.DiscoveryAgent;
import org.cit.mcaleerj.thesis.discovery.agent.exception.DiscoveryException;
import org.cit.mcaleerj.thesis.discoveryservice.service.DiscoveryListener;
import org.cit.mcaleerj.thesis.discoveryservice.service.DiscoveryService;
import org.cit.mcaleerj.thesis.discoveryservice.service.exception.DiscoveryServiceException;
import org.cit.mcaleerj.thesis.management.client.ManagementServiceClient;
import org.cit.mcaleerj.thesis.management.client.exception.ManagementServiceClientException;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.management.dto.TopologyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

/**
 * Discovery service implementation.
 */
@Service
public class DiscoveryServiceImpl implements DiscoveryService {

  /*
   * Logging.
   */
  private static final Logger log = LoggerFactory.getLogger(DiscoveryServiceImpl.class);

  /*
   * Discovery agent factory.
   */
  private final DiscoveryAgentFactory agentFactory;

  /*
   * Management service client..
   */
  private final ManagementServiceClient mgmtServiceClient;

  /*
   * Discovery notifications.
   */
  private DiscoveryNotificationHandler notificationHandler = new DiscoveryNotificationHandler();

  /**
   * Constructor.
   *
   * @param agentFactory {@link DiscoveryAgentFactory instance}
   */
  public DiscoveryServiceImpl(@NonNull final DiscoveryAgentFactory agentFactory,
                              @NonNull final ManagementServiceClient mgmtServiceClient) {
    this.agentFactory = agentFactory;
    this.mgmtServiceClient = mgmtServiceClient;
  }

  @Override
  public void discover(@NonNull final List<EnvironmentDto> environments) throws DiscoveryServiceException {
    for (EnvironmentDto env : environments) {
      TopologyDto environmentTopology = null;
      for (DiscoveryAgent agent : this.agentFactory.getDiscoveryAgents()) {
        try {
          if (!agent.canDiscover(env)) {
            continue;
          }
          final TopologyDto topology = agent.discover(env);
          topology.setEnvironmentUuid(env.getUuid());
          topology.setEnvironmentName(env.getName());

          environmentTopology = this.mgmtServiceClient.updateTopology(topology);
          log.info(String.format(MessageFormat.format("Update topology with UUID {0}", environmentTopology.getUuid())));
          this.notificationHandler.topologyDiscovered(env, environmentTopology);
        } catch (DiscoveryException | ManagementServiceClientException ex) {
          throw new DiscoveryServiceException("Discovery failed", ex);
        }
      }
      if (environmentTopology != null) {
        this.notificationHandler.topologyDiscovered(env, environmentTopology);
      }
    }
  }

  @Override
  public void addDiscoveryListener(@NonNull final DiscoveryListener listener, @NonNull final EnvironmentDto environment) {
    this.notificationHandler.addDiscoveryListener(listener, environment);
  }

  @Override
  public void removeDiscoveryListener(@NonNull final DiscoveryListener listener, @NonNull final EnvironmentDto environment) {
    this.notificationHandler.removeDiscoveryListener(listener, environment);
  }
}

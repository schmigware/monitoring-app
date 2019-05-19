package org.cit.mcaleerj.thesis.discovery.kafka.provider;

import org.cit.mcaleerj.thesis.correlation.client.CorrelationServiceClient;
import org.cit.mcaleerj.thesis.discovery.agent.api.DiscoveryAgent;
import org.cit.mcaleerj.thesis.discovery.agent.spi.DiscoveryAgentProvider;
import org.cit.mcaleerj.thesis.discovery.kafka.agent.MessageCorrelationDiscoveryAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageCorrelationDiscoveryAgentProvider implements DiscoveryAgentProvider {

  @Autowired
  private CorrelationServiceClient correlationServiceClient;

  @Override
  public DiscoveryAgent getDiscoveryAgent() {
    return new MessageCorrelationDiscoveryAgent(this.correlationServiceClient);
  }

}

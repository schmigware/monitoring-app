package org.cit.mcaleerj.thesis.discovery.k8s.provider;

import org.cit.mcaleerj.thesis.discovery.agent.api.DiscoveryAgent;
import org.cit.mcaleerj.thesis.discovery.agent.spi.DiscoveryAgentProvider;
import org.cit.mcaleerj.thesis.discovery.k8s.agent.KubernetesDiscoveryAgent;
import org.cit.mcaleerj.thesis.management.client.ManagementServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KubernetesAgentProviderImpl implements DiscoveryAgentProvider {


  @Autowired
  private ManagementServiceClient managementServiceClient;

  @Override
  public DiscoveryAgent getDiscoveryAgent() {
    return new KubernetesDiscoveryAgent(this.managementServiceClient);
  }


}

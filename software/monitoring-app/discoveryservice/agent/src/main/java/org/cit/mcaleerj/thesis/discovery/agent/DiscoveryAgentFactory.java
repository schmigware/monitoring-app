package org.cit.mcaleerj.thesis.discovery.agent;

import org.cit.mcaleerj.thesis.discovery.agent.api.DiscoveryAgent;
import org.cit.mcaleerj.thesis.discovery.agent.spi.DiscoveryAgentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Component
public class DiscoveryAgentFactory {

  @Autowired
  private AutowireCapableBeanFactory beanFactory;

  private final List<DiscoveryAgent> agents = new ArrayList<>();

  @PostConstruct
  public void initAgents() {
    final List<DiscoveryAgent> agents = new ArrayList<>();
    SpringFactoriesLoader.loadFactories(DiscoveryAgentProvider.class, null).forEach(
            provider -> {
              this.beanFactory.autowireBean(provider);
              this.agents.add(provider.getDiscoveryAgent());
            });
  }

  /**
   * Returns all available discovery agents.
   *
   * @return <code>List</code> of {@link DiscoveryAgent} instances
   */
  public List<DiscoveryAgent> getDiscoveryAgents() {
    return this.agents;
  }

}

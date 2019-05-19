package org.cit.mcaleerj.thesis.discoveryservice.service;

import org.cit.mcaleerj.thesis.management.dto.TopologyDto;

public interface DiscoveryListener {

  /**
   * Notification of topology discovery.
   * @param topology {@link TopologyDto} instance
   */
  void topologyDiscovered(TopologyDto topology);

}

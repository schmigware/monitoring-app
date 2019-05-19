package org.cit.mcaleerj.thesis.discovery.agent.api;

import org.cit.mcaleerj.thesis.discovery.agent.exception.DiscoveryException;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.management.dto.TopologyDto;

/**
 * Discovery agent API.
 */
public interface DiscoveryAgent {



  /**
   * Returns true if this agent can perform topology discovery for the given environment.
   *
   * @param environment {@link EnvironmentDto} which is candidate for discovery
   *
   * @return true if this agent can perform topology discovery, else false
   *
   * @throws DiscoveryException
   */
  boolean canDiscover(EnvironmentDto environment) throws DiscoveryException;

  /**
   * Perform discovery on the given environment.
   *
   * @param environment {@link EnvironmentDto} to discover
   *
   * @return Discovered {@link TopologyDto} instance
   *
   * @throws DiscoveryException
   */
  TopologyDto discover(EnvironmentDto environment) throws DiscoveryException;

  /**
   * Returns the agent identifier.
   *
   * @return identifier string
   */
  String getIdentifier();

}

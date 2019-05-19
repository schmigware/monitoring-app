package org.cit.mcaleerj.thesis.discoveryservice.service;

import org.cit.mcaleerj.thesis.discoveryservice.service.exception.DiscoveryServiceException;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;

import java.util.List;

/**
 * Discovery service interface.
 */
public interface DiscoveryService {

  /**
   * Discovery environment topologies.
   *
   * @param environments <code>List</code> of {@link EnvironmentDto} instances
   *
   * @throws DiscoveryServiceException if an error occurs
   */
  void discover(List<EnvironmentDto> environments) throws DiscoveryServiceException;

  void addDiscoveryListener(DiscoveryListener listener, EnvironmentDto environment);

  void removeDiscoveryListener(DiscoveryListener listener, EnvironmentDto environment);


}

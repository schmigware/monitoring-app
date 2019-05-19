package org.cit.mcaleerj.thesis.management.client;

import org.cit.mcaleerj.thesis.management.client.exception.ManagementServiceClientException;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentProfileDto;
import org.cit.mcaleerj.thesis.management.dto.TopologyDto;

import java.util.List;
import java.util.UUID;

/**
 * Management service client interface.
 */
public interface ManagementServiceClient {

  /**
   * Return all environment profileId definitions.
   *
   * @return <code>List</code> of {@link EnvironmentProfileDto} instances
   *
   * @throws ManagementServiceClientException if an error occurs
   */
  List<EnvironmentProfileDto> getEnvironmentProfiles() throws ManagementServiceClientException;

  /**
   * Return the environment profileId definitions with the given profileId ID.
   *
   * @return {@link EnvironmentProfileDto} instance or null if does not exist
   *
   * @throws ManagementServiceClientException  if an error occurs
   */
  EnvironmentProfileDto getEnvironmentProfile(String profileId) throws ManagementServiceClientException;

  /**
   * Returns all monitored environment definitions.
   *
   * @return <code>List</code> of {@link EnvironmentDto} instances
   *
   * @throws ManagementServiceClientException if an error occurs
   */
  List<EnvironmentDto> getMonitoredEnvironments() throws ManagementServiceClientException;

  /**
   * Returns the environment definition with the given UUID.
   *
   * @param uuid Environment UUID
   *
   * @return EnvironmentDto instance, or null if no such environment exists
   *
   * @throws ManagementServiceClientException if an error occurs
   */
  EnvironmentDto getEnvironmentByUuid(UUID uuid) throws ManagementServiceClientException;

  /**
   * Updates the given {@link TopologyDto}.
   *
   * @param topology  topology to update
   *
   * @return The update topology
   *
   * @throws ManagementServiceClientException if an error occurs
   */
  TopologyDto updateTopology(TopologyDto topology) throws ManagementServiceClientException;

}

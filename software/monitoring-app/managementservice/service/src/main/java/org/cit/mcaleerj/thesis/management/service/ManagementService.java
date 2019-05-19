package org.cit.mcaleerj.thesis.management.service;

import io.leangen.graphql.annotations.GraphQLArgument;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentFilterDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentProfileDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentProfileFilterDto;
import org.cit.mcaleerj.thesis.management.dto.TopologyDto;
import org.cit.mcaleerj.thesis.management.service.exception.ManagementServiceException;

import java.util.List;
import java.util.UUID;

/**
 * Management service interface.
 * Environment profileId / environment / topology creation, lookup and management.
 */
public interface ManagementService {

  /**
   * Registers an environment definition.
   *
   * @param profileDto {@link EnvironmentProfileDto} instance
   * @return The persisted {@link EnvironmentProfileDto} instance
   * @throws ManagementServiceException if an exception occurs
   */
  EnvironmentProfileDto registerEnvironmentProfile(EnvironmentProfileDto profileDto) throws ManagementServiceException;

  /**
   * Returns all registered environment profiles.
   *
   * @param filter {@link EnvironmentProfileFilterDto} instance
   *
   * @return List of {@link EnvironmentProfileDto} instances
   * @throws ManagementServiceException if an exception occurs
   */
  List<EnvironmentProfileDto> getEnvironmentProfiles(EnvironmentProfileFilterDto filter) throws ManagementServiceException;

  /**
   * Deletes the environment profileId definition with the given id.
   *
   * @param id the id of environment to delete
   * @return The deleted {@link EnvironmentProfileDto} instance
   * @throws ManagementServiceException if an exception occurs
   */
  EnvironmentProfileDto deleteEnvironmentProfile(String id) throws ManagementServiceException;


  /**
   * Registers an environment definition.
   *
   * @param environmentDto {@link EnvironmentDto} instance
   * @return The persisted {@link EnvironmentDto} instance
   * @throws ManagementServiceException if an exception occurs
   */
  EnvironmentDto registerEnvironment(EnvironmentDto environmentDto) throws ManagementServiceException;

  /**
   * Returns all registered environment definitions.
   *
   * @return List of {@link EnvironmentDto} instances
   * @throws ManagementServiceException if an exception occurs
   */
  List<EnvironmentDto> getAllEnvironments() throws ManagementServiceException;

  /**
   * Updates the given environment definition.
   *
   * @param environmentDto {@link EnvironmentDto} instance
   * @return The updated {@link EnvironmentDto} instance
   * @throws ManagementServiceException if an exception occurs
   */
  EnvironmentDto updateEnvironment(EnvironmentDto environmentDto) throws ManagementServiceException;

  /**
   * Deletes the environment definition with the given UUID.
   *
   * @param uuid {@link java.util.UUID} of environment to delete
   * @return The deleted {@link EnvironmentDto} instance
   * @throws ManagementServiceException if an exception occurs
   */
  EnvironmentDto deleteEnvironment(UUID uuid) throws ManagementServiceException;

  /**
   * Enables monitoring for the environment with the given UUID.
   *
   * @param uuid {@link java.util.UUID} environment UUID
   * @return The deleted {@link EnvironmentDto} instance
   * @throws ManagementServiceException if an exception occurs
   */
  EnvironmentDto enableMonitoring(UUID uuid) throws ManagementServiceException;

  /**
   * Disables monitoring for the environment with the given UUID.
   *
   * @param uuid {@link java.util.UUID} environment UUID
   * @return The deleted {@link EnvironmentDto} instance
   * @throws ManagementServiceException if an exception occurs
   */
  EnvironmentDto disableMonitoring(UUID uuid) throws ManagementServiceException;

  /**
   * Returns all monitored environment definitions.
   *
   * @return List of {@link EnvironmentDto} instances
   * @throws ManagementServiceException if an exception occurs
   */
  List<EnvironmentDto> getMonitoredEnvironments() throws ManagementServiceException;

  /**
   * Registers the given topology definition.
   *
   * @param topologyDTO {@link TopologyDto} instance
   * @return The updated {@link TopologyDto} instance
   * @throws ManagementServiceException if an exception occurs
   */
  TopologyDto registerTopology(TopologyDto topologyDTO) throws ManagementServiceException;

  /**
   * Updates the given topology definition.
   *
   * @param topologyDTO {@link TopologyDto} instance
   * @return The updated {@link TopologyDto} instance
   * @throws ManagementServiceException if an exception occurs
   */
  TopologyDto updateTopology(TopologyDto topologyDTO) throws ManagementServiceException;

  /**
   * Lists topologies.
   * @param filter {@link EnvironmentFilterDto} instance
   * @return <code>List</code> of {@link TopologyDto} instances
   * @throws ManagementServiceException if an exception occurs
   */
  List<TopologyDto> getTopologies(EnvironmentFilterDto filter) throws ManagementServiceException;

  /**
   * Deletes the given topology definition.
   *
   * @param topologyUuid UUID of toplogy to delete
   * @return The deleted {@link TopologyDto} instance
   * @throws ManagementServiceException if an exception occurs
   */
  TopologyDto deleteTopology(UUID topologyUuid) throws ManagementServiceException;


}

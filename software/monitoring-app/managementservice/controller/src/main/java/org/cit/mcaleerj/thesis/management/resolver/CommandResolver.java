
package org.cit.mcaleerj.thesis.management.resolver;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.NonNull;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentFilterDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentProfileDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentProfileFilterDto;
import org.cit.mcaleerj.thesis.management.dto.TopologyDto;
import org.cit.mcaleerj.thesis.management.service.ManagementService;
import org.cit.mcaleerj.thesis.management.service.exception.ManagementServiceException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * GraphQL query/mutation resolver.
 *
 */
@Component
public class CommandResolver {

  /*
   * The management service.
   */
  private final ManagementService managementService;

  /**
   * Constructor.
   *
   * @param managementService  {@link ManagementService} instance.
   *
   */
  public CommandResolver(@NonNull final ManagementService managementService) {
    this.managementService = managementService;
  }

  /**
   * Environment profile registration.
   *
   * @param profileDto The {@link EnvironmentProfileDto} to persist
   *
   * @return The persisted {@link EnvironmentProfileDto} instance
   *
   * @throws ManagementServiceException
   */
  @GraphQLMutation(name = "registerEnvironmentProfile")
  public EnvironmentProfileDto registerEnvironmentProfile(
          @GraphQLArgument(name = "profile") @GraphQLNonNull final EnvironmentProfileDto profileDto) throws ManagementServiceException {
    return this.managementService.registerEnvironmentProfile(profileDto);
  }

  /**
   * List environment profiles.
   *
   * @param filter {@link EnvironmentProfileFilterDto} instance
   *
   * @return  List of {@link EnvironmentProfileDto} instances
   *
   * @throws ManagementServiceException
   */
  @GraphQLQuery(name="environmentProfiles")
  public List<EnvironmentProfileDto> getEnvironmentProfiles(@GraphQLArgument(name = "filter") final EnvironmentProfileFilterDto filter) throws ManagementServiceException {
    return this.managementService.getEnvironmentProfiles(filter);
  }

  /**
   * Delete an environment profileId.
   *
   * @param id the id of environment profileId to delete
   * @return {@link EnvironmentDto} instance
   *
   * @throws ManagementServiceException
   */
  @GraphQLMutation(name="deleteEnvironmentProfile")
  public EnvironmentProfileDto deleteEnvironmentProfile(
          @GraphQLArgument(name = "id") @GraphQLNonNull final String id) throws ManagementServiceException {
    return this.managementService.deleteEnvironmentProfile(id);
  }

  /**
   * Environment registration.
   *
   * @param environmentDto The {@link EnvironmentDto} to persist
   *
   * @return The persisted {@link EnvironmentDto} instance
   *
   * @throws ManagementServiceException
   */
  @GraphQLMutation(name = "registerEnvironment")
  public EnvironmentDto registerEnvironment(
    @GraphQLArgument(name = "environment") @GraphQLNonNull final EnvironmentDto environmentDto) throws ManagementServiceException {
    return this.managementService.registerEnvironment(environmentDto);
  }

  /**
   * List all environments.
   *
   * @return  List of {@link EnvironmentDto} instances
   *
   * @throws ManagementServiceException
   */
  @GraphQLQuery(name="environments")
  public List<EnvironmentDto> getAllEnvironments() throws ManagementServiceException {

    return this.managementService.getAllEnvironments();

  }

  /**
   * Update an environment.
   *
   * @param environmentDto {@link EnvironmentDto} to update
   * @return {@link EnvironmentDto} instance
   *
   * @throws ManagementServiceException
   */
  @GraphQLMutation(name="updateEnvironment")
  public EnvironmentDto updateEnvironment(
          @GraphQLArgument(name = "environment") @GraphQLNonNull final EnvironmentDto environmentDto) throws ManagementServiceException {

    return this.managementService.updateEnvironment(environmentDto);

  }

  /**
   * Delete an environment.
   *
   * @param uuid {@link UUID} of environment to delete
   * @return {@link EnvironmentDto} instance
   *
   * @throws ManagementServiceException
   */
  @GraphQLMutation(name="deleteEnvironment")
  public EnvironmentDto deleteEnvironment(
          @GraphQLArgument(name = "uuid") @GraphQLNonNull final UUID uuid) throws ManagementServiceException {

    return this.managementService.deleteEnvironment(uuid);

  }

  /**
   * Enable monitoring of an environment.
   *
   * @param uuid {@link UUID} of environment to monitor
   * @return Environment on which monitoring has been disabled
   *
   * @throws ManagementServiceException
   */
  @GraphQLMutation(name="monitor")
  public EnvironmentDto monitorEnvironment(
          @GraphQLArgument(name = "uuid") @GraphQLNonNull final UUID uuid) throws ManagementServiceException {

    return this.managementService.enableMonitoring(uuid);

  }

  /**
   * Find environment by UUID.
   *
   * @param uuid {@link UUID} of environment to find
   * @return Matching environment or null if none exists
   *
   * @throws ManagementServiceException
   */
  @GraphQLMutation(name="unmonitor")
  public EnvironmentDto unmonitorEnvironment(
          @GraphQLArgument(name = "uuid") @GraphQLNonNull final UUID uuid) throws ManagementServiceException {

    return this.managementService.disableMonitoring(uuid);

  }

  /**
   * Returns a list of monitored environment UUIDs.
   *
   * @return List of monitored environments.
   *
   * @throws ManagementServiceException
   */
  @GraphQLQuery(name="monitored")
  public List<EnvironmentDto> getMonitoredEnvironments() throws ManagementServiceException {

    return this.managementService.getMonitoredEnvironments();

  }

  /**
   * Register a topology.
   *
   * @param topologyDTO {@link TopologyDto} to register
   *
   * @return {@link TopologyDto} instance
   *
   * @throws ManagementServiceException
   */
  @GraphQLMutation(name="registerTopology")
  public TopologyDto registerTopology(
          @GraphQLArgument(name = "topology") @GraphQLNonNull final TopologyDto topologyDTO) throws ManagementServiceException {
    return this.managementService.registerTopology(topologyDTO);
  }

  /**
   * Update a topology.
   *
   * @param topologyDTO {@link TopologyDto} to update
   * @return {@link TopologyDto} instance
   *
   * @throws ManagementServiceException
   */
  @GraphQLMutation(name="updateTopology")
  public TopologyDto updateTopology(
          @GraphQLArgument(name = "topology") @GraphQLNonNull final TopologyDto topologyDTO) throws ManagementServiceException {
    return this.managementService.updateTopology(topologyDTO);
  }

  /**
   * List topologies.
   *
   * @param filter {@link EnvironmentProfileFilterDto} instance
   *
   * @return  List of {@link EnvironmentProfileDto} instances
   *
   * @throws ManagementServiceException
   */
  @GraphQLQuery(name="getTopologies")
  public List<TopologyDto> getTopologies(@GraphQLArgument(name = "filter") final EnvironmentFilterDto filter) throws ManagementServiceException {
    return this.managementService.getTopologies(filter);
  }

  /**
   * Delete a topology.
   *
   * @param topologyUuid UIUID of topology to delete
   *
   * @return {@link TopologyDto} instance
   *
   * @throws ManagementServiceException
   */
  @GraphQLMutation(name="deleteTopology")
  public TopologyDto deleteTopology(
          @GraphQLArgument(name = "uuid") @GraphQLNonNull final UUID topologyUuid) throws ManagementServiceException {

    return this.managementService.deleteTopology(topologyUuid);

  }



}

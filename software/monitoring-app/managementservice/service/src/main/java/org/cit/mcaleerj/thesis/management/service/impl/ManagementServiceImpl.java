package org.cit.mcaleerj.thesis.management.service.impl;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.management.dao.repository.EnvironmentProfileRepository;
import org.cit.mcaleerj.thesis.management.dao.repository.EnvironmentRepository;
import org.cit.mcaleerj.thesis.management.dao.repository.TopologyRepository;
import org.cit.mcaleerj.thesis.management.domain.ConfigurationProperty;
import org.cit.mcaleerj.thesis.management.domain.Edge;
import org.cit.mcaleerj.thesis.management.domain.Environment;
import org.cit.mcaleerj.thesis.management.domain.EnvironmentProfile;
import org.cit.mcaleerj.thesis.management.domain.Node;
import org.cit.mcaleerj.thesis.management.domain.Topology;
import org.cit.mcaleerj.thesis.management.dto.ConfigurationPropertyDto;
import org.cit.mcaleerj.thesis.management.dto.EdgeDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentFilterDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentProfileDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentProfileFilterDto;
import org.cit.mcaleerj.thesis.management.dto.NodeDto;
import org.cit.mcaleerj.thesis.management.dto.TopologyDto;
import org.cit.mcaleerj.thesis.management.service.ManagementService;
import org.cit.mcaleerj.thesis.management.service.exception.ManagementServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Management service implementation.
 */
@Service
public class ManagementServiceImpl implements ManagementService {

  /*
   * Repository references.
   */
  private final EnvironmentProfileRepository environmentProfileRepository;
  private final EnvironmentRepository environmentRepository;
  private final TopologyRepository topologyRepository;

  /**
   * Constructor.
   *
   * @param environmentProfileRepository {@link EnvironmentProfileRepository} instance
   * @param environmentRepository        {@link EnvironmentRepository} instance
   * @param topologyRepository           {@link TopologyRepository} instance
   */
  public ManagementServiceImpl(@NonNull final EnvironmentProfileRepository environmentProfileRepository,
                               @NonNull final EnvironmentRepository environmentRepository,
                               @NonNull final TopologyRepository topologyRepository) {
    this.environmentProfileRepository = environmentProfileRepository;
    this.environmentRepository = environmentRepository;
    this.topologyRepository = topologyRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnvironmentProfileDto registerEnvironmentProfile(@NonNull final EnvironmentProfileDto profileDto) throws ManagementServiceException {

    validate(profileDto);

    //verify that no such profileId ID already exists
    final String profileId = profileDto.getId();
    final List<EnvironmentProfile> existing = this.environmentProfileRepository.findByProfileId(profileId);
    if (!CollectionUtils.isEmpty(existing)) {
      throw new ManagementServiceException(MessageFormat.format("An environment with profileId ID {0} already exists.", profileId));
    }

    final EnvironmentProfile domainObject = environmentProfileDtoToDomain(profileDto);


    //save
    this.environmentProfileRepository.save(domainObject);

    return environmentProfileDomainToDto(domainObject);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EnvironmentProfileDto> getEnvironmentProfiles(final EnvironmentProfileFilterDto filter) {
    final List<EnvironmentProfileDto> dtos = new ArrayList<>();
    for (EnvironmentProfile profile : this.environmentProfileRepository.findAll()) {

      if (filter != null) {
        if (profile.getProfileId().equals(filter.getId())) {
          dtos.add(environmentProfileDomainToDto(profile));
        }
      } else {
        dtos.add(environmentProfileDomainToDto(profile));
      }
    }
    return dtos;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnvironmentProfileDto deleteEnvironmentProfile(@NonNull final String id) throws ManagementServiceException {
    final EnvironmentProfile profile = this.findEnvironmentProfileById(id);
    this.environmentProfileRepository.delete(profile);
    return environmentProfileDomainToDto(profile);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnvironmentDto registerEnvironment(@NonNull final EnvironmentDto environmentDto) throws ManagementServiceException {

    final Environment domainObject = environmentDtoToDomain(environmentDto);
    if (domainObject.getUuid() == null) {
      domainObject.setUuid(UUID.randomUUID());
    }

    // resolve the environment profileId
    final String profileId = environmentDto.getProfileId();
    if (StringUtils.isEmpty(profileId)) {
      throw new ManagementServiceException(MessageFormat.format("No environment profileId specified", profileId));
    }
    final List<EnvironmentProfile> profiles = this.environmentProfileRepository.findByProfileId(profileId);
    if (CollectionUtils.isEmpty(profiles)) {
      throw new ManagementServiceException(MessageFormat.format("Cannot register environment for unknown environment profileId {0}", profileId));
    }

    final EnvironmentProfile profile = profiles.get(0);
    domainObject.setProfile(profile);

    //save
    this.environmentRepository.save(domainObject);

    return environmentDomainToDto(domainObject);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EnvironmentDto> getAllEnvironments() {
    final List<EnvironmentDto> dtos = new ArrayList<>();
    for (Environment env : this.environmentRepository.findAll()) {
      dtos.add(environmentDomainToDto(env));
    }
    return dtos;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnvironmentDto updateEnvironment(@NonNull final EnvironmentDto environmentDto) throws ManagementServiceException {
    final UUID envUUID = environmentDto.getUuid();
    final Environment existing = this.findEnvironmentByUUID(envUUID);
    this.environmentRepository.delete(existing);

    final Environment updatedEnv = environmentDtoToDomain(environmentDto);
    this.environmentRepository.save(updatedEnv);
    return environmentDto;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnvironmentDto deleteEnvironment(@NonNull final UUID uuid) throws ManagementServiceException {
    final Environment env = this.findEnvironmentByUUID(uuid);
    this.environmentRepository.delete(env);
    return environmentDomainToDto(env);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnvironmentDto enableMonitoring(@NonNull final UUID uuid) throws ManagementServiceException {
    final Environment env = this.findEnvironmentByUUID(uuid);
    env.setMonitored(true);
    this.environmentRepository.save(env);
    return environmentDomainToDto(env);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnvironmentDto disableMonitoring(@NonNull final UUID uuid) throws ManagementServiceException {
    final Environment env = this.findEnvironmentByUUID(uuid);
    env.setMonitored(false);
    this.environmentRepository.save(env);
    return environmentDomainToDto(env);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EnvironmentDto> getMonitoredEnvironments() {
    final List<EnvironmentDto> dtos = new ArrayList<>();
    for (Environment env : this.environmentRepository.findByMonitored(true)) {
      dtos.add(environmentDomainToDto(env));
    }
    return dtos;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public synchronized TopologyDto registerTopology(@NonNull final TopologyDto topologyDTO) throws ManagementServiceException {
    Topology domainObject = topologyDtoToDomain(topologyDTO);

    //find existing environment topology
    final Environment env = domainObject.getEnvironment();
    final List<Topology> existing = this.topologyRepository.findByEnvironment(env);
    if (!CollectionUtils.isEmpty(existing)) {
      if (existing.size() > 1) {
        throw new ManagementServiceException(MessageFormat.format(
                "A topology already exists for environment", domainObject.getEnvironment().getUuid()));
      }
    }

    this.topologyRepository.save(domainObject);
    return topologyDomainToDto(domainObject);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public synchronized TopologyDto updateTopology(@NonNull final TopologyDto topologyDTO) throws ManagementServiceException {
    Topology domainObject = topologyDtoToDomain(topologyDTO);

    //find existing environment topology
    final List<Topology> existing = this.topologyRepository.findByEnvironment(domainObject.getEnvironment());
    if (CollectionUtils.isEmpty(existing)) {
      this.topologyRepository.save(domainObject);
      return topologyDomainToDto(domainObject);
    }

    if (existing.size() > 1) {
      throw new ManagementServiceException(MessageFormat.format(
              "Multiple topologies defined for environment {0}", domainObject.getEnvironment().getUuid()));
    }

    final Topology existingTopology = existing.get(0);

    TopologyHelper.mergeTopologies(existingTopology, domainObject);
    this.topologyRepository.save(existingTopology);
    return topologyDomainToDto(existingTopology);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TopologyDto> getTopologies(final EnvironmentFilterDto filter) {

    final List<TopologyDto> result = new ArrayList<>();
    final List<Topology> topologies = this.topologyRepository.findAll();
    for (Topology topology : topologies) {
      if (filter != null && filter.getUuid() != null) {
        if(filter.getUuid().equals(topology.getEnvironment().getUuid())) {
          result.add(topologyDomainToDto(topology));
        }
      } else {
        result.add(topologyDomainToDto(topology));
      }
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public TopologyDto deleteTopology(@NonNull final UUID topologyUuid) throws ManagementServiceException {
    final Topology topology = this.findTopologyByUUID(topologyUuid);
    this.topologyRepository.delete(topology);
    return topologyDomainToDto(topology);
  }

  private Environment findEnvironmentByUUID(@NonNull final UUID uuid) throws ManagementServiceException {
    List<Environment> environments = this.environmentRepository.findByUuid(uuid);
    if (environments.isEmpty()) {
      throw new ManagementServiceException(MessageFormat.format("No environment with UUID {0}", uuid));
    }
    if (environments.size() > 1) {
      throw new ManagementServiceException(MessageFormat.format("Multiple environments match UUID {0}", uuid));
    }
    return environments.get(0);
  }

  private EnvironmentProfile findEnvironmentProfileById(@NonNull final String id) throws ManagementServiceException {
    List<EnvironmentProfile> profiles = this.environmentProfileRepository.findByProfileId(id);
    if (profiles.isEmpty()) {
      throw new ManagementServiceException(MessageFormat.format("No environment profileId with id {0}", id));
    }
    if (profiles.size() > 1) {
      throw new ManagementServiceException(MessageFormat.format("Multiple profiles match id {0}", id));
    }
    return profiles.get(0);
  }

  private Topology findTopologyByUUID(@NonNull final UUID uuid) throws ManagementServiceException {
    List<Topology> topologies = this.topologyRepository.findByUuid(uuid);
    if (topologies.isEmpty()) {
      throw new ManagementServiceException(MessageFormat.format("No topology with UUID {0}", uuid));
    }
    if (topologies.size() > 1) {
      throw new ManagementServiceException(MessageFormat.format("Multiple topologies with uuid {0}", uuid));
    }
    return topologies.get(0);
  }

  /**
   * Converts {@link EnvironmentProfileDto} instance to {@link EnvironmentProfile} instance.
   *
   * @param profileDto EnvironmentProfileDto to convert
   * @return {@link EnvironmentProfile} instance
   */
  private static EnvironmentProfile environmentProfileDtoToDomain(@NonNull final EnvironmentProfileDto profileDto) {

    final EnvironmentProfile domainObject = new EnvironmentProfile();
    domainObject.setProfileId(profileDto.getId());
    domainObject.setName(profileDto.getName());
    for (ConfigurationPropertyDto propertyDto : profileDto.getConfigurationProperties()) {
      final ConfigurationProperty property = configurationPropertyDtoDomain(propertyDto);
      property.setConfigurable(domainObject);
      domainObject.getConfigurationProperties().add(property);
    }
    return domainObject;
  }

  /**
   * Converts {@link EnvironmentProfile} instance to {@link EnvironmentProfileDto} instance.
   *
   * @param domainObject EnvironmentProfile to convert
   * @return {@link EnvironmentProfileDto} instance
   */
  private static EnvironmentProfileDto environmentProfileDomainToDto(@NonNull final EnvironmentProfile domainObject) {

    final EnvironmentProfileDto dto = new EnvironmentProfileDto();
    dto.setId(domainObject.getProfileId());
    dto.setName(domainObject.getName());
    for (ConfigurationProperty server : domainObject.getConfigurationProperties()) {
      dto.getConfigurationProperties().add(configurationPropertyDomainToDto(server));
    }

    return dto;
  }

  /**
   * Converts {@link EnvironmentDto} instance to {@link Environment} instance.
   *
   * @param environmentDto EnvironmentDto to convert
   * @return {@link Environment} instance
   */
  private static Environment environmentDtoToDomain(@NonNull final EnvironmentDto environmentDto) {

    final Environment domainObject = new Environment();
    domainObject.setUuid(environmentDto.getUuid());
    domainObject.setName(environmentDto.getName());

    for (ConfigurationPropertyDto propertyDto : environmentDto.getConfigurationProperties()) {
      final ConfigurationProperty property = configurationPropertyDtoDomain(propertyDto);
      property.setConfigurable(domainObject);
      domainObject.getConfigurationProperties().add(property);
    }

    return domainObject;
  }

  /**
   * Converts {@link ConfigurationPropertyDto} instance to {@link ConfigurationProperty} instance.
   *
   * @param propertyDto ConfigurationPropertyDto to convert
   * @return {@link ConfigurationProperty} instance
   */
  private static ConfigurationProperty configurationPropertyDtoDomain(@NonNull final ConfigurationPropertyDto propertyDto) {

    final ConfigurationProperty domainObject = new ConfigurationProperty();
    domainObject.setName(propertyDto.getName());
    domainObject.setValue(propertyDto.getValue());
    return domainObject;
  }

  /**
   * Converts {@link Environment} instance to {@link EnvironmentDto} instance.
   *
   * @param domainObject Environment to convert
   * @return {@link EnvironmentDto} instance
   */
  private static EnvironmentDto environmentDomainToDto(@NonNull final Environment domainObject) {

    final EnvironmentDto dto = new EnvironmentDto();
    dto.setUuid(domainObject.getUuid());
    dto.setName(domainObject.getName());
    dto.setProfileId(domainObject.getProfile().getProfileId());

    for (ConfigurationProperty server : domainObject.getConfigurationProperties()) {
      dto.getConfigurationProperties().add(configurationPropertyDomainToDto(server));
    }

    return dto;
  }

  /**
   * Converts {@link ConfigurationProperty} instance to {@link ConfigurationPropertyDto} instance.
   *
   * @param domainObject ConfigurationProperty to convert
   * @return {@link ConfigurationPropertyDto} instance
   */
  private static ConfigurationPropertyDto configurationPropertyDomainToDto(@NonNull final ConfigurationProperty domainObject) {

    final ConfigurationPropertyDto dto = new ConfigurationPropertyDto();
    dto.setName(domainObject.getName());
    dto.setValue(domainObject.getValue());
    return dto;
  }

  /**
   * Converts {@link TopologyDto} instance to {@link Topology} instance.
   *
   * @param topologyDto TopologyDto to convert
   * @return {@link Topology} instance
   */
  private Topology topologyDtoToDomain(@NonNull final TopologyDto topologyDto) throws ManagementServiceException {
    final Topology domainObject = new Topology();
    final UUID environmentUuid = topologyDto.getEnvironmentUuid();
    final Environment environment = this.findEnvironmentByUUID(environmentUuid);

    domainObject.setEnvironment(environment);
    domainObject.setUuid(UUID.randomUUID());

    for (NodeDto nodeDto : topologyDto.getNodes()) {
      domainObject.getNodes().add(nodeDtoToDomain(nodeDto));
    }

    for (EdgeDto edgeDto : topologyDto.getEdges()) {
      domainObject.getEdges().add(edgeDtoToDomain(edgeDto, domainObject));
    }


    return domainObject;
  }

  /**
   * Converts {@link NodeDto} instance to {@link Node} instance.
   *
   * @param nodeDto NodeDto to convert
   * @return {@link Node} instance
   */
  private Node nodeDtoToDomain(@NonNull final NodeDto nodeDto) {
    final Node domainObject = new Node();

    domainObject.setName(nodeDto.getName());

    return domainObject;
  }


  /**
   * Converts {@link EdgeDto} instance to {@link Edge} instance.
   *
   * @param edgeDto EdgeDto to convert
   * @return {@link Edge} instance
   */
  private Edge edgeDtoToDomain(@NonNull final EdgeDto edgeDto, final Topology domainTopology) {
    final Edge edge = new Edge();
    edge.setLabel(edgeDto.getLabel());
    edge.setSourceNode(domainTopology.getNode(edgeDto.getSource()));
    edge.setTargetNode(domainTopology.getNode(edgeDto.getTarget()));
    return edge;
  }

  /**
   * Converts {@link Topology} instance to {@link TopologyDto} instance.
   *
   * @param domainObject Topology to convert
   * @return {@link TopologyDto} instance
   */
  private static TopologyDto topologyDomainToDto(@NonNull final Topology domainObject) {
    final TopologyDto dto = new TopologyDto();
    dto.setUuid(domainObject.getUuid());
    dto.setEnvironmentUuid(domainObject.getEnvironment().getUuid());
    dto.setEnvironmentName(domainObject.getEnvironment().getName());
    domainObject.getNodes().forEach(node -> dto.getNodes().add(nodeDomainToDto(node)));
    domainObject.getEdges().forEach(edge -> dto.getEdges().add(edgeDomainToDto(edge)));
    return dto;
  }

  /**
   * Converts {@link Node} instance to {@link NodeDto} instance.
   *
   * @param domainObject Node to convert
   * @return {@link NodeDto} instance
   */
  private static NodeDto nodeDomainToDto(@NonNull final Node domainObject) {
    final NodeDto dto = new NodeDto();
    dto.setName(domainObject.getName());
    return dto;
  }

  /**
   * Converts {@link Edge} instance to {@link EdgeDto} instance.
   *
   * @param domainObject Edge to convert
   * @return {@link EdgeDto} instance
   */
  private static EdgeDto edgeDomainToDto(@NonNull final Edge domainObject) {
    final EdgeDto dto = new EdgeDto();
    dto.setId(domainObject.getId());
    dto.setLabel(domainObject.getLabel());
    final Node sourceNode = domainObject.getSourceNode();
    if (sourceNode != null) {
      dto.setSource(sourceNode.getName());
    }
    final Node targetNode = domainObject.getTargetNode();
    if (targetNode != null) {
      dto.setTarget(targetNode.getName());
    }
    return dto;
  }

  private static void validate(EnvironmentProfileDto profileDto) throws ManagementServiceException {
    if (StringUtils.isEmpty(profileDto.getId())) {
      throw new ManagementServiceException("A unique ID must be specified for this environment profileId.");
    }
    if (StringUtils.isEmpty(profileDto.getName())) {
      throw new ManagementServiceException("A name must be specified for this environment profileId.");
    }
  }
}

package org.cit.mcaleerj.thesis.discovery.k8s.agent;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1ConfigMap;
import io.kubernetes.client.models.V1ConfigMapList;
import io.kubernetes.client.util.Config;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.cit.mcaleerj.thesis.discovery.agent.api.DiscoveryAgent;
import org.cit.mcaleerj.thesis.discovery.agent.exception.DiscoveryException;
import org.cit.mcaleerj.thesis.management.client.ManagementServiceClient;
import org.cit.mcaleerj.thesis.management.client.exception.ManagementServiceClientException;
import org.cit.mcaleerj.thesis.management.dto.ConfigurationPropertyDto;
import org.cit.mcaleerj.thesis.management.dto.EdgeDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentProfileDto;
import org.cit.mcaleerj.thesis.management.dto.NodeDto;
import org.cit.mcaleerj.thesis.management.dto.TopologyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Discovery agent implementation.
 * Reads node and edge information from metadata contained in Kubernetes configmaps.
 */
public class KubernetesDiscoveryAgent implements DiscoveryAgent {

  /*
   * Logging.
   */
  private static final Logger log = LoggerFactory.getLogger(KubernetesDiscoveryAgent.class);

  /*
   * Agent identifier.
   */
  private static final String IDENTIFIER = "K8S-AGENT";

  /*
   * Configmap property denoting node name edges.
   */
  private static final String PROPERTY_NODE_NAME = "mon.agent.nodename";

  /*
   * Configmap property denoting incoming edges.
   */
  private static final String PROPERTY_INCOMING_EDGES = "mon.agent.incoming-edges";

  /*
   * Configmap property denoting outgoing edges.
   */
  private static final String PROPERTY_OUTGOING_EDGES = "mon.agent.outgoing-edges";

  /*
   * Configuration property names.
   */
  private static final String CFG_PROP_K8S_AGENT_CERT_PATH = "discovery.agent.k8s.cert.path";
  private static final String CFG_PROP_K8S_NAMESPACE = "discovery.agent.k8s.namespace";

  /*
   * Management service client.
   */
  private final ManagementServiceClient mgmtServiceClient;

  /**
   * Constructor.
   *
   * @param mgmtServiceClient Management service client
   */
  public KubernetesDiscoveryAgent(ManagementServiceClient mgmtServiceClient) {
    this.mgmtServiceClient = mgmtServiceClient;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIdentifier() {
    return IDENTIFIER;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canDiscover(final EnvironmentDto environment) throws DiscoveryException {

    final String namespace = getEnvironmentNamespace(environment);
    if (StringUtils.isBlank(namespace)) {
      log.debug(MessageFormat.format("Cannot discover environment {0}, no k8s namespace defined by configuration", environment.getUuid()));
      return false;
    }

    final String certificatePath = getCertificatePath(environment);
    if (StringUtils.isBlank(certificatePath)) {
      log.debug(MessageFormat.format("Cannot discover environment {0}, no k8s certificate path defined by configuration", environment.getUuid()));
      return false;
    }

    final File cfgFile = new File(certificatePath);
    if (!cfgFile.exists()) {
      log.debug(MessageFormat.format("Cannot discover environment {0}, certifcate at {1} does not exist",
                                     environment.getUuid(), cfgFile.getAbsolutePath()));
      return false;
    }

    // configuration is good
    return true;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TopologyDto discover(@NonNull final EnvironmentDto environment) throws DiscoveryException {

    // determine environment namespace and k8s certificate path
    final String namespace = getEnvironmentNamespace(environment);
    final String certificatePath = getCertificatePath(environment);

    final TopologyDto dto = new TopologyDto();

    try {

      final File cfgFile = new File(certificatePath);
      final FileInputStream fis = new FileInputStream(cfgFile);
      final ApiClient client = Config.fromConfig(fis);

      Configuration.setDefaultApiClient(client);

      final CoreV1Api api = new CoreV1Api();

      final V1ConfigMapList configMapList = api.listNamespacedConfigMap(namespace, false, null, null, null,
                                                                        null, 0, null, 0, false);

      if (configMapList != null) {
        configMapList.getItems().forEach(configMap -> updateTopology(configMap, dto));
      }
    } catch (ApiException | IOException e) {
      throw new DiscoveryException("Discovery failed", e);
    }
    return dto;
  }

  private void updateTopology(final V1ConfigMap configMap, final TopologyDto topology) {

    final String nodeName = configMap.getData().get(PROPERTY_NODE_NAME);
    if(StringUtils.isBlank(nodeName)) {
      //no metadata present
      return;
    }

    //add node
    final NodeDto node = new NodeDto();
    node.setName(nodeName);
    topology.getNodes().add(node);

    getTopicNames(configMap, PROPERTY_INCOMING_EDGES).forEach(
            topicName -> {
              final EdgeDto edge = new EdgeDto();
              edge.setLabel(topicName);
              edge.setTarget(node.getName());
              topology.getEdges().add(edge);
            });

    getTopicNames(configMap, PROPERTY_OUTGOING_EDGES).forEach(
            topicName -> {
              final EdgeDto edge = new EdgeDto();
              edge.setLabel(topicName);
              edge.setSource(node.getName());
              topology.getEdges().add(edge);
            });

  }

  private static List<String> getTopicNames(final V1ConfigMap configMap, final String configMapDataPropertyName) {

    final String topicList = configMap.getData().get(configMapDataPropertyName);
    if (StringUtils.isBlank(topicList)) {
      return Collections.emptyList();
    }

    final List<String> result = new ArrayList<>();
    String[] topicNames = topicList.split(",");
    for (String topicName : topicNames) {
      result.add(topicName.trim());
    }
    return result;
  }

  /**
   * Returns value of certificate path environment config property, if set
   *
   * @param environmentDto {@link EnvironmentDto instance}
   * @return value of certificate path environment config property if set, else null
   */
  private static String getCertificatePath(final EnvironmentDto environmentDto) {
    return getEnvironmentPropertyValue(environmentDto, CFG_PROP_K8S_AGENT_CERT_PATH);
  }

  /**
   * Returns value of k8s namespace environment config property, if set
   *
   * @param environmentDto {@link EnvironmentDto instance}
   * @return value of k8s namespace environment config property if set, else null
   */
  private static String getEnvironmentNamespace(final EnvironmentDto environmentDto) {
    return getEnvironmentPropertyValue(environmentDto, CFG_PROP_K8S_NAMESPACE);
  }

  /**
   * Returns value of the named config property, if set
   *
   * @param environmentDto {@link EnvironmentDto instance}
   * @return value of named config property if set, else null
   */
  private static String getEnvironmentPropertyValue(final EnvironmentDto environmentDto, final String propertyName) {
    for (ConfigurationPropertyDto prop : environmentDto.getConfigurationProperties()) {
      if (propertyName.equals(prop.getName())) {
        return prop.getValue();
      }
    }
    return null;
  }
}

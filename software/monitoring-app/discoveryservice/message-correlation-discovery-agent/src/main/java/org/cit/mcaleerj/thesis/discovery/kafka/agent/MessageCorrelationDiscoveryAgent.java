package org.cit.mcaleerj.thesis.discovery.kafka.agent;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.cit.mcaleerj.thesis.correlation.client.CorrelationServiceClient;
import org.cit.mcaleerj.thesis.correlation.client.exception.CorrelationServiceClientException;
import org.cit.mcaleerj.thesis.correlation.dto.CorrelationTraceDto;
import org.cit.mcaleerj.thesis.correlation.dto.MessageDto;
import org.cit.mcaleerj.thesis.discovery.agent.api.DiscoveryAgent;
import org.cit.mcaleerj.thesis.discovery.agent.exception.DiscoveryException;
import org.cit.mcaleerj.thesis.management.dto.ConfigurationPropertyDto;
import org.cit.mcaleerj.thesis.management.dto.EdgeDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.management.dto.NodeDto;
import org.cit.mcaleerj.thesis.management.dto.TopologyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageCorrelationDiscoveryAgent implements DiscoveryAgent {

  private static final String IDENTIFIER = "MESSAGE-CORRELATION-AGENT";

  private static final String CFG_PROP_CORRELATION_FIELD = "discovery.agent.correlation.field.name";

  /*
   * Logging.
   */
  private static final Logger log = LoggerFactory.getLogger(MessageCorrelationDiscoveryAgent.class);

  /*
   * Correlation service client.
   */
  private final CorrelationServiceClient correlationServiceClient;


  /**
   * Constructor.
   *
   * @param correlationServiceClient The correlation service client
   */
  public MessageCorrelationDiscoveryAgent(@NonNull final CorrelationServiceClient correlationServiceClient) {
    this.correlationServiceClient = correlationServiceClient;
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
    final String fieldName = getCorrelationFieldName(environment);
    return !StringUtils.isBlank(fieldName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TopologyDto discover(@NonNull final EnvironmentDto environment) throws DiscoveryException {

    final String fieldName = getCorrelationFieldName(environment);

    try {
      List<CorrelationTraceDto> traces = this.correlationServiceClient.getTraces(environment.getUuid(), fieldName);
      return buildTopology(traces);
    } catch (CorrelationServiceClientException e) {
      throw new DiscoveryException("Discovery Failed", e);
    }

  }

  /**
   * Returns value of k8s namespace environment config property, if set
   *
   * @param environmentDto {@link EnvironmentDto instance}
   * @return value of k8s namespace environment config property if set, else null
   */
  private static String getCorrelationFieldName(final EnvironmentDto environmentDto) {
    return getEnvironmentPropertyValue(environmentDto, CFG_PROP_CORRELATION_FIELD);
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

  /**
   * Given a list of correlation traces, returns a corresponding topology.
   *
   * @param traces <code>List</code> of {@link CorrelationTraceDto} instances
   *
   * @return TopologyDto
   */
  private static TopologyDto buildTopology(@NonNull final List<CorrelationTraceDto> traces) {

    final TopologyDto topology = new TopologyDto();

    final Set<NodeDto> allNodes = new HashSet<>();
    final Set<EdgeDto> allEdges = new HashSet<>();

    EdgeDto lastEdge = null;

    for(CorrelationTraceDto trace : traces) {

      for(MessageDto message : trace.getMessages()) {

        if(!StringUtils.isBlank(message.getSourceNode())) {
          NodeDto node = new NodeDto();
          node.setName(message.getSourceNode());
          allNodes.add(node);
        }

        EdgeDto edge = findOrCreateEdge(message.getEdgeLabel(), message.getSourceNode(), allEdges);
        if(lastEdge != null) {
          lastEdge.setTarget(message.getSourceNode());
        }
        lastEdge = edge;
      }
    }

    topology.getNodes().addAll(allNodes);
    topology.getEdges().addAll(allEdges);
    return topology;

  }

  private static EdgeDto findOrCreateEdge(String edgeLabel, String sourceNode, Set<EdgeDto> allEdges) {
    for (EdgeDto edge : allEdges) {
      if (edge.getLabel().equals(edgeLabel) && StringUtils.equals(edge.getSource(), sourceNode)) {
        return edge;
      }
    }
    EdgeDto edge = new EdgeDto();
    edge.setLabel(edgeLabel);
    edge.setSource(sourceNode);
    allEdges.add(edge);
    return edge;
  }

}

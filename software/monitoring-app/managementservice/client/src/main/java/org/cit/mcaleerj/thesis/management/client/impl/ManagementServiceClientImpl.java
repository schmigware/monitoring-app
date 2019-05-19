package org.cit.mcaleerj.thesis.management.client.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import lombok.NonNull;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.cit.mcaleerj.thesis.management.client.ManagementServiceClient;
import org.cit.mcaleerj.thesis.management.client.exception.ManagementServiceClientException;
import org.cit.mcaleerj.thesis.management.client.model.GetEnvironmentProfilesResponse;
import org.cit.mcaleerj.thesis.management.client.model.GetEnvironmentsResponse;
import org.cit.mcaleerj.thesis.management.client.model.GetMonitoredEnvironmentsResponse;
import org.cit.mcaleerj.thesis.management.client.model.MergeTopologyResponse;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentProfileDto;
import org.cit.mcaleerj.thesis.management.dto.TopologyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@SuppressWarnings("unused")
public class ManagementServiceClientImpl implements ManagementServiceClient {

  /*
   * Query names.
   */
  private static final String QUERY_GET_ENVIRONMENT_PROFILES = "getEnvironmentProfiles.query";
  private static final String QUERY_GET_ENVIRONMENT_PROFILES_BY_ID = "getEnvironmentProfilesById.query";
  private static final String QUERY_GET_MONITORED_ENVIRONMENTS = "getMonitored.query";
  private static final String QUERY_GET_ALL_ENVIRONMENT = "getEnvironments.query";
  private static final String QUERY_UPDATE_TOPOLOGY = "update.topology.mutation";

  /**
   * Query placeholders.
   */
  private static final String PLACEHOLDER_ENVIRONMENT_PROFLE_ID = "PROFILEID";

  /*
   * Request map attributes.
   */
  private static final String MAP_ATTRIB_QUERY = "query";

  /*
   * Http constants.
   */
  private static final String CONTENT_TYPE_HEADER_KEY = "Content-type";

  /*
   * GraphQL endpoint URL.
   */
  private static final String QUERY_BASE_PATH = "/graphql";

  /*
   * Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ManagementServiceClientImpl.class);

  /*
   * JSON deserialisation.
   */
  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    // graphQL response isn't quite JSON...
    objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private final EurekaClient discoveryClient;

  private final HttpClient httpClient;
  private final QueryCache queryCache;

  @Value("${management.service.serviceId}")
  private String managementServiceId;

  /**
   * Constructor.
   */
  public ManagementServiceClientImpl(@NonNull final EurekaClient discoveryClient) {
    this.discoveryClient = discoveryClient;
    this.httpClient = HttpClientBuilder.create().build();
    this.queryCache = new QueryCache(QUERY_BASE_PATH);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EnvironmentProfileDto> getEnvironmentProfiles() throws ManagementServiceClientException {
    try {
      final String query = this.queryCache.getQuery(QUERY_GET_ENVIRONMENT_PROFILES);
      final HttpPost httpPost = this.buildHTTPPost(query);
      final HttpResponse response = this.httpClient.execute(httpPost);
      return parseGetEnvironmentProfilesResponse(response);
    } catch (IOException e) {
      throw new ManagementServiceClientException("Query execution failed", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnvironmentProfileDto getEnvironmentProfile(final String profileId) throws ManagementServiceClientException {
    try {
      String query = this.queryCache.getQuery(QUERY_GET_ENVIRONMENT_PROFILES);

      query = query.replaceAll(PLACEHOLDER_ENVIRONMENT_PROFLE_ID, profileId);
      final HttpPost httpPost = this.buildHTTPPost(query);
      final HttpResponse response = this.httpClient.execute(httpPost);

      List<EnvironmentProfileDto> profiles = parseGetEnvironmentProfilesResponse(response);
      return CollectionUtils.isEmpty(profiles) ? null : profiles.get(0);

    } catch (IOException e) {
      throw new ManagementServiceClientException("Query execution failed", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EnvironmentDto> getMonitoredEnvironments() throws ManagementServiceClientException {
    try {
      final String query = this.queryCache.getQuery(QUERY_GET_MONITORED_ENVIRONMENTS);
      final HttpPost httpPost = this.buildHTTPPost(query);
      final HttpResponse response = this.httpClient.execute(httpPost);
      return parseGetMonitoredEnvironmentsResponse(response);
    } catch (IOException e) {
      throw new ManagementServiceClientException("Query execution failed", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnvironmentDto getEnvironmentByUuid(final UUID uuid) throws ManagementServiceClientException {
    try {
      final String query = this.queryCache.getQuery(QUERY_GET_ALL_ENVIRONMENT);
      final HttpPost httpPost = this.buildHTTPPost(query);
      final HttpResponse response = this.httpClient.execute(httpPost);
      List<EnvironmentDto> allEnvs = parseGetEnvironmentsResponse(response);
      for (EnvironmentDto dto : allEnvs) {
        if (dto.getUuid().equals(uuid)) {
          return dto;
        }
      }
      return null;
    } catch (IOException e) {
      throw new ManagementServiceClientException("Query execution failed", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TopologyDto updateTopology(final TopologyDto topology) throws ManagementServiceClientException {
    try {
      String query = this.queryCache.getQuery(QUERY_UPDATE_TOPOLOGY);
      //remove key quotes
      query = String.format(query, objectMapper.writeValueAsString(topology).replaceAll("\"(\\w+)\":", "$1:"));
      final HttpPost httpPost = this.buildHTTPPost(query);
      final HttpResponse response = this.httpClient.execute(httpPost);
      return parseMergeTopologyResponse(response);
    } catch (IOException e) {
      throw new ManagementServiceClientException("Query execution failed", e);
    }
  }

  /**
   * Builds HttpPost for the given GraphQL query string.
   *
   * @param query GraphQL query
   * @return {@link HttpPost} instance
   * @throws IOException if construction of {@link HttpPost} fails
   */
  private HttpPost buildHTTPPost(final String query) throws IOException, ManagementServiceClientException {

    final Application application = this.discoveryClient.getApplication(this.managementServiceId);
    if(application == null || CollectionUtils.isEmpty(application.getInstances())) {
      throw new ManagementServiceClientException("Management service unavailable");
    }
    InstanceInfo instanceInfo = application.getInstances().get(0);
    final String url = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort() + "/" + "graphql";

    final HttpPost httpPost = new HttpPost(url);

    final Map<String, String> requestMap = Collections.singletonMap(MAP_ATTRIB_QUERY, query);
    final String requestJSON = objectMapper.writeValueAsString(requestMap);

    httpPost.setEntity(new StringEntity(requestJSON, ContentType.APPLICATION_JSON));
    httpPost.setHeader(CONTENT_TYPE_HEADER_KEY, ContentType.APPLICATION_JSON.toString());
    return httpPost;
  }

  /**
   * Extract and return <code>List</code> of {@link EnvironmentDto} instances from response.
   *
   * @param httpResponse {@link} HttpResponse instance
   * @return List of {@link EnvironmentDto} instances
   * @throws IOException If environment DTO objects cannot be read from response
   */
  private List<EnvironmentDto> parseGetEnvironmentsResponse(final HttpResponse httpResponse) throws IOException {

    final int statusCode = httpResponse.getStatusLine().getStatusCode();

    if (statusCode != HttpStatus.SC_OK) {
      log.error("Bad response with status code : {}", statusCode);
      throw new IOException("Bad response");
    }

    final String responseDataInString = EntityUtils.toString(httpResponse.getEntity());

    log.debug("Response from management service: {}", responseDataInString);

    final GetEnvironmentsResponse responseObject =
            objectMapper.readValue(responseDataInString, GetEnvironmentsResponse.class);

    if (responseObject != null) {
      return responseObject.getEnvironments();
    }

    log.warn("GraphQL response does not contain monitored environment information");
    return Collections.emptyList();
  }

  /**
   * Extract and return {@link EnvironmentProfileDto} instance from response.
   *
   * @param httpResponse {@link} HttpResponse instance
   * @return {@link TopologyDto} instance
   * @throws IOException If TopologyDto instance cannot be read from response
   */
  private List<EnvironmentProfileDto> parseGetEnvironmentProfilesResponse(
          final HttpResponse httpResponse) throws IOException {

    final int statusCode = httpResponse.getStatusLine().getStatusCode();

    if (statusCode != HttpStatus.SC_OK) {
      log.error("Bad response with status code : {}", statusCode);
      throw new IOException("Bad response");
    }

    final String responseDataInString = EntityUtils.toString(httpResponse.getEntity());

    log.debug("Response from management service: {}", responseDataInString);

    final GetEnvironmentProfilesResponse responseObject =
            objectMapper.readValue(responseDataInString, GetEnvironmentProfilesResponse.class);

    if (responseObject != null) {
      return responseObject.getEnvironmentProfiles();
    }

    log.warn("GraphQL response does not contain environment profileId information");
    return null;
  }



  /**
   * Extract and return <code>List</code> of {@link EnvironmentDto} instances from response.
   *
   * @param httpResponse {@link} HttpResponse instance
   * @return List of {@link EnvironmentDto} instances
   * @throws IOException If environment DTO objects cannot be read from response
   */
  private List<EnvironmentDto> parseGetMonitoredEnvironmentsResponse(
          final HttpResponse httpResponse) throws IOException {

    final int statusCode = httpResponse.getStatusLine().getStatusCode();

    if (statusCode != HttpStatus.SC_OK) {
      log.error("Bad response with status code : {}", statusCode);
      throw new IOException("Bad response");
    }

    final String responseDataInString = EntityUtils.toString(httpResponse.getEntity());

    log.debug("Response from management service: {}", responseDataInString);

    final GetMonitoredEnvironmentsResponse responseObject =
            objectMapper.readValue(responseDataInString, GetMonitoredEnvironmentsResponse.class);

    if (responseObject != null) {
      return responseObject.getEnvironments();
    }

    log.warn("GraphQL response does not contain monitored environment information");
    return Collections.emptyList();
  }

  /**
   * Extract and return {@link TopologyDto} instance from response.
   *
   * @param httpResponse {@link} HttpResponse instance
   * @return {@link TopologyDto} instance
   * @throws IOException If TopologyDto instance cannot be read from response
   */
  private TopologyDto parseMergeTopologyResponse(
          final HttpResponse httpResponse) throws IOException {

    final int statusCode = httpResponse.getStatusLine().getStatusCode();

    if (statusCode != HttpStatus.SC_OK) {
      log.error("Bad response with status code : {}", statusCode);
      throw new IOException("Bad response");
    }

    final String responseDataInString = EntityUtils.toString(httpResponse.getEntity());

    log.debug("Response from management service: {}", responseDataInString);

    final MergeTopologyResponse responseObject =
            objectMapper.readValue(responseDataInString, MergeTopologyResponse.class);

    if (responseObject != null) {
      return responseObject.getTopologyDto();
    }

    log.warn("GraphQL response does not contain merged topology information");
    return null;
  }


}


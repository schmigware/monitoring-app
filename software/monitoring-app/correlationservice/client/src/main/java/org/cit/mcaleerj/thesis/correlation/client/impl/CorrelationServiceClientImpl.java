package org.cit.mcaleerj.thesis.correlation.client.impl;

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
import org.cit.mcaleerj.thesis.correlation.client.CorrelationServiceClient;
import org.cit.mcaleerj.thesis.correlation.client.exception.CorrelationServiceClientException;
import org.cit.mcaleerj.thesis.correlation.client.model.GetCorrelationTracesResponse;
import org.cit.mcaleerj.thesis.correlation.dto.CorrelationTraceDto;
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
public class CorrelationServiceClientImpl implements CorrelationServiceClient {

  /*
   * Query names.
   */
  private static final String QUERY_GET_TRACES = "getTraces.query";

  /**
   * Query placeholders.
   */
  private static final String PLACEHOLDER_ENVIRONMENT_UUID = "ENVIRONMENT_UUID";
  private static final String PLACEHOLDER_FIELD_NAME = "FIELD_NAME";

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
  private static final Logger log = LoggerFactory.getLogger(CorrelationServiceClientImpl.class);

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

  @Value("${correlation.service.serviceId}")
  private String correlationServiceId;

  /**
   * Constructor.
   */
  public CorrelationServiceClientImpl(@NonNull final EurekaClient discoveryClient) {
    this.discoveryClient = discoveryClient;
    this.httpClient = HttpClientBuilder.create().build();
    this.queryCache = new QueryCache(QUERY_BASE_PATH);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CorrelationTraceDto> getTraces(final UUID environmentUUID, final String fieldName) throws CorrelationServiceClientException {
    try {
      String query = this.queryCache.getQuery(QUERY_GET_TRACES);

      query = query.replaceAll(PLACEHOLDER_ENVIRONMENT_UUID, environmentUUID.toString());
      query = query.replaceAll(PLACEHOLDER_FIELD_NAME, fieldName);

      final HttpPost httpPost = this.buildHTTPPost(query);
      final HttpResponse response = this.httpClient.execute(httpPost);

      return parseGetTracesResponse(response);

    } catch (IOException e) {
      throw new CorrelationServiceClientException("Query execution failed", e);
    }
  }

  /**
   * Builds HttpPost for the given GraphQL query string.
   *
   * @param query GraphQL query
   * @return {@link HttpPost} instance
   * @throws IOException if construction of {@link HttpPost} fails
   */
  private HttpPost buildHTTPPost(final String query) throws IOException, CorrelationServiceClientException {

    final Application application = this.discoveryClient.getApplication(this.correlationServiceId);
    if(application == null || CollectionUtils.isEmpty(application.getInstances())) {
      throw new CorrelationServiceClientException("Correlation service unavailable");
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
   * Extract and return <code>List</code> of {@link CorrelationTraceDto} instances from response.
   *
   * @param httpResponse {@link} HttpResponse instance
   * @return List of {@link CorrelationTraceDto} instances
   * @throws IOException If trace DTO objects cannot be read from response
   */
  private List<CorrelationTraceDto> parseGetTracesResponse(final HttpResponse httpResponse) throws IOException {

    final int statusCode = httpResponse.getStatusLine().getStatusCode();

    if (statusCode != HttpStatus.SC_OK) {
      log.error("Bad response with status code : {}", statusCode);
      throw new IOException("Bad response");
    }

    final String responseDataInString = EntityUtils.toString(httpResponse.getEntity());

    log.debug("Response from management service: {}", responseDataInString);

    final GetCorrelationTracesResponse responseObject =
            objectMapper.readValue(responseDataInString, GetCorrelationTracesResponse.class);

    if (responseObject != null) {
      return responseObject.getCorrelationTraces();
    }

    log.warn("GraphQL response does not contain correlation trace information");
    return Collections.emptyList();
  }


}


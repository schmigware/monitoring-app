package org.cit.mcaleerj.thesis.aggregationservice.websocket;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.cit.mcaleerj.thesis.aggregationservice.service.AggregationService;
import org.cit.mcaleerj.thesis.management.client.ManagementServiceClient;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.UUID;

@Configuration
@EnableWebSocket
@ComponentScan("org.cit.mcaleerj.thesis")
public class WebsocketConfiguration implements WebSocketConfigurer {

  static final String ATTRIB_ENVIRONMENT_UUID = "environmentUuid";

  @Value("${aggregation.service.endpoints.environment-update}")
  private String endpointPath;

  private final AggregationService aggregationService;
  private final ManagementServiceClient managementService;

  /**
   * Constructor.
   * @param aggregationService {@link AggregationService} instance
   * @param managementService {@link ManagementServiceClient} instance
   */
  public WebsocketConfiguration(@NonNull final AggregationService aggregationService,
                                @NonNull final ManagementServiceClient managementService) {
    this.aggregationService = aggregationService;
    this.managementService = managementService;
  }


  @Bean
  public WebSocketHandler environmentPushMessageHandler() {
    return new AggregationServiceMessageHandler(this.aggregationService);
  }

  @Override
  public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
    registry.addHandler(environmentPushMessageHandler(), this.endpointPath).addInterceptors(handshakeInterceptor(this.managementService));
  }

  private HandshakeInterceptor handshakeInterceptor(final ManagementServiceClient managementService) {
    return new HandshakeInterceptor() {

      @Override
      public boolean beforeHandshake(final ServerHttpRequest request, final ServerHttpResponse response,
                                     final WebSocketHandler socketHandler, final Map<String, Object> attributes)
              throws Exception {

        final String path = request.getURI().getPath();
        final String environmentUuidStr = path.substring(path.lastIndexOf('/') + 1);

        if (StringUtils.isBlank(environmentUuidStr)) {
          return false;
        }

        final UUID envUuid = UUID.fromString(environmentUuidStr);
        final EnvironmentDto environmentDto = managementService.getEnvironmentByUuid(envUuid);

        if(environmentDto == null) {
          return false;

        }

        attributes.put(ATTRIB_ENVIRONMENT_UUID, environmentDto);
        return true;
      }

      public void afterHandshake(final ServerHttpRequest request, final ServerHttpResponse response,
                                 final WebSocketHandler socketHandler, Exception exception) {
      }
    };
  }


}
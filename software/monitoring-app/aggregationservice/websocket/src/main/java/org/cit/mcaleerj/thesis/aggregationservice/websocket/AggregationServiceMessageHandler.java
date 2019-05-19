package org.cit.mcaleerj.thesis.aggregationservice.websocket;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.aggregationservice.service.AggregationService;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Aggregation service websocket push handler.
 */
public class AggregationServiceMessageHandler extends TextWebSocketHandler {

  private static final Logger log = LoggerFactory.getLogger(EnvironmentAggregationListener.class);

  private final AggregationService aggregationService;

  private final Map<WebSocketSession, EnvironmentAggregationListener> listenerMap = new HashMap<>();

  /**
   * Constructor.
   * @param aggregationService {@link AggregationService} instance
   */
  AggregationServiceMessageHandler(@NonNull final AggregationService aggregationService) {
    this.aggregationService = aggregationService;
  }

  @Override
  public void afterConnectionEstablished(final WebSocketSession session) {

    final EnvironmentDto environment = (EnvironmentDto) session.getAttributes().get(WebsocketConfiguration.ATTRIB_ENVIRONMENT_UUID);

    final EnvironmentAggregationListener updateListener = new EnvironmentAggregationListener(session);

    this.listenerMap.put(session, updateListener);

    this.aggregationService.addAggregationListener(updateListener, environment);

    log.info("Environment aggregation notification session established for environment  " + environment.getUuid());
  }

  @Override
  public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {

    log.info(MessageFormat.format("Clean up websocket session {0}", session.getId()));
    final EnvironmentDto environment = (EnvironmentDto) session.getAttributes().get(WebsocketConfiguration.ATTRIB_ENVIRONMENT_UUID);

    final EnvironmentAggregationListener listener = this.listenerMap.get(session);
    if (listener == null) {
      log.warn(MessageFormat.format("No environment listener found during cleanup of session {0}", session.getId()));
      return;
    }

    this.listenerMap.remove(session);
    this.aggregationService.removeAggregationListener(listener, environment);
  }

}

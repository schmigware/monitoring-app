package org.cit.mcaleerj.thesis.discoveryservice.websocket;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.discoveryservice.service.DiscoveryService;
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
 * Discovery service websocket push handler.
 */
public class DiscoveryServiceMessageHandler extends TextWebSocketHandler {

  private static final Logger log = LoggerFactory.getLogger(TopologyDiscoveryListener.class);

  private final DiscoveryService discoveryService;

  private final Map<WebSocketSession, TopologyDiscoveryListener> listenerMap = new HashMap<>();

  /**
   * Constructor.
   * @param discoveryService {@link DiscoveryService} instance
   */
  DiscoveryServiceMessageHandler(@NonNull final DiscoveryService discoveryService) {
    this.discoveryService = discoveryService;
  }

  @Override
  public void afterConnectionEstablished(final WebSocketSession session) {

    final EnvironmentDto environment = (EnvironmentDto) session.getAttributes().get(WebsocketConfiguration.ATTRIB_ENVIRONMENT_UUID);

    final TopologyDiscoveryListener updateListener = new TopologyDiscoveryListener(session);

    this.listenerMap.put(session, updateListener);

    this.discoveryService.addDiscoveryListener(updateListener, environment);

    log.info("Discovery notification session established for environment  " + environment.getUuid());
  }


  public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {

    final EnvironmentDto environment = (EnvironmentDto) session.getAttributes().get(WebsocketConfiguration.ATTRIB_ENVIRONMENT_UUID);

    final TopologyDiscoveryListener listener = this.listenerMap.get(session);
    if (listener == null) {
      log.warn(MessageFormat.format("No discovery listener found during cleanup of session {0}", session.getId()));
      return;
    }

    this.listenerMap.remove(session);
    this.discoveryService.removeDiscoveryListener(listener, environment);
  }

}

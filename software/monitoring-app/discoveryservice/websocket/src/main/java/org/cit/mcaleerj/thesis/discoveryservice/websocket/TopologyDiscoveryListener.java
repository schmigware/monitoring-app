package org.cit.mcaleerj.thesis.discoveryservice.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.cit.mcaleerj.thesis.discoveryservice.service.DiscoveryListener;
import org.cit.mcaleerj.thesis.management.dto.TopologyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * {@link DiscoveryListener} implementation which pushes discovered
 * topologies over a websocket.
 */
public class TopologyDiscoveryListener implements DiscoveryListener {

  private static final Logger log = LoggerFactory.getLogger(TopologyDiscoveryListener.class);

  private final WebSocketSession session;

  private final static ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Constructor.
   *
   * @param session web socket session
   */
  TopologyDiscoveryListener(@NonNull final WebSocketSession session) {
    this.session = session;
  }

  @Override
  public void topologyDiscovered(@NonNull final TopologyDto topology) {
    try {
      final String payload = objectMapper.writeValueAsString(topology);
      this.session.sendMessage(new TextMessage(payload));
    } catch (IOException e) {
      log.error("Failed to send websocket message", e);
    }
  }

}

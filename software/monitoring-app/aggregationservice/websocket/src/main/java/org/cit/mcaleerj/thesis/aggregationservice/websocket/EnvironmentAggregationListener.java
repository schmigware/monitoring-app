package org.cit.mcaleerj.thesis.aggregationservice.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.cit.mcaleerj.thesis.aggregationservice.dto.EnvironmentSnapshotDto;
import org.cit.mcaleerj.thesis.aggregationservice.service.AggregationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class EnvironmentAggregationListener implements AggregationListener {

  private static final Logger log = LoggerFactory.getLogger(EnvironmentAggregationListener.class);

  private final WebSocketSession session;

  private final static ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Constructor.
   *
   * @param session web socket session
   */
  EnvironmentAggregationListener(@NonNull final WebSocketSession session) {
    this.session = session;
  }

  @Override
  public void aggregationCreated(@NonNull final EnvironmentSnapshotDto snapshot) {
    try {
      final String payload = objectMapper.writeValueAsString(snapshot);
      if (this.session.isOpen()) {
        this.session.sendMessage(new TextMessage(payload));
      }
    } catch (IOException e) {
      log.error("Failed to send websocket message", e);
    }
  }

}

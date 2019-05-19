package org.cit.mcaleerj.thesis.testbed.ingestionservice.consumer;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.testbed.ingestionservice.service.impl.DummyService;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class InputStreamListener {

  private final DummyService service;

  public InputStreamListener(@NonNull DummyService service) {
    this.service = service;
  }

  @StreamListener(InputStreams.INPUT)
  public void handleGreetings(final @Payload Object message) {
    this.service.handleMessage(message);
  }

}

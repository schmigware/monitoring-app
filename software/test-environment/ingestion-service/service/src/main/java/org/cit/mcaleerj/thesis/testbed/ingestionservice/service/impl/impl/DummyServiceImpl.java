package org.cit.mcaleerj.thesis.testbed.ingestionservice.service.impl.impl;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.testbed.ingestionservice.producer.OutputStreams;
import org.cit.mcaleerj.thesis.testbed.ingestionservice.service.impl.DummyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class DummyServiceImpl implements DummyService {

  private static final Logger log = LoggerFactory.getLogger(DummyServiceImpl.class);

  private final OutputStreams outputStreams;

  public DummyServiceImpl(@NonNull final OutputStreams outputStreams) {
    this.outputStreams = outputStreams;
  }

  @Override
  public void handleMessage(final Object message) {

      log.info("Processing message...");
      MessageChannel messageChannel = outputStreams.outboundMessages();
      messageChannel.send(MessageBuilder
                                  .withPayload(message)
                                  .build());
    }

}

package org.cit.mcaleerj.thesis.testbed.sinkservice.service.impl.impl;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.testbed.sinkservice.service.impl.DummyService;
import org.cit.mcaleerj.thesis.testbed.sinkservice.producer.OutputStreams;
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

  private long msgCount;

  @Override
  public void handleMessage(final Object message) {

      msgCount++;
      log.info("Processing message...");
      final MessageChannel outputChannel = msgCount % 10 == 0 ? outputStreams.deadletterMessages() : outputStreams.outboundMessages();
      outputChannel.send(MessageBuilder
                                  .withPayload(message)
                                  .build());
    }

}

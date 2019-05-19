package org.cit.mcaleerj.thesis.testbed.validationservice.service.impl.impl;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.testbed.validationservice.producer.OutputStreams;
import org.cit.mcaleerj.thesis.testbed.validationservice.service.impl.DummyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class DummyServiceImpl implements DummyService {

  /**
   * Processing time in miliseconds.
   */
  private static final long DEFAULT_PROCESSING_TIME = 0l;

  private static final Logger log = LoggerFactory.getLogger(DummyServiceImpl.class);

  private final OutputStreams outputStreams;

  public DummyServiceImpl(@NonNull final OutputStreams outputStreams) {
    this.outputStreams = outputStreams;
  }

  /**
   * Message count.
   */
  private long msgCount;

  /**
   * Message processing time.
   */
  private long processingTime = DEFAULT_PROCESSING_TIME;

  @Override
  public void handleMessage(final Object message) {

    if(this.processingTime > 0) {
      try {
        Thread.sleep(this.processingTime);
      } catch(InterruptedException e) {
        log.error(e.getLocalizedMessage());
      }
    }
    msgCount++;
    log.info("Processing message...");
    final MessageChannel outputChannel = msgCount % 10 == 0 ? outputStreams.deadletterMessages() : outputStreams.validatedMessages();
    outputChannel.send(MessageBuilder
                               .withPayload(message)
                               .build());
    }

  @Override
  public long setProcessingTime(final long time) {
    this.processingTime = time;
    return this.processingTime;
  }

}

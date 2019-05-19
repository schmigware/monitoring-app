package org.cit.mcaleerj.thesis.testbed.validationservice.consumer;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface InputStreams {

  String INPUT = "input-stream";

  @Input(INPUT)
  SubscribableChannel inboundMessages();

}

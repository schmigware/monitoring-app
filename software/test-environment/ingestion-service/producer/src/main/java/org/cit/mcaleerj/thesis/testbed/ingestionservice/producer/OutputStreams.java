package org.cit.mcaleerj.thesis.testbed.ingestionservice.producer;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
public interface OutputStreams {

  String OUTPUT = "output-stream";

  @Output(OUTPUT)
  MessageChannel outboundMessages();

}

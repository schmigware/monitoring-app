package org.cit.mcaleerj.thesis.testbed.sinkservice.producer;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OutputStreams {

  String OUTPUT_PROCESSED = "processed-output-stream";
  String DEADLETTER_PROCESSED = "deadletter-output-stream";

  @Output(OUTPUT_PROCESSED)
  MessageChannel outboundMessages();

  @Output(DEADLETTER_PROCESSED)
  MessageChannel deadletterMessages();

}

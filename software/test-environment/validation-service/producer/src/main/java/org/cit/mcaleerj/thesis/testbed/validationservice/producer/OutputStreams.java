package org.cit.mcaleerj.thesis.testbed.validationservice.producer;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
public interface OutputStreams {

  String OUTPUT_VALIDATED = "validated-output-stream";
  String DEADLETTER_PROCESSED = "deadletter-output-stream";

  @Output(OUTPUT_VALIDATED)
  MessageChannel validatedMessages();

  @Output(DEADLETTER_PROCESSED)
  MessageChannel deadletterMessages();
}

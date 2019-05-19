package org.cit.mcaleerj.thesis.testbed.validationservice.configuration;

import org.cit.mcaleerj.thesis.testbed.validationservice.consumer.InputStreams;
import org.cit.mcaleerj.thesis.testbed.validationservice.producer.OutputStreams;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding({InputStreams.class, OutputStreams.class})
public class Configuration {
}

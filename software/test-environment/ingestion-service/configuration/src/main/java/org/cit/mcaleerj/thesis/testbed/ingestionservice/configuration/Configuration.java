package org.cit.mcaleerj.thesis.testbed.ingestionservice.configuration;

import org.cit.mcaleerj.thesis.testbed.ingestionservice.consumer.InputStreams;
import org.cit.mcaleerj.thesis.testbed.ingestionservice.producer.OutputStreams;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding({InputStreams.class, OutputStreams.class})
public class Configuration {

}

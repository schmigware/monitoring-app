package org.cit.mcaleerj.thesis.testbed.sinkservice.configuration;

import org.cit.mcaleerj.thesis.testbed.sinkservice.consumer.InputStreams;
import org.cit.mcaleerj.thesis.testbed.sinkservice.producer.OutputStreams;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding({InputStreams.class, OutputStreams.class})
public class Configuration {
}

package org.cit.mcaleerj.thesis.monitorservice.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Monitoring service configuration.
 */
@Configuration
@EnableScheduling
@ComponentScan("org.cit.mcaleerj.thesis")
public class MonitoringServiceConfiguration {

}

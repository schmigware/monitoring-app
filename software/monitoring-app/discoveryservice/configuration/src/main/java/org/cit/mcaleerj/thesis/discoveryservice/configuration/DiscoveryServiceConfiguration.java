package org.cit.mcaleerj.thesis.discoveryservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Discovery service configuration.
 */
@Configuration
@EnableScheduling
@ComponentScan("org.cit.mcaleerj.thesis")
public class DiscoveryServiceConfiguration {

  @Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(10);
    taskScheduler.initialize();
    return taskScheduler;
  }

}

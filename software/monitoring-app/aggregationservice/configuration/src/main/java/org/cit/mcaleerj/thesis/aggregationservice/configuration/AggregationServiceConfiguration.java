package org.cit.mcaleerj.thesis.aggregationservice.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Aggregation service configuration.
 */
@Configuration
@EnableScheduling
@ComponentScan("org.cit.mcaleerj.thesis")
@EnableJpaRepositories({"org.cit.mcaleerj.thesis.monitorservice.dao.repository",
                       "org.cit.mcaleerj.thesis.aggregationservice.dao.repository"})
@EntityScan({"org.cit.mcaleerj.thesis.monitorservice.domain",
             "org.cit.mcaleerj.thesis.aggregationservice.domain"})
public class AggregationServiceConfiguration {

  @Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(10);
    taskScheduler.initialize();
    return taskScheduler;
  }

}

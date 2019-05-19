package org.cit.mcaleerj.thesis.correlation.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Correlation service configuration.
 */
@Configuration
@EnableDiscoveryClient
@ComponentScan("org.cit.mcaleerj.thesis")
@EnableJpaRepositories({"org.cit.mcaleerj.thesis.monitorservice.dao.repository"})
@EntityScan({"org.cit.mcaleerj.thesis.monitorservice.domain"})
public class CorrelationServiceConfiguration {

}

package org.cit.mcaleerj.thesis.aggregationservice.job;

import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;

import java.util.UUID;

/**
 * Aggregation task interface.
 */
public interface AggregationTask {

  /**
   * Returns task UUID.
   */
  UUID getUuid();

  /**
   * Returns task environment.
   */
  EnvironmentDto getEnvironment();

  /**
   * Start monitoring.
   */
  void start();

  /**
   * Stop monitoring.
   */
  void stop();

  /**
   * Add aggregation listener.
   */
  void addAggregationListener(AggregationListener listener);

  /**
   * Remove aggregation listener.
   */
  void removeAggregationListener(AggregationListener listener);

}


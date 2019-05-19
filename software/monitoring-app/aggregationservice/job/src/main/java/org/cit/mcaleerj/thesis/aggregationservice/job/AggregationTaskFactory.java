
package org.cit.mcaleerj.thesis.aggregationservice.job;

import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;

/**
 * Aggregation task factory.
 *
 */
public interface AggregationTaskFactory {

  /**
   * Returns an aggregation task for the given environment.
   * @param environment {@link EnvironmentDto} instance
   * @return {@link AggregationTask} instance
   */
  AggregationTask getTask(EnvironmentDto environment);

}


package org.cit.mcaleerj.thesis.aggregationservice.job;

import org.cit.mcaleerj.thesis.aggregationservice.job.exception.AggregationTaskManagerException;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;

import java.util.List;

/**
 * Aggregation job management.
 *
 */
public interface AggregationTaskManager {

  /**
   * Returns active aggregation tasks.
   * @return  active aggregation tasks.
   *
   */
  List<AggregationTask> getActiveAggregationTasks();

  /**
   * Starts an aggregation task.
   *
   */
  AggregationTask requestAggregationTask(final EnvironmentDto environment) throws AggregationTaskManagerException;

  /**
   * Stops an aggregation task.
   *
   */
  void stopAggregationTask(final AggregationTask task) throws AggregationTaskManagerException;

  /**
   * Adds an aggregation listener for the given environment.
   */
  void addAggregationListener(final AggregationListener listener, final EnvironmentDto environment);

  /**
   * Removes an aggregation listener  for the given environment.
   */
  void removeAggregationListener(final AggregationListener listener, final EnvironmentDto environment);

}

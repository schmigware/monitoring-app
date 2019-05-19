
package org.cit.mcaleerj.thesis.monitorservice.job;

import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.monitorservice.job.exception.MonitoringTaskManagerException;

/**
 * Monitoring task factory.
 *
 */
public interface MonitoringTaskFactory {

  /**
   * Returns a monitoring task for the given environment.
   * @param environment {@link EnvironmentDto} instance
   * @return {@link MonitoringTask} instance
   */
  MonitoringTask getTask(EnvironmentDto environment) throws MonitoringTaskManagerException ;

}

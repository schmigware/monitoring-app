
package org.cit.mcaleerj.thesis.monitorservice.job;

import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.management.dto.MonitoringTaskDto;
import org.cit.mcaleerj.thesis.monitorservice.job.exception.MonitoringTaskManagerException;

import java.util.List;

/**
 * Monitor job management.
 *
 */
public interface MonitoringTaskManager {

  /**
   * Returns active monitoring tasks.
   * @return  active monitoring tasks.
   *
   */
  List<MonitoringTaskDto> getActiveMonitoringTasks();

  /**
   * Starts a monitoring task.
   *
   */
  MonitoringTaskDto requestMonitoringTask(final EnvironmentDto environment) throws MonitoringTaskManagerException;

  /**
   * Stops a monitoring task.
   *
   */
  void stopMonitoringTask(final MonitoringTaskDto task) throws MonitoringTaskManagerException;

}

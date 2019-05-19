package org.cit.mcaleerj.thesis.monitorservice.service.impl;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.management.dto.MonitoringTaskDto;
import org.cit.mcaleerj.thesis.monitorservice.job.MonitoringTaskManager;
import org.cit.mcaleerj.thesis.monitorservice.job.exception.MonitoringTaskManagerException;
import org.cit.mcaleerj.thesis.monitorservice.service.MonitoringService;
import org.cit.mcaleerj.thesis.monitorservice.service.exception.MonitoringServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Monitoring service implementation.
 */
@Service
public class MonitoringServiceImpl implements MonitoringService {

  private static final Logger log = LoggerFactory.getLogger(MonitoringServiceImpl.class);

  private final MonitoringTaskManager taskManager;

  /**
   * Constructor.
   * @param taskManager {@link MonitoringTaskManager instance}
   */
  public MonitoringServiceImpl(@NonNull final MonitoringTaskManager taskManager) {
    this.taskManager = taskManager;
  }

  @Override
  public List<MonitoringTaskDto> synchronizeMonitoredEnvironments(@NonNull final List<EnvironmentDto> environments) throws MonitoringServiceException {

    // determine environments which are no longer monitored
    List<MonitoringTaskDto> deadTasks = new ArrayList<>();

    // determine environments which are not already monitored
    List<MonitoringTaskDto> activeTasks = this.taskManager.getActiveMonitoringTasks();
    for(MonitoringTaskDto activeTask : activeTasks) {
      final EnvironmentDto monitoredEnv = activeTask.getEnvironment();
      if (!environments.contains(monitoredEnv)) {
        deadTasks.add(activeTask);
      }
      environments.remove(monitoredEnv);
    }

    // stop dead tasks
    for(MonitoringTaskDto deadTask : deadTasks) {
      try {
        this.taskManager.stopMonitoringTask(deadTask);
      } catch(MonitoringTaskManagerException ex) {
        throw new MonitoringServiceException("Failed to stop monitoring job", ex);
      }
    }

    // start new tasks
    for(EnvironmentDto env : environments) {
      try {
        this.taskManager.requestMonitoringTask(env);
      } catch(MonitoringTaskManagerException ex) {
        throw new MonitoringServiceException("Failed to start monitoring job", ex);
      }
    }

    return this.taskManager.getActiveMonitoringTasks();
  }

}

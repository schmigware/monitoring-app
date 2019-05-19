package org.cit.mcaleerj.thesis.monitorservice.job.impl;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.management.dto.MonitoringTaskDto;
import org.cit.mcaleerj.thesis.monitorservice.job.MonitoringTaskManager;
import org.cit.mcaleerj.thesis.monitorservice.job.MonitoringTask;
import org.cit.mcaleerj.thesis.monitorservice.job.MonitoringTaskFactory;
import org.cit.mcaleerj.thesis.monitorservice.job.exception.MonitoringTaskManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Component
public class MonitoringTaskManagerImpl implements MonitoringTaskManager {

  private static final Logger log = LoggerFactory.getLogger(MonitoringTaskManagerImpl.class);

  private List<MonitoringTask> activeTasks  = Collections.synchronizedList(new ArrayList<>());

  /*
   * Task factory.
   */
  private final MonitoringTaskFactory taskFactory;

  /**
   * Constructor.
   * @param taskFactory Monitoring task factory.
   */
  public MonitoringTaskManagerImpl(@NonNull final MonitoringTaskFactory taskFactory) {
    this.taskFactory = taskFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MonitoringTaskDto> getActiveMonitoringTasks() {
    final List<MonitoringTaskDto> activeDtos = new ArrayList<>();
    for (MonitoringTask task : this.activeTasks) {
      activeDtos.add(monitoringTaskToDto(task));
    }
    return activeDtos;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MonitoringTaskDto requestMonitoringTask(@NonNull final EnvironmentDto environment) throws MonitoringTaskManagerException {
    log.info(MessageFormat.format("Starting monitoring task for environment {0}", environment.getUuid().toString()));
    final MonitoringTask monitoringTask = this.taskFactory.getTask(environment);
    this.activeTasks.add(monitoringTask);
    monitoringTask.start();
    return monitoringTaskToDto(monitoringTask);
  }

  @Override
  public void stopMonitoringTask(final MonitoringTaskDto task) throws MonitoringTaskManagerException {
    log.info(MessageFormat.format("Stopping monitoring task {0}", task.getUuid()));

    Iterator<MonitoringTask> taskIterator = this.activeTasks.iterator();
    while(taskIterator.hasNext()) {
      MonitoringTask activeTask = taskIterator.next();
      if(activeTask.getUuid().equals(task.getUuid())) {
        activeTask.stop();
        taskIterator.remove();
      }
    }
  }

  /**
   * Convert aggregation task to corresponding DTO.
   *
   * @param task {@link MonitoringTask} instance
   *
   * @return {@link MonitoringTaskDto} instance
   */
  private static MonitoringTaskDto monitoringTaskToDto(final MonitoringTask task) {
    final MonitoringTaskDto dto = new MonitoringTaskDto();
    dto.setUuid(task.getUuid());
    dto.setEnvironment(task.getEnvironment());
    return dto;
  }

  /**
   * Shutdown hook.
   */
  @PreDestroy
  public void destroy() {
    log.info("Shutting down monitoring tasks ...");
    this.activeTasks.forEach(MonitoringTask::stop);
  }

}

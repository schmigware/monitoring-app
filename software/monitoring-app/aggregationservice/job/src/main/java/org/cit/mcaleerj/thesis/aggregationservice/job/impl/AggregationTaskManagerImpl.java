package org.cit.mcaleerj.thesis.aggregationservice.job.impl;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.aggregationservice.job.AggregationListener;
import org.cit.mcaleerj.thesis.aggregationservice.job.AggregationTask;
import org.cit.mcaleerj.thesis.aggregationservice.job.AggregationTaskFactory;
import org.cit.mcaleerj.thesis.aggregationservice.job.AggregationTaskManager;
import org.cit.mcaleerj.thesis.aggregationservice.job.exception.AggregationTaskManagerException;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PreDestroy;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class AggregationTaskManagerImpl implements AggregationTaskManager {

  private static final Logger log = LoggerFactory.getLogger(AggregationTaskManagerImpl.class);

  private List<AggregationTask> activeTasks  = new ArrayList<>();

  private Map<EnvironmentDto, List<AggregationListener>> environmentListeners = new HashMap<>();

  /*
   * Task factory.
   */
  private final AggregationTaskFactory taskFactory;

  /**
   * Constructor.
   * @param taskFactory Monitoring task factory.
   */
  public AggregationTaskManagerImpl(@NonNull final AggregationTaskFactory taskFactory) {
    this.taskFactory = taskFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<AggregationTask> getActiveAggregationTasks() {
    return new ArrayList<>(this.activeTasks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AggregationTask requestAggregationTask(EnvironmentDto environment) throws AggregationTaskManagerException {
    log.info(MessageFormat.format("Starting aggregation task for environment {0}", environment.getUuid().toString()));
    final AggregationTask aggregationTask = this.taskFactory.getTask(environment);

    List<AggregationListener> listeners = this.environmentListeners.get(environment);
    if (!CollectionUtils.isEmpty(listeners)) {
      listeners.forEach(aggregationTask::addAggregationListener);
    }

    this.activeTasks.add(aggregationTask);
    aggregationTask.start();

    return aggregationTask;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stopAggregationTask(AggregationTask task) throws AggregationTaskManagerException {
    log.info(MessageFormat.format("Stopping monitoring task {0}", task.getUuid()));

    Iterator<AggregationTask> taskIterator = this.activeTasks.iterator();
    while(taskIterator.hasNext()) {
      AggregationTask activeTask = taskIterator.next();
      if(activeTask.getUuid().equals(task.getUuid())) {
        activeTask.stop();
        taskIterator.remove();
      }
    }
  }

  @Override
  public void addAggregationListener(AggregationListener listener, EnvironmentDto environment) {
    this.environmentListeners.computeIfAbsent(environment, e -> new ArrayList(Arrays.asList(listener)));
    this.activeTasks.forEach(task -> {
      if(task.getEnvironment().equals(environment)) {
        task.addAggregationListener(listener);
      }
    });
  }

  @Override
  public void removeAggregationListener(AggregationListener listener, EnvironmentDto environment) {
    List<AggregationListener> listeners = this.environmentListeners.get(environment);
    if (listeners != null) {
      listeners.remove(listener);
    }
    this.activeTasks.forEach(task -> {
      if(task.getEnvironment().equals(environment)) {
        task.removeAggregationListener(listener);
      }
    });
  }

  /**
   * Shutdown hook.
   */
  @PreDestroy
  public void destroy() {
    log.info("Shutting down aggregation tasks ...");
    this.activeTasks.forEach(AggregationTask::stop);
  }

}

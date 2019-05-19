package org.cit.mcaleerj.thesis.aggregationservice.service.impl;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.aggregationservice.domain.EnvironmentAggregation;
import org.cit.mcaleerj.thesis.aggregationservice.domain.EdgeAggregation;
import org.cit.mcaleerj.thesis.aggregationservice.dto.AggregationTaskDto;
import org.cit.mcaleerj.thesis.aggregationservice.dto.EnvironmentSnapshotDto;
import org.cit.mcaleerj.thesis.aggregationservice.dto.EdgeSnapshotDto;
import org.cit.mcaleerj.thesis.aggregationservice.job.AggregationTask;
import org.cit.mcaleerj.thesis.aggregationservice.job.AggregationTaskManager;
import org.cit.mcaleerj.thesis.aggregationservice.job.exception.AggregationTaskManagerException;
import org.cit.mcaleerj.thesis.aggregationservice.service.AggregationListener;
import org.cit.mcaleerj.thesis.aggregationservice.service.AggregationService;
import org.cit.mcaleerj.thesis.aggregationservice.service.exception.AggregationServiceException;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Aggregation service implementation.
 */
@Service
public class AggregationServiceImpl implements AggregationService {

  private final AggregationTaskManager taskManager;

  /**
   * Constructor.
   * @param taskManager {@link AggregationTaskManager instance}
   */
  public AggregationServiceImpl(@NonNull final AggregationTaskManager taskManager) {
    this.taskManager = taskManager;
  }


  @Override
  public List<AggregationTaskDto> synchronizeAggregatedEnvironments(@NonNull List<EnvironmentDto> environments) throws AggregationServiceException {

    // determine environments which are no longer monitored
    List<AggregationTask> deadTasks = new ArrayList<>();

    // determine environments which are not already monitored
    List<AggregationTask> activeTasks = this.taskManager.getActiveAggregationTasks();
    for(AggregationTask activeTask : activeTasks) {
      final EnvironmentDto aggregatedEnv = activeTask.getEnvironment();
      if (!environments.contains(aggregatedEnv)) {
        deadTasks.add(activeTask);
      }
      environments.remove(aggregatedEnv);
    }

    // stop dead tasks
    for(AggregationTask deadTask : deadTasks) {
      try {
        this.taskManager.stopAggregationTask(deadTask);
      } catch(AggregationTaskManagerException ex) {
        throw new AggregationServiceException("Failed to stop aggregation job", ex);
      }
    }

    // start new tasks
    for(EnvironmentDto env : environments) {
      try {
        this.taskManager.requestAggregationTask(env);
      } catch(AggregationTaskManagerException ex) {
        throw new AggregationServiceException("Failed to start aggregation job", ex);
      }
    }

    final List<AggregationTaskDto> dtos = new ArrayList<>();
    this.taskManager.getActiveAggregationTasks().forEach( task -> dtos.add(aggregationTaskToDto(task)));
    return dtos;
  }

  @Override
  public void addAggregationListener(@NonNull final AggregationListener listener, @NonNull final EnvironmentDto environment) {
    this.taskManager.addAggregationListener(new AggregationListenerAdapter(listener), environment);
  }

  @Override
  public void removeAggregationListener(@NonNull final AggregationListener listener, @NonNull final EnvironmentDto environment) {
    this.taskManager.removeAggregationListener(new AggregationListenerAdapter(listener), environment);
  }

  /**
   * Convert aggregation task to corresponding DTO.
   *
   * @param task {@link AggregationTask} instance
   *
   * @return {@link AggregationTaskDto} instance
   */
  private static AggregationTaskDto aggregationTaskToDto(final AggregationTask task) {
    AggregationTaskDto dto = new AggregationTaskDto();
    dto.setUuid(task.getUuid());
    dto.setEnvironment(task.getEnvironment());
    return dto;
  }

  private static EnvironmentSnapshotDto environmentAggregationToDTO(final EnvironmentAggregation aggregation) {

    if (aggregation == null) {
      return null;
    }

    final EnvironmentSnapshotDto envDto = new EnvironmentSnapshotDto();
    envDto.setEnvironmentUUID(aggregation.getEnvironmentUuid());
    aggregation.getEdgeAggregations().forEach(topicAgg -> envDto.getEdgeSnapshots().add(topicAggregationToDto(topicAgg)));
    envDto.setWindowEnd(aggregation.getWindowEnd().getTime());
    envDto.setWindowSize(aggregation.getWindowSize());
    return envDto;
  }

  private static EdgeSnapshotDto topicAggregationToDto(final EdgeAggregation aggregation) {

    if (aggregation == null) {
      return null;
    }
    final EdgeSnapshotDto dto = new EdgeSnapshotDto();
    dto.setMessageCount(aggregation.getCount());
    dto.setEdgeLabel(aggregation.getEdgeLabel());
    dto.setSourceNode(aggregation.getSourceNode());
    return dto;
  }

  private static class AggregationListenerAdapter implements org.cit.mcaleerj.thesis.aggregationservice.job.AggregationListener {

    private final AggregationListener adapted;

    AggregationListenerAdapter(@NonNull final AggregationListener serviceListener) {
      this.adapted = serviceListener;
    }

    @Override
    public void aggregationCreated(EnvironmentAggregation snapshot) {
      this.adapted.aggregationCreated(environmentAggregationToDTO(snapshot));
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      AggregationListenerAdapter that = (AggregationListenerAdapter) o;
      return Objects.equals(this.adapted, that.adapted);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.adapted);
    }
  }

}

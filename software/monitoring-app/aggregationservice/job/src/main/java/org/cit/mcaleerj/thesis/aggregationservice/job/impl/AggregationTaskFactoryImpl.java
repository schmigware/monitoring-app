package org.cit.mcaleerj.thesis.aggregationservice.job.impl;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.aggregationservice.dao.repository.EnvironmentAggregationRepository;
import org.cit.mcaleerj.thesis.aggregationservice.job.AggregationTask;
import org.cit.mcaleerj.thesis.aggregationservice.job.AggregationTaskFactory;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;

import org.cit.mcaleerj.thesis.monitorservice.dao.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Aggregation task factory.
 */
@Component
public class AggregationTaskFactoryImpl implements AggregationTaskFactory {

  private static final long DEFAULT_AGGREGATION_WINDOW_SIZE_MS = 5000;

  @Value("${aggregation.service.aggregation.window.size}")
  private long aggregationWindowSize = DEFAULT_AGGREGATION_WINDOW_SIZE_MS;

  /**
   * Aggregation repository.
   */
  private final EnvironmentAggregationRepository aggregationRepository;

  /**
   * Message repository.
   */
  private final MessageRepository messageRepository;

  /**
   * Constructor.
   * @param aggregationRepository {@link EnvironmentAggregationRepository} instance
   */
  public AggregationTaskFactoryImpl(@NonNull final EnvironmentAggregationRepository aggregationRepository,
                                    @NonNull final MessageRepository messageRepository) {
    this.aggregationRepository = aggregationRepository;
    this.messageRepository = messageRepository;
  }

  /**
   * {@inheritDoc}
   */
  public AggregationTask getTask(@NonNull final EnvironmentDto environment) {

    return new AggregationTaskImpl(environment, aggregationRepository,
                                   this.messageRepository, this.aggregationWindowSize);
  }

}

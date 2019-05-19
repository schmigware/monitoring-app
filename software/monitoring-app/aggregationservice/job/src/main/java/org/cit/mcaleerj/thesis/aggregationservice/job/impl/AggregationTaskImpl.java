package org.cit.mcaleerj.thesis.aggregationservice.job.impl;

import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang.time.DateUtils;
import org.cit.mcaleerj.thesis.aggregationservice.dao.repository.EnvironmentAggregationRepository;
import org.cit.mcaleerj.thesis.aggregationservice.domain.EnvironmentAggregation;
import org.cit.mcaleerj.thesis.aggregationservice.domain.EdgeAggregation;
import org.cit.mcaleerj.thesis.aggregationservice.job.AggregationListener;
import org.cit.mcaleerj.thesis.aggregationservice.job.AggregationTask;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.monitorservice.dao.repository.MessageRepository;
import org.cit.mcaleerj.thesis.monitorservice.domain.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class AggregationTaskImpl implements AggregationTask {

  /*
   * Logging.
   */
  private static final Logger log = LoggerFactory.getLogger(AggregationTaskImpl.class);

  /*
   * Task UUID.
   */
  @Getter
  private final UUID uuid;

  /*
   * Aggregated environment.
   */
  @Getter
  private final EnvironmentDto environment;

  /*
   * Aggregation listeners.
   */
  private List<AggregationListener> listeners = new ArrayList<>();

  /*
   * Aggregation repository.
   */
  private final EnvironmentAggregationRepository environmentAggregationRepository;

  /*
   * Message repository.
   */
  private final MessageRepository messageRepository;

  /*
   * Task thread.
   */
  private Thread aggregationThread;

  /*
   * Aggregation window size.
   */
  private final long aggregationWindowSize;

  /*
   * Flags running state.
   */
  private boolean running;

  /*
   * Flags sleeping state.
   */
  private boolean sleeping;

  /**
   * Constructor.
   *
   * @param environment Aggregated environment.
   */
  AggregationTaskImpl(@NonNull final EnvironmentDto environment,
                      @NonNull final EnvironmentAggregationRepository environmentAggregationRepository,
                      @NonNull final MessageRepository messageRepository,
                      final long aggregationWindowSize) {
    this.environment = environment;
    this.environmentAggregationRepository = environmentAggregationRepository;
    this.messageRepository = messageRepository;
    this.aggregationWindowSize = aggregationWindowSize;
    this.uuid = UUID.randomUUID();
    this.aggregationThread = this.createAggregationThread(environment);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start() {
    this.running = true;
    this.aggregationThread.start();
    log.info(MessageFormat.format("Started aggregation task {0}", this.uuid));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop() {
    log.info(MessageFormat.format("Stopping aggregation task {0}", this.uuid));
    this.running = false;
    if(this.sleeping) {
      this.aggregationThread.interrupt();
    }
    try {
      this.aggregationThread.join();
    } catch (InterruptedException e) {
      log.error("Shutdown interrupted", e);
    }
  }

  @Override
  public void addAggregationListener(@NonNull final AggregationListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public void removeAggregationListener(@NonNull final AggregationListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * Creates and returns aggregation thread.
   *
   * @param environment aggregation environment
   * @return aggregation thread
   */
  private Thread createAggregationThread(final EnvironmentDto environment) {
    return new Thread(() -> {

      while (this.running) {

        final long currTime = System.currentTimeMillis();

        //determine start time of next aggregation window
        Timestamp windowStart = null;
        final EnvironmentAggregation lastEnvironmentAggregation =
                this.environmentAggregationRepository.findLatestAggregation(environment.getUuid());

        if (lastEnvironmentAggregation != null) {
          windowStart = lastEnvironmentAggregation.getWindowEnd();
        } else {
          Message firstMessage = this.messageRepository.findFirstMessage(environment.getUuid());
          if (firstMessage != null) {
            windowStart = firstMessage.getTimestamp();
          }
        }

        final Date date = new Date(windowStart.getTime());
        final Date truncatedDate = DateUtils.truncate(date, Calendar.SECOND);
        windowStart = new Timestamp(truncatedDate.getTime());

        boolean sleep = windowStart == null;

        if (!sleep) {

          final long windowEndTime = windowStart.getTime() + this.aggregationWindowSize;
          Timestamp windowEnd = new Timestamp(windowEndTime);

          if (windowEndTime > currTime) {

            sleep = true;

          } else {

            final List<String> edgeLabels = this.messageRepository.findAllEdgeLabels(environment.getUuid());

            final EnvironmentAggregation environmentAggregation =
                    new EnvironmentAggregation(environment.getUuid());
            environmentAggregation.setWindowSize(this.aggregationWindowSize);

            for (final String edgeLabel : edgeLabels) {

              List<String> sourceNodes = this.messageRepository.findAllSourceNodeNames(environment.getUuid(), edgeLabel);

              for(final String sourceNode : sourceNodes) {

                final List<Message> windowMessages = this.messageRepository.findAllInWindow(
                        environment.getUuid(), edgeLabel, sourceNode, windowStart, windowEnd);

                System.out.println("messages: " + windowMessages.size());
                System.out.println("next: " + this.messageRepository.findNextMessage(environment.getUuid(), windowStart));


                final EdgeAggregation agg = new EdgeAggregation();
                agg.setEdgeLabel(edgeLabel);
                agg.setSourceNode(sourceNode);
                agg.setCount(windowMessages.size());
                environmentAggregation.getEdgeAggregations().add(agg);
                agg.setEnvironmentAggregation(environmentAggregation);

              }

            }

            if(!environmentAggregation.hasAggregations()) {
              //push out the aggregation window to the next available event
              Message nextMessage = this.messageRepository.findNextMessage(environment.getUuid(), windowEnd);
              if(nextMessage != null) {
               windowEnd = nextMessage.getTimestamp();
              } else {
                windowEnd = new Timestamp(currTime);
              }
            }

            environmentAggregation.setWindowEnd(windowEnd);

            log.info(MessageFormat.format("Environment {0}: Aggregating message window {1} - {2}",
                                          environment.getUuid(), windowStart, windowEnd));

            this.environmentAggregationRepository.save(environmentAggregation);
            this.listeners.forEach(lister -> lister.aggregationCreated(environmentAggregation));

          }
        }

        if (sleep) {
          try {
            log.info(MessageFormat.format("Snoozing aggregation task {0}", this.uuid));
            this.sleeping = true;
            Thread.sleep(this.aggregationWindowSize);
            this.sleeping = false;
          } catch (InterruptedException e) {
            //no-op
          }
        }

      }

    });
  }


}

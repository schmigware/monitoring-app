package org.cit.mcaleerj.thesis.monitorservice.job;

import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.monitorservice.dao.repository.MessageRepository;
import org.cit.mcaleerj.thesis.monitorservice.job.exception.MonitoringTaskException;

import java.util.UUID;

/**
 * Monitoring task interface.
 */
public interface MonitoringTask {

  /**
   * Returns task UUID.
   */
  UUID getUuid();

  /**
   * Returns task environment.
   */
  EnvironmentDto getEnvironment();

  /**
   * Initialisation.
   * @param  env {@link EnvironmentDto} instance
   * @param  messageRepository {@link MessageRepository} instance
   *
   * @throws MonitoringTaskException if an exception occurs during task initialisation
   */
  void init(EnvironmentDto env, MessageRepository messageRepository) throws MonitoringTaskException;

  /**
   * Start monitoring.
   */
  void start();

  /**
   * Stop monitoring.
   */
  void stop();

}

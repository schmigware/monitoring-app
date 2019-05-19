package org.cit.mcaleerj.thesis.monitorservice.job.impl;

import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import org.cit.mcaleerj.thesis.management.client.ManagementServiceClient;
import org.cit.mcaleerj.thesis.management.client.exception.ManagementServiceClientException;
import org.cit.mcaleerj.thesis.management.dto.ConfigurationPropertyDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentProfileDto;
import org.cit.mcaleerj.thesis.monitorservice.dao.repository.MessageRepository;
import org.cit.mcaleerj.thesis.monitorservice.job.MonitoringTask;
import org.cit.mcaleerj.thesis.monitorservice.job.MonitoringTaskFactory;
import org.cit.mcaleerj.thesis.monitorservice.job.exception.MonitoringTaskException;
import org.cit.mcaleerj.thesis.monitorservice.job.exception.MonitoringTaskManagerException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * Monitoring task factory implementation.
 */
@Component
public class MonitoringTaskFactoryImpl implements MonitoringTaskFactory {

  /*
   * Monitoring class profile configuration property.
   */
  private static final String CONFIG_PROP_MONITORING_CLASS = "monitoringTaskClass";

  /*
   * Message repository.
   */
  private final MessageRepository messageRepository;

  /*
   * Management service client.
   */
  private final ManagementServiceClient mgmtServiceClient;

  /**
   * Constructor.
   *
   * @param messageRepository JPA message repository
   */
  public MonitoringTaskFactoryImpl(@NonNull final MessageRepository messageRepository,
                                   @NonNull final ManagementServiceClient mgmtServiceClient) {
    this.messageRepository = messageRepository;
    this.mgmtServiceClient = mgmtServiceClient;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MonitoringTask getTask(@NonNull final EnvironmentDto environment) throws MonitoringTaskManagerException {

    final String environmentProfileId = environment.getProfileId();
    try {
      final EnvironmentProfileDto profile = this.mgmtServiceClient.getEnvironmentProfile(environmentProfileId);
      if (profile == null) {
        throw new MonitoringTaskManagerException(MessageFormat.format("No environment profile exists for profile id {0}", environmentProfileId));
      }

      final String monitoringClazz = getMonitoringClassName(profile);
      if(StringUtils.isBlank(monitoringClazz)) {
        throw new MonitoringTaskManagerException(MessageFormat.format("Monitoring task implementation not configured for environmen profile {0} ",
                                                                      profile.getId()));
      }
      final MonitoringTask taskInstance = (MonitoringTask) Class.forName(monitoringClazz).newInstance();
      taskInstance.init(environment, this.messageRepository);
      return taskInstance;

    } catch (ManagementServiceClientException | ClassNotFoundException | IllegalAccessException | InstantiationException | MonitoringTaskException e) {
      throw new MonitoringTaskManagerException("Failed to create monitoring task", e);
    }
  }

  /**
   * Returns the monitoring class name for the given profile, or null if not defined.
   *
   * @param profile {@link EnvironmentProfileDto} instance
   *
   * @return FQN of environment monitoring class
   */
  private static String getMonitoringClassName(final EnvironmentProfileDto profile) {
    for(ConfigurationPropertyDto prop : profile.getConfigurationProperties()) {
      if(CONFIG_PROP_MONITORING_CLASS.equals(prop.getName())) {
        return prop.getValue();
      }
    };
    return null;
  }

}

package org.cit.mcaleerj.thesis.monitorservice.service;

import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.management.dto.MonitoringTaskDto;
import org.cit.mcaleerj.thesis.monitorservice.service.exception.MonitoringServiceException;

import java.util.List;

/**
 * Monitoring service interface.
 */
public interface MonitoringService {

  List<MonitoringTaskDto> synchronizeMonitoredEnvironments(List<EnvironmentDto> environments) throws MonitoringServiceException;

}

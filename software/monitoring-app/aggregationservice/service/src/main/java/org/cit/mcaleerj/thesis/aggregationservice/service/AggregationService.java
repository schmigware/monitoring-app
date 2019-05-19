package org.cit.mcaleerj.thesis.aggregationservice.service;

import org.cit.mcaleerj.thesis.aggregationservice.dto.AggregationTaskDto;
import org.cit.mcaleerj.thesis.aggregationservice.service.exception.AggregationServiceException;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;

import java.util.List;

/**
 * Aggregation service interface.
 */
public interface AggregationService {

  /**
   * Synchonises the aggregated environments.
   *
   * @param environments <code>List</code> of {@link EnvironmentDto} instances
   * @return <code>List</code> of aggregated environments
   *
   * @throws AggregationServiceException if an error occurs
   */
  List<AggregationTaskDto> synchronizeAggregatedEnvironments(List<EnvironmentDto> environments) throws AggregationServiceException;

  void addAggregationListener(AggregationListener listener, EnvironmentDto environment);

  void removeAggregationListener(AggregationListener listener, EnvironmentDto environment);


}

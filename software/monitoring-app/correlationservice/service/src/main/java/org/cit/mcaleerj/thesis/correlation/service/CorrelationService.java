package org.cit.mcaleerj.thesis.correlation.service;

import org.cit.mcaleerj.thesis.correlation.dto.CorrelationTraceDto;
import org.cit.mcaleerj.thesis.correlation.service.exception.CorrelationServiceException;

import java.util.List;
import java.util.UUID;

/**
 * Correlation service interface.
 */
public interface CorrelationService {

  /**
   * Returns longest correlation traces by message content field name, for the given environment UUID.
   *
   * @param environmentUUID environment UUID,
   * @param fieldName message content JSON field name
   *
   * @throws CorrelationServiceException thrown if an error occurs
   */
  List<CorrelationTraceDto> getTraces(UUID environmentUUID, String fieldName) throws CorrelationServiceException;

}

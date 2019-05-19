package org.cit.mcaleerj.thesis.correlation.client;

import org.cit.mcaleerj.thesis.correlation.client.exception.CorrelationServiceClientException;
import org.cit.mcaleerj.thesis.correlation.dto.CorrelationTraceDto;

import java.util.List;
import java.util.UUID;

/**
 * Correlation service client interface.
 */
public interface CorrelationServiceClient {

  /**
   * Returns longest correlation traces by message content field name, for the given environment UUID.
   *
   * @param environmentUUID environment UUID,
   * @param fieldName message content JSON field name
   *
   * @throws CorrelationServiceClientException thrown if an error occurs
   */
  List<CorrelationTraceDto> getTraces(UUID environmentUUID, String fieldName) throws CorrelationServiceClientException;


}

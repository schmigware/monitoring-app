package org.cit.mcaleerj.thesis.correlation.service.impl;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.correlation.dto.CorrelationTraceDto;
import org.cit.mcaleerj.thesis.correlation.dto.MessageDto;
import org.cit.mcaleerj.thesis.correlation.service.CorrelationService;
import org.cit.mcaleerj.thesis.correlation.service.exception.CorrelationServiceException;
import org.cit.mcaleerj.thesis.monitorservice.dao.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Correlation service implementation.
 */
@Service
public class CorrelationServiceImpl implements CorrelationService {

  private final MessageRepository messageRepository;

  private static final String PLACEHOLDER_FIELDNAME = "%FIELDNAME%";
  private static final String PLACEHOLDER_FIELDVALUE = "%FIELDVALUE%";
  private static final String PLACEHOLDER_ENV_UUID = "%ENV_UUID%";

  private final String JSON_FIELD_QUERY = "SELECT " +
                                          "content->>'" + PLACEHOLDER_FIELDNAME + "', " +
                                          "count(content->>'" + PLACEHOLDER_FIELDNAME + "') " +
                                          "FROM " +
                                          "messages " +
                                          "where environment_uuid = '" + PLACEHOLDER_ENV_UUID + "' " +
                                          "and timestamp >= NOW() - INTERVAL '1 hour' " +
                                          "GROUP BY " +
                                          "content->>'" + PLACEHOLDER_FIELDNAME + "' " +
                                          "order by count(content->>'" + PLACEHOLDER_FIELDNAME + "') desc ";

  private final String MESSAGE_QUERY =
          "select edge_label, source_node, timestamp, CAST(content AS text) from messages where content @> '{\"" + PLACEHOLDER_FIELDNAME +
          "\" : \"" + PLACEHOLDER_FIELDVALUE + "\"}' order by timestamp";

  /**
   * Constructor.
   *
   * @param messageRepository message repository
   */
  public CorrelationServiceImpl(@NonNull final MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CorrelationTraceDto> getTraces(final UUID environmentUUID, final String fieldName) throws CorrelationServiceException {

    final List<CorrelationTraceDto> traces = new ArrayList<>();

    final String correlationQuery = JSON_FIELD_QUERY.replace(PLACEHOLDER_FIELDNAME, fieldName).
            replace(PLACEHOLDER_ENV_UUID, environmentUUID.toString());

    final List results = this.messageRepository.executeQuery(correlationQuery, Collections.emptyMap());

    for (Object result : results) {

      final Object[] resultArray = (Object[]) result;
      final CorrelationTraceDto traceDto = new CorrelationTraceDto();
      final Object fieldValue = resultArray[0];

      if (fieldValue == null) {
        continue;
      }

      final String messageQuery = MESSAGE_QUERY.replace(PLACEHOLDER_FIELDNAME, fieldName).replace(PLACEHOLDER_FIELDVALUE, fieldValue.toString());
      final List messages = this.messageRepository.executeQuery(messageQuery, Collections.emptyMap());

      for (Object message : messages) {

        final Object[] messageArray = (Object[]) message;
        final MessageDto messageDto = new MessageDto();
        messageDto.setEdgeLabel((String)messageArray[0]);
        messageDto.setSourceNode((String)messageArray[1]);
        messageDto.setTimestamp((Timestamp) messageArray[2]);
        traceDto.getMessages().add(messageDto);
      }

      traces.add(traceDto);
    }
    return traces;
  }


}

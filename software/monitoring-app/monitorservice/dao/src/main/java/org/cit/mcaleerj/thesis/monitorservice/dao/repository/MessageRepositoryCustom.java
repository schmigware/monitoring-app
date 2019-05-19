package org.cit.mcaleerj.thesis.monitorservice.dao.repository;

import org.cit.mcaleerj.thesis.monitorservice.domain.Message;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import java.sql.Date;

/**
 * Message repository customisation.
 */
public interface MessageRepositoryCustom {

  /**
   * Finds the next message after the given timestamp, for the given environment UUID.
   *
   * @param environmentUuid Environment {@link UUID}
   * @param timestamp {@link Timestamp} instance
   *
   * @return {@link Message} instance or null if no matching message found.
   */
  Message findNextMessage(UUID environmentUuid, Timestamp timestamp);

  /**
   * Finds the first message for the given environment UUID.
   *
   * @param environmentUuid Environment {@link UUID}
   *
   * @return {@link Message} instance or null if no matching message found.
   */
  Message findFirstMessage(UUID environmentUuid);

  /**
   * Finds the all messages in the given temporal window for the given environment UUID and edge label.
   *
   * @param environmentUuid Environment {@link UUID}
   * @param edgeLabel edge label
   * @param startTime window start time
   * @param endTime window end time
   *
   * @return <code>List</code> of matching {@link Message} instances
   */
  List<Message> findAllInWindow(UUID environmentUuid, String edgeLabel, Timestamp startTime, Timestamp endTime);

  /**
   * Finds the all messages in the given temporal window for the given environment UUID, edge label and source node name.
   *
   * @param environmentUuid Environment {@link UUID}
   * @param edgeLabel edge label
   * @param sourceNodeName source node name
   * @param startTime window start time
   * @param endTime window end time
   *
   * @return <code>List</code> of matching {@link Message} instances
   */
  List<Message> findAllInWindow(UUID environmentUuid, String edgeLabel, String sourceNodeName, Timestamp startTime, Timestamp endTime);


  /**
   * Finds all edge labels for the given environment UUID.
   *
   * @return <code>List</code> of edge names
   */
  List<String> findAllEdgeLabels(UUID environmentUuid);

  /**
   * Finds all source node names for the given edge label and environment UUID.
   *
   * @return <code>List</code> of source node names
   */
  List<String> findAllSourceNodeNames(UUID environmentUuid, String edgeLabel);

  /**
   * Executes the given query for the given parameters.
   *
   * @param queryString query to execute
   * @param parameters query parameter map
   *
   * @return list of query results
   */
  List executeQuery(String queryString, Map<String, Object> parameters);

  /**
   * Remove all messages older than the given date.
   * @param date {@link Date} instance
   * @return
   */
  @Modifying
  @Transactional
  int removeOlderThan(@Param("date")Date date);

}
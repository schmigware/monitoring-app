package org.cit.mcaleerj.thesis.monitorservice.dao.repository.impl;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.monitorservice.dao.repository.MessageRepository;
import org.cit.mcaleerj.thesis.monitorservice.dao.repository.MessageRepositoryCustom;
import org.cit.mcaleerj.thesis.monitorservice.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MessageRepositoryImpl implements MessageRepositoryCustom {

  @Autowired
  private MessageRepository messageRepository;

  @PersistenceContext
  private EntityManager em;

  /**
   * {@inheritDoc}
   */
  @Override
  public Message findFirstMessage(UUID environmentUuid) {
    final TypedQuery<Message> query = em.createQuery("select msg from Message msg where msg.environmentUuid = :environmentUuid " +
                                                     "order by msg.timestamp asc", Message.class)
                          .setParameter("environmentUuid", environmentUuid)
                          .setMaxResults(1);
    List<Message> messages = query.getResultList();
    return messages.isEmpty() ? null : messages.get(0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Message findNextMessage(final UUID environmentUuid, final Timestamp timestamp) {
    final TypedQuery<Message> query = em.createQuery("select msg from Message msg where msg.environmentUuid = :environmentUuid and msg" +
                                       ".timestamp > :timestamp order by msg.timestamp asc", Message.class)
                          .setParameter("environmentUuid", environmentUuid)
                          .setParameter("timestamp", timestamp)
                          .setMaxResults(1);
    List<Message> messages = query.getResultList();
    return messages.isEmpty() ? null : messages.get(0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Message> findAllInWindow(final UUID environmentUuid, final String edgeLabel, final Timestamp startTime, final Timestamp endTime) {
    final TypedQuery<Message> query = em.createQuery("select msg from Message msg where msg.environmentUuid = :environmentUuid and msg.edgeLabel = :edgeLabel " +
                                            "and msg.timestamp >= :startTime and msg.timestamp < :endTime order by msg.timestamp asc", Message.class)
                               .setParameter("environmentUuid", environmentUuid)
                               .setParameter("edgeLabel", edgeLabel)
                               .setParameter("startTime", startTime)
                               .setParameter("endTime", endTime);
    return query.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Message> findAllInWindow(final UUID environmentUuid, final String edgeLabel, final String sourceNodeName, final Timestamp startTime, final Timestamp endTime) {

    TypedQuery<Message> query;
    if(sourceNodeName == null) {
      query = em.createQuery("select msg from Message msg where msg.environmentUuid = :environmentUuid and msg.edgeLabel = :edgeLabel " +
                             "and msg.timestamp >= :startTime and msg.timestamp < :endTime and msg.sourceNode IS NULL " +
                             "order by msg.timestamp asc", Message.class)
                .setParameter("environmentUuid", environmentUuid)
                .setParameter("edgeLabel", edgeLabel)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime);
    } else {
      query = em.createQuery("select msg from Message msg where msg.environmentUuid = :environmentUuid and msg.edgeLabel = :edgeLabel " +
                             "and msg.timestamp >= :startTime and msg.timestamp < :endTime and msg.sourceNode = :sourceNodeName " +
                             "order by msg.timestamp asc", Message.class)
                .setParameter("environmentUuid", environmentUuid)
                .setParameter("edgeLabel", edgeLabel)
                .setParameter("sourceNodeName", sourceNodeName)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime);
    }
    return query.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> findAllEdgeLabels(final UUID environmentUuid) {
    final TypedQuery<String> query = em.createQuery("select distinct msg.edgeLabel from Message msg where " +
                                                           "msg.environmentUuid = :environmentUuid", String.class)
                          .setParameter("environmentUuid", environmentUuid);
    return query.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> findAllSourceNodeNames(final UUID environmentUuid, final String edgeLabel) {
    final TypedQuery<String> query = em.createQuery("select distinct msg.sourceNode from Message msg where " +
                                                    "msg.environmentUuid = :environmentUuid and msg.edgeLabel = :edgeLabel", String.class)
                                       .setParameter("environmentUuid", environmentUuid)
                                       .setParameter("edgeLabel", edgeLabel);
    return query.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List executeQuery(final String queryString, final Map<String, Object> parameters) {
    final Query query = em.createNativeQuery(queryString);
    parameters.forEach(query::setParameter);
    return query.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int removeOlderThan(@NonNull final Date date) {
    final Query query = em.createQuery("DELETE FROM Message m WHERE m.timestamp < :date")
            .setParameter("date", date);
    return query.executeUpdate();
  }

}

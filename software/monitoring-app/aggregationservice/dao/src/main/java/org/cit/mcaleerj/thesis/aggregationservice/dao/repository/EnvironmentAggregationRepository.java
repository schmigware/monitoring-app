package org.cit.mcaleerj.thesis.aggregationservice.dao.repository;

import org.cit.mcaleerj.thesis.aggregationservice.domain.EnvironmentAggregation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Environment Aggregation repository.
 *
 */
public interface EnvironmentAggregationRepository extends JpaRepository<EnvironmentAggregation, Long> {

  @Query("SELECT agg FROM EnvironmentAggregation agg WHERE agg.environmentUuid = ?1 and " +
         "agg.id = (SELECT MAX(agg_inner.id) FROM EnvironmentAggregation agg_inner WHERE agg_inner.environmentUuid = " +
         "agg.environmentUuid)")
  EnvironmentAggregation findLatestAggregation(UUID environmentUUID);

  @Modifying
  @Transactional
  @Query("DELETE FROM EnvironmentAggregation agg WHERE agg.windowEnd < :date")
  int removeOlderThan(@Param("date") java.sql.Date date);

}
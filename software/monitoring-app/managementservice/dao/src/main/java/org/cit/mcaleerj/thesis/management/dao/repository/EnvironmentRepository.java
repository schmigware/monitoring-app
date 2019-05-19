package org.cit.mcaleerj.thesis.management.dao.repository;

import org.cit.mcaleerj.thesis.management.domain.Environment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Environment repository.
 *
 */
public interface EnvironmentRepository extends JpaRepository<Environment, Long> {

  List<Environment> findByUuid(UUID uuid);
  List<Environment> findByMonitored(boolean monitored);

}
package org.cit.mcaleerj.thesis.management.dao.repository;

import org.cit.mcaleerj.thesis.management.domain.Environment;
import org.cit.mcaleerj.thesis.management.domain.Topology;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Topology repository.
 */
public interface TopologyRepository extends JpaRepository<Topology, Long> {

  List<Topology> findByEnvironment(Environment env);
  List<Topology> findByUuid(UUID uuid);

}
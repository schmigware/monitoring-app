package org.cit.mcaleerj.thesis.management.dao.repository;

import org.cit.mcaleerj.thesis.management.domain.EnvironmentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Environment profileId repository.
 */
public interface EnvironmentProfileRepository extends JpaRepository<EnvironmentProfile, Long> {

  List<EnvironmentProfile> findById(String id);
  List<EnvironmentProfile> findByProfileId(String profileId);

}
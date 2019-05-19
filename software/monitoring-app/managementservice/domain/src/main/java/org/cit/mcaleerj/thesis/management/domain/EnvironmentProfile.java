package org.cit.mcaleerj.thesis.management.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Environment profileId model.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "environment_profile")
public class EnvironmentProfile extends Configurable {

  /*
   * Profile ID.
   */
  @Column(name = "profile_id", unique = true, nullable = false)
  private String profileId;

  /*
   * The environment profileId name.
   */
  @Column(name = "name", nullable = false)
  private String name;

}

package org.cit.mcaleerj.thesis.management.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Environment model.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "environment")
public class Environment extends Configurable {

  /*
   * The environment UUID.
   */
  @Column(name = "uuid", nullable = false)
  private UUID uuid;

  /*
   * The environment name.
   */
  @Column(name = "name", nullable = false, unique = true)
  private String name;

  /*
   * Monitored flag.
   */
  @Column(name = "monitored", nullable = false)
  private boolean monitored;

  /**
   * The environment profileId.
   */
  @OneToOne
  @JoinColumn(name = "profileId", referencedColumnName = "id")
  private EnvironmentProfile profile;

}

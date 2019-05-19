package org.cit.mcaleerj.thesis.management.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Environment configuration property model.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "configuration_property")
public class ConfigurationProperty {

  /*
   * ID.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private long id;

  /*
   * The propery name.
   */
  @Column(name = "name", nullable = false)
  private String name;

  /*
   * The property value.
   */
  @Column(name = "value")
  private String value;

  @ManyToOne
  private Configurable configurable;

}

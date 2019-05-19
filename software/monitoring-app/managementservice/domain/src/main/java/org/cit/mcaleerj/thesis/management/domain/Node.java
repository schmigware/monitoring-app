package org.cit.mcaleerj.thesis.management.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Node model.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "node")
@EqualsAndHashCode(of = {"name"})
public class Node {

  /*
   * Node ID.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private long id;

  /**
   * The node name.
   */
  @Column(name = "name", nullable = false)
  private String name;


}

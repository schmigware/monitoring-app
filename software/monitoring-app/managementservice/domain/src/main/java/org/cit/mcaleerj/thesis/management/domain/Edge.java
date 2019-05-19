package org.cit.mcaleerj.thesis.management.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Edge model.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "edge")
@EqualsAndHashCode(of = {"label", "sourceNode", "targetNode"})
public class Edge {

  /*
   * Edge id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private long id;

  /**
   * The edge label.
   */
  @Column(name = "label", nullable = false)
  private String label;

  /**
   * The source node.
   */
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "source_node", referencedColumnName = "id")
  private Node sourceNode;

  /**
   * The target node.
   */
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "target_node", referencedColumnName = "id")
  private Node targetNode;

}

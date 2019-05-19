package org.cit.mcaleerj.thesis.management.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Topology model.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "topology")
public class Topology {

  /*
   * Unique ID.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private long id;

  /*
   * The topology UUID.
   */
  @Column(name = "uuid", nullable = false)
  private UUID uuid;

  /**
   * The topology environment.
   */
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "environment", referencedColumnName = "id")
  private Environment environment;

  /*
   * Node list.
   */
  @ToString.Exclude
  @OneToMany(cascade = CascadeType.ALL)
  private Set<Node> nodes = new HashSet<>();

  /*
   * Edge list.
   */
  @ToString.Exclude
  @OneToMany(cascade = CascadeType.ALL)
  private Set<Edge> edges = new HashSet<>();

  /**
   * Returns the node with the given ID, or null if does not exist
   *
   * @param nodeName node name
   * @return matching node or null if not found
   */
  public Node getNode(final String nodeName) {
    if (nodeName == null) {
      return null;
    }
    for (Node node : this.nodes) {
      if (nodeName.equals(node.getName())) {
        return node;
      }
    }
    return null;
  }

  /**
   * Returns all edge labels.
   *
   * @return Set of edge labels
   */
  public Set<String> getEdgeLabels() {
    Set<String> labels = new HashSet<>();
    for (Edge edge : this.getEdges()) {
      labels.add(edge.getLabel());
    }
    return labels;
  }

  /**
   * Returns all edges with the given label.
   *
   * @param label edge label
   * @return <code>List</code> of {@link Edge} instances
   */
  public List<Edge> getEdgesWithLabel(@NonNull final String label) {
    List<Edge> edges = new ArrayList<>();
    for (Edge edge : this.getEdges()) {
      if (label.equals(edge.getLabel())) {
        edges.add(edge);
      }
    }
    return edges;
  }

}

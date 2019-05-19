package org.cit.mcaleerj.thesis.aggregationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
 * Edge aggregation model.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "edge_aggregation")
public class EdgeAggregation {

  /*
   * ID.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private long id;

  /*
   * The edge label.
   */
  @Column(name = "edge_label", nullable = false)
  private String edgeLabel;

  /*
   * The source node name.
   */
  @Column(name = "source_node")
  private String sourceNode;

  /*
   * The message count.
   */
  @Column(name = "count", nullable = false)
  private long count;

  @ManyToOne
  @JoinColumn
  private EnvironmentAggregation environmentAggregation;

  /**
   * Returns true if this aggregation contains any aggregated data.
   * @return true if this aggregation contains any aggregated data, else false
   */
  public boolean hasAggregations() {
    return count > 0;
  }

}

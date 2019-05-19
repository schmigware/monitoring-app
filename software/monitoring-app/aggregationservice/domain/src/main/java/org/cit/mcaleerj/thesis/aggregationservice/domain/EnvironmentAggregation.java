package org.cit.mcaleerj.thesis.aggregationservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Environment aggregation model.
 */
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Table(name = "environment_aggregation")
public class EnvironmentAggregation {

  /*
   * Unique ID.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false)
  private long id;

  /*
   * The environment UUID.
   */
  @Column(name = "environment_uuid", nullable = false)
  @NonNull
  private UUID environmentUuid;

  /*
   * Aggregation window size (ms).
   */
  @Column(name = "window_size", nullable = false)
  private long windowSize;

  /*
   * Aggregation window end.
   */
  @Column(name = "window_end", nullable = false)
  private Timestamp windowEnd;

  /*
   * Edge aggregations.
   */
  @ToString.Exclude
  @OneToMany(mappedBy = "environmentAggregation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<EdgeAggregation> edgeAggregations = new ArrayList<>();

  /**
   * Returns true if this aggregation contains any aggregated data.
   * @return true if this aggregation contains any aggregated data, else false
   */
  public boolean hasAggregations() {
    for(EdgeAggregation agg : this.edgeAggregations) {
      if(agg.hasAggregations()) {
        return true;
      }
    }
    return false;
  }

}

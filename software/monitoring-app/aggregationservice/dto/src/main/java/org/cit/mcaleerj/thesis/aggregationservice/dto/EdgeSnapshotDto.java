package org.cit.mcaleerj.thesis.aggregationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Edge snapshot DTO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EdgeSnapshotDto {

  /*
   * Edge label.
   */
  private String edgeLabel;

  /*
   * Source node name.
   */
  private String sourceNode;

  /*
   * Message count.
   */
  private long messageCount;

}

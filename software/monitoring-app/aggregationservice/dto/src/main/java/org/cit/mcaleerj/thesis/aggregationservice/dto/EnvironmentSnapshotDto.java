package org.cit.mcaleerj.thesis.aggregationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * EnvironmentSnapshotDto DTO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvironmentSnapshotDto {

  /*
   * Environment UUID.
   */
  private UUID environmentUUID;

  /*
   * Topic snapshots.
   */
  private List<EdgeSnapshotDto> edgeSnapshots = new ArrayList<>();

  /*
   * Window size.
   */
  private long windowSize;

  /*
   * Window size.
   */
  private long windowEnd;
  
}

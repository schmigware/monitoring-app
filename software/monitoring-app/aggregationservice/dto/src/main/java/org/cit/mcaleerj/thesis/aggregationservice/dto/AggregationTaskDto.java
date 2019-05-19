package org.cit.mcaleerj.thesis.aggregationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;

import java.util.UUID;

/**
 * Aggregation Task DTO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregationTaskDto {

  /*
   * UUID.
   */
  private UUID uuid;

  /*
   * Monitored environment.
   */
  private EnvironmentDto environment;

}
